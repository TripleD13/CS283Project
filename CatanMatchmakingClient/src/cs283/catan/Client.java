/**
 * Main game client class. Allows the user to logon to the server. Basic
 * TCP client code based on code from CS283 homepage.
 */
package cs283.catan;

import java.io.*;
import java.net.*;

import javax.swing.*;
import javax.swing.UIManager.*;

/**
 * @author John
 *
 */
public class Client {

    /**
     * Server IP address
     */
    private static final String SERVER_IP = "127.0.0.1";
    
    /**
     * Server port
     */
    private static final int PORT = 8888;
    
    
    /**
     * TCP socket
     */
    private static Socket clientSocket;
    
    
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
                                                          "user name");
            
            if (username == null) {
                System.exit(0);
            }
            
            // Get rid of whitespace
            username = username.trim();
            
            if (!username.equals("")) {
                String message = attemptLogon(username);
                if (message.equals("Successfully logged on!")) {
                    logonSuccessful = true;
                } else {
                    JOptionPane.showMessageDialog(null, message);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a name");
            }
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
    
}