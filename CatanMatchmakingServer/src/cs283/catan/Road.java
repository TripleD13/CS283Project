/**
 * Road class used primarily for drawing the roads in the GUI.
 */
package cs283.catan;

import java.io.Serializable;

public class Road implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 7219271518442053368L;

    /**
     * First coordinate of the road
     */
    private final Coordinate start;
    
    /**
     * Second coordinate of the road
     */
    private final Coordinate finish;
    
    /**
     * Owner of the road
     */
    private final Player owner;
    
    
    /**
     * Constructor
     * @start
     * @finish
     * @owner
     */
    public Road(Coordinate start, Coordinate finish, Player owner) {
        this.start = start;
        this.finish = finish;
        this.owner = owner;
    }
    
    
    /**
     * Gets the start coordinate.
     * @return the start coordinate.
     */
    public final Coordinate getStart() {
        return start;
    }
    
    /**
     * Gets the finish coordinate.
     * @return the finish coordinate.
     */
    public final Coordinate getFinish() {
        return finish;
    }
    
    /**
     * Get the owner of the road.
     * @return the owner.
     */
    public final Player getOwner() {
        return owner;
    }
}
