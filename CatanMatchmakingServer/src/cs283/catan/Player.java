package cs283.catan;
public class Player extends User
{
	public ResourceCard[] resCards;
	public DevelopmentCard[] devCards;
	public int points;
	
	public String username;
	
	public Player(String username)
	{
		this.username = username;
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
}