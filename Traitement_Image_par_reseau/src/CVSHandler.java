import java.io. * ;
import java.util.Scanner;

public class CVSHandler {
	public String file = "utilisateur.csv";
	public String delimiter = ",";
	private File csv ;
	
	public boolean login(String user, String password) {
		String line;
		String[] range = null;
		int position = 0;
		boolean exists =  false;
		boolean status = false;
		
		try {
			if(csvIsReal()) {
				Scanner scaner = new Scanner(csv);
				scaner.useDelimiter(delimiter);
				
				while(scaner.hasNextLine() && !exists) {
					line = scaner.nextLine();
					range = line.split(delimiter);
					position = PositionOfUser(range, user);
					if (position != -1) {
						exists = true;
						break;
					}
				}
				
				if (!exists) {
					addNewUser(user, password);
					status = true;
				}
				else if (range[position + 1].equals(password)) {
					status = true;
						
				} else status = false;
					
				scaner.close();
			}
			else {
				addNewUser(user, password);
				status = true;
			}
			}
		catch (IOException e) {
			e.printStackTrace();
			status = false;
		}
		
		return status;
		}
		
	
	
	private boolean csvIsReal() {
		csv = new File(file);
		if(csv.exists()) {
			return true;
		} else {
			try {
				csv.createNewFile();
				return true;
			}
			catch (Exception e) {
				return false;
			}
		}
	}
	
	private int PositionOfUser(String[] data, String user) {
		for (int i = 0; i<data.length; i = i+2){
			if (data[i].equals(user)) {
				return i;
			}
				
		}
		return -1;
	}
	
	private void addNewUser(String user, String pass) {
		try {
			FileWriter writer = new FileWriter(file,true);
			StringBuilder sb = new StringBuilder();
			sb.append(user);
			sb.append(delimiter);
			sb.append(pass);
			sb.append('\n');
			writer.write(sb.toString());
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
