package cs283.catan;

import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JLayeredPane;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import java.awt.Insets;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JEditorPane;
import javax.swing.JButton;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import cs283.catan.ClientMain.LobbyMessageType;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.swing.JTextField;


public class CatanGUI {

	private JFrame frame;
	private JTable lobby_table;
	private JTabbedPane gameAndMatch;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	private JTextField textField_11;
	private JTextField textField_12;
	private JTextField textField_13;
	private JTextField textField_14;
	private JTextField textField_16;
	private JTextField textField_15;
	
	JButton btnCreateGame;
    JButton btnJoinGame;
    JButton btnLeaveGame;
    private String username;
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CatanGUI window = new CatanGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CatanGUI() {
		initialize();
	}
	
	/**
     * Set the username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }
	
	/**
	 * @return the GUI frame.
	 */
	public JFrame getFrame() {
	    return frame;
	}
	

	/**
     * Update the lobby table.
     * @param lobbyGames
     */
    public void updateLobby(Map<String, String[]> lobbyGames) {
        // Keep track of whether the user is currently in a game        
        boolean isAlreadyInGame = false;
        
        DefaultTableModel model = (DefaultTableModel) lobby_table.getModel();
        
        // Delete previous data
        model.setRowCount(0);
        
        for (Map.Entry<String, String[]> game : lobbyGames.entrySet()) {
            String rowData[] = new String[5];
            rowData[0] = game.getKey();
            
            String playerNames[] = game.getValue();
            
            for (int i = 0; i < playerNames.length; i++) {
                if (playerNames[i] != null && playerNames[i].equals(username)) {
                    isAlreadyInGame = true;
                }
                rowData[i + 1] = playerNames[i];
            }
            
            // Add the game to the lobby table
            model.addRow(rowData);
        }
        
        btnCreateGame.setEnabled(!isAlreadyInGame);
        btnJoinGame.setEnabled(!isAlreadyInGame && 
                               lobby_table.getSelectedRow() != -1);
        btnLeaveGame.setEnabled(isAlreadyInGame &&
                                lobby_table.getSelectedRow() != -1);
    }
	
	/**
	 * Switch to the game mode
	 */
	public void switchToGameMode() {
	    // Switch the tab to game mode and disable the lobby
	    gameAndMatch.setEnabledAt(1, true);
	    gameAndMatch.setSelectedIndex(1);
	    gameAndMatch.setEnabledAt(0,  false);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosed(WindowEvent e) {
		        synchronized (ClientMain.waitForModeChanged) {
		            ClientMain.waitForModeChanged.notifyAll();
		        }
		    }
		});
		frame.setMinimumSize(new Dimension(1000, 900));
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 300);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{650, 233, 0};
		gridBagLayout.rowHeights = new int[]{0, 580, 80, 50, 115, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		gameAndMatch = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_gameAndMatch = new GridBagConstraints();
		gbc_gameAndMatch.fill = GridBagConstraints.BOTH;
		gbc_gameAndMatch.gridheight = 2;
		gbc_gameAndMatch.insets = new Insets(0, 0, 5, 5);
		gbc_gameAndMatch.gridx = 0;
		gbc_gameAndMatch.gridy = 0;
		frame.getContentPane().add(gameAndMatch, gbc_gameAndMatch);
		
		JPanel matchmaking = new JPanel();
		gameAndMatch.addTab("Lobby", null, matchmaking, null);
		gameAndMatch.setEnabledAt(0, true);
		matchmaking.setLayout(new BoxLayout(matchmaking, BoxLayout.Y_AXIS));
		
		JScrollPane scrollPane = new JScrollPane();
		matchmaking.add(scrollPane);
		
		lobby_table = new JTable();
		lobby_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(lobby_table);
		lobby_table.setModel(new DefaultTableModel(
		    new Object[][] {
		    },
		    new String[] {
		        "Game Name", "Player 1", "Player 2", "Player 3", "Player 4"
		    }
		) {
		    Class[] columnTypes = new Class[] {
		        String.class, String.class, String.class, String.class, String.class
		    };
		    public Class getColumnClass(int columnIndex) {
		        return columnTypes[columnIndex];
		    }
		    
		    boolean[] columnEditables = new boolean[] {
	                false, false, false, false, false
	            };
            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
		});
		
		lobby_table.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                int row = lobby_table.getSelectedRow();
                if (btnCreateGame.isEnabled() && row != -1) {
                    btnJoinGame.setEnabled(true);
                } else if (!btnCreateGame.isEnabled() && row != -1) {
                    Vector data = ((Vector) ((DefaultTableModel) lobby_table
                                   .getModel()).getDataVector().get(row));
                    
                    btnLeaveGame.setEnabled(false);
                    for (int i = 0; i < data.size(); i++) {
                        String currentString = (String) data.get(i);
                        if (currentString != null && 
                            currentString.equals(username)) {
                            
                            btnLeaveGame.setEnabled(true);
                            break;
                        }
                    }
                }
                
            }
        });
		
		JPanel panel_1 = new JPanel();
		matchmaking.add(panel_1);
		
		btnCreateGame = new JButton("Create Game");
		btnCreateGame.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent arg0) {
		        String gameName = JOptionPane.showInputDialog(frame, 
		                                          "Enter the name of the game");
		        
		        gameName = gameName.trim();
		        
		        if (gameName != null && !gameName.equals("")) {
		            try {
		                ClientMain.sendLobbyMsg(ClientMain.LobbyMessageType
		                                        .CreateGame, gameName);
		            } catch (Exception e) {
		                e.printStackTrace();
		                frame.dispose();
		            }
		        }
		    }
		});
		panel_1.add(btnCreateGame);
		
		btnJoinGame = new JButton("Join Game");
		btnJoinGame.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent arg0) {
		        int row = lobby_table.getSelectedRow();
		        
		        if (row != -1) {
		            String gameName = (String) ((Vector) 
		                              ((DefaultTableModel) lobby_table
		                                .getModel()).getDataVector().get(row))
		                                .get(0);
		            
                    if (gameName != null) {
                        try {
                            ClientMain.sendLobbyMsg(ClientMain.LobbyMessageType
                                                    .AddUserToGame, gameName);
                        } catch (Exception e) {
                            e.printStackTrace();
                            frame.dispose();
                        }
                    }
                }
		    }
		});
		panel_1.add(btnJoinGame);
		
		btnLeaveGame = new JButton("Leave Game");
		btnLeaveGame.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent arg0) {
		        int row = lobby_table.getSelectedRow();
		        
		        if (row != -1) {
		            String gameName = (String) ((Vector)
		                              ((DefaultTableModel) lobby_table
		                                .getModel()).getDataVector().get(row))
		                                .get(0);
		            
		            if (gameName != null) {
		                try {
		                    ClientMain.sendLobbyMsg(ClientMain.LobbyMessageType
		                                            .RemoveUserFromGame,
		                                            gameName);
		                } catch (Exception e) {
		                    e.printStackTrace();
		                    frame.dispose();
		                }
		            }
		        }
		    }
		});
		panel_1.add(btnLeaveGame);
		lobby_table.getColumnModel().getColumn(0).setResizable(false);
		lobby_table.getColumnModel().getColumn(1).setResizable(false);
		lobby_table.getColumnModel().getColumn(2).setResizable(false);
		lobby_table.getColumnModel().getColumn(3).setResizable(false);
		lobby_table.getColumnModel().getColumn(4).setResizable(false);
		
		lobby_table.getTableHeader().setReorderingAllowed(false);
		
		JPanel gameplay = new JPanel();
		gameAndMatch.addTab("Game", null, gameplay, null);
		gameAndMatch.setEnabledAt(1, false);
				
		BufferedImage myPicture;
		try {
			myPicture = ImageIO.read(Thread.currentThread()
			                        .getContextClassLoader()
			                .getResourceAsStream("cs283/catan/catanBoard.jpg"));
			JLabel boardLabel = new JLabel(new ImageIcon( myPicture ));
			gameplay.add(boardLabel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JLabel lblNewLabel_7 = new JLabel("Smack-talkin");
		GridBagConstraints gbc_lblNewLabel_7 = new GridBagConstraints();
		gbc_lblNewLabel_7.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_7.gridx = 1;
		gbc_lblNewLabel_7.gridy = 0;
		frame.getContentPane().add(lblNewLabel_7, gbc_lblNewLabel_7);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 1;
		frame.getContentPane().add(scrollPane_1, gbc_scrollPane_1);
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);
		scrollPane_1.setViewportView(editorPane);
		
		JPanel gameInfoTop = new JPanel();
		GridBagConstraints gbc_gameInfoTop = new GridBagConstraints();
		gbc_gameInfoTop.anchor = GridBagConstraints.NORTH;
		gbc_gameInfoTop.fill = GridBagConstraints.HORIZONTAL;
		gbc_gameInfoTop.insets = new Insets(0, 0, 5, 5);
		gbc_gameInfoTop.gridx = 0;
		gbc_gameInfoTop.gridy = 2;
		frame.getContentPane().add(gameInfoTop, gbc_gameInfoTop);
		GridBagLayout gbl_gameInfoTop = new GridBagLayout();
		gbl_gameInfoTop.columnWidths = new int[]{100, 100, 100, 100, 100, 100, 0};
		gbl_gameInfoTop.rowHeights = new int[]{0, 0, 0};
		gbl_gameInfoTop.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_gameInfoTop.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gameInfoTop.setLayout(gbl_gameInfoTop);
		
		JLabel lblNewLabel = new JLabel("Settlements");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		gameInfoTop.add(lblNewLabel, gbc_lblNewLabel);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		gameInfoTop.add(textField, gbc_textField);
		textField.setColumns(10);
		
		JLabel lblCities = new JLabel("Biggest Army");
		GridBagConstraints gbc_lblCities = new GridBagConstraints();
		gbc_lblCities.anchor = GridBagConstraints.WEST;
		gbc_lblCities.insets = new Insets(0, 0, 5, 5);
		gbc_lblCities.gridx = 2;
		gbc_lblCities.gridy = 0;
		gameInfoTop.add(lblCities, gbc_lblCities);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 3;
		gbc_textField_2.gridy = 0;
		gameInfoTop.add(textField_2, gbc_textField_2);
		
		JLabel lblBiggestArmy = new JLabel("Victory Points");
		GridBagConstraints gbc_lblBiggestArmy = new GridBagConstraints();
		gbc_lblBiggestArmy.anchor = GridBagConstraints.WEST;
		gbc_lblBiggestArmy.insets = new Insets(0, 0, 5, 5);
		gbc_lblBiggestArmy.gridx = 4;
		gbc_lblBiggestArmy.gridy = 0;
		gameInfoTop.add(lblBiggestArmy, gbc_lblBiggestArmy);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.insets = new Insets(0, 0, 5, 0);
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.gridx = 5;
		gbc_textField_4.gridy = 0;
		gameInfoTop.add(textField_4, gbc_textField_4);
		
		JLabel lblNewLabel_1 = new JLabel("Cities");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		gameInfoTop.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 0, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 1;
		gameInfoTop.add(textField_1, gbc_textField_1);
		
		JLabel lblLongestRoad = new JLabel("Longest Road");
		GridBagConstraints gbc_lblLongestRoad = new GridBagConstraints();
		gbc_lblLongestRoad.anchor = GridBagConstraints.WEST;
		gbc_lblLongestRoad.insets = new Insets(0, 0, 0, 5);
		gbc_lblLongestRoad.gridx = 2;
		gbc_lblLongestRoad.gridy = 1;
		gameInfoTop.add(lblLongestRoad, gbc_lblLongestRoad);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.insets = new Insets(0, 0, 0, 5);
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.gridx = 3;
		gbc_textField_3.gridy = 1;
		gameInfoTop.add(textField_3, gbc_textField_3);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.gridheight = 2;
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 2;
		frame.getContentPane().add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{143, 189, 0};
		gbl_panel_2.rowHeights = new int[]{32, 43, 22, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		JLabel lblNewLabel_8 = new JLabel("Chat Here!");
		GridBagConstraints gbc_lblNewLabel_8 = new GridBagConstraints();
		gbc_lblNewLabel_8.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_8.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_8.gridx = 0;
		gbc_lblNewLabel_8.gridy = 0;
		panel_2.add(lblNewLabel_8, gbc_lblNewLabel_8);
		
		textField_15 = new JTextField();
		textField_15.setColumns(10);
		GridBagConstraints gbc_textField_15 = new GridBagConstraints();
		gbc_textField_15.gridwidth = 2;
		gbc_textField_15.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_15.insets = new Insets(0, 0, 5, 0);
		gbc_textField_15.gridx = 0;
		gbc_textField_15.gridy = 1;
		panel_2.add(textField_15, gbc_textField_15);
		
		JButton button = new JButton("Send Message");
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.anchor = GridBagConstraints.EAST;
		gbc_button.gridx = 1;
		gbc_button.gridy = 2;
		panel_2.add(button, gbc_button);
		
		JPanel commandPanel = new JPanel();
		GridBagConstraints gbc_commandPanel = new GridBagConstraints();
		gbc_commandPanel.insets = new Insets(0, 0, 5, 5);
		gbc_commandPanel.anchor = GridBagConstraints.NORTH;
		gbc_commandPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_commandPanel.gridx = 0;
		gbc_commandPanel.gridy = 3;
		frame.getContentPane().add(commandPanel, gbc_commandPanel);
		GridBagLayout gbl_commandPanel = new GridBagLayout();
		gbl_commandPanel.columnWidths = new int[]{0, 0, 0};
		gbl_commandPanel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_commandPanel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_commandPanel.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		commandPanel.setLayout(gbl_commandPanel);
		
		textField_16 = new JTextField();
		GridBagConstraints gbc_textField_16 = new GridBagConstraints();
		gbc_textField_16.gridwidth = 2;
		gbc_textField_16.insets = new Insets(0, 0, 5, 0);
		gbc_textField_16.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_16.gridx = 0;
		gbc_textField_16.gridy = 0;
		commandPanel.add(textField_16, gbc_textField_16);
		textField_16.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("Send Command");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		JTextField ErrorText = new JTextField();
		ErrorText.setEditable(false);
		ErrorText.setText("Error Messages");
		ErrorText.setSize(new Dimension(100, 22));
		ErrorText.setMinimumSize(new Dimension(100, 22));
		ErrorText.setToolTipText("Displays error messages");
		GridBagConstraints gbc_ErrorText = new GridBagConstraints();
		gbc_ErrorText.insets = new Insets(0, 0, 5, 0);
		gbc_ErrorText.gridwidth = 2;
		gbc_ErrorText.anchor = GridBagConstraints.WEST;
		gbc_ErrorText.fill = GridBagConstraints.VERTICAL;
		gbc_ErrorText.gridx = 0;
		gbc_ErrorText.gridy = 1;
		commandPanel.add(ErrorText, gbc_ErrorText);
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_1.anchor = GridBagConstraints.EAST;
		gbc_btnNewButton_1.gridx = 1;
		gbc_btnNewButton_1.gridy = 1;
		commandPanel.add(btnNewButton_1, gbc_btnNewButton_1);
		
		JPanel resourcePanel = new JPanel();
		GridBagConstraints gbc_resourcePanel = new GridBagConstraints();
		gbc_resourcePanel.fill = GridBagConstraints.BOTH;
		gbc_resourcePanel.insets = new Insets(0, 0, 0, 5);
		gbc_resourcePanel.gridx = 0;
		gbc_resourcePanel.gridy = 4;
		frame.getContentPane().add(resourcePanel, gbc_resourcePanel);
		GridBagLayout gbl_resourcePanel = new GridBagLayout();
		gbl_resourcePanel.columnWidths = new int[]{100, 100, 100, 100, 200, 0};
		gbl_resourcePanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_resourcePanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_resourcePanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		resourcePanel.setLayout(gbl_resourcePanel);
		
		JLabel lblNewLabel_2 = new JLabel("Wheat");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 0;
		resourcePanel.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		textField_5 = new JTextField();
		textField_5.setColumns(10);
		GridBagConstraints gbc_textField_5 = new GridBagConstraints();
		gbc_textField_5.insets = new Insets(0, 0, 5, 5);
		gbc_textField_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_5.gridx = 1;
		gbc_textField_5.gridy = 0;
		resourcePanel.add(textField_5, gbc_textField_5);
		
		JLabel lblMonopoly = new JLabel("Monopoly");
		GridBagConstraints gbc_lblMonopoly = new GridBagConstraints();
		gbc_lblMonopoly.anchor = GridBagConstraints.WEST;
		gbc_lblMonopoly.insets = new Insets(0, 0, 5, 5);
		gbc_lblMonopoly.gridx = 2;
		gbc_lblMonopoly.gridy = 0;
		resourcePanel.add(lblMonopoly, gbc_lblMonopoly);
		
		JButton btnNewButton = new JButton("End Turn");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		textField_10 = new JTextField();
		textField_10.setColumns(10);
		GridBagConstraints gbc_textField_10 = new GridBagConstraints();
		gbc_textField_10.insets = new Insets(0, 0, 5, 5);
		gbc_textField_10.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_10.gridx = 3;
		gbc_textField_10.gridy = 0;
		resourcePanel.add(textField_10, gbc_textField_10);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.gridheight = 5;
		gbc_btnNewButton.gridx = 4;
		gbc_btnNewButton.gridy = 0;
		resourcePanel.add(btnNewButton, gbc_btnNewButton);
		
		JLabel lblNewLabel_3 = new JLabel("Sheep");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 1;
		resourcePanel.add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		textField_6 = new JTextField();
		textField_6.setColumns(10);
		GridBagConstraints gbc_textField_6 = new GridBagConstraints();
		gbc_textField_6.insets = new Insets(0, 0, 5, 5);
		gbc_textField_6.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_6.gridx = 1;
		gbc_textField_6.gridy = 1;
		resourcePanel.add(textField_6, gbc_textField_6);
		
		JLabel lblYearOfPlenty = new JLabel("Year of Plenty");
		GridBagConstraints gbc_lblYearOfPlenty = new GridBagConstraints();
		gbc_lblYearOfPlenty.anchor = GridBagConstraints.WEST;
		gbc_lblYearOfPlenty.insets = new Insets(0, 0, 5, 5);
		gbc_lblYearOfPlenty.gridx = 2;
		gbc_lblYearOfPlenty.gridy = 1;
		resourcePanel.add(lblYearOfPlenty, gbc_lblYearOfPlenty);
		
		textField_11 = new JTextField();
		textField_11.setColumns(10);
		GridBagConstraints gbc_textField_11 = new GridBagConstraints();
		gbc_textField_11.insets = new Insets(0, 0, 5, 5);
		gbc_textField_11.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_11.gridx = 3;
		gbc_textField_11.gridy = 1;
		resourcePanel.add(textField_11, gbc_textField_11);
		
		JLabel lblNewLabel_4 = new JLabel("Ore");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 0;
		gbc_lblNewLabel_4.gridy = 2;
		resourcePanel.add(lblNewLabel_4, gbc_lblNewLabel_4);
		
		textField_7 = new JTextField();
		textField_7.setColumns(10);
		GridBagConstraints gbc_textField_7 = new GridBagConstraints();
		gbc_textField_7.insets = new Insets(0, 0, 5, 5);
		gbc_textField_7.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_7.gridx = 1;
		gbc_textField_7.gridy = 2;
		resourcePanel.add(textField_7, gbc_textField_7);
		
		JLabel lblKnights = new JLabel("Knights");
		GridBagConstraints gbc_lblKnights = new GridBagConstraints();
		gbc_lblKnights.anchor = GridBagConstraints.WEST;
		gbc_lblKnights.insets = new Insets(0, 0, 5, 5);
		gbc_lblKnights.gridx = 2;
		gbc_lblKnights.gridy = 2;
		resourcePanel.add(lblKnights, gbc_lblKnights);
		
		textField_12 = new JTextField();
		textField_12.setColumns(10);
		GridBagConstraints gbc_textField_12 = new GridBagConstraints();
		gbc_textField_12.insets = new Insets(0, 0, 5, 5);
		gbc_textField_12.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_12.gridx = 3;
		gbc_textField_12.gridy = 2;
		resourcePanel.add(textField_12, gbc_textField_12);
		
		JLabel lblNewLabel_5 = new JLabel("Lumber");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 0;
		gbc_lblNewLabel_5.gridy = 3;
		resourcePanel.add(lblNewLabel_5, gbc_lblNewLabel_5);
		
		textField_8 = new JTextField();
		textField_8.setColumns(10);
		GridBagConstraints gbc_textField_8 = new GridBagConstraints();
		gbc_textField_8.insets = new Insets(0, 0, 5, 5);
		gbc_textField_8.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_8.gridx = 1;
		gbc_textField_8.gridy = 3;
		resourcePanel.add(textField_8, gbc_textField_8);
		
		JLabel lblArmy = new JLabel("Army");
		GridBagConstraints gbc_lblArmy = new GridBagConstraints();
		gbc_lblArmy.anchor = GridBagConstraints.WEST;
		gbc_lblArmy.insets = new Insets(0, 0, 5, 5);
		gbc_lblArmy.gridx = 2;
		gbc_lblArmy.gridy = 3;
		resourcePanel.add(lblArmy, gbc_lblArmy);
		
		textField_13 = new JTextField();
		textField_13.setColumns(10);
		GridBagConstraints gbc_textField_13 = new GridBagConstraints();
		gbc_textField_13.insets = new Insets(0, 0, 5, 5);
		gbc_textField_13.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_13.gridx = 3;
		gbc_textField_13.gridy = 3;
		resourcePanel.add(textField_13, gbc_textField_13);
		
		JLabel lblNewLabel_6 = new JLabel("Brick");
		GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
		gbc_lblNewLabel_6.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_6.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_6.gridx = 0;
		gbc_lblNewLabel_6.gridy = 4;
		resourcePanel.add(lblNewLabel_6, gbc_lblNewLabel_6);
		
		textField_9 = new JTextField();
		textField_9.setColumns(10);
		GridBagConstraints gbc_textField_9 = new GridBagConstraints();
		gbc_textField_9.insets = new Insets(0, 0, 0, 5);
		gbc_textField_9.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_9.gridx = 1;
		gbc_textField_9.gridy = 4;
		resourcePanel.add(textField_9, gbc_textField_9);
		
		JLabel lblRoadBuilder = new JLabel("Road Builder");
		GridBagConstraints gbc_lblRoadBuilder = new GridBagConstraints();
		gbc_lblRoadBuilder.insets = new Insets(0, 0, 0, 5);
		gbc_lblRoadBuilder.anchor = GridBagConstraints.WEST;
		gbc_lblRoadBuilder.gridx = 2;
		gbc_lblRoadBuilder.gridy = 4;
		resourcePanel.add(lblRoadBuilder, gbc_lblRoadBuilder);
		
		textField_14 = new JTextField();
		textField_14.setColumns(10);
		GridBagConstraints gbc_textField_14 = new GridBagConstraints();
		gbc_textField_14.insets = new Insets(0, 0, 0, 5);
		gbc_textField_14.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_14.gridx = 3;
		gbc_textField_14.gridy = 4;
		resourcePanel.add(textField_14, gbc_textField_14);
	}

}