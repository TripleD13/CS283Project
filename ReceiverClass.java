import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class ReceiverClass extends Thread{
	public ReceiverClass(Socket rcvSocket)
	{
		receiveSocket = rcvSocket;
		start();
	}
	
	public void run()
	{
		//when a thread, while(1)
		DataInputStream input;
		StringBuffer messageString = new StringBuffer();
		try{
			input = new DataInputStream(receiveSocket.getInputStream());
			BufferedReader message = new BufferedReader(new InputStreamReader(input));
			int data = 0;
			while ((data = message.read()) != -1)
			{
				messageString.append(data);
				System.out.print((char) data);
			}
		}catch (IOException e)
		{
			System.out.println(e);
		}
		System.out.println(messageString);
		
		
		// chat* is chat command
		//route commands to essential people
	}
	
	public void finalize()
	{
		//close the socket?
	}
	
	public Socket receiveSocket;
}
