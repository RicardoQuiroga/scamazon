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
				System.out.println("2: Create Account");
				System.out.println("3: Exit\n");
				String input = user.nextLine();

				if (input.equals("1")) {
					logIn(myCon, user);
				} else if (input.equals("2")) {
					createAccount(myCon, user);
				} else if (input.equals("3")) {
					loop = 0;
				} else {
					System.out.println("Please enter a valid input.\n");
				}
			}
			System.out.println("Now exiting. Thank you for choosing Scamazon");
			user.close();
		} catch (Exception e) {

		}
	}

	public static void logIn(Connection myCon, Scanner user) throws SQLException {
		try {
			boolean valid = false;

			// will first determine if user is customer or employee. Will allow different
			// options for different people
			System.out.print("Enter Username or Employee ID:  ");
			String userID = user.nextLine();
			System.out.println();
			System.out.print("Enter Password:  ");
			String password = user.nextLine();
			System.out.println();

			// prevents SQLInjection from login attempts
			PreparedStatement login = myCon.prepareStatement("Select * from users where userid=? and userpass=?");
			login.setString(1, userID.trim());
			login.setString(2, password.trim());
			ResultSet userLogin = login.executeQuery();

			// if user exists create users class
			if (userLogin.next()) {
				if (userLogin.getString(2).equals(userID) && userLogin.getString(4).equals(password)) {
					valid = true;
					System.out.println("Welcome to Scamazon " + userID + "!\n");
					Scamazon_Users.customerOptions(myCon, user, userID);
				}
			}

			login = myCon.prepareStatement("Select * from emp where empid=? and empPass=?");
			login.setString(1, userID.trim());
			login.setString(2, password.trim());
			userLogin = login.executeQuery();

			// if employee exists create emp class
			if (userLogin.next()) {
				if (userLogin.getString(1).equals(userID) && userLogin.getString(2).equals(password)) {
					valid = true;
					if (userLogin.getString(1).equals("1")) Scamazon_Admin.adminOptions(myCon, user);
					else Scamazon_Emp.employeeOptions(myCon, user, userID);
				}
			}

			if (valid == false) {
				System.out.println("Either the Username doesn't exist or the Password was incorrect.\n");
			}
		} catch (Exception e) {

		}
	}

	public static void createAccount(Connection myCon, Scanner user) throws SQLException {
		String p1 = null;
		String userID = null;
		String email = null;
		String name = null;
		String address = null;
		String city = null;
		String post = null;
		String state = null;
		try {
			int loop = 1;
			int restart = 0;

			do {
				restart = 0;
				System.out.println("Creating account.\n");
				System.out.print("Enter your user name: ");
				userID = user.nextLine();
				System.out.print("Enter your email address: ");
				email = user.nextLine();
				while (true) {
					System.out.print("Enter your password: ");
					p1 = user.nextLine();
					System.out.print("Re-enter your password: ");
					String p2 = user.nextLine();
					if (p1.equals(p2)) {
						break;
					} else {
						System.out.println("The passwords do not match.\n");
					}
				}
				System.out.print("Enter your First and Last name: ");
				name = user.nextLine();
				System.out.print("Enter the Address:  ");
				address = user.nextLine();
				System.out.print("Enter your City: ");
				city = user.nextLine();
				System.out.print("Enter your Postal Code:  ");
				post = user.nextLine();
				System.out.print("Enter your State:  ");
				state = user.nextLine();

				// testing for complete info

				String data[] = { userID, email, p1, name, address, city, post, state };
				for (int i = 0; i < data.length; i++) {
					if (data[i].isEmpty()) {
						restart = 1;
					}
				}
				if (restart == 1) {
					System.out.println("Restarting, please enter all required information.");
				} else {
					loop = 0;
				}
			} while (loop == 1);

			Statement stat1 = myCon.createStatement();
			ResultSet id = stat1.executeQuery(("Select userID from users " + "where userID = " + '"' + userID + '"'));
			id.next();
			id.getString(1);
			System.out.println("This account already exists.\n");

		} catch (Exception e) {
			Statement stat1 = myCon.createStatement();
			stat1.execute("start transaction");
			PreparedStatement prep1 = myCon.prepareStatement("insert into users "
					+ "values ((select max(u.userNum) + 1 from users u),?, ?, ?, ?, ?, ?, ?, ?, 1)");
			String data[] = { userID, email, p1, name, address, city, post, state };
			for (int i = 0; i < data.length; i++) {
				prep1.setString((i + 1), data[i]);
			}
			prep1.executeUpdate();
			stat1.execute("commit");
			System.out.println();
			System.out.println("Your account has been created. Welcome to Scamazon!\n");
		}
	}
}