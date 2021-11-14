// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  private ServerConsole server;// Changed for E50 JW
  private boolean isClosed; // Changed for E50 JW


  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
    isClosed = false;// changed for E50 JW
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
  }
    
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
	isClosed = false;// changed for E50 JW
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }

  //Changed for E50  JW
  protected void serverClosed() {
    isClosed = true;
    System.out.println("Server has closed");
  }
  
  //Changed for E50 JW
  public boolean isClosed(){
    return isClosed;
  }
  
  //Changed for E50 JW
  synchronized protected void clientException(
    ConnectionToClient client, Throwable exception) {
    clientDisconnected(client);
  }


  //Changed for E49  JW

  protected void clientConnected(ConnectionToClient client) {
     client.setInfo("hostname", client.toString()); 
	   System.out.println("The following client has connected: " + client);
     this.sendToAllClients("The following client has connected: " + client);
  }
  
  //Changed for E49   JW
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  System.out.println("The following client has disconnected: " + client.getInfo("hostname"));
    this.sendToAllClients("The following client has disconnected: " + client.getInfo("hostname"));
  } 


  

  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */

  //Changed for E50  JW
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }

    ServerConsole serverUI = new ServerConsole(sv);

  }
}
//End of EchoServer class
