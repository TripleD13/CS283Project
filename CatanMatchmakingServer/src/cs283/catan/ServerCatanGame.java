//Kevin Zeillmann
//CS283
package cs283.catan;
import java.util.Random;






public class ServerCatanGame
{
	//number of users playing the game
	private int numUsers;
	//array representing the users in the game
	private User[] userArray; //keep users in sorted order
	
	 
	// array of development cards represents the deck
	private DevelopmentCard[] resDeck;
	//this should represent an index in the userArray - whose turn it is
	private int turn; 
	private int diceRoll;
	private Random rollGenerator;
	
	//alternate constructor = we will use this to construct the game
	public ServerCatanGame()
	{
		rollGenerator = new Random();
		numUsers = 4;
		turn = 0;
	}
	/**
	 * getTurn
	 * @return - the index of the userArray representing the user currently
	 * on his/her turn
	 */
	public int getTurn()
	{
		return turn;
	}
	
	public void rollDice()
	{
		diceRoll = rollGenerator.nextInt(6)+
				 rollGenerator.nextInt(6)+1;
	}
	
	public int getDiceRoll()
	{
		return diceRoll;
	}
	
	public void advanceTurn()
	{
		turn = (turn+1)%numUsers;
	}
	
	public DevelopmentCard drawDevelopmentCard()
	{
		int nextCard = rollGenerator.nextInt(25);
		System.out.println("DEBUG: Card number is: " + nextCard);
		//if 0<=nextCard<=14
		if(nextCard <15)
		{
			return new DevelopmentCard(DevelopmentCard.DevCardType.KNIGHT);
		}
		//if 15<=nextCard<=18
		else if(nextCard < 19)
		{
			return new DevelopmentCard(DevelopmentCard.DevCardType.VICTORY_POINTS);
		}
		//if nextCard is 19 or 20
		else if(nextCard < 21)
		{
			return new DevelopmentCard(DevelopmentCard.DevCardType.YEAR_OF_PLENTY);
		}
		else if(nextCard < 23)
		{
			return new DevelopmentCard(DevelopmentCard.DevCardType.MONOPOLY);
		}
		else
		{
			return new DevelopmentCard(DevelopmentCard.DevCardType.ROAD_BUILDING);
		}
		
	}
	
	
	
	
	
	
}