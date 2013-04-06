package cs283.catan;
public class ResourceCard extends Card
{
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
}