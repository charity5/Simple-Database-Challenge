import java.util.*;
import java.io.*;

public class run {

	public static void main(String[] args) throws IOException {
		simpleDB db = new simpleDB();

		//Read data from file or from terminal interactively
		BufferedReader reader;
		if (args == null || args.length == 0) {
			reader = new BufferedReader(new InputStreamReader(System.in));
		}else {
			File file = new File(args[0]);
			if (!file.exists()) {
				System.out.println("Input file doesn't exist!");
				return;
			}
			reader = new BufferedReader(new FileReader(file));
		}
		
		
		while (true) {
			//System.out.println("Please enter your command: ");
			String word = reader.readLine();
			if (word == null) {          //Handle EOF
				return;
			}
			String[] input = word.split(" ");
			String command = input[0];
			String name;
			int value;
			
			try {
				switch(command) {
				case "SET":
					name = input[1];
					value = Integer.parseInt(input[2]);
					db.set(name, value);
					break;
				case "GET":
					name = input[1];
					Integer rst = db.get(name);					
					if (rst == null) {
						System.out.println("NULL");
					}else {
						System.out.println(rst);
					}
					break;
				case "UNSET":
					name = input[1];
					db.unset(name);
					break;
				case "NUMEQUALTO":
					value = Integer.parseInt(input[1]);
					System.out.println(db.numEqualTo(value));
					break;
				case "END":
					reader.close();
					return;
				case "BEGIN":
					db.begin();
					break;
				case "ROLLBACK":
					db.rollback();
					break;
				case "COMMIT":
					db.commit();
					break;
				default:
					System.out.println("Invalid Command: " + word);	
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid number format: " + word);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Missing operand: " + word);
			}
			
		}

	}
}