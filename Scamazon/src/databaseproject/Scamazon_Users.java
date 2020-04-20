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
				System.out.println("4: Add a Review");
				System.out.println("5: Change Password");
				System.out.println("6: Exit\n");
				String input = user.nextLine();

				if (input.equals("1")) {
					viewProducts(myCon, user, userID);
				} else if (input.equals("2")) {
					viewCart();
				} else if (input.equals("3")) {
					checkout(myCon, user, userID);
				} else if (input.equals("4")) {
					addReview(myCon, user, userID);
				} else if (input.equals("5")) {
					resetPassword(myCon, user, userID);
				} else if (input.equals("6")) {
					loop = 0;
				} else {
					System.out.println("Please enter a valid input.\n");
				}
			}
			System.out.println("Now exiting. Thank you for choosing Scamazon\n");
			cart.clear();

		} catch (Exception e) {

		}
	}

	public static void viewProducts(Connection myCon, Scanner user, String userID) throws SQLException {

		try {
			boolean loop = true;
			while (loop == true) {

				int count = 1;
				Statement stat1 = myCon.createStatement();
				ResultSet products = stat1.executeQuery("select description from categories");
				System.out.println("Select a Category of Product: (1-4), Enter 0 to return to the main menu.");
				while (products.next()) {
					System.out.println(count + ": " + products.getString(1));
					count++;
				}
				System.out.println();
				String input = user.nextLine();
				ResultSet items = stat1.executeQuery(
						"select itemID, itemName, itemPrice, itemStock, description, avgRating from items where itemcategory = "
								+ "'" + input + "'");
				System.out.println();

				if (input.equals("0")) {
					loop = false;
				}

				else if (items.next() == true) {

					System.out.println("Product ID: " + items.getString(1) + ", " + items.getString(2) + ", Price:"
							+ items.getString(3) + ", Supply:" + items.getString(4) + ", Rating: "
							+ items.getString(6));
					System.out.println(items.getString(5));
					System.out.println("---------------------------------------\n");

					while (items.next()) {
						System.out.print("Product ID: " + items.getString(1) + ", " + items.getString(2) + ", Price: "
								+ items.getString(3) + ", ");
						if (Integer.parseInt(items.getString(4)) >= 5) {
							System.out.print("Supply: in stock, Rating: " + items.getString(6) + "\n");
						} else if (Integer.parseInt(items.getString(4)) == 0) {
							System.out.print("This item is out of stock\n");
						} else if (Integer.parseInt(items.getString(4)) < 5) {
							System.out.print("Supply: " + items.getString(4) + ", Rating: " + items.getString(6) + "\n");
						}
						System.out.println(items.getString(5));
						System.out.println("---------------------------------------\n");
					}
					System.out.println(
							"Enter 1 to add items to cart. Enter 2 to view reviews. Enter 0 to exit. Any other key to return to product select.");
					input = user.nextLine();
					if (input.equals("1")) {
						addItem(user, myCon);
					} else if (input.equals("2")) {
						viewReviews(myCon, user);
					} else if (input.equals("0")) {
						loop = false;
					}

				} else {
					System.out.println("This category does not contain any items at this time.\n");
				}
			}
		} catch (Exception e) {

		}
	}

	public static void addItem(Scanner user, Connection myCon) throws SQLException {
		try {
			boolean loop = true;
			do {
				System.out.print("Enter the Product ID to be added: ");
				String input = user.nextLine();
				System.out.print("Enter the quantity to be added: ");
				String in2 = user.nextLine();
				Statement stat1 = myCon.createStatement();
				ResultSet r1 = stat1.executeQuery(
						"select itemid, itemStock, itemName, itemPrice from items where itemid = " + "'" + input + "'");
				if (r1.next() && Integer.parseInt(r1.getString(2)) != 0
						&& Integer.parseInt(r1.getString(2)) >= Integer.parseInt(in2)) {
					String items[] = { input, r1.getString(3), r1.getString(4), in2 };
					cart.add(items);
					if (cart.size() == 1) {
						System.out
								.println("This item has been added! There is " + cart.size() + " item in your cart.\n");
					} else {
						System.out.println(
								"This item has been added! There are " + cart.size() + " items in your cart.\n");
					}
				} else {
					System.out.println("This product could not be added.");
				}
				System.out.println("Would you like to add another item to the cart? Enter yes or no.");
				input = user.nextLine();
				System.out.println();
				if (input.trim().equalsIgnoreCase("no")) {
					loop = false;
					System.out.println();
				}
			} while (loop == true);
		} catch (Exception e) {
			System.out.println("Please enter a valid quantity.\n");
		}
	}

	public static void viewCart() {

		if (cart.size() == 0) {
			System.out.println("There are no items in your cart.\n");
		} else {
			System.out.println("Items in your cart.\n");
			for (int i = 0; i < cart.size(); i++) {
				System.out.println((i + 1) + ": " + cart.get(i)[1] + ", Price: " + cart.get(i)[2] + ", Quantity: "
						+ cart.get(i)[3]);
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

					stat1.execute("start transaction");
					ResultSet userData = stat1
							.executeQuery("Select userEmail from users where userid =" + "'" + userID + "'");
					userData.next();
					stat1.executeUpdate(
							"insert into orders(userID, userEmail, orderedOn, shippedBy, shippedOn)" + " values(" + "'" + userID
									+ "','" + userData.getString(1) + "', '" + java.time.LocalDate.now() + "', 'UPS', '" +
									java.time.LocalDate.now().plusDays(1) +"')");
					// update orderdetails

					for (int i = 0; i < cart.size(); i++) {
						stat1.executeUpdate("insert into orderDetails(orderID, itemID, quantity) "
								+ "values((select max(orderID) from orders)" + ", '" + cart.get(i)[0] + "', '"
								+ cart.get(i)[3] + "')");
						stat1.executeUpdate("update items set itemStock = (itemStock - '" + cart.get(i)[3]
								+ "') where itemID = '" + cart.get(i)[0] + "'");
					}
					stat1.execute("commit");

					System.out.println("Your order has been placed!\n");
					cart.clear();
				} else {
					System.out.println("Please enter yes or no.");
				}
			} else {
				System.out.println("The cart is empty.\n");
			}
		} catch (Exception e) {

		}
	}

	public static void addReview(Connection myCon, Scanner user, String userID) throws SQLException {
		try {
			System.out.println("Enter the productID to review.\n");
			Statement stat1 = myCon.createStatement();
			ResultSet rs = stat1
					.executeQuery("select distinct d.itemID, i.itemName from orderDetails d, orders o, items i "
							+ "where d.orderID = o.orderID and d.itemID = i.itemID and o.userID = '" + userID
							+ "' group by d.itemID, i.itemName");
			if (rs.next()) {
				System.out.println(rs.getString(1) + ": " + rs.getString(2));

				while (rs.next()) {
					System.out.println(rs.getString(1) + ": " + rs.getString(2));
				}
				System.out.println();
				String product = user.nextLine();
				System.out.print("Enter a rating of 1-10: ");
				String rating = user.nextLine();
				System.out.println("Enter a review for the product.\n");
				String review = user.nextLine();

				if (Integer.parseInt(rating) >= 1) {
					rs = stat1.executeQuery(
							"select * from reviews where userID = '" + userID + "'" + "and itemID = '" + product + "'");
					if (rs.next()) {
						System.out.println(
								"You have already placed a review for this item. Would you like to update the review? (yes or no)");
						String input = user.nextLine();
						System.out.println();
						if (input.equalsIgnoreCase("yes")) {
							stat1.execute("start transaction");
							stat1.executeUpdate("update reviews set rating ='" + rating + "', review = '" + review
									+ "' where itemID ='" + product + "' and userID = '" + userID + "'");
							stat1.executeUpdate(
									"update items set avgRating = (select avg(rating) from reviews where itemID = '"
											+ product + "') " + "where itemID = '" + product + "'");
							stat1.execute("commit");
							System.out.println("Review has been updated!\n");
						}
					} else {
						stat1.execute("start transaction");
						stat1.executeUpdate("insert into reviews(itemID, rating, review, userID) " + "values('"
								+ product + "','" + rating + "','" + review + "','" + userID + "')");
						stat1.executeUpdate(
								"update items set avgRating = (select avg(rating) from reviews where itemID = '"
										+ product + "') " + "where itemID = '" + product + "'");
						stat1.execute("commit");
						System.out.println("Review has been placed!\n");
					}
				}
			} else {
				System.out.println("You have not placed any orders.\n");
			}
		} catch (Exception e) {
			System.out.println("Please enter a valid review score.\n");
		}
	}

	public static void resetPassword(Connection myCon, Scanner user, String userID) throws SQLException {
		Statement stat1 = myCon.createStatement();
		System.out.print("Enter the new password: ");
		String p1 = user.nextLine();
		System.out.println();
		System.out.print("Enter the password again to confirm change: ");
		String p2 = user.nextLine();

		if (p1.equals(p2)) {
			stat1.execute("start transaction");
			PreparedStatement prep = myCon
					.prepareStatement("update users set userPass=? where userID = '" + userID + "'");
			prep.setString(1, p1);
			prep.executeUpdate();
			stat1.execute("commit");
			System.out.println();
		} else {
			System.out.println("The passwords were not the same\n");
		}
	}

	public static void viewReviews(Connection myCon, Scanner user) throws SQLException {
		boolean loop = true;
		do {
			System.out.print("Select the item to view reviews: ");
			String product = user.nextLine();
			PreparedStatement prep = myCon.prepareStatement(
					"select r.review, i.itemName, r.rating, r.userID from reviews r, items i where r.itemID = ? and i.itemID = ?");
			prep.setString(1, product);
			prep.setString(2, product);
			ResultSet rs = prep.executeQuery();
			System.out.println();
			if (rs.next()) {
				System.out.println(rs.getString(2) + ":\n");
				System.out.println(rs.getString(1) + " --- " + rs.getString(3) + "/10 --- " + rs.getString(4) + "\n");
				System.out.println("---------------------------------------\n");
				while (rs.next()) {
					System.out
							.println(rs.getString(1) + " --- " + rs.getString(3) + "/10 --- " + rs.getString(4) + "\n");
					System.out.println("---------------------------------------\n");
				}
			} else {
				System.out.println("This product has no reviews at this time\n");
			}
			System.out.println(
					"Enter 0 to return to the product selection menu. Any other key to see another product's reviews");
			String input = user.nextLine();
			if (input.equals("0")) {
				loop = false;
			}
		} while (loop == true);
	}
}
