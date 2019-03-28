package ch.heigvd.res.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A very simple example of TCP server. When the server starts, it binds a
 * server socket on any of the available network interfaces and on port 2205. It
 * then waits until one (only one!) client makes a connection request. When the
 * client arrives, the server does not even check if the client sends data. It
 * simply writes the current time, every second, during 15 seconds.
 *
 * To test the server, simply open a terminal, do a "telnet localhost 2205" and
 * see what you get back. Use Wireshark to have a look at the transmitted TCP
 * segments.
 *
 * @author Olivier Liechti
 */
public class Client {

  static final Logger LOG = Logger.getLogger(Client.class.getName());
  private Socket clientSocket = null;
  private BufferedReader reader = null;
  private PrintWriter writer = null;
  
  public Client(int port, String ip) {
	
	  
    try {
		clientSocket = new Socket(ip,port);
		reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	    writer = new PrintWriter(clientSocket.getOutputStream());
	} catch (UnknownHostException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
    
    
  }
  
  public String compute(String userInput) {
	  String result = "";
	  try {
    	writer.println(userInput);
        writer.flush();
		result = reader.readLine();
		if(result.equals("Error")) {
			throw new IOException("Erreur");
		}
	} catch (IOException e) {
		e.printStackTrace();
	} 
	  return result;
  }
  
  public void close() {
      try {
        reader.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
      writer.close();
      try {
        clientSocket.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
  }
  

  /**
   * A utility method to print socket information
   *
   * @param clientSocket the socket that we want to log
   */
  public void logSocketAddress() {
    LOG.log(Level.INFO, "       Local IP address: {0}", new Object[]{clientSocket.getLocalAddress()});
    LOG.log(Level.INFO, "             Local port: {0}", new Object[]{Integer.toString(clientSocket.getLocalPort())});
    LOG.log(Level.INFO, "  Remote Socket address: {0}", new Object[]{clientSocket.getRemoteSocketAddress()});
    LOG.log(Level.INFO, "            Remote port: {0}", new Object[]{Integer.toString(clientSocket.getPort())});
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    System.setProperty("java.util.logging.SimpleFormatter.format", "%5$s %n");
    
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter ip address: ");
    String ip = sc.nextLine();
    
    System.out.println("Connecting to the server...");
    System.out.println("Starting client...");
    Client client = new Client(420, ip);
    
    client.logSocketAddress();
    
    System.out.println("The type must be : <operand1 operation operand2> or <operand1 operation>");
    System.out.println("Operation supported : + - * / % sqrt carre ");
    
    while(true) {
      	
        System.out.println("Please enter an equation: ");
        String userInput = "";
            try {
            	userInput = sc.nextLine();
            }
            catch (NoSuchElementException e) {
                userInput = "";
            }
    	
        if (userInput.equals("exit")) {
        	client.close();

            break; 
        }

        String result = client.compute(userInput);
        System.out.println("The result is : " + result);
      }
    sc.close();
  }
  


}
