import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;




public class ChatClient {
	//ip address of server
	//port of server
	
	public static void main(){
		
		//spin thread to receive messages
		
		
		BufferedReader commandLineInput = new BufferedReader(new InputStreamReader(System.in));
		while (true)
		{
			String message = "NULL";
			String messageToSend = "NULL";
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
				messageToSend = message.substring(firstIndex+1, lastIndex);
				int spacesToAppend = 16-messageToSend.length();
				char [] spaces = new char [spacesToAppend];
				for (int i = 0; i <spacesToAppend; i++)
				{
					spaces[i] = ' ';
				}
				String stringSpace = new String(spaces);
				messageToSend = messageToSend.concat(stringSpace);
				messageToSend = messageToSend.concat(message.substring(lastIndex+1));
			}else
			{
			//if not targeted, packetize
			messageToSend = "            ";	
			messageToSend = messageToSend.concat(message);
			}
			
			byte [] messageBytes = messageToSend.getBytes();
			
			
		}
	}
	
	
	public static void messageDisplayThread()
	{
		
		while (true)
		{
		//receive message
		
		//depacketize message
		
		//display message
		}
	}
	
}
