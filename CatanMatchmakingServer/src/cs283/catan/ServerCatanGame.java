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
	
	private int turnNumber; //for debugging purposes
	
	//alternate constructor = we will use this to construct the game
	public ServerCatanGame()
	{
		myBoard = new Board();
			
		numUsers = 4;
		turn = 0;
		turnNumber = 0; // zero indexed
		victory = false; // nobody has declared victory yet - it's the start of the game
		//set up user array - using objects John gives us
		userArray = new Player[numUsers];
		
		//this isn't C++, so we need to initialize the array with new objects
		for(int i = 0; i < numUsers; ++i)
		{
			String playerString = "Player " + i;// for debugging
			userArray[i] = new Player(playerString);
		}
		
		// we also need a to generate dice rolls
		rollGenerator = new Random();
		
		
	}
	/**
	 *  - performs all setup not done in constructor, involves putting board together
	 *  involves the board. ugh.
	 */
	public void gameSetup()
	{
		//stuff involving the board
	       
        // Initialize the board graph and tiles
        if (!myBoard.
             loadBoardGraphFromResource("cs283/catan/resources/board.csv")) {
            
            System.out.println("Unable to load board graph data!");
        }
        
        // TODO: if we have time, we could make tile generation random
        if (!myBoard.
             loadBoardTilesFromResource("cs283/catan/resources/tiles.csv")) {
            
            System.out.println("Unable to load tiles from file!");
        }
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
			System.out.println("It's turn number: " + turnNumber + ".");
			playTurn();
			advanceTurn();
			if(turnNumber == 100)
				victory = true;
		}
		System.out.println("Victory!");
		cleanup();
	}
	
	/**
	 * - performs all necessary cleanup to end the game - requires integration
	 * with networking environment - perhaps unnecessary due to Java's memory
	 * managmeent
	 */
	public void cleanup()
	{
		
	}
	
	/**
	 * playTurn () - three things happen - the server rolls the dice and 
	 * players get resource cards and perform trades. They may also play
	 * development cards
	 */
	public void playTurn()
	{
		
		deliverResCards();
		trade(); //complete any trades
		playDevCards();		
		
	}
	
	public void playDevCards()
	{
		System.out.println("Here's where we play development cards."+
				userArray[turn].getUsername() + " can play development cards."); 
	}
	
	public void trade()
	{
		System.out.println("Here's where we trade. At this time, " 
				+ userArray[turn].getUsername() + " can trade."); 
		
	}
	
	public void deliverResCards()
	{
		//we roll the dice
		rollDice();
		System.out.println("Our dice roll is " + getDiceRoll() + ".");
		if(getDiceRoll()==7)
		{
			rolledSeven();
		}
		//now we iterate through players to see what they get
		for(int i = 0; i < numUsers; ++i)
		{
			userArray[i].giveResCard(diceRoll);
		}
		// users now have updated dice rolls
		
		
	}
	
	public void rolledSeven()
	{
		moveRobber();
		stealCard();
	}
	
	private void moveRobber()
	{
		
	}
	
	private void stealCard()
	{
		
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
		turnNumber++;
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