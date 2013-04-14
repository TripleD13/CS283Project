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
      
    /**
     * Maximum number of simultaneous connections
     */
    private static final int MAX_CONNECTIONS = 16;
    
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
     * Set that will store the usernames currently in use
     */
    private static Map<String, ServerConnectionHandler> userList = 
                                 new HashMap<String, ServerConnectionHandler>();
    
    /**
     * Map that will store the games currently in the lobby. The key is the
     * String name of the game, and the value is the String array of players 
     * in the game (should be size 4).
     */
    private static Map<String, String[]> lobbyGames = 
                                                new HashMap<String, String[]>();
    
    /**
     * Map that will store the games currently in progress. The key is the
     * String name of the game, and the value is the game object.
     */
    private static Map<String, Object> inProgressGames = 
                                                  new HashMap<String, Object>();
    
    
    /**
     * Object that is used to notify user threads that the lobby has changed
     */
    private static Object lobbyChangeNotifier = new Object();
    
    
    /**
     * Timestamp when the server started (used for debugging purposes) 
     */
    private static long serverStartTime;

    
    /**
     * @param args
     * @throws Exception
     */
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        // Record the starting timestamp and set the thread name for debugging 
        // purposes
        serverStartTime = (new Date()).getTime();
        Thread.currentThread().setName("Main Thread");
        
        // Read the port from the command line arguments
        if (args.length != 1) {
            System.out.println("Usage: <Port>");
            System.exit(0);
        }
        
        // Open the server socket
        ServerSocket parentSocket = null;
        
        try {
            int listenBacklog = 10;
            // Create the socket. Address 0.0.0.0 indicates that the socket
            // will accept connections on any of the interfaces of the server
            parentSocket = 
                      new ServerSocket(Integer.parseInt(args[0]), listenBacklog,
                                       InetAddress.getByName("0.0.0.0"));
        } catch (IOException e) {
            System.out.println("Unable to open server socket: " +
                               e.getMessage());
            System.exit(0);
        } catch (NumberFormatException e) {
            System.out.println("Invalid port: " + args[0]);
            System.exit(0);
        } catch (IllegalArgumentException e) {
            System.out.println("Port not in range: " + args[0]);
            System.exit(0);
        }
        
        System.out.println("Server starting...\n");
        
        // Code for testing purposes. These players are not actually playing.
        String sampleNames[] = {"Austin", "Daniel", "Kevin", null};
        lobbyGames.put("Ultimate Showdown", sampleNames);
        
        int totalThreadsCreated = 0; // Counter used for debugging purposes
        
        // Accept connections and start new threads for each connection
        while (true) {
            try {
                Socket childSocket = parentSocket.accept();
                
                // Check whether the maximum number of connections is being used
                if (numberConnections.get() < MAX_CONNECTIONS) {
                    numberConnections.getAndIncrement();
                    
                    totalThreadsCreated++;
                    
                    Thread connectionThread = 
                           new Thread(new ServerConnectionHandler(childSocket));
                    
                    // Set name for debugging purposes
                    connectionThread.setName("Handler Thread " +
                                             totalThreadsCreated);
                    
                    connectionThread.start();
                    
                } else {
                    // Notify the client that it cannot be served at this moment
                    ObjectOutputStream outputStream = new ObjectOutputStream(
                                                 childSocket.getOutputStream());
                    outputStream.writeObject(CONN_LIMIT_MSG);
                    outputStream.flush();
                    
                    childSocket.close();
                }
            } catch (IOException e) {
                printServerMsg("Error accepting new connection: " +
                                   e.getMessage());
            }
        }
    }
    
    /**
     * Prints a message, prepended with a timestamp and thread id.
     * @param message
     */
    private static void printServerMsg(String message) {
        double timestamp = 0.001 * ((new Date().getTime()) - serverStartTime);
        String threadName = Thread.currentThread().getName();
        
        System.out.format("[time=%.4f, thread='%s']\t%s\n", timestamp,
                          threadName, message);
    }

    
    /**
     * Adds a new game to lobby with the name gameName and one player so far
     * with the name username. Note: responsibility of caller to synchronize
     * access to lobbyGames.
     * @param gameName
     * @param username
     * @return true if the game was added, false if the game could not be added.
     */
    private static boolean addGame(String gameName, String username) {
        boolean isGameAdded = false;
        
        // Make sure the game does not already exist
        if (!lobbyGames.containsKey(gameName) && 
            !inProgressGames.containsKey(gameName)) {
            
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
     * gameName. Note: responsibility of caller to synchronize access to
     * lobbyGames.
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
     * gameName. Note: responsibility of sender to synchronize access to
     * lobbyGames.
     * @param gameName
     * @param username
     * @return true if the player was removed from the game, false if the player
     *         was not removed or was not part of the game.
     */
    private static boolean removePlayerFromGame(String gameName, 
                                                String username) {
        boolean isPlayerRemoved = false;
        
        String playerArray[] = lobbyGames.get(gameName);
        
        if (playerArray != null) {
            // Remove username from the player array, shifting
            // everyone in the array down one place
            
            for (int i = 0; i < playerArray.length && 
                                playerArray[i] != null; i++) {
                
                // If this element is not the last element, the next element
                // is at position i + 1. Otherwise, the next element is null
                String nextPlayer = i < playerArray.length - 1 
                              ? playerArray[i + 1] : null;
                              
                if (!isPlayerRemoved) {
                    if (playerArray[i].equals(username)) {
                        
                        playerArray[i] = nextPlayer;
                        isPlayerRemoved = true;
                        
                    }
                } else {
                    // Shift elements to the left now that username
                    // has been deleted
                    playerArray[i] = nextPlayer;
                }
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
     * of players. Note: responsibility of sender to synchronize access to
     * lobbyGames.
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
     * Removes a game named gameName from the lobby and starts the game. Note:
     * responsibility of sender to synchronize access to lobbyGames.
     * @param gameName
     */
    private static void startNewGame(String gameName) {
        // Remove the game from the lobby and add to in progress games
        String playerNameArray[] = lobbyGames.remove(gameName);
        inProgressGames.put(gameName, new Object());
        
        System.out.print("Starting the game '" + gameName + "' with the " +
                           "players ");
        System.out.printf("'%s', '%s', '%s', and '%s'.\n", playerNameArray[0], 
                          playerNameArray[1], playerNameArray[2], 
                          playerNameArray[3]);
        
        
        notifyLobbyChanged();

        // Start a new game
        
        // Create the player array
        Player playerArray[] = new Player[4];
        
        for (int i = 0; i < playerArray.length; i++) {
            playerArray[i] = new Player(playerNameArray[i], 
                                        userList.get(playerNameArray[i]));
        }
        
        // Create the Catan game
        ServerCatanGame game = new ServerCatanGame(playerArray);
        
        for (int i = 0; i < playerArray.length; i++) {
            playerArray[i].getPlayerHandler().setCatanGame(game);
        }
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
        
        printServerMsg("Successfully logged off user '" + username + "'!");
    }
    
     
    /**
     * Connection handler class that manages one user.
     * @author John
     *
     */
    public static class ServerConnectionHandler implements Runnable {
        
        /**
         * Enum that represents the possible modes the user could be in
         *
         */
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
         * if the user is not in an in-progress game.
         */
        private String inProgressGameName;
        
        /**
         * Catan game object
         */
        private ServerCatanGame catanGame;
        
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
            this.catanGame = null;
            this.currentMode = UserMode.Initialization;
        }
        
        /**
         * Set the Catan game object
         * @param game
         */
        public void setCatanGame(ServerCatanGame game) {
            // TODO: SYNCHRONIZATION!!!
            this.catanGame = game;
        }
        
        /**
         * Main entry point of the connection handler
         */
        @Override
        public void run() {
            
            boolean isLogonSuccessful = false;
            
            Thread lobbyPushThread = null;
            
            try {
                
                // Create the input and output streams
                objInputStream = 
                        new ObjectInputStream(clientSocket.getInputStream());
                
                objOutputStream = 
                        new ObjectOutputStream(clientSocket.getOutputStream());
                
                
                // Read the username sent from the client
                
                username = (String) objInputStream.readObject();
                
                // Limit the username to 16 characters
                if (username.length() > MAX_USERNAME_LENGTH) {
                    username = username.substring(0,  MAX_USERNAME_LENGTH);
                }
                

                // Attempt a logon of username. First check ensure that the
                // username is available
                synchronized (userList) {
                    if (!userList.containsKey(username)) {
                        userList.put(username, this);
                        
                        isLogonSuccessful = true;
                    }
                }
                
                // Make sure logon was successful
                if (isLogonSuccessful) {
                    
                    printServerMsg("'" + username + "' logged on!"); 
                    
                    objOutputStream.writeObject(LOGIN_SUCCESS_MSG);
                    objOutputStream.flush();
                    
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
                    
                    // Handle the game
                    handleGame();
                    
                } else {
                    printServerMsg("Logon attempt by '" + username +
                                       "' failed!");
                    
                    objOutputStream.writeObject(LOGIN_FAILURE_MSG);
                    objOutputStream.flush();
                }  
                
            } catch (Exception e) {
                printServerMsg("Connection problem with '" + username + 
                                   "': " + e.getMessage());
                
                if (lobbyPushThread != null && lobbyPushThread.isAlive()) {
                    lobbyPushThread.interrupt();
                }
            }
            
            
            // Attempt to close the socket
            try {
                clientSocket.close();
            } catch (Exception e) {
                // Just ignore error
            }
            
            
            // Log the user off the system if the user is logged on
            // This code needs to be outside of the try/catch block 
            // so that the user will always be logged off the system, 
            // even when an exception is thrown
            if (isLogonSuccessful) {
                logoffUser(username, lobbyGameName, inProgressGameName);
            }
            
            // Decrease the number of connections
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
                    objOutputStream.writeObject("Lobby");
                    objOutputStream.writeObject(lobbyGames);
                    objOutputStream.flush();
                }
            }
  
            // Listen for client commands while the client is in the lobby.
            while (currentMode == UserMode.LobbyMode) {
                // Receive command from the client
                String msg = (String) objInputStream.readObject();
                printServerMsg("Messaged received.");
                System.out.println("\n=========RECEIVED MESSAGE=========");
                System.out.println(msg);
                System.out.println("==================================\n");

                String split[] = msg.split("\n");
                
                boolean isSuccessful = false;
                String failureMsg = null;
                
                // Perform the appropriate actions in response to the message
                synchronized (lobbyGames) {
                    if (split[0].equals("Create Game")) {
                        
                        if (lobbyGameName == null) {
                            if (addGame(split[1], split[2])) {
                                lobbyGameName = split[1];
                                isSuccessful = true;
                            }
                        }
                        
                        if (!isSuccessful) {
                            failureMsg = "Failed to create game '" +
                                    split[1] + "'";
                        }
                        
                    } else if (split[0].equals("Join Game")) {
                        
                        if (lobbyGameName == null) {
                            if (addPlayerToGame(split[1], split[2])) {
                                lobbyGameName = split[1];
                                isSuccessful = true;
                                
                                // Check to see if the game is now full. If so,
                                // start the game
                                if (isGameFull(lobbyGameName)) {
                                    startNewGame(lobbyGameName);
                                }
                            }
                        }
                        
                        if (!isSuccessful) {
                            failureMsg = "Failed to add user '" +
                                    split[2] + "' to game '" +
                                    split[1] + "'";
                        }
                        
                    } else if (split[0].equals("Remove User from Game")) {
                        
                        if (removePlayerFromGame(split[1], split[2])) {
                            lobbyGameName = null;
                            isSuccessful = true;
                        } 
                        
                        if (!isSuccessful) {
                            failureMsg = "Failed to remove user '" +
                                         split[2] + "' from game '" +
                                         split[1] + "'";
                        }
                    } else if (split[0].equals("Begin Game")) {
                        // Make sure game can start
                        if (lobbyGameName != null &&
                            inProgressGames.containsKey(lobbyGameName)) {
                            
                            currentMode = UserMode.GameMode;
                            lobbyGameName = null;
                            
                            isSuccessful = true;
                        }
                        
                        if (!isSuccessful) {
                            failureMsg = "Failed to begin playing game '" +
                                         split[1] + "'";
                        }
                    } else if (split[0].equals("Query")) { // DEBUGGING CODE
                        // TEMPORARY CODE FOR DEBUGGING
                        synchronized (objOutputStream) {
                            objOutputStream.reset();
                            objOutputStream.writeObject("Lobby");
                            objOutputStream.writeObject(lobbyGames);
                            objOutputStream.flush();
                        }
                        
                        continue;
                    }
                }
                
                String response = isSuccessful ? "Success" : failureMsg;
                
                synchronized (objOutputStream) {
                    objOutputStream.writeObject(response);
                    objOutputStream.flush();
                }
            }
            
            printServerMsg("'" + username + "' leaving lobby");
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
                    synchronized (lobbyGames) {
                        synchronized (objOutputStream) {
                            objOutputStream.reset();
                            objOutputStream.writeObject(new String("Lobby"));
                            objOutputStream.writeObject(lobbyGames);
                            objOutputStream.flush();
                            
                            
                            // Check if lobby game has been move to in progress
                            // games
                            if (lobbyGameName != null &&
                                inProgressGames.containsKey(lobbyGameName)) {
                                
                                // Send message to the client indicating that
                                // the game is starting
                                objOutputStream.writeObject("Game starting");
                                objOutputStream.flush();
                            }
                        }
                    }
                    
                } catch (InterruptedException e) {
                    // End the thread since it has been interrupted
                    break;
                } catch (Exception e) {
                    // Just continue loop
                }
            }

            printServerMsg("Ending lobby push thread.");
            
        }
        
        /**
         * Manages all of the user interaction with the game, including chat.
         * @throws Exception
         */
        private void handleGame() throws Exception {
            // Initially the game sends a chat message to the client welcoming
            // it to the game
            synchronized(objOutputStream) {
                objOutputStream.writeObject("chat*SERVER: Welcome!");
                objOutputStream.flush();
            }
            
            while (currentMode == UserMode.GameMode) {
                // Receive a message
                String msg = (String) objInputStream.readObject();
                
                printServerMsg("Messaged received.");
                System.out.println("\n=========RECEIVED MESSAGE=========");
                System.out.println(msg);
                System.out.println("==================================\n");
                
                if (msg.startsWith("chat*")) { // Chat message received
                    
                    Player playerArray[] = catanGame.getPlayerArray();
                    
                    int firstIndex = msg.indexOf("/*/");
                    //if this is a targeted message
                    if (firstIndex != -1) {
                        int lastIndex = msg.indexOf("*/*");
                        String address = msg.substring(firstIndex+3, 
                                                           lastIndex);
                        
                        //match address with user's socket
                        ServerConnectionHandler target = null;  
                        
                        // Find the target ServerConnectionHandler to which the
                        // message will be routed
                        if (playerArray != null) {
                            for (int i = 0; i < playerArray.length; i++) {
                                if (playerArray[i].getUsername()
                                    .equals(address)) {
                                    
                                    target = playerArray[i].getPlayerHandler();
                                    break;
                                }
                            }
                        }
                        
                        //attach chat* command to send out
                        String chatCommand = "chat*";
                        String messageToSend = new String(msg.substring(0, 
                                                                   firstIndex));
                        messageToSend = messageToSend.concat(msg
                                                       .substring(lastIndex+3));
                        //messageToSend = chatCommand.concat(messageToSend);
                        
                        if (target != null) {
                            // Send the message to the target and the sender
                            int colonIndex = messageToSend.indexOf(":");
                            String senderConfirmation = 
                                            String.format("%s: \"%s\" %s",
                                       messageToSend.substring(0, colonIndex),
                                       address,
                                       messageToSend.substring(colonIndex + 2));
                            
                            sendChatMessage(senderConfirmation);
                            target.sendChatMessage(messageToSend);
                        } else {
                            // Send an error message to the client
                            sendChatMessage("chat*SERVER: User '" + address +
                                            "' was not found!");
                        }
                        
                    }else
                    {
                        //not a targeted chat signal
                        String messageToSend = msg;
                        
                        //Iterate through all sockets and send
                        if (playerArray != null) {
                            for (int i = 0; i < playerArray.length; i++) {
                                ServerConnectionHandler handler = 
                                              playerArray[i].getPlayerHandler();
                                
                                if (handler != null) {
                                    handler.sendChatMessage(messageToSend);
                                }
                            }
                        }
                    }
                    
                }
            }
        }
        
        /**
         * Send a chat message to the client.
         * @param message
         */
        public void sendChatMessage(String message) throws Exception {
            printServerMsg("Sending chat message to " + username + ": " + 
                           message);
            synchronized (objOutputStream) {
                objOutputStream.writeObject(message);
                objOutputStream.flush();
            }
        }
        
    }
}