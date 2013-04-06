import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ClientCommands extends Thread {
	/*
Help list
i = number
buy settlement i i i
buy city i i i
buy road i i i , i i i
buy devcard
tradepoint i resource to resource
trade i resource for i resource
trade i resource and i resource for i resource
trade i resource for i resource and i resource
trade i resource and i resource for i resource and i resource
play devcard
steal player
	 */
	public ClientCommands(Socket srvSocket)
	{
		serverSocket = srvSocket;
		start();
	}
	
	
	public void run()
	{
		BufferedReader commandLineInput = new BufferedReader(new InputStreamReader(System.in));

		String message = "NULL";
	
		while (true)
		{
			//input message
			try
			{
				message = commandLineInput.readLine();
			}catch (IOException ioe)
			{
				System.out.println("Failed to read from command line");
			}
			
			if (message.indexOf("buy") != -1)
			{
				/*
				buy
					settlement (coord)
					city (coord)
					road (coord) to (coord)
					devcard
				*/
				if (message.indexOf("settlement") != -1)
				{
					try{
						message = message.substring(message.indexOf("settlement") + 10);
						Scanner scanMessage = new Scanner(message);
						int coordinate1 = scanMessage.nextInt();
						int coordinate2 = scanMessage.nextInt();
						int coordinate3 = scanMessage.nextInt();
						//command
						}catch (Exception InputMismatchException)
						{
							System.out.println("Invalid");
						}
					
				}else if (message.indexOf("city") != -1)
				{
					try{
						message = message.substring(message.indexOf("city") + 4);
						Scanner scanMessage = new Scanner(message);
						int coordinate1 = scanMessage.nextInt();
						int coordinate2 = scanMessage.nextInt();
						int coordinate3 = scanMessage.nextInt();
						//command
						}catch (Exception InputMismatchException)
						{
							System.out.println("Invalid");
						}
					
				}else if (message.indexOf("road") != -1)
				{
					try{
						message = message.substring(message.indexOf("road") + 4);
						Scanner scanMessage = new Scanner(message);
						int coordinate1 = scanMessage.nextInt();
						int coordinate2 = scanMessage.nextInt();
						int coordinate3 = scanMessage.nextInt();
						message = message.substring(message.indexOf(',') +1);
						scanMessage = new Scanner(message);
						int coordinate4 = scanMessage.nextInt();
						int coordinate5 = scanMessage.nextInt();
						int coordinate6 = scanMessage.nextInt();
						//command
						}catch (Exception InputMismatchException)
						{
							System.out.println("Invalid");
						}
					
				}else if (message.indexOf("devcard") != -1)
				{
				
					//command
				}else 
				{
					System.out.println("Command Failure");
				}
				
				
			}else if(message.indexOf("tradepoint") != -1)
			{
				try
				{
					message = message.substring(message.indexOf("tradepoint")+ 10);
					Scanner messageScanner = new Scanner(message);
					int tradeNumber = messageScanner.nextInt();
					String resourceOne = message.substring(3, message.indexOf("to"));
					String resourceTwo = message.substring(message.indexOf("to")+3);
					
					
				}catch (Exception InputMismatchException)
				{
					System.out.println("Invalid");
				}
				
			}else if(message.indexOf("trade") != -1)
			{
				int andIndex1 = message.indexOf("and");
				int andIndex2 = message.indexOf("and", andIndex1 +1);
				int forIndex = message.indexOf("for");
				
				if (forIndex != -1)
				{
					try
					{
					message = message.substring(6);
				//	System.out.println(message);
					if (andIndex1 == -1)
					{
						//single trade
						Scanner numberScan = new Scanner(message);
						int thisItemNumber = numberScan.nextInt();
						String thisItem = message.substring(2, message.indexOf("for")-1);
						message = message.substring(message.indexOf("for")+3);
						numberScan = new Scanner(message);
						int thatItemNumber = numberScan.nextInt();
						String thatItem = message.substring(3);
						//command
						System.out.println(thisItemNumber);
						System.out.println(thisItem);
						System.out.println(thatItemNumber);
						System.out.println(thatItem);
					}else if (andIndex2 == -1 && andIndex1 < forIndex)
					{
						//2 for 1 trade
					//	System.out.println(message);
						Scanner numberScan = new Scanner(message);
						int thisItemNumber = numberScan.nextInt();
						String thisItem = message.substring(2, message.indexOf("and")-1);
						message = message.substring(message.indexOf("and")+4);
						//System.out.println(message);
						numberScan = new Scanner(message);
						int andThisItemNumber = numberScan.nextInt();
						String andThisItem = message.substring(2, message.indexOf("for")-1);
						
						message = message.substring(message.indexOf("for")+4);
					//	System.out.println(message);
						numberScan = new Scanner(message);
						int thatItemNumber = numberScan.nextInt();
						String thatItem = message.substring(2);
						//command


						System.out.println(thisItemNumber);
						System.out.println(thisItem);
						System.out.println(andThisItemNumber);
						System.out.println(andThisItem);
						System.out.println(thatItemNumber);
						System.out.println(thatItem);

					}else if (andIndex2 == -1 && andIndex1 > forIndex)
					{
						//1 for 2 trade
						//System.out.println(message);
						Scanner numberScan = new Scanner(message);
						int thisItemNumber = numberScan.nextInt();
						String thisItem = message.substring(2 ,message.indexOf("for")-1);
						message = message.substring(message.indexOf("for")+4);
					//	System.out.println(message);
						numberScan = new Scanner(message);
						int thatItemNumber = numberScan.nextInt();
						String thatItem = message.substring(2, message.indexOf("and")-1);
						message = message.substring(message.indexOf("and")+4);
					//	System.out.println(message);
						numberScan = new Scanner(message);
						int andThatItemNumber = numberScan.nextInt();
						String andThatItem = message.substring(2);
						//command
						
						System.out.println(thisItemNumber);
						System.out.println(thisItem);
						System.out.println(thatItemNumber);
						System.out.println(thatItem);
						System.out.println(andThatItemNumber);
						System.out.println(andThatItem);

					}else
					{
					    //2 for 2 trade	
						Scanner numberScan = new Scanner(message);
						int thisItemNumber = numberScan.nextInt();
						String thisItem = message.substring(2, message.indexOf("and")-1);
						message = message.substring(message.indexOf("and")+4);
						//System.out.println(message);
						numberScan = new Scanner(message);
						int andThisItemNumber = numberScan.nextInt();
						String andThisItem = message.substring(2, message.indexOf("for")-1);
						message = message.substring(message.indexOf("for")+4);
						//System.out.println(message);
						numberScan = new Scanner(message);
						int thatItemNumber = numberScan.nextInt();
						String thatItem = message.substring(2, message.indexOf("and")-1);
						message = message.substring(message.indexOf("and")+4);
						//System.out.println(message);
						numberScan = new Scanner(message);
						int andThatItemNumber = numberScan.nextInt();
						String andThatItem = message.substring(2);
						//command
						//command
						System.out.println(thisItemNumber);
						System.out.println(thisItem);
						System.out.println(andThisItemNumber);
						System.out.println(andThisItem);
						System.out.println(thatItemNumber);
						System.out.println(thatItem);
						System.out.println(andThatItemNumber);
						System.out.println(andThatItem);
					}
					}catch (Exception InputMismatchException)
					{
						System.out.println(InputMismatchException);
						System.out.println("Invalid");
					}
				}
			}else if(message.indexOf("play") != -1)
			{
				if(message.indexOf("year of plenty") != -1)
				{
					//command
				}else if (message.indexOf("knight") != -1)
				{
					//command
				}else if (message.indexOf("road builder") != -1)
				{
					//command
				}else if (message.indexOf("monopoly") != -1)
				{
					//command
				}else
				{
					System.out.println("Invalid");
				}
				
			}else if(message.indexOf("steal") != -1)
			{
				String toStealFrom = message.substring(6);
				//command
				
			}else if(message.indexOf("Get Ye Flask") != -1)
			{
				System.out.println("You Can't Get Ye Flask");
			  //debug mode	
			}else
			{
				System.out.println("Command Failure");
			}
			
		}
	}
	public Socket serverSocket;

}
