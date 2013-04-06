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
    b.loadBoardFromFile("board.csv");
    
    for (Map.Entry<Coordinate, Node> x : b.nodeSet.entrySet()) {
        System.out.println(x.getKey());
    }
    
    Scanner in = new Scanner(System.in);
    
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
     * Set of all of the nodes in the graph
     */
    Map<Coordinate, Node> nodeSet = new HashMap<Coordinate, Node>();
    
    /**
     * Object representing the robber
     */
	public Robber robber;
	
	
	/*
	 * Constructor
	 */
	public Board() {
	    
	}  
	
	
	/**
	 * Initializes the board from a csv file that contains an adjacency list
	 * representation of the graph
	 * @param fileName
	 * @return whether or not the board was successfully loaded.
	 */
	public boolean loadBoardFromFile(String fileName) {
	    boolean isSuccessful = false;
	    
	    // Attempt to read the data from the file
	    File graphFile = new File(fileName);
	    if (graphFile.exists()) {
	        try {
	            Scanner fileInput = new Scanner(new FileInputStream(graphFile));
	            
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
	 * Class representing a tile on the board
	 */
	public class Tile {
	    	    
	    /**
	     * X coordinate of the tile
	     */
	    private int x;
	    
	    /**
	     * Y coordinate of the tile
	     */
	    private int y;
	    
	    /**
	     * Roll number on the tile
	     */
	    private int rollNumber;
	    
	    /**
	     * Resource type of the tile
	     */
	    private ResourceCard.CardType tileType;
	    
	    /**
	     * Whether or not the tile is a desert (in which case the resource type
	     * of the tile is irrelevant)
	     */
	    private boolean isDesert;
	    
	    
	    /**
	     * Constructor (assumes that the tile is not a desert)
	     * @param x
	     * @param y
	     * @param rollNumber
	     * @param tileType
	     */
	    public Tile(int x, int y, int rollNumber, 
	                ResourceCard.CardType tileType) {
	        this(x, y, rollNumber, tileType, false);
	    }
	    
	    /**
	     * Full constructor
	     * @param x
	     * @param y
	     * @param rollNumber
	     * @param tileType
	     * @param isDesert
	     */
	    public Tile(int x, int y, int rollNumber,
	                ResourceCard.CardType tileType, boolean isDesert) {
	        this.x = x;
	        this.y = y;
	        this.rollNumber = rollNumber;
	        this.isDesert = isDesert;
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
	        return isDesert;
	    }
	}
	
	
	/**
	 * Class representing a settlement or city
	 */
	public class Settlement {
	    
	    /**
	     * Coordinates of the settlement on the board
	     */
	    private Coordinate location;
	    
	    /**
	     * Owner of the settlement
	     */
	    private Player owner;
	    
	    /**
	     * Whether or not the settlement is a city
	     */
	    boolean isCity;
	    
	    
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
	}
	
	
	/**
	 * Class representing a node on the board
	 */
	private class Node {
	    
	    /**
	     * List of all nodes adjacent to this node
	     */
	    private Coordinate neighbors[];
	    
	    
	    /**
	     * Sets the list of nodes adjacent to this node.
	     * @param neighbors
	     */
	    public void setNeighbors(Coordinate neighbors[]) {
	        this.neighbors = neighbors;
	    }
	    
	    /**
	     * Gets the list of nodes adjacent to this node.
	     * @return the list of neighbors.
	     */
	    public final Coordinate[] getNeighbors() {
	        return neighbors;
	    }
	    
	    
	    /**
	     * Returns whether or not this node is adjacent to a certain node.
	     * @param node
	     * return whether or not this node is adjacent to node.
	     */
	    public boolean isAdjacent(Coordinate node) {
	        boolean isAdj = false;
	        
	        if (neighbors != null) {
	            for (int i = 0; i < neighbors.length; i++) {
	                if (neighbors[i].equals(node)) {
	                    isAdj = true;
	                    break;
	                }
	            }   
	        }
	        
	        return isAdj;
	    }
	    
	}
	
	
}