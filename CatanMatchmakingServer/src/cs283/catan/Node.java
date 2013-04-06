/**
 * Class representing a node on the board
 */
package cs283.catan;
import java.util.*;


public class Node {
    
    /**
     * List of all nodes adjacent to this node
     */
    private List<Coordinate> neighbors = new LinkedList<Coordinate>();
    
    /**
     * Settlement on this node, if any.
     */
    private Settlement settlement = null;
    
    
    /**
     * Sets the list of nodes adjacent to this node.
     * @param neighbors
     */
    public void setNeighbors(Coordinate neighbors[]) {
        for (Coordinate neighbor : neighbors) {
            this.neighbors.add(neighbor);
        }
    }
    
    /**
     * Gets the list of nodes adjacent to this node.
     * @return the list of neighbors.
     */
    public final List<Coordinate> getNeighbors() {
        return neighbors;
    }
    
    /**
     * Adds a neighbor to the list of neighbors
     */
    public void addNeighbor(Coordinate neighbor) {
        neighbors.add(neighbor);
    }
    
    /**
     * Sets the settlement on this node.
     * @param settlement
     */
    public void setSettlement(Settlement settlement) {
        this.settlement = settlement;
    }
    
    /**
     * Gets the settlement on this node.
     * @return the settlement on this node, or null if one does not exist.
     */
    public Settlement getSettlement() {
        return settlement;
    }
    
    
    /**
     * Returns whether or not this node is adjacent to a certain node.
     * @param node
     * return whether or not this node is adjacent to node.
     */
    public boolean isAdjacent(Coordinate node) {
        boolean isAdj = false;
        
        for (Coordinate neighbor : neighbors) {
            if (neighbor.equals(node)) {
                isAdj = true;
                break;
            }
        }
        
        return isAdj;
    }
    
    /**
     * Returns whether or not this node has a settlement.
     * @return whether or not this node has a settlement.
     */
    public boolean hasSettlement() {
        return settlement != null;
    }
    
}