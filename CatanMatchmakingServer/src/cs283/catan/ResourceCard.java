package cs283.catan;

import java.io.Serializable;

public class ResourceCard extends Card implements Serializable
{
	/**
     * 
     */
    private static final long serialVersionUID = -4232348882475754703L;

    public enum CardType
	{
		LUMBER, WOOL, WHEAT, BRICK, ORE, /* used by tiles */ DESERT
	}
	
	private final CardType cardType;
	
	public ResourceCard(CardType cardType) {
	    this.cardType = cardType;
	}
	
	public final CardType getCardType() {
	    return cardType;
	}
	
	@Override
	public String toString() {
	    return cardType.toString();
	}
}