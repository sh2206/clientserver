import java.io.*;
import java.net.*;

class TCPAdditionServer
{
	public static void main(String[] args)throws Exception
	{
		//If no port is given, exit program with prompt on proper usage.
		if(args.length <= 0)
		{
			System.out.println("Enter a port number! Usage: java TCPAdditionServer valid_portnumber ");
			System.exit(-1);
		} 
		
		//If port is given, save it into a string and convert to integer.
		String argport = args[0];
		int portnumber = Integer.parseInt(argport);
		
		//Now check if port number is within acceptable range. Exit program with prompt on appropriate ranges if not.
		if(portnumber < 1024 && portnumber >= 0)
		{
			System.out.println("Please don't use a well-known port. Enter a number between 1024 and 65535.");
			System.exit(-1);
		}
		if(portnumber < 0)
		{
			System.out.println("Port number out of bounds! Enter a number between 1024 and 65535.");
			System.exit(-1);
		}
		if(portnumber > 65535)
		{
			System.out.println("Port number out of bounds! Enter a number between 1024 and 65535.");
			System.exit(-1);
		}
		
		//Open new server socket on the entered port number, start listening.
		ServerSocket welcomeSocket=new ServerSocket(portnumber);
		System.out.println("Server started on TCP port " + portnumber + ".");
		System.out.println("Connections will be logged below.\n");
		
		while(true)
		{
			//While server is listening, create sockets for incoming connections and establish input-output streams.
			Socket connectionSocket=welcomeSocket.accept();
			
			//Print connection info (IP address, port)
			System.out.println("Received connection from " + connectionSocket.getRemoteSocketAddress().toString());
			
			//Create new thread to handle this socket, go back to listening.
			TCPClientHandler handler = new TCPClientHandler(connectionSocket);
			handler.start();
		}
	}
}