import java.io.*;
import java.net.*;

class TCPClientHandler extends Thread
{
	//Variables for the passed socket and its input-output streams.
    private Socket client;
    private BufferedReader inFromClient;
    private DataOutputStream outToClient;

	//Constructor to create a particular socket object's unique handler.
    public TCPClientHandler(Socket socket)
    {
		client = socket;
		//Create input and output streams for this socket.
		try {
				inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
				outToClient = new DataOutputStream(client.getOutputStream());
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
    }
    
	//Method run by each new handler thread.
	public void run()
    {
		//Initialize variables.
		String intlist;
		String exitreceive;
		int finalSum = 0;
		try
		{	
			do
			{
				//Get the integer list from client and split it by whitespace delimiter into an array.
				intlist=inFromClient.readLine();
				//System.out.println("From client: " + intlist);
				String[] listarray = intlist.split("\\s+");
				
				//For loop to convert the contents of the array to integers, and sum them.
				for(int i = 0; i < listarray.length; i++)
				{
					int value = Integer.parseInt(listarray[i]);
					finalSum = finalSum + value;
				}
				
				//Save result into string and write to client. Reset finalSum variable to 0 so the server doesn't keep cumulatively summing every entry.
				String sum = "Sum = " + finalSum;
				outToClient.writeBytes(sum+"\n");
				finalSum = 0;
				//Read in from the client the answer to the 'Enter more integers?' question. Exit this do-while loop and proceed to closing connection if the answer is 'N,' otherwise continue with loop.
				exitreceive = inFromClient.readLine();
			} while(!exitreceive.equalsIgnoreCase("N"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		//Once finished, if socket exists, close connection down.
		finally
		{
			try
			{
				if (client!=null)
				{
					System.out.println("Closing down connection from " + client.getInetAddress() + ":" + client.getPort() + ".");
					client.close();
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}