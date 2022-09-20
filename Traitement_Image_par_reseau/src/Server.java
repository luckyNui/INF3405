import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
	
	 private static ServerSocket listener;
	 
	    // Application Serveur
	 private static String validateIP(Scanner reader) {
		 System.out.println("Bonjour mon cher ami, Veuillez entrer votre adresse ip.");
		 String ipAdress ="";
		 boolean ipValid = true;
			while (ipValid) {
					ipAdress =  reader.nextLine();
					boolean ipV = Ipvalidation(ipAdress);
			     	
			     	if (ipV == true) {
			     		System.out.println("IP valide");
			     		ipValid = false;
			     	}
			     	else {
			     		System.out.println("L'adresse IP entrer est invalide. Veuillez entre une adresse du format XXX.XXX.XXX.XXX : ");
			     	}
			}	
		return ipAdress;
		}
	
	 private static boolean Ipvalidation(String ip)
     {
     	// validation pour les 4 octets
     	int count = 0;
     	int defaultComma = 3;

     	for (int i = 0; i< ip.length(); i++) {
     		if(ip.charAt(i) == '.') {
     			count++;
     		}
     	}
     	
     	if (count > defaultComma) {
     		
     		return false;
     	}
     	
     	/// validation de l'IP
     	return isValidIpAdress(ip);	
     }
     
     private static boolean isValidIpAdress(String ip)
     {
     	try {
     		return Inet4Address.getByName(ip).getHostAddress().equals(ip);
     	}
     	catch (UnknownHostException ex) {
     		return false;
     	}
     }
     
     private static int validatePort(Scanner reader) {
    	 
    	int portMin = 5000;
 		int portMax = 5050;
 		boolean portValid = true;
 		int portEntered = 0;
 		
    	 while (portValid) {
 	     	System.out.println("Veuillez entrer un port valide.");
 	     	
 	     	if(reader.hasNextInt()) {
 	     		portEntered = reader.nextInt();
 	     	}
 	     	else 
 	     	{
 	     		System.out.println("Veuiller entrer un nombre entier comme port.");
 	     	}
 	     	
 	    	boolean isPortValid  = (portMin <= Math.floor(portEntered) && portMax >= Math.floor(portEntered)) ? true: false;
 	     	if (isPortValid == true) {
 	     		portValid = false;
 	     		System.out.println("Port valide");
 	     	}
 	     	else {
 	     		System.out.println ("Veuiller entrer un port inclu entre 5000 et 5050 !");
 	     	}
 	     	///
 	     	}
    	 
    	 return portEntered;
     }
     
	    public static void main(String[] args) throws Exception
	    {
	        // Compteur de connexion
	    	
	        int clientNumber = 0;
	        
	        // Adresse et port du serveur
	        
	        Scanner reader = new Scanner (System.in);
	        String serverAddress = validateIP(reader);
	        int serverPort = validatePort(reader);
	        
	        
	        // Création d'une connexion avec les clients
	        
	        listener = new ServerSocket();
	        listener.setReuseAddress(true);
	        InetAddress serverIP = InetAddress.getByName (serverAddress);
	        
	        // Association de l'adresse et du port
	        listener.bind(new InetSocketAddress(serverIP, serverPort));
	        System.out. format ("The server is running %s:%d%n", serverAddress, serverPort);
	        
	        
	        try
	        {
	            // Pour chaque connexion d'un client
	            while(true)
	            {
	            // On attend le prochain client
	            // Note : la fonction accept est bloquante
	            new ClientHandler(listener.accept(), clientNumber++).start();
	            }
	        }
	        finally
	        {
	            // Fermeture de la connexion avec le client
	            listener.close();
	        }
	    }
	    
	    private static class ClientHandler extends Thread
	    {
	        private Socket socket;
	        private int clientNumber;
	        public ClientHandler(Socket socket, int clientNumber)
	        {
	            this.socket = socket;
	            this.clientNumber = clientNumber;
	            System.out.println( "New connection with client#" + clientNumber + " at " + socket);
	        }
	        public void run()
	        {
	            try
	            {
	            	///
	                // Création d'un canal pour envoyer des messages au client
	                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
	        
	                // Envoie d'un message      
	                DataInputStream in = new DataInputStream(socket.getInputStream());
	                String helloMessageFromServer = in.readUTF();
	        		
	        		System.out.println(helloMessageFromServer);
	            }
	            catch (IOException e)
	            {
	            System.out.println( "Error handling client#" + clientNumber + " . " + e);
	            }
	            finally
	            {
	            try
	            {
	            // Fermeture de la connexion avec le client
	            socket.close();
	            }
	            catch (IOException e)
	            {
	            System.out.println( "Could not close a socket");
	            }
	            System.out.println( "Connection with client#" + clientNumber + " closed") ;
	            }
	        } 
	    }
}
