/* 
 * Client.java
 */

import java.io.*;
import java.net.*;

public class Client 
{
    
    public static final int SERVER_PORT = 7652;

    @SuppressWarnings("null")
    public static void main(String[] args) 
    {
	Socket clientSocket = null;  
	PrintStream os = null;
	BufferedReader is = null;
	String userInput = null;
	String serverInput = null;
	BufferedReader stdInput = null;

	//Check the number of command line parameters
	if (args.length < 1)
	{
	    System.out.println("Usage: client <Server IP Address>");
	    System.exit(1);
	}

	// Try to open a socket on SERVER_PORT
	// Try to open input and output streams
	try 
	{
	    clientSocket = new Socket(args[0], SERVER_PORT);
	    os = new PrintStream(clientSocket.getOutputStream());
	    is = new BufferedReader (
		new InputStreamReader(clientSocket.getInputStream()));
	    stdInput = new BufferedReader(new InputStreamReader(System.in));
	} 
	catch (UnknownHostException e) 
	{
	    System.err.println("Don't know about host: hostname");
	} 
	catch (IOException e) 
	{
	    System.err.println("Couldn't get I/O for the connection to: hostname");
	}

	// If everything has been initialized then we want to write some data
	// to the socket we have opened a connection to on port 

	if(clientSocket != null && os != null && is != null) 
	{
	    try 
	    {
                
                
                
		while ((userInput = stdInput.readLine())!= null)
		{
                    if(userInput.equals("SHUTDOWN")){os.close();
		is.close();
		clientSocket.close();
                System.exit(0);}
                    else{
		    os.println(userInput);//SEND DATA TO SERVER
                    
                     while(true){
                         serverInput = is.readLine();
                         if(serverInput.equalsIgnoreCase("END")){
                             break;
                         }
                      
		    System.out.println("S:" + serverInput);
                     }
                }}
                
            
            }
		
             
                
	    catch (IOException e) 
	    {
		System.err.println("IOException:  " + e);
	    }
            
	}
    }           
}
