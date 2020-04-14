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
				System.out.println("4: Exit\n");
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
					System.out.println("Please enter a valid input.\n");
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
			String userID = user.nextLine();
			System.out.println();
			System.out.print("Enter Password:  ");
			String password = user.nextLine();
			System.out.println();
			
			//prevents SQLInjection from login attempts
			PreparedStatement login = myCon.prepareStatement("Select * from users where userid=? and userpass=?");
			login.setString(1, userID.trim());
			login.setString(2, password.trim());
			ResultSet userLogin = login.executeQuery();
			
			//if user exists create users class
			if (userLogin.next()) {
				valid = true;
				System.out.println("Welcome to Scamazon " + userID +"\n");
				Scamazon_Users.customerOptions(myCon, user, userID);
			}

			login = myCon.prepareStatement("Select * from emp where empid=? and empPass=?");
			login.setString(1, userID.trim());
			login.setString(2, password.trim());
			userLogin = login.executeQuery();
			
			//if employee exists create emp class
			if (userLogin.next()) {
				valid = true;
				Scamazon_Emp.employeeOptions(myCon, user, userID);
			}
			
			if (valid == false) {
				System.out.println("Either the Username doesn't exist or the Password was incorrect.\n");
			}
		}
		catch (Exception e) {
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