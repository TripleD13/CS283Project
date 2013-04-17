/**
 * Class used to represent resource cards. A resource card object is immutable.
 */
package cs283.catan;

import java.io.Serializable;

public final class ResourceCard extends Card implements Serializable
{
	/**
     * 
     */
    private static final long serialVersionUID = -4232348882475754703L;

    /**
     * Enumeration of the different card types. DESERT is not a card type,
     * but it is included so that a tile can indicate when it is a desert and
     * does not represent any of the resources.
     */
    public enum CardType
	{
		LUMBER, WOOL, WHEAT, BRICK, ORE, /* used by tiles */ DESERT
	}
	
	private final CardType cardType;
	
	public ResourceCard(CardType cardType) {
	    this.cardType = cardType;
	}
	
	public CardType getCardType() {
	    return cardType;
	}
	
	/**
	 * Overrides the toString method of the class so that when a ResourceCard
	 * is converted to a string, the name of the card type is returned.
	 */
	@Override
	public String toString() {
	    return cardType.toString();
	}
}