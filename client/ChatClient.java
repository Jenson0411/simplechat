// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */

   // *** Changed for E50 VF
  public void handleMessageFromClientUI(String message) 
  { 
  if(message.charAt(0)=='#') {
    String[] words=message.split(" ");
    String command=words[0];
    switch(command) {
    
    case "#quit":
      quit();
      break;
      
    case "#logoff":
      try {
        this.closeConnection();
      }
      catch(IOException e) {
        System.out.println("Can not disconnect!");
      }
      break;
      
    case "#sethost":
      if(this.isConnected()) {
        System.out.println("Error: client is connected, cannot set host.");
      }
      else {
        try {
          this.setHost(words[1]);
        }
        catch(ArrayIndexOutOfBoundsException e) {
          System.out.println("Error, host can not be empty.");
        }
      }
      break;
      
    case "#setport":
      if(this.isConnected()) {
        System.out.println("Error: client is connected, cannot set port.");
      }
      else {
        try {
          this.setPort(Integer.parseInt(words[1]));
        }
        catch(ArrayIndexOutOfBoundsException e) {
          System.out.println("Error, port can not be empty.");
        }
        catch(NumberFormatException e) {
          System.out.println("Error, port must be an integer.");
        }
      }
      break;
      
    case "#login":
      if(this.isConnected()) {
        System.out.println("Error: Client has already connected to the server.");
      }
      else {
        try {
          this.openConnection();
          System.out.println("Connect successfully");
        }
        catch(IOException e) {
          System.out.println("Error: can not connect to server.");
        }
      }
      break;
      
    case "#gethost":
      System.out.println("Current host: "+this.getHost());
      break;
      
    case "#getport":
      System.out.println("Current port: "+this.getPort());
      break;
      
    default:
      System.out.println("No such command: "+command);
      break;
    } 
  }
  else {
      try
      {
        sendToServer(message);
      }
      catch(IOException e)
      {
        clientUI.display
          ("Could not send message to server.  Terminating client.");
        quit();
      }
  }

  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  

  // *** Changed for E49 VF
  @Override
  protected void connectionClosed() {
  clientUI.display("The connection is closed to server");
  }
  
  // *** Changed for E49 VF
  @Override
  protected void connectionException(Exception exception) {
      clientUI.display("The server has shut down");
      quit();
  }
}
//End of ChatClient class
