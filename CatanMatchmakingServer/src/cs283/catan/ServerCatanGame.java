//Kevin Zeillmann
//CS283
/**
 * Class representing a single in-progress Catan game
 */
package cs283.catan;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;

public class ServerCatanGame implements Serializable
{
	/**
     * 
     */
    private static final long serialVersionUID = -5218511975631672561L;
    
    //number of users playing the game
	private int numUsers;
	
	//array representing the users in the game
	private Player[] userArray; //keep users in sorted order
	
	/**
	 * Object used to notify threads when the game has been modified
	 *
	 */
	public class Notifier implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 4132743921540603718L;
	    
	}
	/**
	 * Object used to notify threads when the game has been modified
	 */
	public Notifier gameChangedNotifier = new Notifier();
	 
	// array of development cards represents the deck
	//private DevelopmentCard[] resDeck;
	//this should represent an index in the userArray - whose turn it is
	
	private int turn; 
	
	private int diceRoll;
	
	private Random rollGenerator;
	
	private Board myBoard;
	
	private boolean victory;
	
	private int turnNumber; //for debugging purposes
	
	/**
	 * Deck of development cards
	 */
	private LinkedList<DevelopmentCard> cardDeck = 
	                                          new LinkedList<DevelopmentCard>();
	
	/**
     * Name of the player with the current longest road
     */
    private String longestRoadOwner = null;
    
    /**
     * Length of the current longest road
     */
    private int longestRoadLength = 0;
    
    /**
     * Name of the player with the current largest army
     */
    private String largestArmyOwner = null;
    
    /**
     * Size of the current largest army
     */
    private int largestArmySize = 0;
    
	
	//alternate constructor = we will use this to construct the game
	public ServerCatanGame(Player playerArray[])
	{
		myBoard = new Board();
			
		numUsers = 4;
		turn = 0;
		turnNumber = 0; // zero indexed
		victory = false; // nobody has declared victory yet 
		                 // - it's the start of the game
		//set up user array - using objects John gives us
		//userArray = new Player[numUsers];
		
		//this isn't C++, so we need to initialize the array with new objects
		//for(int i = 0; i < numUsers; ++i)
		//{
		//	String playerString = "Player " + i;// for debugging
		//	userArray[i] = new Player(playerString);
		//}
		
		this.userArray = playerArray;
		
		// we also need a to generate dice rolls
		rollGenerator = new Random();
		
		
		// Generate the development card deck

        // KNIGHT, VICTORY_POINTS, YEAR_OF_PLENTY, MONOPOLY, ROAD_BUILDING

        for (int i = 0; i < 15; i++) {
            cardDeck.add(new DevelopmentCard(DevelopmentCard.DevCardType.KNIGHT));
        }

        for (int i = 0; i < 4; i++) {
            cardDeck.add(new DevelopmentCard(DevelopmentCard
                                             .DevCardType.VICTORY_POINTS));
        }

        for (int i = 0; i < 2; i++) {
            cardDeck.add(new DevelopmentCard(DevelopmentCard
                                             .DevCardType.YEAR_OF_PLENTY));
        }

        for (int i = 0; i < 2; i++) {
            cardDeck.add(new DevelopmentCard(DevelopmentCard
                                             .DevCardType.MONOPOLY));
        }

        for (int i = 0; i < 2; i++) {
            cardDeck.add(new DevelopmentCard(DevelopmentCard
                                             .DevCardType.ROAD_BUILDING));
        }
		
	}
	
	/**
	 * Retrieve the player array.
	 * @return the player array.
	 */
	public Player[] getPlayerArray() {
	    return this.userArray;
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
        
        
        // For debugging, set up an initial configuration
        myBoard.addSettlement(new Coordinate(0,2,0), userArray[0], false, 
                              false);
        myBoard.addRoad(new Coordinate(0,2,0), new Coordinate(-1,1,1),
                        userArray[0], false);
        
        myBoard.addSettlement(new Coordinate(-1,-1,0), userArray[0], false,
                              false);
        myBoard.addRoad(new Coordinate(-1,-1,0), new Coordinate(-1,-1,1),
                        userArray[0], false);
        
        userArray[0].resCards.addAll(
                    myBoard.getPlacementResourceCards(new Coordinate(-1,-1,0)));
        
        myBoard.addSettlement(new Coordinate(-2,2,0), userArray[1], false,
                              false);
        myBoard.addRoad(new Coordinate(-2,2,0), new Coordinate(-1,1,1),
                        userArray[1], false);
        myBoard.addSettlement(new Coordinate(1,-2,0), userArray[1], false,
                              false);
        myBoard.addRoad(new Coordinate(1,-2,0), new Coordinate(1,-2,5),
                        userArray[1], false);
        
        userArray[1].resCards.addAll(
                     myBoard.getPlacementResourceCards(new Coordinate(1,-2,0)));
        
        myBoard.addSettlement(new Coordinate(0,0,0), userArray[2], false,
                              false);
        myBoard.addRoad(new Coordinate(0,0,0), new Coordinate(0,0,1),
                        userArray[2], false);
        myBoard.addSettlement(new Coordinate(-2,0,0), userArray[2], false,
                              false);
        myBoard.addRoad(new Coordinate(-2,0,0), new Coordinate(-2,0,1),
                        userArray[2], false);
        
        userArray[2].resCards.addAll(
                     myBoard.getPlacementResourceCards(new Coordinate(-2,0,0)));
        
        myBoard.addSettlement(new Coordinate(2,-1,0), userArray[3], false,
                              false);
        myBoard.addRoad(new Coordinate(2,-1,0), new Coordinate(2,-1,5),
                        userArray[3], false);
        myBoard.addSettlement(new Coordinate(1,1,0), userArray[3], false,
                              false);
        myBoard.addRoad(new Coordinate(1,1,0), new Coordinate(1,1,5),
                        userArray[3], false);
        
        userArray[3].resCards.addAll(
                myBoard.getPlacementResourceCards(new Coordinate(1,1,0)));
        
        
        // Make the first roll
        rollDice();
	}
	
	
	
	
	/**
	 * mainGameLoop - we set up the game, play until victory
	 * then clean up the game
	 */
	/*public void mainGameLoop()
	{
		gameSetup();
		while(!victory)
		{
			System.out.println("It's turn number: " + turnNumber + ".");
			playTurn();
			advanceTurn("");
			if(turnNumber == 100)
				victory = true;
		}
		System.out.println("Victory!");
		cleanup();
	}*/
	
	/**
	 * - performs all necessary cleanup to end the game - requires integration
	 * with networking environment - perhaps unnecessary due to Java's memory
	 * managmeent
	 */
	/*public void cleanup()
	{
		
	}*/
	
	/**
	 * playTurn () - three things happen - the server rolls the dice and 
	 * players get resource cards and perform trades. They may also play
	 * development cards
	 */
	/*public void playTurn()
	{
		
		deliverResCards();
		trade(); //complete any trades
		playDevCards();		
		
	}*/
	
	/*public void playDevCards()
	{
		System.out.println("Here's where we play development cards."+
				userArray[turn].getUsername() + " can play development cards."); 
	}*/
	
	/*public void trade()
	{
		System.out.println("Here's where we trade. At this time, " 
				+ userArray[turn].getUsername() + " can trade."); 
		
	}*/
	
	/*public void deliverResCards()
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
		
		
	}*/
	
	/*public void rolledSeven()
	{
		moveRobber();
		stealCard();
	}*/
	
	/*private void moveRobber()
	{
		
	}*/
	
	/*private void stealCard()
	{
		
	}*/
	
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
		diceRoll = rollGenerator.nextInt(5)+
				 rollGenerator.nextInt(5)+2;
		
		
		// Give the appropriate resources to the players
		for (int i = 0 ; i < userArray.length; i++) {
		    userArray[i].resCards.addAll(myBoard
		                                 .getResourceCardsEarned(diceRoll, 
		                                                         userArray[i]));
		}
	}
	
	public int getDiceRoll()
	{
		return diceRoll;
	}
	
	/**
	 * Returns whether or not the turn belongs to the user with username.
	 * @param username
	 * @return whether or not the turn belongs to the user.
	 */
	public boolean isTurn(String username) {
	    return userArray[turn].getUsername().equals(username);
	}
	
	public void advanceTurn(String username)
	{
	    // Make sure user advancing the turn is the user currently with control
	    if (userArray[turn].getUsername().equals(username)) {
	        turn = (turn+1)%numUsers;
	        turnNumber++;
	    }
	    
	    rollDice();
	}
	
	public boolean drawDevelopmentCard(Player owner)
	{
	    boolean devCardAdded = false;
	    
	    DevelopmentCard devCard = null;
	    
		int nextCard = rollGenerator.nextInt(25);
		System.out.println("DEBUG: Card number is: " + nextCard);

		
		// Make sure user has proper hand
        boolean hasSheep = false;
        boolean hasOre = false;
        boolean hasWheat = false;
        
        for (ResourceCard card : owner.resCards) {
            switch (card.getCardType()) {
            case WOOL:
                hasSheep = true;
                break;
            case ORE:
                hasOre = true;
                break;
            case WHEAT:
                hasWheat = true;
                break;
            default:
            }
        }
		
        if (hasSheep && hasOre && hasWheat && cardDeck.size() > 0) {
            

            int purchasedCard = rollGenerator.nextInt(cardDeck.size());

            devCard = cardDeck.get(purchasedCard);

            cardDeck.remove(purchasedCard);

            owner.addDevCard(devCard);
            
            owner.doDevCardPurchase();
            
            devCardAdded = true;
        }
        
        return devCardAdded;
		
	}
	
	
	/**
	 * Returns the board.
	 * @return the board.
	 */
	public Board getBoard() {
	    return this.myBoard;
	}
	
	/**
	 * Returns the current turn number for debugging purposes.
	 * @return the turn number.
	 */
	public int getTurnNumber() {
	    return this.turnNumber;
	}
	
	/**
	 * Returns whether or not victory has been achieved.
	 * @return true if victory has been achieved, false otherwise.
	 */
	public boolean isVictory() {
	    return this.victory;
	}
	
	
	/**
     * Returns the name of the player who has the longest road. If no one
     * has the longest road, return null.
     * @return the name of the player with the longest road, or null if no
     *         one has the longest road.
     */
    public String whoHasLongestRoad() {
        //if (roadSet.containsKey("John")) {
        //    longestRoadOwner = "John";
        //}
        
        // The current owner of the road keeps the road in the event of a tie
        String defendingOwner = this.longestRoadOwner;
        int defendingOwnerLength = this.longestRoadLength;
        
        
        String updatedLongestRoadPlayer = null;
        int updatedLongestRoadLength = 0;
        
        for (Player player: userArray) {
            int playersLongestRoad = 
                            myBoard.getPlayersLongestRoad(player.getUsername());
            
            // If the player is the defending owner, store the length of the
            // defending owner's road
            if (player.getUsername().equals(defendingOwner)) {
                defendingOwnerLength = playersLongestRoad;
            }
            
            // If this player has the longest road so far, set the road length
            // as the longest length and the player as the owner of the longest
            // road so far
            if (playersLongestRoad > updatedLongestRoadLength) {
                updatedLongestRoadLength = playersLongestRoad;
                
                updatedLongestRoadPlayer = player.getUsername();
            }
            
            // DEBUG MESSAGE
            System.out.println(player.getUsername() + "'s longest road: " + 
                               playersLongestRoad);
        }
        
        this.longestRoadLength = updatedLongestRoadLength;
        
        // If all of the roads have length less than 5, no one has longest road
        if (updatedLongestRoadLength < 5) {
            this.longestRoadOwner = null;
        } else {
            // If the defending owner still has the longest road length, keep
            // the defending owner (even in the event of a tie). Otherwise,
            // choose the player with the greatest longest road.
            if (defendingOwnerLength != updatedLongestRoadLength) {
                this.longestRoadOwner = updatedLongestRoadPlayer;
            }
        }
        
        return longestRoadOwner;
    }
    
    /**
     * Returns the name of the player who has the largest army. If no one
     * has the largest army, return null.
     * @return the name of the player with the largest army, or null if no
     *         one has the largest army.
     */
    public String whoHasLargestArmy() {
        // The current owner of the road keeps the road in the event of a tie
        String defendingOwner = this.largestArmyOwner;
        int defendingOwnerLength = this.largestArmySize;
        
        
        String updatedLargestArmyPlayer = null;
        int updatedLargestArmySize = 0;

        for (Player player: userArray) {
            int playersArmySize = 0;
            
            // Determine the number 
            for (DevelopmentCard devCard : player.devCards) {
                if (devCard.getDevCardType() == 
                    DevelopmentCard.DevCardType.KNIGHT) {
                    
                    playersArmySize++;
                }
            }
            
            // If the player is the defending owner, store the length of the
            // defending owner's road
            if (player.getUsername().equals(defendingOwner)) {
                defendingOwnerLength = playersLongestRoad;
            }
            
            // If this player has the longest road so far, set the road length
            // as the longest length and the player as the owner of the longest
            // road so far
            if (playersLongestRoad > updatedLongestRoadLength) {
                updatedLongestRoadLength = playersLongestRoad;
                
                updatedLongestRoadPlayer = player.getUsername();
            }
            
            // DEBUG MESSAGE
            System.out.println(player.getUsername() + "'s longest road: " + 
                               playersLongestRoad);
        }
        
        this.longestRoadLength = updatedLongestRoadLength;
        
        // If all of the roads have length less than 5, no one has longest road
        if (updatedLongestRoadLength < 5) {
            this.longestRoadOwner = null;
        } else {
            // If the defending owner still has the longest road length, keep
            // the defending owner (even in the event of a tie). Otherwise,
            // choose the player with the greatest longest road.
            if (defendingOwnerLength != updatedLongestRoadLength) {
                this.longestRoadOwner = updatedLongestRoadPlayer;
            }
        }
        
        return longestRoadOwner;
    }
}