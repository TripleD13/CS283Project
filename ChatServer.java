
public class ChatServer {

	//array of user objects
	
	public void redirecteMessage(String message)
	{
		int firstIndex = message.indexOf("/*/");
		if ( firstIndex != -1)
		{
			int lastIndex = message.indexOf("*/*");
			String address = message.substring(firstIndex+3, lastIndex);
			
			//match address with user's socket
			
			String chatCommand = "chat*";
			String messageToSend = new String(message.substring(0, firstIndex));
			messageToSend = messageToSend.concat(message.substring(lastIndex+3));
			messageToSend = chatCommand.concat(messageToSend);
			byte [] messageBytes = messageToSend.getBytes();
			System.out.println(messageToSend);
			
		}else
		{
			String chatCommand = "chat*";
			String messageToSend = chatCommand.concat(message);
			byte [] messageBytes = messageToSend.getBytes();
			//Iterate through all sockets and send
			System.out.println(messageToSend);
		}
	}
	
	
}
