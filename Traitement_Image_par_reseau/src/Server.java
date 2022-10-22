import java.net.*;
import java.io.*;
import java.util.*;
import java.sql.Timestamp;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class Server {
	
	 private static ServerSocket listener;
	 
	    // Application Serveur
	 private static String validateIP(Scanner reader) {
		 System.out.println("Welcome dear friend user, Please enter your Ip adress .");
		 String ipAdress ="";
		 boolean ipValid = true;
			while (ipValid) {
					ipAdress =  reader.nextLine();
					boolean ipV = Ipvalidation(ipAdress);
			     	if (ipV == true) {
			     		ipValid = false;
			     	}
			     	else {
			     		System.out.println("The Ip adress is invalid. Please enter an adress of the format XXX.XXX.XXX.XXX : ");
			     	}
			}	
		return ipAdress;
		}
	
	 private static boolean Ipvalidation( String ip )
     {
     	// number of comma for 4 octets 
     	int defaultComma = 3;
     	//count occurence of "." in string
     	int count = ip.split("\\.",-1).length-1; 
     	boolean isValid = (count > defaultComma)? false:isValidIpAdress(ip);	  
		
     	return isValid;     	
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
  		int port = 0;
  		
     	 while (portValid) {
     		 
  	     	System.out.println("Please enter a valid port. (should be between 5000 and 5050 ): ");
  	     	String portEntered = reader.nextLine();
  	     	port = Integer.parseInt(portEntered);
  	     	
  	    	boolean isPortValid  = (portMin <= port && portMax >= port) ? true: false;
  	     	if (isPortValid == true) {
  	     		portValid = false;
  	     	}
  	     	else {
  	     		System.out.println ("Please enter a port between 5000 and 5050 !");
  	     	}
  	     }
     	 
     	 return port;
      }
     
	    public static void main(String[] args) throws Exception
	    {
	        // Compteur de connexion
	    	
	        int clientNumber = 0;
	        
	        // Adresse et port du serveur
	        
	        Scanner reader = new Scanner (System.in);
	        String serverAddress = validateIP(reader);
	        int serverPort = validatePort(reader);
	        
	        
	        // Crï¿½ation d'une connexion avec les clients
	        
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
	        	String user = "";
	        	String passeword;
	            try
	            {
	   
	                // Création d'un canal pour envoyer des messages au client
	                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
	        
	                // Envoie d'un message      
	                DataInputStream in = new DataInputStream(socket.getInputStream());
	        		user = in.readUTF();
	        		passeword = in.readUTF();
	        		
	        		CVSHandler csv = new CVSHandler();
	        		boolean succes = csv.login(user, passeword);
	        		if (!succes) { 
	        			out.writeUTF("Erreur dans la saisie du mot de passe");
	        			socket.close();
	        			return;
	        		} else {
	        			out.writeUTF("Welcome " + user);}
	            }
	            catch (IOException e)
	            {
	            System.out.println( "Error handling client#" + clientNumber + " . " + e);
	            }
	            
	            try {
	            	DataOutputStream out = new DataOutputStream(socket.getOutputStream());
	    	        
	                // Envoie d'un message      
	                DataInputStream in = new DataInputStream(socket.getInputStream());
	                String ImageName = in.readUTF();
	                System.out.format( "[%s - %s:%d - %s] : Image %s recue pour taitement%n", user,
	                        socket.getInetAddress().toString(), socket.getPort(),
	                        new Timestamp(System.currentTimeMillis()).toString(), ImageName);
	                
	                byte[] sizeArr = new byte[4];
	            	in.read(sizeArr);
	            	int size = ByteBuffer.wrap(sizeArr).asIntBuffer().get();
	            	
	            	byte[] imageArr = new byte[size];
	            	in.read(imageArr);
	            	
	            	BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageArr));
	                BufferedImage sobel = Sobel.process(image);
	                
	                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					ImageIO.write(sobel, "JPEG", byteArrayOutputStream);
					
					byte[] outSize = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
					out.write(outSize);
					out.write(byteArrayOutputStream.toByteArray());
					out.flush();

					out.close();
					in.close();
	            	
	            }catch (IOException e) {
	            	System.out.println( "Error handling client#" + clientNumber + " . " + e);}
	        
	            
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
