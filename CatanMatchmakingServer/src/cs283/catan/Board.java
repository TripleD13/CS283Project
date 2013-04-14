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

import cs283.catan.ResourceCard.CardType;


public class Board implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1070958368003158458L;


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
    
    Player bob = new Player("Bob", 0);
    Player joe = new Player("Joe", 1);
    
    System.out.println(b.addSettlement(new Coordinate(0,0,2), joe, false));
    System.out.println(b.addSettlement(new Coordinate(0,0,4), bob, false));
    System.out.println(b.addSettlement(new Coordinate(1,-1,5), joe, false));
    System.out.println(b.addSettlement(new Coordinate(1,-1,1), joe, false));
    
    System.out.println(b.upgradeSettlement(new Coordinate(0, 0, 2), joe));
    
    System.out.println(b.addRoad(new Coordinate(0,0,4), new Coordinate(0, 0, 3), bob, false));
    System.out.println(b.addRoad(new Coordinate(0, 0, 3), new Coordinate(0, 0, 2), joe, false));
    System.out.println(b.addRoad(new Coordinate(0, 0, 2), new Coordinate(0, 0, 1), joe, false));
    
    
    System.out.println(b.addSettlement(new Coordinate(-2,2,0), bob, false));
    System.out.println(b.addRoad(new Coordinate(-2,2,0), new Coordinate(-2, 2, 1), bob, false));
    System.out.println(b.addRoad(new Coordinate(-2,2,1), new Coordinate(-2, 2, 2), bob, false));
    System.out.println(b.addRoad(new Coordinate(-2,2,3), new Coordinate(-2, 2, 2), bob, false));
    System.out.println(b.addRoad(new Coordinate(-2,2,3), new Coordinate(-2, 2, 4), bob, false));
    System.out.println(b.addRoad(new Coordinate(-2,2,4), new Coordinate(-2, 2, 5), bob, false));
    System.out.println(b.addRoad(new Coordinate(-2,2,0), new Coordinate(-2, 2, 5), bob, false));
    System.out.println(b.addRoad(new Coordinate(-2, 1, 2), new Coordinate(-2, 1, 3), bob, false));
    System.out.println(b.addRoad(new Coordinate(-2, 1, 3), new Coordinate(-2, 1, 4), bob, false));
    
    System.out.println(b.getResourceCardsEarned(5, joe));
    System.out.println(b.getResourceCardsEarned(5, bob));
    
    System.out.println(b.moveRobber(0, 0));
    
    System.out.println(b.getResourceCardsEarned(5, joe));
    System.out.println(b.getResourceCardsEarned(5, bob));
    
    System.out.println(b.moveRobber(0, 0));
    System.out.println(b.moveRobber(0, 0));
    System.out.println(b.moveRobber(1, -1));
    System.out.println(b.moveRobber(2, 0));
    
    System.out.println(b.whoHasLongestRoad());
    
    System.out.println(b.addSettlement(new Coordinate(-2,1,3), joe, false));

    System.out.println(b.whoHasLongestRoad());
    
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
     * Also keep a running list of all of the settlements on the board. This
     * list is used for GUI drawing purposes only.
     */
    private List<Settlement> settlementList = new LinkedList<Settlement>();
    
    /**
     * Map of all road node sets in the graph. The key is the player name and
     * the value is the map of all nodes of the roads owned by the player.
     */
    private Map<String, Map<Coordinate, Node>> roadSet = 
                                   new HashMap<String, Map<Coordinate, Node>>();
    
    /**
     * Also keep a running list of all of the roads on the board. This list
     * is used for GUI drawing purposes only.
     */
    private List<Road> roadList = new LinkedList<Road>();
    
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
	
	
	/**
	 * Name of the player with the current longest road
	 */
	private String longestRoadOwner = null;
	
	/**
	 * Length of the current longest road
	 */
	private int longestRoadLength = 0;
	
	
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
     * @param checkCards - whether or not the method checks to make sure the
     *                     player has appropriate resources.
	 * @return whether or not the settlement was successfully added.
	 */
	public boolean addSettlement(Coordinate location, Player owner, 
	                             boolean checkCards) {
	    boolean isSettlementAdded = false;
	    
	    location = location.normalizeCoordinate();
	    Node locationNode = nodeSet.get(location);
	    
	    // Make sure user has proper hand
	    boolean hasSheep = false;
	    boolean hasLumber = false;
	    boolean hasBrick = false;
	    boolean hasWheat = false;
	    
	    for (ResourceCard card : owner.resCards) {
	        switch (card.getCardType()) {
	        case WOOL:
	            hasSheep = true;
	            break;
	        case LUMBER:
	            hasLumber = true;
	            break;
	        case BRICK:
	            hasBrick = true;
	            break;
	        case WHEAT:
	            hasWheat = true;
	            break;
            default:
	        }
	    }
	    
	    if (locationNode != null && ((hasSheep && hasLumber && hasBrick &&
            hasWheat) | !checkCards)) {
	        
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
                Settlement newSettlement = new Settlement(location, owner);
               
                locationNode.setSettlement(newSettlement);
                
                // Add the settlement to the settlement list for GUI drawing
                // purposes
                settlementList.add(newSettlement);
                
                isSettlementAdded = true;
                
                if (checkCards) {
                    owner.doSettlementPurchase();
                }
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
	 * @param owner
	 * @param checkCards - whether or not the method checks to make sure the
	 *                     player has appropriate resources.
	 * @return whether or not the road was successfully added.
	 */
	public boolean addRoad(Coordinate start, Coordinate finish, Player owner,
	                       boolean checkCards) {
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
	    
        
        // Make sure user has proper hand
        boolean hasLumber = false;
        boolean hasBrick = false;
        
        for (ResourceCard card : owner.resCards) {
            switch (card.getCardType()) {
            case LUMBER:
                hasLumber = true;
                break;
            case BRICK:
                hasBrick = true;
                break;
            default:
            }
        }
        
        canAddRoad = (canAddRoad && hasLumber && hasBrick) | !checkCards;
        
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
	        
	        // Add a road object to the list for GUI drawing purposes
	        roadList.add(new Road(start,finish, owner));
	        
	        isRoadAdded = true;
	        
	        if (checkCards) {
	            owner.doRoadPurchase();
	        }
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
	    
	    // Make sure user has proper hand
        int numOre = 0;
        int numWheat = 0;
        
        for (ResourceCard card : owner.resCards) {
            switch (card.getCardType()) {
            case ORE:
                numOre++;
                break;
            case WHEAT:
                numWheat++;
                break;
            default:
            }
        }
	    
	    if (locationNode != null && locationNode.hasSettlement() &&
	        numOre >= 3 && numWheat >= 2) {
	        
	        Settlement settlement = locationNode.getSettlement();
	        
	        if (settlement.getOwner() == owner) {
	            settlement.upgradeToCity();
	            isUpgraded = true;
	            
	            owner.doCityPurchase();
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
	    //if (roadSet.containsKey("John")) {
	    //    longestRoadOwner = "John";
	    //}
	    
	    // The current owner of the road keeps the road in the event of a tie
	    String defendingOwner = this.longestRoadOwner;
	    int defendingOwnerLength = this.longestRoadLength;
	    
	    
	    String updatedLongestRoadPlayer = null;
	    int updatedLongestRoadLength = 0;
	    
	    for (String playerName : roadSet.keySet()) {
	        int playersLongestRoad = playersLongestRoad(playerName);
	        
	        // If the player is the defending owner, store the length of the
	        // defending owner's road
	        if (playerName.equals(defendingOwner)) {
	            defendingOwnerLength = playersLongestRoad;
	        }
	        
	        // If this player has the longest road so far, set the road length
	        // as the longest length and the player as the owner of the longest
	        // road so far
	        if (playersLongestRoad > updatedLongestRoadLength) {
	            updatedLongestRoadLength = playersLongestRoad;
	            
	            // If the length of this road is greater than the previous
	            // length of the longest road, take ownership. Otherwise, the
	            // 
	            updatedLongestRoadPlayer = playerName;
	        }
	        
	        // DEBUG MESSAGE
	        System.out.println(playerName + "'s longest road: " + 
	                           playersLongestRoad);
	    }
	    
        this.longestRoadLength = updatedLongestRoadLength;
	    
	    // If all of the roads have length less than 5, no one has longest road
	    if (updatedLongestRoadLength < 5) {
	        this.longestRoadOwner = null;
	    } else {
	        // If the defending owner still has the longest road length, keep
	        // the defending owner (even in the event of a tie). Otherwise,
	        // choose the player with the greatest longest road.
	        if (defendingOwnerLength != updatedLongestRoadLength) {
	            this.longestRoadOwner = updatedLongestRoadPlayer;
	        }
	    }
	    
	    return longestRoadOwner;
	}
	
	/**
	 * Calculates the longest road for a specific player. Returns 0 if the
	 * player has no roads or the player does not exist. The algorithm works
	 * by running a depth-first search starting from each node in the players
	 * road graph. Each depth-first search returns the length of the longest
	 * path or cycle from the resulting depth-first search tree. The maximum of 
	 * all of these values is equal to the player's longest road.
	 * @param playerName
	 * @return the length of the player's longest road, or 0 if the player
	 *         has no roads or does not exist.
	 */
	private int playersLongestRoad(String playerName) {
	    int longestRoad = 0;
	    
	    // Make sure the road set contains the player
	    if (roadSet.containsKey(playerName)) {
	        // Get the map of coordinates and nodes that make up the player's
	        // roads
	        Map<Coordinate, Node> nodeMap = roadSet.get(playerName);
	        
	        // Run DFS starting with each of the nodes
	        for (Node startNode : nodeMap.values()) {
	            // Set of nodes that have already been visited in the DFS
	            Set<Node> visitedNodes = new HashSet<Node>();
	            
	            longestRoad = Math.max(longestRoad, 
	                                   longestRoadDFS(startNode, null,
	                                                  visitedNodes, 
	                                                  nodeMap, playerName));
	        }
	    }
	    
	    return longestRoad;
	}
	
	/**
	 * Performs a depth-first search using the given node as the current
	 * node and the set as a set of vertices that have been visited already.
	 * Returns 1 plus the length of the longest path branching out from the
	 * current node. If the current node is a leaf in the DFS tree, return
	 * 0 if the leaf does not have a back edge, and 1 if the leaf does have
	 * a back edge. The parent node is the previous node visited, or null
	 * if the current node is the root of the DFS tree.
	 * @param current
	 * @param visitedNodes
	 * @param nodeMap
	 * @param playerName
	 * @return 0 if the leaf does not have a back edge, and 1 if the leaf does
	 *           have a back edge.
	 */
	private int longestRoadDFS(Node current, Node parent,
	                           Set<Node> visitedNodes,
	                           Map<Coordinate, Node> nodeMap,
	                           String playerName) {
	    int maxLength = 0;
	    	    
	    // Add the current node to the set of visited nodes
	    visitedNodes.add(current);
	    
	    // Look at all of the unvisited neighbors of the current node
	    for (Coordinate neighborCoord : current.getNeighbors()) {
	        Node neighbor = nodeMap.get(neighborCoord);
	        
	        // Make sure the neighbor does not have a settlement from
            // another player
            Settlement settlement = nodeSet.get(neighborCoord).getSettlement();
            
            if (settlement != null && 
                !settlement.getOwner().getUsername().equals(playerName)) {
                
                maxLength = Math.max(maxLength, 1);
                
            } else if (!visitedNodes.contains(neighbor)) {
	            maxLength = Math.max(maxLength,
	                                 longestRoadDFS(neighbor, current, 
	                                                visitedNodes,
	                                                nodeMap, playerName) + 1);
	        } else if (neighbor != parent) {
	            // The neighbor must be connected to the current node by a
	            // back edge, so the maximum length is at least 1
	            maxLength = Math.max(maxLength,  1);
	        }
	    }
	    
	    return maxLength;
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
	 * Returns a list of the resource cards earned by placing a settlement.
	 * @param coord
	 * @return a list of resource cards.
	 */
	public List<ResourceCard> getPlacementResourceCards(Coordinate coord) {
	    coord = coord.normalizeCoordinate();
	    
	    List<ResourceCard> cardsEarned = new LinkedList<ResourceCard>();
	    
	    // Get the node at the coordinate
	    Node settlementLocation = nodeSet.get(coord);
	    
	    if (settlementLocation != null && settlementLocation.hasSettlement()) {
	        // Obtain the three bordering tiles
	        Coordinate neighboringTiles[] = new Coordinate[3];
	        
	        if (coord.z == 0) {
	            neighboringTiles[0] = new Coordinate(coord.x, coord.y, 0);
	            neighboringTiles[1] = new Coordinate(coord.x + 1, coord.y, 0);
	            neighboringTiles[2] = new Coordinate(coord.x + 1, coord.y - 1,
	                                                 0);
	        } else {
	            neighboringTiles[0] = new Coordinate(coord.x, coord.y, 0);
                neighboringTiles[1] = new Coordinate(coord.x, coord.y + 1, 0);
                neighboringTiles[2] = new Coordinate(coord.x + 1, coord.y, 0);
	        }
	        
	        // Add the resources, if any
	        for (int i = 0; i < neighboringTiles.length; i++) {
                
                Tile tile = tileSet.get(neighboringTiles[i]);
                if (tile != null) {
                    cardsEarned.add(new ResourceCard(tile.getTileType()));
                }
            }
	    }
	    
	    return cardsEarned;
	}
		
	/**
	 * Returns a list of all of the settlements on the board.
	 * @return a list of settlements.
	 */
	public List<Settlement> getSettlementList() {
	    return settlementList;
	}
	
	/**
	 * Returns a list of all of the roads on the board
	 * @return a list of roads.
	 */
	public List<Road> getRoadList() {
	    return roadList;
	}
	
}