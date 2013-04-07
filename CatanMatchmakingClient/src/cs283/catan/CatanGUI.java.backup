package cs283.catan;

import java.awt.EventQueue;

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
import java.util.*;


public class CatanGUI {

	private JFrame frame;
	private JSeparator separator;
	private JTable lobby_table;
	private JTabbedPane gameAndMatch;
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
		frame.setMinimumSize(new Dimension(1000, 800));
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 300);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{650, 233, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 553, 0, 30, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
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
		        if (gameName != null) {
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
		
		JLabel lblNewLabel_7 = new JLabel("Smack-talkin");
		GridBagConstraints gbc_lblNewLabel_7 = new GridBagConstraints();
		gbc_lblNewLabel_7.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_7.gridx = 1;
		gbc_lblNewLabel_7.gridy = 0;
		frame.getContentPane().add(lblNewLabel_7, gbc_lblNewLabel_7);
		
		JEditorPane editorPane = new JEditorPane();
		GridBagConstraints gbc_editorPane = new GridBagConstraints();
		gbc_editorPane.insets = new Insets(0, 0, 5, 5);
		gbc_editorPane.fill = GridBagConstraints.BOTH;
		gbc_editorPane.gridx = 1;
		gbc_editorPane.gridy = 1;
		frame.getContentPane().add(editorPane, gbc_editorPane);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 2;
		frame.getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{100, 100, 100, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblNewLabel = new JLabel("Settlements");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel.add(lblNewLabel, gbc_lblNewLabel);
		
		JLabel lblCities = new JLabel("Biggest Army");
		GridBagConstraints gbc_lblCities = new GridBagConstraints();
		gbc_lblCities.anchor = GridBagConstraints.WEST;
		gbc_lblCities.insets = new Insets(0, 0, 5, 5);
		gbc_lblCities.gridx = 1;
		gbc_lblCities.gridy = 0;
		panel.add(lblCities, gbc_lblCities);
		
		JLabel lblBiggestArmy = new JLabel("Victory Points");
		GridBagConstraints gbc_lblBiggestArmy = new GridBagConstraints();
		gbc_lblBiggestArmy.anchor = GridBagConstraints.WEST;
		gbc_lblBiggestArmy.insets = new Insets(0, 0, 5, 0);
		gbc_lblBiggestArmy.gridx = 2;
		gbc_lblBiggestArmy.gridy = 0;
		panel.add(lblBiggestArmy, gbc_lblBiggestArmy);
		
		JLabel lblNewLabel_1 = new JLabel("Cities");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		panel.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		JLabel lblLongestRoad = new JLabel("Longest Road");
		GridBagConstraints gbc_lblLongestRoad = new GridBagConstraints();
		gbc_lblLongestRoad.anchor = GridBagConstraints.WEST;
		gbc_lblLongestRoad.insets = new Insets(0, 0, 0, 5);
		gbc_lblLongestRoad.gridx = 1;
		gbc_lblLongestRoad.gridy = 1;
		panel.add(lblLongestRoad, gbc_lblLongestRoad);
		
		separator = new JSeparator();
		separator.setPreferredSize(new Dimension(0, 10));
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(0, 0, 5, 5);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 3;
		frame.getContentPane().add(separator, gbc_separator);
		
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
		gbc_lblNewLabel_2.gridwidth = 2;
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 0;
		resourcePanel.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
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
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.gridheight = 5;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 4;
		gbc_btnNewButton.gridy = 0;
		resourcePanel.add(btnNewButton, gbc_btnNewButton);
		
		JLabel lblNewLabel_3 = new JLabel("Sheep");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.gridwidth = 2;
		gbc_lblNewLabel_3.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 1;
		resourcePanel.add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		JLabel lblYearOfPlenty = new JLabel("Year of Plenty");
		GridBagConstraints gbc_lblYearOfPlenty = new GridBagConstraints();
		gbc_lblYearOfPlenty.anchor = GridBagConstraints.WEST;
		gbc_lblYearOfPlenty.insets = new Insets(0, 0, 5, 5);
		gbc_lblYearOfPlenty.gridx = 2;
		gbc_lblYearOfPlenty.gridy = 1;
		resourcePanel.add(lblYearOfPlenty, gbc_lblYearOfPlenty);
		
		JLabel lblNewLabel_4 = new JLabel("Ore");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.gridwidth = 2;
		gbc_lblNewLabel_4.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 0;
		gbc_lblNewLabel_4.gridy = 2;
		resourcePanel.add(lblNewLabel_4, gbc_lblNewLabel_4);
		
		JLabel lblKnights = new JLabel("Knights");
		GridBagConstraints gbc_lblKnights = new GridBagConstraints();
		gbc_lblKnights.anchor = GridBagConstraints.WEST;
		gbc_lblKnights.insets = new Insets(0, 0, 5, 5);
		gbc_lblKnights.gridx = 2;
		gbc_lblKnights.gridy = 2;
		resourcePanel.add(lblKnights, gbc_lblKnights);
		
		JLabel lblNewLabel_5 = new JLabel("Lumber");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.gridwidth = 2;
		gbc_lblNewLabel_5.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 0;
		gbc_lblNewLabel_5.gridy = 3;
		resourcePanel.add(lblNewLabel_5, gbc_lblNewLabel_5);
		
		JLabel lblArmy = new JLabel("Army");
		GridBagConstraints gbc_lblArmy = new GridBagConstraints();
		gbc_lblArmy.anchor = GridBagConstraints.WEST;
		gbc_lblArmy.insets = new Insets(0, 0, 5, 5);
		gbc_lblArmy.gridx = 2;
		gbc_lblArmy.gridy = 3;
		resourcePanel.add(lblArmy, gbc_lblArmy);
		
		JLabel lblNewLabel_6 = new JLabel("Brick");
		GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
		gbc_lblNewLabel_6.gridwidth = 2;
		gbc_lblNewLabel_6.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_6.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_6.gridx = 0;
		gbc_lblNewLabel_6.gridy = 4;
		resourcePanel.add(lblNewLabel_6, gbc_lblNewLabel_6);
		
		JLabel lblRoadBuilder = new JLabel("Road Builder");
		GridBagConstraints gbc_lblRoadBuilder = new GridBagConstraints();
		gbc_lblRoadBuilder.insets = new Insets(0, 0, 0, 5);
		gbc_lblRoadBuilder.anchor = GridBagConstraints.WEST;
		gbc_lblRoadBuilder.gridx = 2;
		gbc_lblRoadBuilder.gridy = 4;
		resourcePanel.add(lblRoadBuilder, gbc_lblRoadBuilder);
	}
	public JSeparator getSeparator() {
		return separator;
	}
}
