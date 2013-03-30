package cs283.catan;
public class DevelopmentCard extends Card
{
	public enum DevCardType
	{
		KNIGHT, VICTORY_POINTS, YEAR_OF_PLENTY, MONOPOLY, ROAD_BUILDING
	}
	
	private DevCardType myType;
	
	public DevelopmentCard(DevCardType type)
	{
		myType = type;
	
	}
	
	public DevCardType getDevCardType()
	{
		return myType;
	}
	
	public String toString()
	{
		if(myType == DevCardType.KNIGHT)
			return "KNIGHT";
		else if(myType == DevCardType.VICTORY_POINTS)
			return "VICTORY_POINTS";
		else if(myType == DevCardType.YEAR_OF_PLENTY)
			return "YEAR_OF_PLENTY";
		else if(myType == DevCardType.MONOPOLY)
			return "MONOPOLY";
		else
			return "ROAD_BUILDING";
		
	}
	
	
	
}