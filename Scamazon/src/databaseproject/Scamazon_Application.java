package databaseproject;

import java.sql.*;
import java.util.Scanner;

public class Scamazon_Application {

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection myCon = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/scamazon?useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC",
					"cs4430", "cs4430");

			Scanner user = new Scanner(System.in);
			int loop = 1;
			while (loop == 1) {
				System.out.println("Welcome to Scamazon!");
				System.out.println("Select an Option:");
				System.out.println("1: Log in");
				System.out.println("2: Reset Password");
				System.out.println("3: Create Account");
				System.out.println("4: Exit");
				System.out.println();
				String input = user.nextLine();
				
				if (input.equals("1")) {
					logIn(myCon, user);
				}
				else if (input.equals("2")) {
					resetPassword(myCon, user);
				}
				else if (input.equals("3")) {
					createAccount(myCon, user);
				}
				else if (input.equals("4")) {
					loop = 0;
				} else {
					System.out.println("Please enter a valid input.");
				}
			}
			System.out.println("Now exiting. Thank you for choosing Scamazon");
			user.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void logIn(Connection myCon, Scanner user) throws SQLException {
		try {
			boolean valid = false;
			//will first determine if user is customer or employee. Will allow different options for different people
			System.out.print("Enter Username or Employee ID:  ");
			String userid = user.nextLine();
			System.out.println();
			System.out.print("Enter Password:  ");
			String password = user.nextLine();
			System.out.println();
			
			//prevents SQLInjection from login attempts
			PreparedStatement login = myCon.prepareStatement("Select * from users where userid=? and userpass=?");
			login.setString(1, userid.trim());
			login.setString(2, password.trim());
			ResultSet userLogin = login.executeQuery();
			
			//if user exists go to customer method
			if (userLogin.next()) {
				valid = true;
				System.out.println("Welcome to Scamazon " + userid);
				System.out.println();
				customerOptions(myCon, user);
			}

			
			login = myCon.prepareStatement("Select * from emp where empid=? and empPass=?");
			login.setString(1, userid.trim());
			login.setString(2, password.trim());
			userLogin = login.executeQuery();
			
			if (userLogin.next()) {
				valid = true;
				employeeOptions(myCon, user, userid);
			}
			
			if (valid == false) {
				System.out.println("Either the Username doesn't exist or the Password was incorrect.");
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void customerOptions(Connection myCon, Scanner user) throws SQLException {
		//not finished with this, but its a good start
		try {
			boolean loop = true;
			while (loop == true) {
				
			int count = 1;
			Statement stat1 = myCon.createStatement();
			ResultSet products = stat1.executeQuery("select description from categories");
			System.out.println("Select a Category of Product: (1-4)");
			while (products.next()) {
				System.out.println(count + ": " + products.getString(1));
				count++;
			}
			System.out.println();
			String input = user.nextLine();
			ResultSet items = stat1.executeQuery("select itemName, itemPrice, itemStock, description from items where itemcategory = " 
					+ "'" + input + "'");
			System.out.println();
			
			while(items.next()) {
				System.out.println(items.getString(1) + ", Price:" + items.getString(2) + ", Supply:" + items.getString(3));
				System.out.println(items.getString(4));
				System.out.println();	
			}
			System.out.println("Enter to continue, Enter 0 to exit.");
			input = user.nextLine();
			if (input.equals("0")) {
				loop = false;
			}
			}
		}catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void employeeOptions(Connection myCon, Scanner user, String empID) throws SQLException {
		try {
			//Get the category to be modified
			int i = 0;
			Statement stat = myCon.createStatement();
			ResultSet rs  = stat.executeQuery("SELECT c.categoryName, e.empName FROM categories c, emp e WHERE e.empID = c.empID AND c.empID = \'" + empID + "\'");
			while (rs.next()) {
				if (i == 0) {
					System.out.println("Hi, " + rs.getString(2) + "! Select a category you would like to modify:");
					System.out.println("---------------------------------------");
				}
				System.out.println(++i + ": " + rs.getString(1));
			}
			String selection = user.nextLine();
			if (checkInt(selection) < 0 || selection.compareTo("") == 0 || checkInt(selection) > i) {
				while (checkInt(selection) < 0 || selection.compareTo("") == 0 || checkInt(selection) > i) {
					System.out.println("\nError: You must input an integer value of your categories for your selection!");
					selection = user.nextLine();
				}
			}
			rs = stat.executeQuery("SELECT categoryName FROM categories WHERE category = " + Integer.parseInt(selection));
			while (rs.next()) System.out.println("\nNow modifying " + rs.getString(1) + "\n\n---------------------------------------");

			//Get the action to be taken
			System.out.println("Would you like to add, delete, or modify an item in the table?");
			System.out.println("1: Add a new item \n2: Delete an existing item \n3: Modify an existing item\n");
			String action = user.nextLine();
			if (checkInt(action) < 0 || action.compareTo("") == 0 || checkInt(action) > 3) {
				while (checkInt(action) < 0 || action.compareTo("") == 0 || checkInt(action) > 3) {
					System.out.println("\nError: You must input an integer value for your selection!");
					action = user.nextLine();
				}
			}
			int switchSel = Integer.parseInt(action);
			System.out.println(switchSel);
			switch (switchSel) {
				case 1:
				System.out.println("Adding a new item...");
				addItem(myCon, user, Integer.parseInt(selection));
				break;
				case 2:
				System.out.println("Deleting an existing item...");
				break;
				case 3:
				System.out.println("Modifying an existing item...");
				break;
			}
			
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void resetPassword(Connection myCon, Scanner user) throws SQLException {
		try {
			
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void createAccount(Connection myCon, Scanner user) throws SQLException {
		try {
			
		}catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void addItem(Connection myCon, Scanner user, int category) throws SQLException {
		try {
			//Get info for new product
			String reqFields[] = new String[] {"itemName", "itemPrice", "itemStock", "onOrder", "description"};
			String newItem[] = new String[5];
			String curInfo = null;

			for (int i = 0; i < reqFields.length; i++) {
				System.out.println("Enter " + reqFields[i] + ":");
				curInfo = user.nextLine();
				if (reqFields[i].compareTo("itemStock") == 0 || reqFields[i].compareTo("onOrder") == 0) {
					while (checkInt(curInfo) < 0 || curInfo.compareTo("") == 0) {
						System.out.println("Error: " + reqFields[i] + " must be an integer value!");
						curInfo = user.nextLine();
					}
				}
				if (reqFields[i].compareTo("itemPrice") == 0) {
					while (checkDouble(curInfo) < 0 || curInfo.compareTo("") == 0) {
						System.out.println("Error: " + reqFields[i] + " must be a double value!");
						curInfo = user.nextLine();
					}
				}
				while(curInfo.compareTo("") == 0) {
					System.out.println("Error: You must input something!");
					System.out.println("Enter " + reqFields[i] + ":");
					curInfo = user.nextLine();
				}
				newItem[i] = curInfo;
			}

			//Check if duplicate
			Statement stat = myCon.createStatement();
			ResultSet rs = stat.executeQuery("SELECT itemName FROM items WHERE itemName = \'" + newItem[0] + "\';");
			if (rs.next()) {
				System.out.println("Error: There's already an item named " + newItem[0] + "!");
				return;
			}

			//Insert into items
			int rs2 = stat.executeUpdate("INSERT INTO items (itemName, itemCategory, itemPrice, itemStock, onOrder, description) VALUES ('" 
			+ newItem[0] + "', " + category + ", " + newItem[1] + ", " + newItem[2] + ", " + newItem[3] + ", '" + newItem[4] + "');");
			System.out.println(newItem[0] + " added to items!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int checkInt(String s) {
		try {
			int i = Integer.parseInt(s);
			return i;
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public static double checkDouble(String s) {
		try {
			double i = Double.parseDouble(s);
			return i;
		} catch (NumberFormatException e) {
			return -1;
		}
	}
}
