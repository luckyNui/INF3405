import java.net.*;
import java.nio.ByteBuffer; // Needed to allocate the size of the image we want to send to the server
import java.nio.file.Path; // to read the path of the image we want to process
import java.nio.file.Paths;// to read the path of the image we want to process
import java.util.Scanner;

import javax.imageio.ImageIO;// to read the image send to the server

import java.io.*;
import java.awt.image.BufferedImage; /// to use for the images 

public class Client {
	private static Socket socket;
	private BufferedImage in;
    private BufferedImage out;
	//Application client
	
	// Welcome message on the creation of client
	public Client() {
        System.out.println("Welcome dear friend !");
    }
	
	public void ConnexionToServer() throws IOException{
		Scanner reader = new Scanner (System.in);
        String serverAddress = validateIP(reader);
        int serverPort = validatePort(reader);
        
        // login informations (username + password ) 
        System.out.println("Please enter your username");
        String username = reader.nextLine();
        
        System.out.println("Please enter your password");
        String password = reader.nextLine();
        
        //creation of the socket and connexion with server
        socket = new Socket(serverAddress, serverPort);
        System.out.format("The client is running on %s:%d%n", serverAddress, serverPort);
        
        // creation of a channel to  send user info to server 
        DataOutputStream loginInfo = new DataOutputStream(socket.getOutputStream());
        loginInfo.writeUTF(username);  
        loginInfo.writeUTF(password); 
        
        //creation of a channel to receive message from server
        DataInputStream connexionStatus = new DataInputStream(socket.getInputStream());
        String status = "";
        
        try {
        	status = connexionStatus.readUTF();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Oups...connexion failed !!!");
		} finally {
			if (status.contains("Erreur")) {
				System.out.println(status);
				socket.close();
				return;
			}
			System.out.println(status);
		}
        
        // image handling
        /** Pass image to socket*/
     	OutputStream outStream = socket.getOutputStream();
     	InputStream inStream = socket.getInputStream();
     	
     	/** Get image path*/
     	Path ImgPath = null;
 		System.out.println("Enter the path of the image you want to process: ");
 		System.out.println("Enter the name of the image : ");
 		String imageName = reader.nextLine();
 		try {
 			ImgPath = Paths.get(reader.nextLine());
 		} catch (Exception e) {
 			e.printStackTrace();
 			socket.close();
 			return;
 		}
 		 loginInfo.writeUTF(imageName); // send image name to server
 		 
 		/**Open the image */
 		File img = null;
 		try {
 			img = ImgPath.toFile();
 		}
 		catch (NullPointerException e) {
 			e.printStackTrace();
 		}
 		/**load the image*/
        out = ImageIO.read(img);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        
    	/**Transform the image data to stream */
        ImageIO.write(out, "JPEG", byteArrayOutputStream);
        
        // Send image stream through socket
        byte[] outSize = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
        outStream.write(outSize);
        outStream.write(byteArrayOutputStream.toByteArray());
        outStream.flush();
        System.out.println("The image has been sent to the server...");

        try {
			// receive the size of the processed image
			byte[] imgSize = new byte[4];
			inStream.read(imgSize);
			int inSize = ByteBuffer.wrap(imgSize).asIntBuffer().get();

			// receive the processed image
			byte[] imageArr = new byte[inSize];
			inStream.read(imageArr);
			System.out.println("The processed image has been received...");
			in = ImageIO.read(new ByteArrayInputStream(imageArr));
		}
		catch (IOException e) {
        	e.printStackTrace();
		}
        // ajout de -sobel au nom du fichier
 		String[] fileName = imageName.split(".jpg");
 		StringBuilder newName = new StringBuilder();
 		newName.append(fileName[0]);
 		newName.append("-sobel");
 		newName.append(".jpg");
 		Path outPath = Paths.get(ImgPath.getParent().toString(), newName.toString());

 		// save the file
 		ImageIO.write(in, "JPEG", new File(outPath.toString()));
 		System.out.format("The new image is saved at: %s%n", outPath.toString());

 		// close stream and socket
 		inStream.close();
 		outStream.close();
 		socket.close();
	}
	
	
	
	private static String validateIP(Scanner reader) {
		 System.out.println("Please enter an Ip adress to connect to the server.");
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
     
     //Runs the client application.
     public static void main(String[] args) throws Exception {
         Client client = new Client();
         client.ConnexionToServer();
     }
}

