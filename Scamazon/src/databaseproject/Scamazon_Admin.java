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
        while (switchSel != 1) {
            System.out.println("---------------------------------------");
            System.out.println("Select what you would like to edit:");
            System.out.println("1: Categories\n2: Employees\n3: exit\n");
            String selection = user.nextLine();
            while (selection.compareTo("") == 0 || checkInt(selection) < 0 || checkInt(selection) > 3) {
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
                    System.out.println("---------------------------------------");
                    modEmployees(myCon, user);
                    break;
                case 3:
                    System.out.println("Exiting");
                    System.out.println("---------------------------------------");
                    return;
            }
        }
    }

    public static void modCategories(Connection myCon, Scanner user) throws SQLException {
        int switchSel = 0;
        while (switchSel != 4) {
            System.out.println("---------------------------------------");
            System.out.println("Select a modification option:");
            System.out.println("1: Add a category\n2: Delete a category\n3: Edit category manager\n4: Exit\n");
            String selection = user.nextLine();
            while (selection.compareTo("") == 0 || checkInt(selection) < 0 || checkInt(selection) > 4) {
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
                    int count = 1;
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
                    int delCount = 1;
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
                    System.out.println("Editing a category manager\n---------------------------------------");
                    System.out.println("Select a category to change:");
                    Statement statEd = myCon.createStatement();
                    ResultSet rsEd = statEd.executeQuery("SELECT category, categoryName FROM categories;");
                    int edCount = 1;
                    while (rsEd.next()) {
                        System.out.println(rsEd.getInt(1) + ": " + rsEd.getString(2));
                        edCount++;
                    }
                    System.out.println();
                    String edChoice = user.nextLine();
                    while (edChoice.compareTo("") == 0 || checkInt(edChoice) < 0 || checkInt(edChoice) > edCount) {
                        System.out.println("Error: You must input an integer provided above!\n");
                        edChoice = user.nextLine();
                    }
                    System.out.println("Select a manager you would like to manage that category:");
                    ResultSet rsMan = statEd.executeQuery("SELECT empID, empName FROM emp WHERE empID <> 1;");
                    int manCount = 1;
                    while (rsMan.next()) {
                        System.out.println(rsMan.getInt(1) + ": " + rsMan.getString(2));
                        manCount++;
                    }
                    System.out.println();
                    String manChoice = user.nextLine();
                    while (manChoice.compareTo("") == 0 || checkInt(manChoice) < 2 || checkInt(manChoice) > manCount) {
                        System.out.println("Error: You must input an integer provided above!");
                        manChoice = user.nextLine();
                    }
                    statEd.execute("start transaction");
                    if (statEd.executeUpdate("UPDATE categories SET empID = " + checkInt(manChoice)
                            + " WHERE category = " + checkInt(edChoice) + ";") != 0)
                        System.out.println("Updated successfully!");
                    else
                        System.out.println("Error: could not update.");
                    statEd.execute("commit");
                    break;
                case 4:
                    System.out.println("Exiting...");
                    break;
            }
        }
    }

    public static void modEmployees(Connection myCon, Scanner user) throws SQLException {
        Statement stat = myCon.createStatement();
        int switchSel = 0;
        while (switchSel != 4) {
            System.out.println("---------------------------------------");
            System.out.println("Select a modification option:");
            System.out.println("1: Add an employee\n2: Deactivate an employee\n3: Update employee info\n4: exit\n");
            String selection = user.nextLine();
            while (selection.compareTo("") == 0 || checkInt(selection) < 0 || checkInt(selection) > 4) {
                System.out.println("Error: You must input an integer provided above!\n");
                selection = user.nextLine();
            }
            switchSel = checkInt(selection);

            switch (switchSel) {
                case 1:
                    System.out.println("Adding an employee\n---------------------------------------");
                    System.out.println("Enter the employee's full name:");
                    String newName = user.nextLine();
                    while (newName.compareTo("") == 0) {
                        System.out.println("Error: you must input something!");
                        newName = user.nextLine();
                    }
                    System.out.println("\nEnter a new password:");
                    String newPass = user.nextLine();
                    while (newPass.compareTo("") == 0) {
                        System.out.println("Error: you must input soemthing!");
                        newPass = user.nextLine();
                    }
                    System.out.println("\nEnter the new employee's address:");
                    String newAddress = user.nextLine();
                    if (newAddress.compareTo("") == 0)
                        newAddress = "NULL";
                    System.out.println("Enter the employee's salary:");
                    String newSalS = user.nextLine();
                    while (newSalS.compareTo("") == 0 || checkInt(newSalS) < 0) {
                        System.out.println("Error: you must input an integer value for salary!");
                        newSalS = user.nextLine();
                    }
                    int newSal = checkInt(newSalS);
                    System.out.println("NAME: " + newName + " | PASS: " + newPass + " | ADDRESS: " + newAddress
                            + " | SALARY: " + newSal);
                    System.out.println("Enter 'confirm' to add this information to emp");
                    if (user.nextLine().compareToIgnoreCase("confirm") == 0) {
                        System.out.println("Making addition...");
                        stat.execute("start transaction");
                        if (stat.executeUpdate(
                                "INSERT INTO emp (empPass, empName, empAddress, empActive, empSalary) VALUES ('"
                                        + newPass + "', '" + newName + "', '" + newAddress + "', 'y', " + newSal
                                        + ");") != 0)
                            System.out.println(newName + " added successfully!");
                        else
                            System.out.println("Error: could not add " + newName + " to emp.");
                        stat.execute("commit");
                    } else
                        System.out.println("Aborting changes...");
                    break;
                case 2:
                    System.out.println("Deactivating an employee\n---------------------------------------");
                    System.out.println("Select the employee you would like to deactivate:");
                    ResultSet rsDeact = stat.executeQuery("SELECT empID, empName FROM emp WHERE empID <> 1;");
                    int deactCount = 0;
                    while (rsDeact.next()) {
                        System.out.println(rsDeact.getInt(1) + ": " + rsDeact.getString(2));
                        deactCount++;
                    }
                    deactCount++;
                    String deact = user.nextLine();
                    while (deact.compareTo("") == 0 || checkInt(deact) < 2 || checkInt(deact) > deactCount) {
                        System.out.println("Error: You must input an integer provided above!\n");
                        deact = user.nextLine();
                    }
                    rsDeact = stat.executeQuery("SELECT * FROM categories WHERE empID = " + checkInt(deact) + ";");
                    if (rsDeact.next()) {
                        System.out.println("Error: That employee still manages a category!");
                        break;
                    }
                    stat.execute("start transaction");
                    if (stat.executeUpdate(
                            "UPDATE emp SET empActive = 'n' WHERE empID = " + checkInt(deact) + ";") != 0)
                        System.out.println("Deactivation successfull!");
                    else
                        System.out.println("Error: Could not deactivate successfully.");
                    stat.execute("commit");
                    break;
                case 3:
                    System.out.println("Modifying an employee\n---------------------------------------");
                    System.out.println("Select the employee you would like to modify:");
                    ResultSet rsMod = stat.executeQuery("SELECT empID, empName FROM emp WHERE empID <> 1;");
                    int modCount = 0;
                    while (rsMod.next()) {
                        System.out.println(rsMod.getInt(1) + ": " + rsMod.getString(2));
                        modCount++;
                    }
                    modCount++;
                    String empSel = user.nextLine();
                    while (empSel.compareTo("") == 0 || checkInt(empSel) < 2 || checkInt(empSel) > modCount) {
                        System.out.println("Error: You must input an integer provided above!\n");
                        empSel = user.nextLine();
                    }
                    ResultSet found = stat
                            .executeQuery("SELECT empName, empPass, empAddress, empSalary FROM emp WHERE empID = "
                                    + checkInt(empSel) + ";");
                    String fEmpName = null;
                    String fEmpPass = null;
                    String fEmpAddress = null;
                    int fEmpSalary = 0;
                    if (found.next()) {
                        fEmpName = found.getString(1);
                        fEmpPass = found.getString(2);
                        fEmpAddress = found.getString(3);
                        fEmpSalary = found.getInt(4);
                    }
                    System.out.println("\nSelect which field you would like to modify:");
                    System.out.println("1: Name\n2: Password\n3: Address\n4: Salary");
                    String fieldSel = user.nextLine();
                    while (fieldSel.compareTo("") == 0 || checkInt(fieldSel) < 0 || checkInt(fieldSel) > 4) {
                        System.out.println("Error: You must input an integer provided above!");
                        fieldSel = user.nextLine();
                    }

                    switch (checkInt(fieldSel)) {
                        case 1:
                            modString(myCon, user, checkInt(empSel), "name", "empName", fEmpName);
                            break;
                        case 2:
                            modString(myCon, user, checkInt(empSel), "password", "empPass", fEmpPass);
                            break;
                        case 3:
                            modString(myCon, user, checkInt(empSel), "address", "empAddress", fEmpAddress);
                            break;
                        case 4:
                            modString(myCon, user, checkInt(empSel), "salary", "empSalary",
                                    Integer.toString(fEmpSalary));
                            break;
                    }
                    break;
                case 4:
                    System.out.println("Exiting...");
                    break;
            }
        }
    }

    public static void modString(Connection myCon, Scanner user, int empID, String field, String updateField,
            String oldVal) throws SQLException {
        Statement stat = myCon.createStatement();
        System.out.println("Enter the new " + field + ":");
        String updateContent = user.nextLine();
        while (updateContent.compareTo("") == 0) {
            System.out.println("Error: You must input something!");
            updateContent = user.nextLine();
        }
        System.out.println("\nORIGNAL " + field.toUpperCase() + ": " + oldVal + " | NEW " + field.toUpperCase() + ": "
                + updateContent);
        System.out.println("Enter 'confirm' to confirm the change:");
        if (user.nextLine().compareToIgnoreCase("confirm") == 0) {
            System.out.println("Making changes...\n");
            updateContent = "'" + updateContent + "'";
        } else {
            System.out.println("Aborting changes...\n");
            System.out.println("---------------------------------------");
            return;
        }
        if (stat.executeUpdate(
                "UPDATE emp SET " + updateField + " = " + updateContent + " WHERE empID = " + empID + ";") != 0)
            System.out.println("Updated successfully!");
        else
            System.out.println("Error: could not update.");
        System.out.println("---------------------------------------");
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