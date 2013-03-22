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
public class Server {

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
                
                DataOutputStream outputStream = new DataOutputStream(
                        clientSocket.getOutputStream());

                if (userList.contains(username)) {
                    outputStream.writeBytes(LOGIN_FAILURE_MSG);
                } else {
                    userList.add(username);
                    System.out.println(username);  
                    outputStream.writeBytes(LOGIN_SUCCESS_MSG);
                    
                    Thread.sleep(10000);
                    
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
     * Synchronized set that will store the usernames currently in progress.
     */
    private static Set<String> userList = 
                             Collections.synchronizedSet(new HashSet<String>());
    
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