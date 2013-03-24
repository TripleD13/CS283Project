/**
 * Main game client class. Allows the user to logon to the server. Basic
 * TCP client code based on code from CS283 homepage.
 */
package cs283.catan;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

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
     * TCP socket
     */
    private static Socket clientSocket;
    
    
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
            String username = JOptionPane.showInputDialog("Please enter your " +
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
        
        // Enter lobby mode
        lobbyMode();

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
        String message;
        
        try {
            InetAddress serverIP = InetAddress.getByName(SERVER_IP);
            
            // If socket previously opened, close it
            if (clientSocket != null) {
                clientSocket.close();
            }
            
            clientSocket = new Socket(serverIP, PORT);
            
            // Send the username to the server
            DataOutputStream outputStream = new DataOutputStream(
                                                clientSocket.getOutputStream());
            
            outputStream.writeBytes(username + "\n");
            outputStream.flush();
            
            // Read the message from the server
            BufferedReader inputStream = new BufferedReader(
                          new InputStreamReader(clientSocket.getInputStream()));
            
            message = inputStream.readLine();
            
        } catch (Exception e) {
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
            ObjectInput objInputStream = 
                           new ObjectInputStream(clientSocket.getInputStream());
            
            lobbyGames = (Map<String, String[]>) objInputStream.readObject();
            
            System.out.println(lobbyGames.keySet().toString());
            System.out.println(lobbyGames.values().toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    
}