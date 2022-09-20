import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {
	private static Socket socket;
	
	//Application client
	
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
     
    private String connexion(Scanner reader) { 
    	
    	
    	return null;
    }
	
	public static void main(String[] args) throws Exception{
		Scanner reader = new Scanner (System.in);
        String serverAddress = validateIP(reader);
        int serverPort = validatePort(reader);
        socket = new Socket(serverAddress, serverPort);
        
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        System.out.format("The server is running %s:%d%n", serverAddress, serverPort);
        
        // Creation d'un canal pour recevoir les messages du serveur
		
        DataInputStream in = new DataInputStream(socket.getInputStream());
     		
     	// Reception du message et impression
     		
     	String helloMessageFromServer = in.readUTF();
     		
     	System.out.println(helloMessageFromServer);
     		
     	//DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        // Envoie d'un message
        out.writeUTF ("Hello from server i'm on the side of the client#");
                   
        socket.close();
	}
	
}

