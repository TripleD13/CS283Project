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
        
        while (!logonSuccessful) {
            
            // Create the login attempt dialog
            username = JOptionPane.showInputDialog("Please enter your " +
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
                        JOptionPane.showMessageDialog(null, message);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, 
                                                  "Username '" + username +
                                                  "' exceeds the 16 character" +
                                                  " limit. Please enter a" +
                                                  " valid username!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a name");
            }
        }
        
        // Check to make sure the user is logged on
        if (logonSuccessful) {
            // Enter lobby mode
            lobbyMode();
        }

        // Close the connection to the server
        clientSocket.close();
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
        try {
            // Receive the initial lobby data from the server
            lobbyGames = (Map<String, String[]>) objInputStream.readObject();
            
            printLobby();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    
    
    /**
     * Print out the current state of the lobby
     */
    private static void printLobby() {
        System.out.println("==============Lobby Games===============");
        
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
        
        // Wait for the server response
        
    }
    
}