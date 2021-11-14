// Changed for E50 b JW

import java.io.*;

import client.ChatClient;
import common.*;
import ocsf.server.*;

public class ServerConsole implements ChatIF{

	private EchoServer server;

	public ServerConsole(EchoServer server){
		this.server = server;
		try
    	{
      		BufferedReader fromConsole = 
        	new BufferedReader(new InputStreamReader(System.in));
      		String message;

    	  	while (true) 
      		{
        		message = fromConsole.readLine();

        		handleMessage(message);
        		
      		}	
    	} 

    	catch (Exception ex) 
    	{
      	System.out.println
        	("Unexpected error while reading from console!");
    	}
	}

	private void handleMessage(String msg){
		if(msg.charAt(0) == '#'){
			String [] words = msg.split(" ");
			switch(words[0]){
				case "#quit":
					try{
						server.close();
					}
					catch(IOException e){
						System.out.println("Unable to close and terminate server");
					}
					finally{
						System.exit(0);
						break;
					}
				case "#stop":
					server.stopListening();
					break;

				case "#close":
					try{
						server.close();
					}
					catch(IOException e){
						System.out.println("Unable to close server");

					}
					finally{
						break;
					}
				case "#start":
					if(!server.isListening()){
						try{
							server.listen();
						}
						catch(IOException e){
							System.out.println("Unable to listen");

						}	
					}
					else{
						System.out.println("The server is already listening");

					}
					break;

				case "#getport":
					System.out.println(server.getPort());
					break;

				case "#setport":
					if(server.isClosed()){
						try {
        					server.setPort(Integer.parseInt(words[1]));
        				}
        				catch(ArrayIndexOutOfBoundsException e) {
				        	System.out.println("Error, port can not be empty.");
				        }
				        catch(NumberFormatException e) {
				        	System.out.println("Error, port must be an integer.");
				        }
					}
					else{
						System.out.println("Server is not closed");
					}
					break;
				default:
					System.out.println("Invalid input");
					break;


			}

		}
		
		else{
			display("Server MSG> "+msg);
			server.sendToAllClients("Server MSG> "+msg);
		}


		
	}


	public void display(String message){
		System.out.println(message);
	}

	
}