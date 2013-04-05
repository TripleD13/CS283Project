package cs283.catan;
public class Player extends User
{
	public ResourceCard[] resCards;
	public DevelopmentCard[] devCards;
	public int points;
	
	
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