/**
 * Class representing a settlement or city
 */
package cs283.catan;

import java.io.Serializable;

public final class Settlement implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 8502684121006880852L;

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
    public Coordinate getLocation() {
        return location;
    }
    
    /**
     * Retrieves the owner of the settlement.
     * @return the owner of the settlement.
     */
    public Player getOwner() {
        return owner;
    }
    
    /**
     * Retrieves whether or not the settlement is a city.
     * @return whether or not the settlement is a city.
     */
    public boolean isCity() {
        return isCity;
    }
    
    
    /**
     * Upgrades the settlement to a city.
     */
    public void upgradeToCity() {
        isCity = true;
    }
}