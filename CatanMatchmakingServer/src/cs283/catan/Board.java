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
     * Class representing a board coordinate (x, y, z). A coordinate object
     * is immutable.
     *
     */
    public static final class Coordinate {
        public final int x;
        public final int y;
        public final int z;
        
        /**
         * Constructor taking coordinate values
         * @param x
         * @param y
         * @param z
         */
        public Coordinate(int x, int y, int z) throws IllegalArgumentException {
            this.x = x;
            this.y = y;
            this.z = z;
            
            // Make sure that the z coordinate is in the range [0, 5]
            if (z < 0 || z > 5) {
                throw new IllegalArgumentException("Coordinate out of range!");
            }
        }
        
        /**
         * Constructor taking a string representing the coordinate that has
         * no spaces, e.g., (-1, 2, 1) would be represented as -121.
         * @param coordinate
         */
        public Coordinate(String coordinate) {
            // Extract the coordinate of the node from the node name
            int start = 0;
            int finish = 1;
            
            if (coordinate.charAt(start) == '-') {
                finish++;
            }
            
            x = Integer.parseInt(coordinate.substring(start, finish));
            
            start = finish;
            finish = start + 1;
            
            if (coordinate.charAt(start) == '-') {
                finish++;
            }
            
            y = Integer.parseInt(coordinate.substring(start, finish));
            
            z = Integer.parseInt(coordinate.substring(finish, 
                                                      coordinate.length()));
            
            
            // Make sure that the z coordinate is in the range [0, 5]
            if (z < 0 || z > 5) {
                throw new IllegalArgumentException("Coordinate out of range!");
            }
        }
        
        /**
         * Normalizes the coordinate and returns the resulting coordinate.
         * Normalizing converts the coordinate into a coordinate that either
         * represents the top right of a tile or the right of a tile. If the
         * coordinate already represents one of those options, the current
         * coordinate is returned unmodified.
         * 
         * @return the normalized coordinate. 
         */
        public Coordinate normalizeCoordinate() {
            Coordinate newCoordinate = null;
            
            switch (z) {
            // If the z coordinate is either 0 or 1, the coordinate is already
            // on the top right or right of a tile, so just return the current
            // coordinate
            case 0:
            case 1:
                newCoordinate = new Coordinate(x, y, z);
                break;
            case 2: // Represent as right of tile left 1, up 1
                newCoordinate = new Coordinate(x - 1, y + 1, 0);
                break;
            case 3: // Represent as top right of tile left 1
                newCoordinate = new Coordinate(x - 1, y, 1);
                break;
            case 4: // Represent as right of tile left 1
                newCoordinate = new Coordinate(x - 1, y, 0);
                break;
            case 5: // Represent as top right of tile down 1
                newCoordinate = new Coordinate(x, y - 1, 1);
                break;
            }
            
            return newCoordinate;
        }
        
        /**
         * Prints out the coordinate in the form (x, y, z)
         * 
         * @return the formatted string.
         */
        @Override
        public String toString() {
            return String.format("(%d, %d, %d)", x, y, z);
        }
        
       
        /**
         * Compares this Coordinate object with another Coordinate object.
         * @param other
         * @return whether or not the objects are the same.
         */
        @Override
        public boolean equals(Object other) {
            boolean isEqual = false;
            
            if (other == this) {
                isEqual = true;
            } else if (other != null && other.getClass() == this.getClass()) {
                Coordinate otherCoord = (Coordinate) other;
                
                isEqual = otherCoord.x == this.x && otherCoord.y == this.y &&
                          otherCoord.z == this.z;
            }

            return isEqual;
        }
        
        /**
         * Computes the hashcode for this class.
         * @return the computed hashcode.
         */
        @Override
        public int hashCode() {
            int code = x * 10000 + y * 100 + z * 100000;
            
            return code;
        }
    }
    
    
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
	
	/**
	 * Class representing a tile on the board
	 */
	public class Tile {
	    	    
	    /**
	     * X coordinate of the tile
	     */
	    private final int x;
	    
	    /**
	     * Y coordinate of the tile
	     */
	    private final int y;
	    
	    /**
	     * Roll number on the tile
	     */
	    private final int rollNumber;
	    
	    /**
	     * Resource type of the tile
	     */
	    private final ResourceCard.CardType tileType;    
	    
	    /**
	     * List of coordinates of nodes adjacent to the tile
	     */
	    private List<Coordinate> coordinates = new LinkedList<Coordinate>();
	    
	    
	    /**
	     * Constructor
	     * @param x
	     * @param y
	     * @param rollNumber
	     * @param tileType
	     */
	    public Tile(int x, int y, int rollNumber,
	                ResourceCard.CardType tileType) {
	        this.x = x;
	        this.y = y;
	        this.rollNumber = rollNumber;
	        this.tileType = tileType;
	        
	        // Build the list of normalized coordinates of nodes adjacent to 
	        // the tile
	        for (int i = 0; i < 6; i++) {
	            Coordinate coord = new Coordinate(x, y, i);
	            coordinates.add(coord.normalizeCoordinate());
	        }
	    }
	    
	    
	    /**
	     * Retrieves the value of the x coordinate.
	     * @return the value of the x coordinate.
	     */
	    public final int getX() {
	        return x;
	    }
	    
	    /**
	     * Retrieves the value of the y coordinate.
	     * @return the value of the y coordinate.
	     */
	    public final int getY() {
	        return y;
	    }
	    
	    /**
	     * Retrieves the location of the tile as a Coordinate. The z value will
	     * always be 0.
	     * @return the location of the tile as a Coordinate.
	     */
	    public final Coordinate getLocation() {
	        return new Coordinate(x, y, 0);
	    }
	    
	    /**
	     * Retrieves the value of the roll number.
	     * @return the value of the roll number.
	     */
	    public final int getRollNumber() {
	        return rollNumber;
	    }
	    
	    /**
	     * Retrieves the tile type. Note that a value will be returned even
	     * if the tile is a desert, so the user of this class should always
	     * check to make sure the tile is not a desert.
	     * @return the tile type.
	     */
	    public final ResourceCard.CardType getTileType() {
	        return tileType;
	    }
	    
	    /**
	     * Determines whether or not the tile is a desert.
	     * @return whether or not the tile is a desert.
	     */
	    public final boolean isDesert() {
	        return tileType == ResourceCard.CardType.DESERT;
	    }
	    
	    /**
	     * Returns a list of normalized coordinates of nodes that are adjacent
	     * to the tile. This list is precomputed in the constructor.
	     * @return a list of normalized coordinates of nodes that are adjacent
	     *         to the tile.
	     */
	    public final List<Coordinate> getNormalizedCoordinates() {
	        return coordinates;
	    }
	}
	
	
	/**
	 * Class representing a settlement or city
	 */
	public class Settlement {
	    
	    /**
	     * Coordinates of the settlement on the board
	     */
	    private final Coordinate location;
	    
	    /**
	     * Owner of the settlement
	     */
	    private final Player owner;
	    
	    /**
	     * Whether or not the settlement is a city
	     */
	    private boolean isCity;
	    
	    
	    /**
	     * Constructor
	     * @param location
	     * @param owner
	     */
	    public Settlement(Coordinate location, Player owner) {
	        this.location = location;
	        this.owner = owner;
	        this.isCity = false;
	    }
	    
	    
	    /**
	     * Retrieves the location of the settlement.
	     * @return the location of the settlement.
	     */
	    public final Coordinate getLocation() {
	        return location;
	    }
	    
	    /**
	     * Retrieves the owner of the settlement.
	     * @return the owner of the settlement.
	     */
	    public final Player getOwner() {
	        return owner;
	    }
	    
	    /**
	     * Retrieves whether or not the settlement is a city.
	     * @return whether or not the settlement is a city.
	     */
	    public final boolean isCity() {
	        return isCity;
	    }
	    
	    
	    /**
	     * Upgrades the settlement to a city.
	     */
	    public void upgradeToCity() {
	        isCity = true;
	    }
	}
	
	
	/**
	 * Class representing a node on the board
	 */
	private class Node {
	    
	    /**
	     * List of all nodes adjacent to this node
	     */
	    private List<Coordinate> neighbors = new LinkedList<Coordinate>();
	    
	    /**
	     * Settlement on this node, if any.
	     */
	    private Settlement settlement = null;
	    
	    
	    /**
	     * Sets the list of nodes adjacent to this node.
	     * @param neighbors
	     */
	    public void setNeighbors(Coordinate neighbors[]) {
	        for (Coordinate neighbor : neighbors) {
	            this.neighbors.add(neighbor);
	        }
	    }
	    
	    /**
	     * Gets the list of nodes adjacent to this node.
	     * @return the list of neighbors.
	     */
	    public final List<Coordinate> getNeighbors() {
	        return neighbors;
	    }
	    
	    /**
	     * Adds a neighbor to the list of neighbors
	     */
	    public void addNeighbor(Coordinate neighbor) {
	        neighbors.add(neighbor);
	    }
	    
	    /**
	     * Sets the settlement on this node.
	     * @param settlement
	     */
	    public void setSettlement(Settlement settlement) {
	        this.settlement = settlement;
	    }
	    
	    /**
	     * Gets the settlement on this node.
	     * @return the settlement on this node, or null if one does not exist.
	     */
	    public Settlement getSettlement() {
	        return settlement;
	    }
	    
	    
	    /**
	     * Returns whether or not this node is adjacent to a certain node.
	     * @param node
	     * return whether or not this node is adjacent to node.
	     */
	    public boolean isAdjacent(Coordinate node) {
	        boolean isAdj = false;
	        
            for (Coordinate neighbor : neighbors) {
                if (neighbor.equals(node)) {
                    isAdj = true;
                    break;
                }
            }
	        
	        return isAdj;
	    }
	    
	    /**
	     * Returns whether or not this node has a settlement.
	     * @return whether or not this node has a settlement.
	     */
	    public boolean hasSettlement() {
	        return settlement != null;
	    }
	    
	}
	
	
}