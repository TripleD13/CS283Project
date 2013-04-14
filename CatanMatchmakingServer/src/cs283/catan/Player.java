package cs283.catan;
public class Player
{
	public ResourceCard[] resCards;
	public DevelopmentCard[] devCards;
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
	
	@Override
	public String toString() {
	    return username;
	}
}