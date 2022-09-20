import java.net.*;

import java.util.*;

public class test {
	
	//Application client
	
	public static void main(String[] args) throws Exception{
		
		//Adresse et port du serveur
		boolean ipValid = true;
		int portMin = 5000;
		int portMax = 5050;
		boolean portValid = true;
		int boucle = 0;
		int boucle2 = 0;
		int portEntered = 0;
		
		while (ipValid) {
	     	Scanner IpAdresses = new Scanner (System.in);
	     	
	     	String message = (boucle == 0) ? "Bonjour cher client, Veuillez entrer votre adresse ip.\n":"Veuillez entrer une adresse ip valide.\n";
	     	System.out.println(message+"\n");
	    
	     	String adressEntered =  IpAdresses.nextLine();
	     	
	     	boolean ipV = Ipvalidation(adressEntered);
	     	
	     	if (ipV == true) {
	     		ipValid = false;
	     		System.out.println("IP valide"+"\n");
	     	}
	     	else {
	     		System.out.println ("L'adresse IP entrer est invalide"+"\n");
	     		boucle++;
	     	}
	     }
		
		while (portValid) {
	     	Scanner portAdress = new Scanner (System.in);
	     	String message2 = (boucle2 == 0) ? "Bonjour cher client, Veuillez entrer votre port.\n":"Veuillez entrer un port valide.\n";
	     	System.out.println(message2+"\n");
	     	
	     	if(portAdress.hasNextInt()) {
	     		portEntered = portAdress.nextInt();
	     	}
	     	else 
	     	{
	     		System.out.println("Veuiller entrer un port valide!");
	     	}
	     	
	    	boolean isPortValid  = (portMin <= Math.floor(portEntered) && portMax >= Math.floor(portEntered)) ? true: false;
	     	if (isPortValid == true) {
	     		portValid = false;
	     		System.out.println("Port valide"+"\n");
	     	}
	     	else {
	     		System.out.println ("Le Port entrer est invalide"+"\n");
	     		boucle2++;
	     	}
	     	///
	     	}
	}
	
	 public static boolean Ipvalidation(String ip)
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
     
     public static boolean isValidIpAdress(String ip)
     {
     	try {
     		return Inet4Address.getByName(ip).getHostAddress().equals(ip);
     	}
     	catch (UnknownHostException ex) {
     		return false;
     	}
     }
     
     
}

/*
 * 
	public static void main(String[] args) throws Exception{
		
		//Adresse et port du serveur
		
		
		
		boolean ipValid = true;
		int portMin = 5000;
		int portMax = 5050;
		boolean portValid = true;
		String serverAdress = "127.0.0.1";
		int serverPort = 5000;
		int boucle = 0;
		int boucle2 = 0;
		int portEntered = 0;
		
		int listeningPort = 5005;

		socket = new Socket(serverAdress, serverPort);
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		
		while (ipValid) {
	     	Scanner IpAdresses = new Scanner (System.in);
	     	System.out.println("Bonjour cher client, Veuillez entrer votre adresse ip.");
	     	String adressEntered =  IpAdresses.nextLine();
	     	
	     	boolean ipV = Ipvalidation(adressEntered);
	     	
	     	if (ipV == true) {
	     		ipValid = true;
	     	}
	     	else {
	     		out.writeUTF ("L'adresse IP entrer est invalide. Veuillez entre une adresse du format XXX.XXX.XXX.XXX :");
	     	}
	     	///
	     	}
		
		System.out.format("The server is running %s:%d%n", serverAdress, serverPort);
		
		// Creation d'un canal pour recevoir les messages du serveur
		
		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		// Reception du message et impression
		
		String helloMessageFromServer = in.readUTF();
		
		System.out.println(helloMessageFromServer);
		
		//DataOutputStream out = new DataOutputStream(socket.getOutputStream());
         // Envoie d'un message
         out.writeUTF ("Hello from server i'm on the side of the client#");
         
         
         ///validation de l'adresse ip
         
         // Création d'un canal pour envoyer des messages au client
  
     	// demande de l'adresse ip
     	
     	//port
         while (portValid) {
 	     	Scanner portAdress = new Scanner (System.in);
 	     	String message2 = (boucle2 == 0) ? "Bonjour cher client, Veuillez entrer votre port.\n":"Veuillez entrer un port valide.\n";
 	     	System.out.println(message2+"\n");
 	     	
 	     	if(portAdress.hasNextInt()) {
 	     		portEntered = portAdress.nextInt();
 	     	}
 	     	else 
 	     	{
 	     		System.out.println("Veuiller entrer un port valide!");
 	     	}
 	     	
 	    	boolean isPortValid1  = (portMin <= Math.floor(portEntered) && portMax >= Math.floor(portEntered)) ? true: false;
 	     	if (isPortValid1 == true) {
 	     		portValid = false;
 	     		System.out.println("Port valide"+"\n");
 	     	}
 	     	else {
 	     		System.out.println ("Le Port entrer est invalide"+"\n");
 	     		boucle2++;
 	     	}
 	     	///
 	     	}
 	
 	
		
		// Fermeture de la connexion avec le serveur
		
		socket.close();
		
	}
	
}

*/
