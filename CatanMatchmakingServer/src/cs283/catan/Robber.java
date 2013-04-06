/**
 * Robber class that represents the robber on the board.
 */
package cs283.catan;

public class Robber {
	
    /**
     * Coordinates of the robber on the board.
     */
    private Board.Coordinate location;
    
    
    /**
     * Constructor
     */
    public Robber() {
        // Move the robber to a location of the board
        this.location = new Board.Coordinate(-50, -50, 0);
    }
    
    
    /**
     * Sets the location of the robber.
     * @param location
     */
    public final void setLocation(Board.Coordinate location) {
        this.location = location;
    }
    
    /**
     * Retrieves the location of the robber.
     * @return the location of the robber.
     */
    public final Board.Coordinate getLocation() {
        return location;
    }
}