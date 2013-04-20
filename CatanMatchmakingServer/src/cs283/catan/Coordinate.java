/**
 * Class representing a board coordinate (x, y, z). A coordinate object
 * is immutable.
 *
 */
package cs283.catan;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.awt.Point;


public final class Coordinate implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 7210895717537168897L;
    
    private static final Map<Coordinate, Point> pixelMap= 
                                               new HashMap<Coordinate, Point>();
    
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
     * Returns the pixel coordinate associated with the coordinate.
     * @return a point representing the pixel coordinate, or null if no such
     *         pixel mapping exists.
     */
    public Point getPixel() {
        return pixelMap.get(this);
    }
    
    /**
     * Loads a pixel mapping.
     * @parameter coordinate
     * @parameter pixel
     */
    public static boolean loadPixelMappingsFromResource(String resourceName) {
        boolean isSuccessful = false;
        
        // Attempt to read the data from the file
        InputStream resourceStream = Thread.currentThread().
                                     getContextClassLoader().
                                     getResourceAsStream(resourceName);
        
        if (resourceStream != null) {
            try {
                Scanner fileInput = new Scanner(resourceStream);
                
                // Read each line of the file. Each line represents a vertex
                // and its associated pixel
                String line;
                while (fileInput.hasNextLine()) {
                
                    line = fileInput.nextLine();
                    
                    String split[] = line.split(",");
                    
                    // Create a new point
                    Point point = new Point();
                    
                    // Create the list of neighbors of the node if the node
                    // has neighbors
                    if (split.length == 3) {
                        point.x = Integer.parseInt(split[1]);
                        point.y = Integer.parseInt(split[2]);
                    }
                    
                    // Extract the coordinate of the node from the node name
                    // and add the node to the set of nodes
                    Coordinate coord = new Coordinate(split[0]);
                    pixelMap.put(coord.normalizeCoordinate(), point);
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
     * Computes the hashcode for this class. This hashcode is method is
     * overridden since the equals method is overridden. 
     * @return the computed hashcode.
     */
    @Override
    public int hashCode() {
        int code = x * 10000 + y * 100 + z * 100000;
        
        return code;
    }
}