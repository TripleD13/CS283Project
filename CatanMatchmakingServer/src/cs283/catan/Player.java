/**
 * Class to represent a player.
 */
package cs283.catan;
import java.io.Serializable;
import java.util.*;

public class Player implements Serializable
{
	/**
     * 
     */
    private static final long serialVersionUID = -687219382118580014L;
    
    /**
     * List of resource cards owned by the player
     */
    public List<ResourceCard> resCards = new LinkedList<ResourceCard>();
    
    /**
     * List of development cards
     */
	public List<DevelopmentCard> devCards = new LinkedList<DevelopmentCard>();
	
	/**
	 * Number of victory points
	 */
	public int points;
	
	/**
	 * Color index used when drawing the board in the GUI.
	 */
	private int colorIndex;
	
	public String username;
	
	public Player(String username, int colorIndex)
	{
		this.username = username;
		this.colorIndex = colorIndex;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	/**
	 * Returns the color index of the player used by the GUI.
	 * @return the color index.
	 */
	public int getColorIndex() {
	    return colorIndex;
	}
	
	
	public void giveResCard(int diceRoll) 
	{
		
	}
	
	/*private void addResCard(ResourceCard.CardType type)
	 *{
	 *
	}*/
	
	public void addDevCard(DevelopmentCard card) {
	    devCards.add(card);
	}
	
	/**
	 * Removes one wheat, one wool, one brick, and one lumber from the player's
	 * hand.
	 */
	public void doSettlementPurchase() {
	    resCards.remove(new ResourceCard(ResourceCard.CardType.WHEAT));
	    resCards.remove(new ResourceCard(ResourceCard.CardType.WOOL));
	    resCards.remove(new ResourceCard(ResourceCard.CardType.BRICK));
	    resCards.remove(new ResourceCard(ResourceCard.CardType.LUMBER));
	}
	
	/**
	 * Removes one brick and one lumber from the player's hand.
	 */
	public void doRoadPurchase() {
	    resCards.remove(new ResourceCard(ResourceCard.CardType.BRICK));
	    resCards.remove(new ResourceCard(ResourceCard.CardType.LUMBER));
	}
	
	/**
	 * Removes three ore and two wheat from the player's hand.
	 */
	public void doCityPurchase() {
	    resCards.remove(new ResourceCard(ResourceCard.CardType.ORE));
	    resCards.remove(new ResourceCard(ResourceCard.CardType.ORE));
	    resCards.remove(new ResourceCard(ResourceCard.CardType.ORE));
	    resCards.remove(new ResourceCard(ResourceCard.CardType.WHEAT));
	    resCards.remove(new ResourceCard(ResourceCard.CardType.WHEAT));
	}
	
	/**
	 * Removes one ore, one wheat, and one wool from the player's hand.
	 */
	public void doDevCardPurchase() {
	    resCards.remove(new ResourceCard(ResourceCard.CardType.ORE));
        resCards.remove(new ResourceCard(ResourceCard.CardType.WHEAT));
        resCards.remove(new ResourceCard(ResourceCard.CardType.WOOL));
	}
	
	/**
	 * Overrides the toString method, so that when a Player is converted to
	 * a String, the name of the player is returned.
	 */
	@Override
	public String toString() {
	    return username;
	}
}