/**
 * Main game server class. Allows users to logon to the server and provides
 * matchmaking services. Basic TCP server code based on code from CS283
 * homepage.
 */
package cs283.catan;

import java.io.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author John
 *
 */
public class ServerMain {

    private static class ServerConnectionHandler implements Runnable {
        
        private enum UserMode {
            Initialization,
            LobbyMode,
            GameMode,
        }
        
        /**
         * Client socket object
         */
        private Socket clientSocket;
        
        /**
         * Object input stream for the socket
         */
        private ObjectInputStream objInputStream;
        
        /**
         * Object output stream for the socket
         */
        private ObjectOutputStream objOutputStream;
        
        /**
         * Value of the username
         */
        private String username;
        
        /**
         * Name of the lobby game the user is currently in. Set to null
         * if the user is not in a lobby game.
         */
        private String lobbyGameName;
        
        /**
         * Name of the in-progress game the user is currently in. Set to null
         * if the user is not in a lobby game.
         */
        private String inProgressGameName;
        
        /**
         * Indicates the mode the user is currently in
         */
        private UserMode currentMode;
        
        /**
         * 
         * @param socket
         */
        public ServerConnectionHandler(Socket socket) {
            this.clientSocket = socket;
            this.username = this.lobbyGameName = this.inProgressGameName = null;
            this.currentMode = UserMode.Initialization;
        }
        
        /**
         * Main entry point of the connection handler
         */
        @Override
        public void run() {
            
            boolean isLogonSuccessful = false;
            
            Thread lobbyPushThread = null;
            
            try {
                // Read the username
                objInputStream = 
                           new ObjectInputStream(clientSocket.getInputStream());
                
                username = (String) objInputStream.readObject();
                
                // Limit the username to 16 characters
                if (username.length() > MAX_USERNAME_LENGTH) {
                    username = username.substring(0,  MAX_USERNAME_LENGTH);
                }
                

                // Attempt a logon of username
                objOutputStream = 
                         new ObjectOutputStream(clientSocket.getOutputStream());
               
                // Check whether the username is valid
                synchronized (userList) {
                    if (userList.contains(username)) {
                        objOutputStream.writeObject(LOGIN_FAILURE_MSG);
                    } else {
                        userList.add(username);
                        System.out.println("'" + username + "' logged on!"); 
                        
                        objOutputStream.writeObject(LOGIN_SUCCESS_MSG);
                        
                        isLogonSuccessful = true;
                    }
                }
                
                objOutputStream.flush();
                
                // Make sure logon was successful
                if (isLogonSuccessful) {
                    
                    // Handle lobby and game stuff
                    

                    // Start thread that will push lobby changes to the user
                    lobbyPushThread = new Thread() {
                        public void run() {
                            handleLobbyPush();
                        }
                    };
                    
                    lobbyPushThread.start();
                    
                    handleLobby();

                    // End the lobby push thread
                    lobbyPushThread.interrupt();
                    lobbyPushThread.join(1000);
                    
                } else {
                    System.out.println("Logon attempt by '" + username +
                                       "' failed!");
                }
                
                clientSocket.close();
                
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                
                if (lobbyPushThread != null && lobbyPushThread.isAlive()) {
                    lobbyPushThread.interrupt();
                }
            }
            
            
            // Log the user off the system if the user is logged on
            // This code needs to be outside of the try/catch block 
            // so that the user will always be logged off the system, 
            // even when an exception is thrown
            if (isLogonSuccessful) {
                logoffUser(username, lobbyGameName, inProgressGameName);
            }
            
            numberConnections.getAndDecrement();
        }
        
        /**
         * Manages all of the user interaction with the lobby.
         * @throws Exception
         */
        private void handleLobby() throws Exception {
            currentMode = UserMode.LobbyMode;
            
            // Initially, server sends the client the current state of the
            // lobby
            synchronized (objOutputStream) {
                synchronized (lobbyGames) {
                    objOutputStream.writeObject(lobbyGames);
                    objOutputStream.flush();
                }
            }
  
            // Listen for client commands while the client is in the lobby. 
            // Also, check if the lobby has been modified. If so, rebroadcast
            // the lobby state to the client.
            while (currentMode == UserMode.LobbyMode) {
                String msg = (String) objInputStream.readObject();
                System.out.println(msg);

                String split[] = msg.split("\n");
                
                boolean isSuccessful = false;
                // Perform the appropriate actions in response to the message
                synchronized (lobbyGames) {
                    if (split[0].equals("Create Game")) {
                        
                        if (lobbyGameName == null) {
                            isSuccessful = addGame(split[1], split[2]);
                            
                            if (isSuccessful) {
                                lobbyGameName = split[1];
                            }
                        }
                        
                    } else if (split[0].equals("Join Game")) {
                        
                        if (lobbyGameName == null) {
                            isSuccessful = addPlayerToGame(split[1], split[2]);
                            if (isSuccessful) {
                                lobbyGameName = split[1];
                                
                                // Check to see if the game is now full. If so,
                                // start the game
                                if (isGameFull(lobbyGameName)) {
                                    startNewGame(lobbyGameName);
                                    currentMode = UserMode.GameMode;
                                    lobbyGameName = null;
                                }
                            }
                        }
                        
                    } else if (split[0].equals("Remove User from Game")) {
                        
                        isSuccessful = removePlayerFromGame(split[1], split[2]);
                        if (isSuccessful) {
                            lobbyGameName = null;
                        }
                    } else if (split[0].equals("Query")) { // DEBUGGING CODE
                        // TEMPORARY CODE FOR DEBUGGING
                        synchronized (objOutputStream) {
                            synchronized (lobbyGames) {
                                objOutputStream.reset();
                                objOutputStream.writeObject("Lobby");
                                objOutputStream.writeObject(lobbyGames);
                                objOutputStream.flush();
                            }
                        }
                        
                        continue;
                    }
                }
                
                String response = isSuccessful ? "Success" : "Failure";
                
                synchronized (objOutputStream) {
                    objOutputStream.writeObject(response);
                    objOutputStream.flush();
                }
            }
            
            System.out.println("Leaving lobby");
        }
     
        /**
         * Waits for notification of a change to the lobby. When the lobby
         * changes, send the update to the user.
         */
        private void handleLobbyPush() {
            while (!Thread.interrupted()) {
                try {
                    // Wait for a notification that the lobby has been 
                    // updated
                    synchronized (lobbyChangeNotifier) {
                        lobbyChangeNotifier.wait();
                    }
                    
                    // Send the lobby data to the user
                    synchronized (objOutputStream) {
                        synchronized (lobbyGames) {
                            objOutputStream.reset();
                            objOutputStream.writeObject(new String("Lobby"));
                            objOutputStream.writeObject(lobbyGames);
                            objOutputStream.flush();
                        }
                    }
                    
                } catch (InterruptedException e) {
                    // End the thread since it has been interrupted
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Ending lobby push thread.");
            
        }
        
    }
    
    /**
     * Maximum number of simultaneous connections
     */
    private static final int MAX_CONNECTIONS = 16;
    
    /**
     * Server port that allows connections
     */
    private static final int PORT = 8888;
    
    /**
     * Maximum username length
     */
    private static final int MAX_USERNAME_LENGTH = 16;
    
    /**
     * Message: too many connections
     */
    private static final String CONN_LIMIT_MSG = "Server handling too " +
                                                 "many users. Please " +
                                                 "try again later.";
    
    /**
     * Message: login success
     */
    private static final String LOGIN_SUCCESS_MSG = "Successfully logged on!";
    
    /**
     * Message: username in use
     */
    private static final String LOGIN_FAILURE_MSG = "Username in use!";
    
    
    /**
     * Current number of connections. An atomic integer since multiple threads
     * will be using the value
     */
    private static AtomicInteger numberConnections = new AtomicInteger(0);
    
    /**
     * Set that will store the usernames currently in progress.
     */
    private static Set<String> userList = new HashSet<String>();
    
    /**
     * Map that will store the games currently in the lobby. The key is the
     * String name of the game, and the value is the String array of players 
     * in the game (should be size 4).
     */
    private static Map<String, String[]> lobbyGames = 
                                                new HashMap<String, String[]>();
    
    
    /**
     * Object that is used to notify user threads that the lobby has changed
     */
    private static Object lobbyChangeNotifier = new Object();
    

    /**
     * Adds a new game to lobby with the name gameName and one player so far
     * with the name username. 
     * @param gameName
     * @param username
     * @return true if the game was added, false if the game could not be added.
     */
    private static boolean addGame(String gameName, String username) {
        boolean isGameAdded = false;
        
        // Make sure the game does not already exist
        if (!lobbyGames.containsKey(gameName)) {
            // Create a String array to hold the 4 players
            String playerArray[] = new String[4];
            
            // Initialize the first player to be username. The remaining 
            // players are set to null by default
            playerArray[0] = username;
            
            // Add the game to the map
            lobbyGames.put(gameName, playerArray);
            
            isGameAdded = true;
        } 
        
        if (isGameAdded) {
            notifyLobbyChanged();
        }
        
        return isGameAdded;
    }
    
    /**
     * Adds a player named username to a game in the lobby with the name
     * gameName.
     * @param gameName
     * @param username
     * @return true if the player was added to the game, false if the player
     *         could not be added to the game.
     */
    private static boolean addPlayerToGame(String gameName, String username) {
        boolean isPlayerAdded = false;
        
        // Make sure the game exists
        if (lobbyGames.containsKey(gameName)) {
            String playerArray[] = lobbyGames.get(gameName);
            
            // Make sure player array is not null (it should not be)
            if (playerArray != null) {
            
                // Add the player to the next available position, if any
                for (int i = 0; i < playerArray.length; i++) {
                    if (playerArray[i] == null) {
                        // Add the player to this available position
                        playerArray[i] = username;
                        isPlayerAdded = true;
                        
                        break;
                    }
                }
            }
        }
        
        if (isPlayerAdded) {
            notifyLobbyChanged();
        }
        
        return isPlayerAdded;
    }
    
    /**
     * Removes a player named username from a game in the lobby with the name
     * gameName.
     * @param gameName
     * @param username
     * @return true if the player was removed from the game, false if the player
     *         was not removed or was not part of the game.
     */
    private static boolean removePlayerFromGame(String gameName, 
                                                String username) {
        boolean isPlayerRemoved = false;
        boolean isGameEmpty = false;
        
        String playerArray[] = lobbyGames.get(gameName);
        
        if (playerArray != null) {
            // Remove username from the player array, shifting
            // everyone in the array down one place
            
            for (int i = 0; i < playerArray.length - 1 && 
                                playerArray[i] != null; i++) {
                if (!isPlayerRemoved) {
                    if (playerArray[i].equals(username)) {
                        
                        playerArray[i] = playerArray[i + 1];
                        isPlayerRemoved = true;
                        
                    }
                } else {
                    // Shift elements to the left now that username
                    // has been deleted
                    playerArray[i] = playerArray[i + 1];
                }
            }
            
            // If the player has been deleted, everything has been
            // shifted left one spot so the last position in the array
            // can be set to null. Also, if the player has not been
            // deleted but the last position holds the player (this
            // *should* always be the case), set the last position in
            // the array to null.
            if (isPlayerRemoved 
                || (playerArray[playerArray.length - 1] != null
                     && playerArray[playerArray.length - 1].equals(username))) {
                
                playerArray[playerArray.length - 1] = null;
                
                isPlayerRemoved = true;
            }
            
            // If the game is empty, delete the game
            if (playerArray[0] == null) {
                lobbyGames.remove(gameName);
            }
        }
        
        if (isPlayerRemoved) {
            notifyLobbyChanged();
        }
        
        return isPlayerRemoved;

    }
    
    /**
     * Determines whether or not a game with name gameName is completely full 
     * of players.
     * @param gameName
     * @return true if the game is full, false if the game does not exist or
     *         is not full.
     */
    private static boolean isGameFull(String gameName) {
        boolean isGameFull = true;
        
        // Make sure the game exists
        if (lobbyGames.containsKey(gameName)) {
            String playerArray[] = lobbyGames.get(gameName);
            
            // Make sure player array is not null (it should not be)
            if (playerArray != null) {
                
                // Iterate through the player array. If at any point a null
                // value is reached, there is room for another player and
                // the game is not full.
                for (int i = 0; i < playerArray.length; i++) {
                    if (playerArray[i] == null) {
                        isGameFull = false;
                        
                        break;
                    }
                }
            }
        } else {
            // The game does not exist, so just return false
            isGameFull = false;
        }
        
        return isGameFull;
    }
    
    /**
     * Removes a game named gameName from the lobby and starts the game.
     * @param gameName
     */
    private static void startNewGame(String gameName) {
        // Remove the game from the lobby
        String playerArray[] = lobbyGames.remove(gameName);
        
        System.out.print("Starting the game '" + gameName + "' with the " +
                           "players ");
        System.out.printf("%s, %s, %s, and %s.\n", playerArray[0], 
                          playerArray[1], playerArray[2], playerArray[3]);
        
        
        notifyLobbyChanged();
        // TODO: Actually start the new game
    }
    
    /**
     * Notifies all of the users that the lobby has changed
     */
    private static void notifyLobbyChanged() {
        synchronized (lobbyChangeNotifier) {
            lobbyChangeNotifier.notifyAll();
        }
    }
    
    /**
     * Logs the user with name username off the system. If the user was in a
     * game currently in the lobby, remove the user from that game. If the user 
     * was in an in-progress game, end that game.
     * @param username
     * @param lobbyGame
     */
    private static void logoffUser(String username, String lobbyGame,
                                   String inProgressGame) {
        // Check if the user is in a lobby game. If so, remove the user from
        // that game.
        if (lobbyGame != null) {
            synchronized (lobbyGames) {
                removePlayerFromGame(lobbyGame, username);
            }
        }
        
        // Check if the user is in an in progress game. If so, end the game.
        if (inProgressGame != null) {
            // TODO
        }
        
        // Remove the user from the list of users
        synchronized (userList) {
            userList.remove(username);
        }
        
        System.out.println("Successfully logged off user " + username + "!");
    }
    
    
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Server starting...");

        // Open the server socket
        int listenBacklog = 5;
        ServerSocket parentSocket = new ServerSocket(PORT, listenBacklog);
        
        String n[] = {"t1", "t2", "t3", "t4"};
        lobbyGames.put("Game 1", n);
        String n2[] = {"Austin", "Daniel", "John", "Kevin"};
        lobbyGames.put("Ultimate Showdown", n2);
        String n3[] = {"p1", null, null, null};
        lobbyGames.put("Open Game",  n3);
        
        // Accept connections and start new threads for each connection
        while (true) {
            Socket childSocket = parentSocket.accept();
            
            // Check whether the maximum number of connections is being used
            if (numberConnections.get() < MAX_CONNECTIONS) {
                numberConnections.getAndIncrement();
                
                (new Thread(new ServerConnectionHandler(childSocket))).start();
                
            } else {
                // Notify the client that it cannot be served at this moment
                ObjectOutputStream outputStream = new ObjectOutputStream(
                                                 childSocket.getOutputStream());
                outputStream.writeObject(CONN_LIMIT_MSG);
                outputStream.flush();
                
                childSocket.close();
            }
        }
    }

}