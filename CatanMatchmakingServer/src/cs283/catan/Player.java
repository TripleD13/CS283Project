package cs283.catan;
import java.io.Serializable;
import java.util.*;

public class Player implements Serializable
{
	/**
     * 
     */
    private static final long serialVersionUID = -687219382118580014L;
    public Set<ResourceCard> resCards = new HashSet<ResourceCard>();
	public Set<DevelopmentCard> devCards = new HashSet<DevelopmentCard>();
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
	
	public int getColorIndex() {
	    return colorIndex;
	}
	
	public boolean hasSettlement(int x, int y)
	{
		return false; //still need to figure out board geometry
		
	}
	
	public void giveResCard(int diceRoll) 
	{
		
	}
	
	private void addResCard(ResourceCard.CardType type)
	{
		
	}
	
	public void addDevCard(DevelopmentCard card) {
	    devCards.add(card);
	}
	
	@Override
	public String toString() {
	    return username;
	}
}