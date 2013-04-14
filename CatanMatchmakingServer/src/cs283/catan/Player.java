package cs283.catan;
import java.util.*;

public class Player
{
	public Set<ResourceCard> resCards = new HashSet<ResourceCard>();
	public Set<DevelopmentCard> devCards = new HashSet<DevelopmentCard>();
	public int points;
	
	public String username;
	
	private ServerMain.ServerConnectionHandler playerHandler;
	
	public Player(String username)
	{
		this(username, null);
	}
	
	public Player(String username, ServerMain.ServerConnectionHandler handler) {
	    this.username = username;
	    this.playerHandler = handler;
	}
	
	public ServerMain.ServerConnectionHandler getPlayerHandler() {
	    return this.playerHandler;
	}
	
	public void setPlayerHandler(ServerMain.ServerConnectionHandler handler) {
	    this.playerHandler = handler;
	}
	
	public String getUsername()
	{
		return this.username;
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