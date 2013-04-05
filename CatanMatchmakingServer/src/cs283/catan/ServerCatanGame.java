//Kevin Zeillmann
//CS283
package cs283.catan;
import java.util.Random;






public class ServerCatanGame
{
	//number of users playing the game
	private int numUsers;
	//array representing the users in the game
	private Player[] userArray; //keep users in sorted order
	
	 
	// array of development cards represents the deck
	private DevelopmentCard[] resDeck;
	//this should represent an index in the userArray - whose turn it is
	private int turn; 
	private int diceRoll;
	private Random rollGenerator;
	
	private Board myBoard;
	
	private boolean victory;
	
	//alternate constructor = we will use this to construct the game
	public ServerCatanGame()
	{
		myBoard = new Board(); //TODO: this will construct the board and set it up
		//this is done the same way every time - the board is not generated randomly
		
		//set up user array - using objects John gives us
		
		rollGenerator = new Random();
		numUsers = 4;
		turn = 0;
		victory = false;
	}
	/**
	 *  - performs all setup not done in constructor, involves putting board together
	 */
	public void gameSetup()
	{
		//stuff involving the board
	}
	
	/**
	 * mainGameLoop - we set up the game, play until victory
	 * then clean up the game
	 */
	public void mainGameLoop()
	{
		gameSetup();
		while(!victory)
		{
			playTurn();
		}
		
		cleanup();
	}
	
	/**
	 * - performs all necessary cleanup to end the game - requires integration
	 * with networking environment
	 */
	public void cleanup()
	{
		
	}
	
	
	public void playTurn()
	{
		//during a turn, three things happen - the server rolls the dice and 
		//players get resource cards and perform trades
		
		deliverResCards();
		trade(); //complete any trades
		playDevCards();		
		
	}
	
	public void playDevCards()
	{
		System.out.println("Here's where we play development cards."); 
	}
	
	public void trade()
	{
		System.out.println("Here's where we trade."); 
		//INCLUDE MESSAGE ABOUT WHOSE TURN IT IS
	}
	
	public void deliverResCards()
	{
		//we roll the dice
		rollDice();
		//now we iterate through players to see what they get
		for(int i = 0; i < numUsers; ++i)
		{
			userArray[i].giveResCard(diceRoll);
		}
		// users now have updated dice rolls
		
		
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