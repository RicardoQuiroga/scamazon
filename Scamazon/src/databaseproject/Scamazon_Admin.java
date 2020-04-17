package databaseproject;

import java.sql.*;
import java.util.Scanner;

public class Scamazon_Admin {
    Connection myCon;
    Scanner user;

    public Scamazon_Admin(Connection myCon, Scanner user) {
        this.myCon = myCon;
        this.user = user;
    }

    public static void adminOptions(Connection myCon, Scanner user) throws SQLException {
        int switchSel = 0;
        while (switchSel != 4) {
            System.out.println("Select what you would like to edit:");
            System.out.println("1: Categories\n2: Employees \n3: Users\n4: exit\n");
            String selection = user.nextLine();
            while (selection.compareTo("") == 0 || checkInt(selection) < 0 || checkInt(selection) > 4) {
                System.out.println("Error: You must input an integer provided above!\n");
                selection = user.nextLine();
            }
            switchSel = checkInt(selection);

            switch (switchSel) {
                case 1:
                    System.out.println("Modifying categories");
                    System.out.println("---------------------------------------");
                    modCategories(myCon, user);
                    break;
                case 2:
                    System.out.println("Modifying employees");
                    break;
                case 3:
                    System.out.println("Modifying users");
                    break;
                case 4:
                    System.out.println("Exiting");
                    System.out.println("---------------------------------------");
                    return;
            }
        }
    }

    public static void modCategories(Connection myCon, Scanner user) throws SQLException {
        int switchSel = 0;
        while (switchSel != 3) {
            System.out.println("---------------------------------------");
            System.out.println("Select a modification option:");
            System.out.println("1: Add a category\n2: Delete a category\n3: exit\n");
            String selection = user.nextLine();
            while (selection.compareTo("") == 0 || checkInt(selection) < 0 || checkInt(selection) > 3) {
                System.out.println("Error: You must input an integer provided above!\n");
                selection = user.nextLine();
            }
            switchSel = checkInt(selection);

            switch (switchSel) {
                case 1:
                    System.out.println("Adding a category\n---------------------------------------");
                    System.out.println("Enter the name of the category you would like to add:");
                    String newCat = user.nextLine();
                    while (newCat.compareTo("") == 0) {
                        System.out.println("Error: You must input something!");
                        newCat = user.nextLine();
                    }
                    System.out.println("\nEnter a short description for " + newCat + ":");
                    String newDesc = user.nextLine();
                    while (newDesc.compareTo("") == 0) {
                        System.out.println("Error: You must input something!");
                        newDesc = user.nextLine();
                    }
                    System.out.println("\nSelect an employee to have manage " + newCat + ":");
                    Statement stat = myCon.createStatement();
                    ResultSet rs = stat.executeQuery("SELECT empID, empName FROM emp WHERE empID <> 1;");
                    int count = 0;
                    while (rs.next()) {
                        System.out.println(rs.getInt(1) + ": " + rs.getString(2));
                        count++;
                    }
                    System.out.println();
                    String emp = user.nextLine();
                    while (emp.compareTo("") == 0 || checkInt(emp) < 0 || checkInt(emp) > count) {
                        System.out.println("Error: You must input an integer provided above!\n");
                        emp = user.nextLine();
                    }
                    int empID = checkInt(emp);
                    System.out
                            .println("NEW CATEGORY: " + newCat + " | DESCRIPTION: " + newDesc + " | MANAGER: " + empID);
                    System.out.println("Enter 'confirm' to add this information to categories");
                    if (user.nextLine().compareToIgnoreCase("confirm") == 0) {
                        System.out.println("Making addition...");
                        stat.execute("start transaction");
                        if (stat.executeUpdate("INSERT INTO categories (categoryName, empID, description) VALUES ('"
                                + newCat + "', " + empID + ", '" + newDesc + "');") != 0) {
                            System.out.println(newCat + " added successfully!");
                        } else {
                            System.out.println("Error: could not add " + newCat + " to categories.");
                        }
                        stat.execute("commit");
                    } else
                        System.out.println("Aborting changes...");
                    break;
                case 2:
                    System.out.println("Deleting a category\n---------------------------------------");
                    System.out.println("Select a category you would like to delete:");
                    Statement statDel = myCon.createStatement();
                    ResultSet rsDel = statDel.executeQuery("SELECT category, categoryName FROM categories;");
                    int delCount = 0;
                    while (rsDel.next()) {
                        System.out.println(rsDel.getInt(1) + ": " + rsDel.getString(2));
                        delCount++;
                    }
                    System.out.println();
                    String delChoice = user.nextLine();
                    while (delChoice.compareTo("") == 0 || checkInt(delChoice) < 0 || checkInt(delChoice) > delCount) {
                        System.out.println("Error: You must input an integer provided above!\n");
                        delChoice = user.nextLine();
                    }
                    int delChoiceI = checkInt(delChoice);
                    rsDel = statDel.executeQuery("SELECT * FROM items WHERE itemCategory = " + delChoiceI + ";");
                    if (rsDel.next()) {
                        System.out.println("Error: That category still has items included in it!");
                        break;
                    }
                    statDel.execute("start transaction");
                    if (statDel.executeUpdate("DELETE FROM categories WHERE category = " + delChoiceI + ";") != 0) {
                        System.out.println("Deletion successful!");
                    } else {
                        System.out.println("Error: Could not successfully delete.");
                    }
                    statDel.execute("commit");
                    break;
                case 3:
                    System.out.println("Exiting...");
            }
        }
    }

    public static int checkArray(int[] arr, int v) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == v) {
                return 0;
            }
        }
        return -1;
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