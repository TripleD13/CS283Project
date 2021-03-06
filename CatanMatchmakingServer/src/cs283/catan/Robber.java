/**
 * Robber class that represents the robber on the board.
 */
package cs283.catan;

import java.io.Serializable;

public final class Robber implements Serializable {
	
    /**
     * 
     */
    private static final long serialVersionUID = -5978014340822365199L;
    /**
     * Coordinates of the robber on the board.
     */
    private Coordinate location;
    
    
    /**
     * Constructor
     */
    public Robber() {
        // Move the robber to a location of the board
        setLocation(-50, -50);
    }
    
    
    /**
     * Sets the location of the robber.
     * @param x
     * @param y
     * @return whether or not the robber was actually moved.
     */
    public boolean setLocation(int x, int y) {
        boolean isRobberMoved = false;
        
        Coordinate newCoord = new Coordinate(x, y, 0);
        
        if (this.location == null || !this.location.equals(newCoord)) {
            this.location = newCoord;
            isRobberMoved = true;
        }

        return isRobberMoved;
    }
    
    /**
     * Retrieves the location of the robber.
     * @return the location of the robber.
     */
    public Coordinate getLocation() {
        return location;
    }
}