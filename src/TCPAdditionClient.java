import java.io.*;
import java.net.*;

class TCPAdditionClient
{
	//Function to discern whether an item is an integer or not.
	public static boolean isNumber(String str)  
	{  
		try  
		{  
			int i = Integer.parseInt(str);  
		}  
		catch(NumberFormatException nfe)  
		{  
			return false;  
		}  
		return true;
	}

	public static void main(String[] args) throws Exception
	{	
		//Proper usage prompt.
		if(args.length <= 1)
		{
			System.out.println("Enter hostname and port number! Usage: java TCPAdditionClient server_name server_port ");
			System.exit(-1);
		} 
		
		//If port is given, save it into a string and convert to integer. Save hostname into string.
		int portnumber = Integer.parseInt(args[1]);
		String hostname=args[0];
		
		//Open a new client socket and input-output streams.
		Socket clientSocket=new Socket(hostname, portnumber);
		BufferedReader inFromUser=new BufferedReader(new InputStreamReader(System.in));
		DataOutputStream outToServer=new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		//Initialize variables to handle integer string, and to store final sum. Set 'check' to 1.
		String[] strarray;
		String intlist;
		String sum;
		String exitchoice;
		int check = 1;
		//Outer loop to send and receive entered integer lists and sums, and to keep prompting the user whether they want to enter another integer list.
		do
		{
			//Inner loop to ensure the list of integers entered is valid. Will allow program to proceed if check is set to 1 (integer entered); keeps reprompting if check is set to 0 (a non-integer was entered).
			do
			{
				System.out.print("Enter a string of integers separated by spaces: ");
				intlist = inFromUser.readLine();
				//Split input by whitespace delimiter into an array.
				strarray = intlist.split("\\s+");
				//Initialize counts of integers and nonintegers to 0.
				int intcount = 0;
				int nonintcount = 0;
				//For loop to examine whether each array element is an integer.
				for(int j = 0; j < strarray.length; j++)
				{
					if(TCPAdditionClient.isNumber(strarray[j]) == true)
					{
						intcount++;
					}
					else
					{
						nonintcount++;
					}
				}
				//If a non-integer is entered, count will be greater than 0. Set check to 0 to remain in loop.
				if(nonintcount > 0)
				{
					System.out.println("You entered a non-integer, or began your list of integers with one or more spaces.");
					check = 0;
				}
				//If count of integers is equal to the number of elements in array, all elements must be integers. Set check to 1, exit loop.
				else if(intcount == strarray.length)
				{
					check = 1;
				}
			} while(check != 1);
			
			//Write the list of integers to the server.
			outToServer.writeBytes(intlist+"\n");
			//Receive the sum of the integers back from the server, and print.
			sum=inFromServer.readLine();
			System.out.println("From the server: " + sum);
			
			//Read in choice of whether to sum another set of integers. Write the choice to the server, to either proceed or terminate the client thread. Exit the outer do-while loop here if the choice entered is 'N.'
			System.out.print("Enter another set of integers to sum? Y/N: ");
			exitchoice = inFromUser.readLine();
			outToServer.writeBytes(exitchoice+"\n");
		} while(!exitchoice.equalsIgnoreCase("N"));
		
		//Close connection.
		clientSocket.close();
	}
}