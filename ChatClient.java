import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;




public class ChatClient {
	public ChatClient(Socket srvSocket)
	{
		serverSocket = srvSocket;
	}
	
	
	
	//send socket
	Socket serverSocket;
	static String identity = "Tester: " ;
	
	
	//or can be sent in a main method.  Need to research how GUI interaction works.
	public void send()
	{
		

		
		BufferedReader commandLineInput = new BufferedReader(new InputStreamReader(System.in));
		while (true)
		{
			String message = "NULL";
			String messageToSend = identity;
			//input message
			try
			{
				message = commandLineInput.readLine();
			}catch (IOException ioe)
			{
				   	System.out.println("Failed to read from command line");
			}
			
			//if targeted, packetize

			if(message.charAt(0) == '/' && message.charAt(1) == 'p')
			{
				int firstIndex = message.indexOf('"');
				int lastIndex = message.indexOf('"', firstIndex+1);
				messageToSend = messageToSend.concat("/*/");
				messageToSend = messageToSend.concat(message.substring(firstIndex+1, lastIndex));
				messageToSend = messageToSend.concat("*/*");
				/*int spacesToAppend = 16-messageToSend.length();
				char [] spaces = new char [spacesToAppend];
				for (int i = 0; i <spacesToAppend; i++)
				{
					spaces[i] = ' ';
				}
				String stringSpace = new String(spaces);
				messageToSend = messageToSend.concat(stringSpace);*/
				messageToSend = messageToSend.concat(message.substring(lastIndex+1));
				messageToSend = messageToSend.concat(" ***PRIVATE***");
			}else
			{
			//if not targeted, packetize
			//messageToSend = "            ";	
			messageToSend = messageToSend.concat(message);
			}
			
			String chatCommand = "chat*";
			messageToSend = chatCommand.concat(messageToSend);
			byte [] messageBytes = messageToSend.getBytes();
			System.out.println(messageToSend);
			
			
		}
	}
	
	
	public static void messageDisplayThread(String messageReceived)
	{

		while (true)
		{
		//receive message
		
		//depacketize message
	//	messageReceived = new String(messageBytesReceived);
		
		//display message
	//	System.out.println(messageReceived);
		}
	}
	
}
