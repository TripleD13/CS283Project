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
     * Changed on 4/19 to be an int array
     * 
     * BRICK
     * LUMBER
     * ORE
     * WHEAT
     * WOOL
     * in that order
     */
    
    public int[] resCards = new int[5];
    
    /**
     * List of development cards
     */
	public List<DevelopmentCard> devCards = new LinkedList<DevelopmentCard>();
	
	/**
	 * Number of victory points
	 */
	public int points;
	
	/**
	 * Number of knights played
	 */
	private int numKnightsPlayed;
	
	/*
	 * Boolean values for trade points
	 */
	
	public boolean has3To1Port = false;
	public boolean has2WoolPort = false;
	public boolean has2WheatPort = false;
	public boolean has2OrePort = false;
	public boolean has2LumberPort = false;
	public boolean has2BrickPort = false;
	
	
	/**
	 * Color index used when drawing the board in the GUI.
	 */
	private int colorIndex;
	
	public String username;
	
	public Player(String username, int colorIndex)
	{
		this.username = username;
		this.colorIndex = colorIndex;
		this.numKnightsPlayed = 0;
		this.resCards[0] = 0;
		this.resCards[1] = 0;
		this.resCards[2] = 0;
		this.resCards[3] = 0;
		this.resCards[4] = 0;
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
	
	public int getNumCards(String type)
	{
		
		if (type == "ore")
		{
			return resCards[2];
		}else if (type == "wool")
		{
			return resCards[4];
		}else if (type == "wheat")
		{
			return resCards[3];
		}else if (type == "lumber")
		{
			return resCards[1];
		}else if (type == "brick")
		{
			return resCards[0];
		}else
		{
			return -1;
		}
		
		
	}
	
	public int getNumCards()
	{
		int total = 0;
		for (int i = 0; i < 5; i++)
		{
			total += resCards[i];
		}
		return total;
	}
	
	public boolean addCards(String type, int number)
	{
		if (type == "ore")
		{
			resCards[2] = resCards[2] + number;
		}else if (type == "wool")
		{
			resCards[4] = resCards[4] + number;
		}else if (type == "wheat")
		{
			resCards[3] = resCards[3] + number;
		}else if (type == "lumber")
		{
			resCards[1] = resCards[1] + number;	
		}else if (type == "brick")
		{
			resCards[0] = resCards[0] + number;
		}else
		{
			return false;
		}
		return true;
	}
	
	public boolean removeCards(String type, int number)
	{
		if (type == "ore")
		{
			if (resCards[2] <= number)
			{
				resCards[2] = resCards[2] - number;
				return true;
			}
		}else if (type == "wool")
		{
			if (resCards[4] <= number)
			{
				resCards[4] = resCards[4] - number;
				return true;
			}
		}else if (type == "wheat")
		{
			if (resCards[3] <= number)
			{
				resCards[3] = resCards[3] - number;
				return true;
			}
		}else if (type == "lumber")
		{
			if (resCards[1] <= number)
			{
				resCards[1] = resCards[1] - number;
				return true;
			}
		}else if (type == "brick")
		{
			if (resCards[0] <= number)
			{
				resCards[0] = resCards[0] - number;
				return true;
			}
		}
		
		return false;
	}
	/**
	 * Returns the number of knights played.
	 * @return the number of knights played.
	 */
	public int getNumKnightsPlayed() {
	    return numKnightsPlayed;
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
	 * Plays a knight if one is available.
	 * @return whether or not a knight was successfully played.
	 */
	public boolean playKnight() {
	    boolean isKnightPlayed = false;
	    
	    // Find a knight card and play it
	    for (DevelopmentCard devCard : devCards) {
	        if (devCard.getDevCardType() == 
	            DevelopmentCard.DevCardType.KNIGHT) {
	            
	            devCards.remove(devCard);
	            isKnightPlayed = true;
	            
	            numKnightsPlayed++;
	            
	            break;
	        }
	    }
	    
	    return isKnightPlayed;
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