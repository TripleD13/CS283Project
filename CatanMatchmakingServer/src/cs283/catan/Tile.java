/**
 * Class representing a tile on the board
 */
package cs283.catan;
import java.util.*;


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