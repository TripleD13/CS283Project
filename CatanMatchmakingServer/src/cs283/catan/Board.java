/**
 * Board class that represents the current state of a Catan board.
 * Uses a 3-dimensional coordinate system where each point is represented as
 * the ordered tuple (x, y, z). x and y represent the position of the tile, and
 * z represents the point on the tile. Because each point on the board has
 * three possible coordinate representations, points are normalized so that
 * each point stored on the map has a coordinate that either represents
 * the top right point of a tile of the right point of a tile. 
 */
package cs283.catan;

import java.io.*;
import java.util.*;


public class Board {
    /**TEMPORARY METHOD!!!*/
public static void main(String args[]) {
    Board b = new Board();
    
    b.loadBoardGraphFromResource("cs283/catan/resources/board.csv");
    b.loadBoardTilesFromResource("cs283/catan/resources/tiles.csv");

    for (Map.Entry<Coordinate, Node> x : b.nodeSet.entrySet()) {
        System.out.println(x.getKey());
    }
    
    for (Map.Entry<Coordinate, Tile> x : b.tileSet.entrySet()) {
        System.out.println("Tile: " + x.getKey() + "    roll: " + x.getValue().getRollNumber() + "\ttype: " + x.getValue().getTileType());
    }
    
    Scanner in = new Scanner(System.in);
    
    Player bob = new Player("Bob");
    Player joe = new Player("Joe");
    
    System.out.println(b.addSettlement(new Coordinate(0,0,2), joe));
    System.out.println(b.addSettlement(new Coordinate(0,0,4), bob));
    System.out.println(b.addSettlement(new Coordinate(1,-1,5), joe));
    System.out.println(b.addSettlement(new Coordinate(1,-1,1), joe));
    
    System.out.println(b.upgradeSettlement(new Coordinate(0, 0, 2), joe));
    
    System.out.println(b.addRoad(new Coordinate(0,0,4), new Coordinate(0, 0, 3), bob));
    System.out.println(b.addRoad(new Coordinate(0, 0, 3), new Coordinate(0, 0, 2), joe));
    
    System.out.println(b.getResourceCardsEarned(5, joe));
    System.out.println(b.getResourceCardsEarned(5, bob));
    
    System.out.println(b.moveRobber(0, 0));
    
    System.out.println(b.getResourceCardsEarned(5, joe));
    System.out.println(b.getResourceCardsEarned(5, bob));
    
    System.out.println(b.moveRobber(0, 0));
    System.out.println(b.moveRobber(0, 0));
    System.out.println(b.moveRobber(1, -1));
    System.out.println(b.moveRobber(2, 0));
    
    while (true) {
     // Extract the coordinate of the node from the node name
        int x = 0; int y = 0; int z = 0;
        int start = 0;
        int finish = 1;
        String input = in.nextLine();
        if (input.charAt(start) == '-') {
            finish++;
        }
        
        x = Integer.parseInt(input.substring(start, finish));
        
        start = finish;
        finish = start + 1;
        
        if (input.charAt(start) == '-') {
            finish++;
        }
        
        y = Integer.parseInt(input.substring(start, finish));
        
        z = Integer.parseInt(input.substring(finish, 
                                                input.length()));
        
        // Add the node to the set of nodes
        Coordinate coord = new Coordinate(x, y, z);
        
        
        start = 0;
         finish = 1;
        input = in.nextLine();
        if (input.charAt(start) == '-') {
            finish++;
        }
        
        x = Integer.parseInt(input.substring(start, finish));
        
        start = finish;
        finish = start + 1;
        
        if (input.charAt(start) == '-') {
            finish++;
        }
        
        y = Integer.parseInt(input.substring(start, finish));
        
        z = Integer.parseInt(input.substring(finish, 
                                                input.length()));
        
        // Add the node to the set of nodes
        Coordinate coord2 = new Coordinate(x, y, z);
        
        System.out.printf("%s is adjacent to %s: ", coord, coord2);
        System.out.printf("Normalized: %s %s\n", coord.normalizeCoordinate(), coord2.normalizeCoordinate());
        
        Node test = b.nodeSet.get(coord.normalizeCoordinate());
        System.out.println(b.nodeSet.get(coord.normalizeCoordinate()).isAdjacent(coord2.normalizeCoordinate()));
        
    }
} /** Temporary Method!!!!*/
   
    
    /**
     * Map of all of the nodes in the graph. The key is the coordinate of the
     * node and the value is the node.
     */
    private Map<Coordinate, Node> nodeSet = new HashMap<Coordinate, Node>();
    
    /**
     * Map of all road node sets in the graph. The key is the player name and
     * the value is the map of all nodes of the roads owned by the player.
     */
    private Map<String, Map<Coordinate, Node>> roadSet = 
                                   new HashMap<String, Map<Coordinate, Node>>();
    
    /**
     * Map of all the tiles on the board. The key is the coordinate of the tile
     * (Coordinate object, but the z coordinate is always 0) and the value is
     * the Tile object representing the tile.
     */
    private Map<Coordinate, Tile> tileSet = new HashMap<Coordinate, Tile>();
    
    /**
     * Object representing the robber
     */
	private Robber robber = new Robber();
	
	
	/*
	 * Constructor
	 */
	public Board() {
	    // Nothing to do
	}  
	
	
	/**
	 * Initializes the board graph from a csv file that contains an adjacency
	 * list representation of the graph
	 * @param resourceName
	 * @return whether or not the board was successfully loaded.
	 */
	public boolean loadBoardGraphFromResource(String resourceName) {
	    boolean isSuccessful = false;
	    
	    // Attempt to read the data from the file
	    InputStream resourceStream = Thread.currentThread().
	                                 getContextClassLoader().
	                                 getResourceAsStream(resourceName);
	    
	    if (resourceStream != null) {
	        try {
	            Scanner fileInput = new Scanner(resourceStream);
	            
	            // Read each line of the file. Each line represents a vertex
	            // and its adjacency list
	            String line;
	            while (fileInput.hasNextLine()) {
	            
	                line = fileInput.nextLine();
	                
	                String split[] = line.split(",");
	                
	                // Create a new node
	                Node newNode = new Node();
	                
	                // Create the list of neighbors of the node if the node
	                // has neighbors
	                if (split.length > 1) {
	                    Coordinate neighbors[] = 
	                                           new Coordinate[split.length - 1];
	                    
	                    for (int i = 1; i < split.length; i++) {
	                        neighbors[i - 1] = new Coordinate(split[i]);
	                    }
	                    
	                    newNode.setNeighbors(neighbors);
	                }
	                
	                // Extract the coordinate of the node from the node name
	                // and add the node to the set of nodes
	                Coordinate coord = new Coordinate(split[0]);
	                nodeSet.put(coord.normalizeCoordinate(), newNode);
	            }
	            
	            fileInput.close();
	            
	            
	            isSuccessful = true;
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    return isSuccessful;
	}
	
	/**
	 * Initializes the board tiles from a csv file that contains the x and y
	 * coordinates, the roll number, and the tile type for each tile.
	 * @param resourceName
	 * @return whether or not the tiles were successfully loaded from the file.
	 */
	public boolean loadBoardTilesFromResource(String resourceName) {
	    boolean isSuccessful = false;
	    
	    // Attempt to read the data from the file
	    InputStream resourceStream = Thread.currentThread().
                                     getContextClassLoader().
                                     getResourceAsStream(resourceName);

        if (resourceStream != null) {
            try {
                Scanner fileInput = new Scanner(resourceStream);
                fileInput.useDelimiter("\\s|,|;");
                
                // Read each line of the file. Each line represents a vertex
                // and its adjacency list
                while (fileInput.hasNext()) {
                    
                    // Obtain the tile information
                    int x = fileInput.nextInt();
                    int y = fileInput.nextInt();
                    int rollNumber = fileInput.nextInt();
                    String cardTypeString = fileInput.next();
                    ResourceCard.CardType cardType;
                    
                    switch (cardTypeString.toLowerCase()) {
                    case "lumber":
                        cardType = ResourceCard.CardType.LUMBER;
                        break;
                    case "wool":
                        cardType = ResourceCard.CardType.WOOL;
                        break;
                    case "wheat":
                        cardType = ResourceCard.CardType.WHEAT;
                        break;
                    case "brick":
                        cardType = ResourceCard.CardType.BRICK;
                        break;
                    case "ore":
                        cardType = ResourceCard.CardType.ORE;
                        break;
                    case "desert":
                        cardType = ResourceCard.CardType.DESERT;
                    default:
                        cardType = ResourceCard.CardType.DESERT;
                    }
                    
                    tileSet.put(new Coordinate(x, y, 0),  
                                new Tile(x, y, rollNumber, cardType));
                }
                
                fileInput.close();
                
                
                isSuccessful = true;
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	    
	    return isSuccessful;
	}
	
	
	/**
	 * Add the specified settlement to the board, assuming that there is not
	 * already another settlement within a distance of 1.
	 * @param location
	 * @param owner
	 * @return whether or not the settlement was successfully added.
	 */
	public boolean addSettlement(Coordinate location, Player owner) {
	    boolean isSettlementAdded = false;
	    
	    location = location.normalizeCoordinate();
	    Node locationNode = nodeSet.get(location);
	    
	    if (locationNode != null) {
	        boolean safeToAdd = true;
	        List<Coordinate> neighbors = locationNode.getNeighbors();
	    
	        // Make sure that there are no settlements directly adjacent to the
	        // location
            for (Coordinate neighborCoordinate : neighbors) {
                Node neighbor = nodeSet.get(neighborCoordinate);

                if (neighbor.hasSettlement()) {
                    safeToAdd = false;
                    break;
                }
            }
            
            // Add the settlement if there are not settlements directly adjacent
            // to the location
            if (safeToAdd) {
                locationNode.setSettlement(new Settlement(location, owner));
                isSettlementAdded = true;
            }
	    }
	    
	    return isSettlementAdded;
	}
	
	/**
	 * Add the specified road to the board, assuming that the road is not
	 * already taken. The road must be of length 1, as well as adjacent to
	 * another road or settlement owned by the player.
	 * @param start
	 * @param finish
	 * @owner
	 * @return whether or not the road was successfully added.
	 */
	public boolean addRoad(Coordinate start, Coordinate finish, Player owner) {
	    boolean isRoadAdded = false;
	    
	    // Normalize the coordinates
	    start = start.normalizeCoordinate();
	    finish = finish.normalizeCoordinate();
	    
	    boolean canAddRoad = false;
	    
	    // Make sure the start and finish points of the road are adjacent
	    Node nodeStartFromBoard = nodeSet.get(start);
	    Node nodeFinishFromBoard = nodeSet.get(finish);
	    
	    canAddRoad = nodeStartFromBoard.isAdjacent(finish); 
	    
	    // Make sure road does not already exist
	    for (Map<Coordinate, Node> roadNodeSet : roadSet.values()) {
	        if (!canAddRoad) {
	            break;
	        }
	        
	        Node possibleRoadNode = roadNodeSet.get(start);
	        
	        if (possibleRoadNode != null) {
	            if (possibleRoadNode.isAdjacent(finish)) {
	                // Road already exists!
	                canAddRoad = false;
	            }
	        }
	    }
	    
        Map<Coordinate, Node> playerRoadNodeSet = 
                                              roadSet.get(owner.getUsername());
	    
	    // Make sure either road is adjacent to a settlement owned by the player
	    // or adjacent to a road owned by the player
	    if (canAddRoad && !((nodeStartFromBoard.hasSettlement() && 
	        (nodeStartFromBoard.getSettlement().getOwner() == owner)) ||
	        (nodeFinishFromBoard.hasSettlement() &&
	        (nodeFinishFromBoard.getSettlement().getOwner() == owner)))) {
	        
	        canAddRoad = false;
	        
	        // Because the road is not adjacent to any settlements owned by
	        // the owner, it must be adjacent to another road owned by the user
	        if (playerRoadNodeSet != null) {
	            if (playerRoadNodeSet.get(start) != null ||
	                playerRoadNodeSet.get(finish) != null) {
	                
	                canAddRoad = true;
	            }
	        }
	        
	    }
	    
	    
	    if (canAddRoad) {
	        // Add the road to the player's set of roads. Create a new set for
	        // the player if the player currently has no roads.
	        
	        if (playerRoadNodeSet == null) {
	            playerRoadNodeSet = new HashMap<Coordinate, Node>();
	            
	            roadSet.put(owner.getUsername(), playerRoadNodeSet);
	        }
	        
	        // Add the nodes if they do not exist
	        Node startNode = playerRoadNodeSet.get(start);
	        if (startNode == null) {
	            startNode = new Node();
	            playerRoadNodeSet.put(start, startNode);
	        }
	        
	        Node finishNode = playerRoadNodeSet.get(finish);
	        if (finishNode == null) {
	            finishNode = new Node();
	            playerRoadNodeSet.put(finish, finishNode);
	        }
	        
	        // Add the edge
	        startNode.addNeighbor(finish);
	        finishNode.addNeighbor(start);
	        
	        isRoadAdded = true;
	    }
	    
	    return isRoadAdded;
	}
	
	/**
	 * Adds a tile to the board if it does not already exist.
	 * @param x
	 * @param y
	 * @param rollNumber
	 * @param tileType
	 * @return whether or not the tile was added.
	 */
	public boolean addTile(int x, int y, int rollNumber,
	                       ResourceCard.CardType tileType) {
	    boolean isTileAdded = false;
	    
	    Coordinate tileCoordinate = new Coordinate(x, y, 0);
	    
	    if (!tileSet.containsKey(tileCoordinate)) {
	        tileSet.put(tileCoordinate, new Tile(x, y, rollNumber, 
	                                             tileType));
	        
	        isTileAdded = true;
	    }
	    
	    return isTileAdded;
	}
	
	/**
	 * Upgrades a settlement to a city.
	 * @param location
	 * @param owner
	 * @return whether or not the upgrade was successful. Returns true if the
	 *         settlement is already a city.
	 */
	public boolean upgradeSettlement(Coordinate location, Player owner) {
	    boolean isUpgraded = false;
	    
	    location = location.normalizeCoordinate();
	    
	    // Attempt to find the settlement and upgrade it
	    Node locationNode = nodeSet.get(location);
	    
	    if (locationNode != null && locationNode.hasSettlement()) {
	        Settlement settlement = locationNode.getSettlement();
	        
	        if (settlement.getOwner() == owner) {
	            settlement.upgradeToCity();
	            isUpgraded = true;
	        }
	    }
	    
	    return isUpgraded;
	}
	
	/**
	 * Attempts to move the robber. Returns set of players adjacent to the
	 * robber.
	 * @param x
	 * @param y
	 * @return a set of the players adjacent to the robber. If the robber could
	 *         not be moved, either because the robber is already on the tile
	 *         or the tile does not exist, the return value will be null.
	 */
	public Set<Player> moveRobber(int x, int y) {
	    Set<Player> adjacentPlayers = null;
	    
	    Tile tile = tileSet.get(new Coordinate(x, y, 0));
	    
	    // Check if the tile exists and the robber is successfully moved
	    if (tile != null) {
	        if (robber.setLocation(x, y)) {
	            adjacentPlayers = new HashSet<Player>();
	            
	            // Look for all players with settlements adjacent to robber
	            for (Coordinate coordinate : tile.getNormalizedCoordinates()) {
	                Settlement settlement = 
	                                    nodeSet.get(coordinate).getSettlement();
	                
	                if (settlement != null) {
	                    adjacentPlayers.add(settlement.getOwner());
	                }
	            }
	        }
	    }

	    return adjacentPlayers;
	}
	
	/**
	 * Returns the name of the player who has the longest road. If no one
	 * has the longest road, return null.
	 * @return the name of the player with the longest road, or null if no
	 *         one has the longest road.
	 */
	public String whoHasLongestRoad() {
	    // TODO: add algorithm to determine longest road
	    
	    String longestRoadOwner = null;
	    
	    if (roadSet.containsKey("John")) {
	        longestRoadOwner = "John";
	    }
	    
	    return longestRoadOwner;
	}
	
	/**
	 * Returns a list of the resource cards a player would earn if a certain 
	 * number was rolled.
	 * @param rollNumber
	 * @param player
	 * @return a list of the earned resource cards.
	 */
	public List<ResourceCard> getResourceCardsEarned(int rollNumber, 
	                                                 Player player) {
	    List<ResourceCard> cardsEarned = new LinkedList<ResourceCard>();
	    
	    // For each tile that has the roll number, look at all of its nodes
	    // and see if the player has a settlement on any of them
	    for (Tile tile : tileSet.values()) {
	        if (tile.getRollNumber() == rollNumber) {
	            for (Coordinate tileCoord : tile.getNormalizedCoordinates()) {
	                
	                Node tileNode = nodeSet.get(tileCoord);
	                
	                if (tileNode.hasSettlement() && 
	                    !robber.getLocation().equals(tile.getLocation())) {
	                    
	                    Settlement settlement = tileNode.getSettlement();
	                    
	                    if (settlement.getOwner() ==  player) {
	                        
	                        // If the settlement is a city, add 2 points,
	                        // otherwise add 1 point
	                        for (int i = 0; i < (settlement.isCity() ? 2 : 1);
	                             i++) {
	                            
	                            cardsEarned.add(
	                                      new ResourceCard(tile.getTileType()));
	                        }
	                    }
	                }
	            }
	        }
	    }
	    
	    return cardsEarned;
	}	
		
}