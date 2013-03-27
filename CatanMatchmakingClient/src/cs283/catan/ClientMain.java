/**
 * Main game client class. Allows the user to logon to the server. Basic
 * TCP client code based on code from CS283 homepage.
 */
package cs283.catan;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;
import javax.swing.UIManager.*;

/**
 * @author John
 *
 */
public class ClientMain {

    /**
     * Server IP address
     */
    private static final String SERVER_IP = "127.0.0.1";
    
    /**
     * Server port
     */
    private static final int PORT = 8888;
    
    /**
     * Maximum username length
     */
    private static final int MAX_USERNAME_LENGTH = 16;
    
    /**
     * Current username of the user logged in
     */
    private static String username;
    
    
    /**
     * TCP socket
     */
    private static Socket clientSocket;
    
    /**
     * Object input stream used to receive data from the socket
     */
    private static ObjectInputStream objInputStream;
    
    /**
     * Object output stream used to send data on the socket
     */
    private static ObjectOutputStream objOutputStream;
    
    
    /**
     * Map that will store the games currently in the lobby. The key is the
     * String name of the game, and the value is the String array of players 
     * in the game (should be size 4).
     */
    private static Map<String, String[]> lobbyGames = 
                                                new HashMap<String, String[]>();
    
    
    /**
     * Handle to the receiver thread
     */
    private static Thread receiverThread = null;
    
    
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        
        // Nicer look and feel
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Leave default look and feel
        }
        
        // Logon loop
        boolean logonSuccessful = false;
        
        do {
            
            // Create the login attempt dialog
            JFrame frame = new JFrame("Login");
            frame.setUndecorated(true);
            frame.setVisible(true);
            frame.setAlwaysOnTop(true);
            frame.setLocationRelativeTo(null);
            
            username = JOptionPane.showInputDialog(frame, "Please enter your " +
                                                          "user name (" + 
                                                          "maximum of 16 " + 
                                                          "characters)");
            
            if (username == null) {
                System.exit(0);
            }
            
            // Get rid of whitespace
            username = username.trim();
            
            if (!username.equals("")) {
                if (username.length() <= MAX_USERNAME_LENGTH) {
                    String message = attemptLogon(username);
                    if (message.equals("Successfully logged on!")) {
                        logonSuccessful = true;
                    } else {
                        JOptionPane.showMessageDialog(frame, message);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, 
                                                  "Username '" + username +
                                                  "' exceeds the 16 character" +
                                                  " limit. Please enter a" +
                                                  " valid username!");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a name");
            }
            
            frame.dispose();
        } while (!logonSuccessful);
        
        // Check to make sure the user is logged on
        if (logonSuccessful) {
            // Enter lobby mode
            lobbyMode();
        }

        // Terminate the receiver thread (calling interrupt() may be unnecessary
        //                                since closing the socket should signal
        //                                to the thread to exit)
        if (receiverThread != null) {
            receiverThread.interrupt();
        }
        
        // Close the connection to the server
        clientSocket.close();
        
        // Wait for the receiver thread to end
        if (receiverThread != null) {
            receiverThread.join(1000);
        }
    }

    /**
     * Attempts to contact the server and logon with the given username
     * @param username
     * @return the message from the server
     */
    private static String attemptLogon(String username) {
        
        
        // Attempt to connect to the server
        String message = null;
        
        try {
            InetAddress serverIP = InetAddress.getByName(SERVER_IP);
            
            // If socket previously opened, close it
            if (clientSocket != null) {
                clientSocket.close();
            }
            
            clientSocket = new Socket(serverIP, PORT);
            
            // Send the username to the server
            objOutputStream = 
                         new ObjectOutputStream(clientSocket.getOutputStream());
            
            objOutputStream.writeObject(username);
            objOutputStream.flush();
            
            // Read the message from the server
            objInputStream =
                           new ObjectInputStream(clientSocket.getInputStream());
            
            message = (String) objInputStream.readObject();
            
        } catch (Exception e) {
            e.printStackTrace();
            return "Unable to connect to game server: " + e.getMessage();
        }
       
        if (message == null) {
            message = "No server response.";
        }
        
        return message;
    }
    
    /**
     * Manage the lobby mode.
     */
    @SuppressWarnings("unchecked")
    private static void lobbyMode() {
        Scanner scanner = new Scanner(System.in);
        
        try {
            // Receive the initial lobby data from the server
            lobbyGames = (Map<String, String[]>) objInputStream.readObject();
            
            printLobby();
            
            
            // Start the receiver thread
            receiverThread = new Thread(new ClientReceiver());
            receiverThread.start();
            
            String input;
            
            do {
                input = scanner.nextLine();
                
                if (input.toLowerCase().equals("new game")) {
                    
                    System.out.print("Please enter the name of the game: ");
                    String gameName = scanner.nextLine();
                    sendCreateGameMsg(gameName);
                    
                } else if (input.toLowerCase().equals("join game")) {
                    
                    System.out.print("Please enter the name of the game: ");
                    String gameName = scanner.nextLine();
                    sendAddUserToGameMsg(gameName);
                    
                } else if (input.toLowerCase().equals("exit game")) {
                    
                    System.out.print("Please enter the name of the game: ");
                    String gameName = scanner.nextLine();
                    sendRemoveUserFromGameMsg(gameName);
                    
                } else if (input.toLowerCase().equals("query")) {
                    // TEMPORARY CODE FOR DEBUGGING PURPOSES
                    objOutputStream.writeObject(new String("Query"));
                    objOutputStream.flush();
                    
                }
                
            } while (!input.toLowerCase().equals("quit"));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        scanner.close();
    }
    
    
    /**
     * Print out the current state of the lobby
     */
    private static void printLobby() {
        System.out.println("==============Lobby Games===============\n");
        
        // Iterate through each game in the lobby and print it out
        for (Map.Entry<String, String[]> entry : lobbyGames.entrySet()) {
            System.out.println("Game: '" + entry.getKey() + "'");
            
            // Print out the players in the game
            for (int i = 0; i < entry.getValue().length; i++) {
                System.out.print("Player " + (i + 1)  + ": ");
                if (entry.getValue()[i] != null) {
                    System.out.print("'" + entry.getValue()[i] + "'");
                } else {
                    System.out.print("N/A");
                }
                
                if (i != entry.getValue().length - 1) {
                    System.out.print("    ");
                }
                
                if (i % 2 == 1) {
                    System.out.println();
                }
            }
            
            System.out.println("\n");
        }
        
        System.out.println("========================================");
    }
    
    /**
     * Sends a message to the server requesting to add the current user
     * to the game with gameName.
     * @param gameName
     */
    private static void sendAddUserToGameMsg(String gameName) throws Exception {
        // Send the request to the server
        String msg = "Join Game\n" + gameName + "\n" + username;
        
        objOutputStream.writeObject(msg);
        objOutputStream.flush();
    }
    
    /**
     * Sends a message to the server requesting to remove the current user from
     * the game with gameName.
     * @param gameName
     */
    private static void sendRemoveUserFromGameMsg(String gameName)
                                                 throws Exception {
        // Send the request to the server
        String msg = "Remove User from Game\n" + gameName + "\n" + username;
        
        objOutputStream.writeObject(msg);
        objOutputStream.flush();
    }
    
    /**
     * Sends a message to the server requesting to create the game with gameName
     * and user username.
     * @param gameName
     */
    private static void sendCreateGameMsg(String gameName) throws Exception {
        // Send the request to the server
        String msg = "Create Game\n" + gameName + "\n" + username;
        
        objOutputStream.writeObject(msg);
        objOutputStream.flush();
    }
    
    /**
     * Class that handles the receiving of network data in a separate thread
     * @author John
     *
     */
    @SuppressWarnings("unchecked")
    private static class ClientReceiver implements Runnable {
        
        /**
         * Run the receiver thread
         */
        @Override
        public void run() {
            String message;
            
            while (!Thread.interrupted()) {
                try {
                    message = (String) objInputStream.readObject();
                    
                    if (message.equals("Lobby")) {
                        lobbyGames = (Map<String, String[]>) 
                                objInputStream.readObject();
                   
                        printLobby();
                    } else {
                        System.out.println("RESPONSE! " + message);
                    }
                } catch (InterruptedIOException e) {
                    // The thread was interrupted, so exit the thread
                    break;
                } catch (EOFException e) {
                    // The server must have closed its connection, so exit
                    // the thread
                    System.out.println("Receiver can no longer receive from " +
                                       "server.");
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            System.out.println("Ending receiver thread");
        }
       
    }
    
}