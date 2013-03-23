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
        Socket clientSocket;
        
        /**
         * Value of the username
         */
        String username;
        
        /**
         * 
         * @param socket
         */
        public ServerConnectionHandler(Socket socket) {
            this.clientSocket = socket;
        }
        
        /**
         * Main entry point of the connection handler
         */
        @Override
        public void run() {
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

                boolean isLogonSuccessful = false;
               
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
                
                // Logon was not successful. End the thread
                if (!isLogonSuccessful) {
                    System.out.println("Logon attempt by '" + username +
                                       "' failed!");
                    
                    clientSocket.close();
                    
                    return;
                }
                
               
                
                
                // Handle lobby and game stuff
                // handleSession
                Thread.sleep(10000);          

                
                
                // Returned from handleSession, so know that the user is done
                
                
                // Remove the user from the list of users
                synchronized (userList) {
                    // TODO: put in a cleanup method that removes user from
                    // games and lobby if necessary - modify lobby and/or kill
                    // a game
                    userList.remove(username);
                }
                
                clientSocket.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            
            numberConnections.getAndDecrement();
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
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Server starting...");

        // Open the server socket
        int listenBacklog = 5;
        ServerSocket parentSocket = new ServerSocket(PORT, listenBacklog);
        
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