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
        
        /**
         * Client socket object
         */
        private Socket clientSocket;
        
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
         * 
         * @param socket
         */
        public ServerConnectionHandler(Socket socket) {
            this.clientSocket = socket;
            this.username = this.lobbyGameName = this.inProgressGameName = null;
        }
        
        /**
         * Main entry point of the connection handler
         */
        @Override
        public void run() {
            
            boolean isLogonSuccessful = false;
            
            try {
                // Read the username
                BufferedReader inputReader = new BufferedReader(
                          new InputStreamReader(clientSocket.getInputStream()));
                
                username = inputReader.readLine();
                
                // Limit the username to 16 characters
                if (username.length() > MAX_USERNAME_LENGTH) {
                    username = username.substring(0,  MAX_USERNAME_LENGTH);
                }
                

                // Attempt a logon of username
                
                DataOutputStream outputStream = new DataOutputStream(
                        clientSocket.getOutputStream());
               
                // Check whether the username is valid
                synchronized (userList) {
                    if (userList.contains(username)) {
                        outputStream.writeBytes(LOGIN_FAILURE_MSG);
                    } else {
                        userList.add(username);
                        System.out.println("'" + username + "' logged on!"); 
                        
                        outputStream.writeBytes(LOGIN_SUCCESS_MSG);
                        
                        isLogonSuccessful = true;
                    }
                }
                
                outputStream.flush();
                
                // Make sure logon was successful
                if (isLogonSuccessful) {
                    
                    // Handle lobby and game stuff
                    

                    handleLobby();

                    
                } else {
                    System.out.println("Logon attempt by '" + username +
                                       "' failed!");
                }
                
                clientSocket.close();
                
            } catch (Exception e) {
                System.out.println(e.getMessage());
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
            ObjectOutput objOutputStream = 
                         new ObjectOutputStream(clientSocket.getOutputStream());
            
            // Initially, server sends the client the current state of the
            // lobby
            synchronized (lobbyGames) {
                objOutputStream.writeObject(lobbyGames);
                objOutputStream.flush();
            }
            
            clientSocket.setSoTimeout(100);
            
            // Listen for client commands. Also, check if the lobby has been
            // modified. If so, rebroadcast the lobby state to the client.
            //try {
              //  clientSocket
           // }// catch (SocketTimeoutException e) {
                
            //}
            
        }
        
    }
    
    /**
     * Maximum number of simultaneous connections
     */
    private static final int MAX_CONNECTIONS = 10;
    
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
                                                 "try again later.\n";
    
    /**
     * Message: login success
     */
    private static final String LOGIN_SUCCESS_MSG = "Successfully logged on!\n";
    
    /**
     * Message: username in use
     */
    private static final String LOGIN_FAILURE_MSG = "Username in use!\n";
    
    
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
        
        return isGameAdded;
    }
    
    /**
     * Adds a player named username to a game in the lobby with the name
     * gameName
     * @param username
     * @param gameName
     * @return true if the player was added to the game, false if the player
     *         could not be added to the game
     */
    private static boolean addPlayerToGame(String username, String gameName) {
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
        
        return isPlayerAdded;
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
                String playerArray[] = lobbyGames.get(lobbyGame);
                
                if (playerArray != null) {
                    // Remove username from the player array, shifting
                    // everyone in the array down one place
                    boolean isPlayerDeleted = false;
                    
                    for (int i = 0; i < playerArray.length - 1; i++) {
                        if (!isPlayerDeleted) {
                            if (playerArray[i].equals(username)) {
                                
                                playerArray[i] = playerArray[i + 1];
                                isPlayerDeleted = true;
                                
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
                    if (isPlayerDeleted ||
                        playerArray[playerArray.length - 1].equals(username)) {
                        
                        playerArray[playerArray.length - 1] = null;
                    }
                }
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
        
        // Accept connections and start new threads for each connection
        while (true) {
            Socket childSocket = parentSocket.accept();
            
            // Check whether the maximum number of connections is being used
            if (numberConnections.get() < MAX_CONNECTIONS) {
                numberConnections.getAndIncrement();
                
                (new Thread(new ServerConnectionHandler(childSocket))).start();
                
            } else {
                // Notify the client that it cannot be served at this moment
                DataOutputStream outputStream = new DataOutputStream(
                                                 childSocket.getOutputStream());
                outputStream.writeBytes(CONN_LIMIT_MSG);
                
                childSocket.close();
            }
        }
    }

}