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
        System.out.println(b.nodeSet.get(coord.normalizeCoordinate()).isAdjacent(coord2.normalizeCoordinate()));
        
    }
}
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
         * Constructor
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
	                    String neighbors[] = new String[split.length - 1];
	                    
	                    for (int i = 1; i < split.length; i++) {
	                        neighbors[i - 1] = split[i];
	                    }
	                    
	                    newNode.setNeighbors(neighbors);
	                }
	                
	                // Extract the coordinate of the node from the node name
	                int x = 0; int y = 0; int z = 0;
	                int start = 0;
	                int finish = 1;
	                
	                if (split[0].charAt(start) == '-') {
	                    finish++;
	                }
	                
	                x = Integer.parseInt(split[0].substring(start, finish));
	                
	                start = finish;
	                finish = start + 1;
	                
	                if (split[0].charAt(start) == '-') {
	                    finish++;
	                }
	                
	                y = Integer.parseInt(split[0].substring(start, finish));
	                
	                z = Integer.parseInt(split[0].substring(finish, 
	                                                        split[0].length()));
	                
	                // Add the node to the set of nodes
	                Coordinate coord = new Coordinate(x, y, z);
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
	 * Class representing a node on the board
	 */
	class Node {
	    
	    /**
	     * List of all nodes adjacent to this node
	     */
	    private String neighbors[];
	    
	    
	    /**
	     * Sets the list of nodes adjacent to this node.
	     * @param neighbors
	     */
	    public void setNeighbors(String neighbors[]) {
	        this.neighbors = neighbors;
	    }
	    
	    /**
	     * Gets the list of nodes adjacent to this node.
	     * @return the list of neighbors.
	     */
	    public final String[] getNeighbors() {
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