/**
 * Class used to represent resource cards. A resource card object is immutable.
 */
package cs283.catan;

/**
 * Enumeration of the different card types. DESERT is not a card type,
 * but it is included so that a tile can indicate when it is a desert and
 * does not represent any of the resources.
 */
public enum ResourceCard
{
    LUMBER(0), WOOL(1), WHEAT(2), BRICK(3), ORE(4), 
    /* used by tiles */ DESERT(5);
    
    private int index;
    
    ResourceCard(int index) {
        this.index = index;
    }
    
    public int getIndex() {
        return index;
    }
}