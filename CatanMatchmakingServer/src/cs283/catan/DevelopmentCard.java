/**
 * Class representing a development card. A development card object is
 * immutable.
 */
package cs283.catan;

import java.io.Serializable;

public final class DevelopmentCard extends Card implements Serializable
{
	/**
     * 
     */
    private static final long serialVersionUID = -4236592709613944278L;

    /**
     * Enumeration representing the different types of development cards.
     */
    public enum DevCardType
	{
		KNIGHT, VICTORY_POINTS, YEAR_OF_PLENTY, MONOPOLY, ROAD_BUILDING
	}
	
	private final DevCardType myType;
	
	public DevelopmentCard(DevCardType type)
	{
		myType = type;
	
	}
	
	public DevCardType getDevCardType()
	{
		return myType;
	}
	
	/**
	 * Overrides the toString method, so that when a DevelopmentCard is
	 * converted to a String, the name of the development card is returned.
	 */
	@Override
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