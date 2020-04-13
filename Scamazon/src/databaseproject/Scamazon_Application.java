package databaseproject;

import java.sql.*;
import java.util.Scanner;

public class Scamazon_Application {

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection myCon = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/scamazon?useSSL=false&allowPublicKeyRetrieval=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
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
				Scamazon_Emp.employeeOptions(myCon, user, userid);
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
}