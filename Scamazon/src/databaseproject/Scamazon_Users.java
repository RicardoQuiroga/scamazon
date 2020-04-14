package databaseproject;

import java.sql.*;
import java.util.*;

public class Scamazon_Users {
	Connection myCon;
	Scanner user;
	String userID;
	private static ArrayList<String[]> cart = new ArrayList<String[]>();

	public Scamazon_Users(Connection myCon, Scanner user, String userID) {
		this.myCon = myCon;
		this.user = user;
		this.userID = userID;
	}

	public static void customerOptions(Connection myCon, Scanner user, String userID) throws SQLException {

		try {
			int loop = 1;
			while (loop == 1) {
				System.out.println("Welcome to Scamazon!");
				System.out.println("Select an Option:");
				System.out.println("1: View Products");
				System.out.println("2: View Cart");
				System.out.println("3: Checkout");
				System.out.println("4: Edit User Information");
				System.out.println("5: Exit\n");
				String input = user.nextLine();

				if (input.equals("1")) {
					viewProducts(myCon, user, userID);
				} else if (input.equals("2")) {
					viewCart();
				} else if (input.equals("3")) {
					checkout(myCon, user, userID);
				} else if (input.equals("5")) {
					loop = 0;
				} else {
					System.out.println("Please enter a valid input.\n");
				}
			}
			System.out.println("Now exiting. Thank you for choosing Scamazon");

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void viewProducts(Connection myCon, Scanner user, String userID) throws SQLException {

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
				ResultSet items = stat1.executeQuery(
						"select itemID, itemName, itemPrice, itemStock, description from items where itemcategory = "
								+ "'" + input + "'");
				System.out.println();

				if (items.next() == true) {

					System.out.println("Product ID: " + items.getString(1) + ", " + items.getString(2) + ", Price:"
							+ items.getString(3) + ", Supply:" + items.getString(4));
					System.out.println(items.getString(5));
					System.out.println("---------------------------------------\n");

					while (items.next()) {
						System.out.print("Product ID: " + items.getString(1) + ", " + items.getString(2) + ", Price: "
								+ items.getString(3) + ", ");
						if (Integer.parseInt(items.getString(4)) >= 5) {
							System.out.print("Supply: in stock\n");
						} else if (Integer.parseInt(items.getString(4)) == 0) {
							System.out.print("This item is out of stock\n");
						} else if (Integer.parseInt(items.getString(4)) < 5) {
							System.out.print("Supply: " + items.getString(4) + "\n");
						}
						System.out.println(items.getString(5));
						System.out.println("---------------------------------------\n");
					}
				} else {
					System.out.println("This category does not contain any items at this time.\n");
				}
				System.out.println(
						"Enter 1 to add items to cart. Enter 0 to exit. Any other key to return to product select.");
				input = user.nextLine();
				if (input.equals("1")) {
					addItem(user, myCon);
				} else if (input.equals("0")) {
					loop = false;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void addItem(Scanner user, Connection myCon) throws SQLException {

		boolean loop = true;
		do {
			System.out.print("Enter the Product ID to be added: ");
			String input = user.nextLine();
			Statement stat1 = myCon.createStatement();
			ResultSet r1 = stat1.executeQuery(
					"select itemid, itemStock, itemName, itemPrice from items where itemid = " + "'" + input + "'");
			if (r1.next() && Integer.parseInt(r1.getString(2)) != 0) {
				String items[] = { input, r1.getString(3), r1.getString(4) };
				cart.add(items);
				if (cart.size() == 1) {
					System.out.println("This item has been added! There is " + cart.size() + " item in your cart.");
				} else {
					System.out.println("This item has been added! There are " + cart.size() + " items in your cart.");
				}
			} else {
				System.out.println("This product could not be added.");
			}
			System.out.println("Would you like to add another item to the cart? Enter yes or no.");
			input = user.nextLine();
			if (input.trim().equalsIgnoreCase("no")) {
				loop = false;
				System.out.println();
			}
		} while (loop == true);
	}

	public static void viewCart() {

		if (cart.size() == 0) {
			System.out.println("There are no items in your cart.\n");
		} else {
			System.out.println("Items in your cart.\n");
			for (int i = 0; i < cart.size(); i++) {
				System.out.println((i + 1) + ": " + cart.get(i)[1] + ", Price: " + cart.get(i)[2]);
			}
			System.out.println();
		}
	}

	public static void checkout(Connection myCon, Scanner user, String userID) throws SQLException {
		Statement stat1 = myCon.createStatement();
		try {
			if (cart.size() > 0) {
				System.out.println("Would you like this shipped to your address on file?");
				System.out.println("Enter yes or no.");
				String input = user.nextLine();
				System.out.println();
				if (input.trim().equalsIgnoreCase("no")) {
					System.out.print("Enter the new shipping address: ");
					String address = user.nextLine();
					System.out.print("Enter the new City: ");
					String city = user.nextLine();
					System.out.print("Enter the new zip code: ");
					String zip = user.nextLine();
					System.out.print("Enter the new state: ");
					String state = user.nextLine();

					stat1.execute("Start transaction");
					PreparedStatement update = myCon.prepareStatement("update users set userAddress = ?, userCity = ?, "
							+ "userZip = ?, userState = ? where userid=" + "'" + userID + "'");
					update.setString(1, address.trim());
					update.setString(2, city.trim());
					update.setString(3, zip.trim());
					update.setString(4, state.trim());
					update.executeUpdate();
					stat1.execute("commit");
				} else if (input.trim().equalsIgnoreCase("yes")) {
					// enter code here
					stat1.execute("start transaction");
					ResultSet userData = stat1
							.executeQuery("Select userEmail from users where userid =" + "'" + userID + "'");
					userData.next();
					stat1.executeUpdate(
							"insert into orders(userID, userEmail, orderedOn, shippedBy)" + " values(" + "'" + userID
									+ "','" + userData.getString(1) + "', '" + java.time.LocalDate.now() + "', 'UPS')");
					stat1.execute("commit");
				} else {
					System.out.println("Please enter yes or no.");
				}
			} else {
				System.out.println("The cart is empty.\n");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
