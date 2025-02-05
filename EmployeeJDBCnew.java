package employee_application;

import java.sql.*;
import java.util.*;
import java.util.regex.*;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;

class DBConfig {
    public static final String URL = "jdbc:postgresql://localhost:5432/empdb";
    public static final String USER = "postgres";
    public static final String PASSWORD = "tiger";
}

class Employee {
    private int eid;
    private String name;
    private int age;
    private int salary;
    private String designation;
    private String department;

    public Employee(int eid, String name, int age, int salary, String designation, String department) {
        this.eid = eid;
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.designation = designation;
        this.department = department;
    }

    public int getEid() { 
    	return eid; 
    }
    public String getName() { 
    	return name; 
    }
    public int getAge() {
    	return age; 
    }
    public int getSalary() {
    	return salary; 
    }
    public String getDesignation() { 
    	return designation;
    }
    public String getDepartment() {
    	return department; 
    }

    public void display() {
        System.out.println("\nEmployee ID: " + eid);
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Salary: " + salary);
        System.out.println("Designation: " + designation);
        System.out.println("Department: " + department);
    }
}

class DatabaseManager {
	private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD);
    }

    public boolean addEmployee(Employee emp) {
        String sql = "INSERT INTO EMPLOYEE VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, emp.getEid());
            pstmt.setString(2, emp.getName());
            pstmt.setInt(3, emp.getAge());
            pstmt.setInt(4, emp.getSalary());
            pstmt.setString(5, emp.getDesignation());
            pstmt.setString(6, emp.getDepartment());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding employee: " + e.getMessage());
            return false;
        }
    }

    public List<Employee> getAllEmployees(String sortBy) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM EMPLOYEE ORDER BY " + sortBy;
        try (JdbcRowSet rowSet = RowSetProvider.newFactory().createJdbcRowSet()) {
            rowSet.setUrl(DBConfig.URL);
            rowSet.setUsername(DBConfig.USER);
            rowSet.setPassword(DBConfig.PASSWORD);
            rowSet.setCommand(sql);
            rowSet.execute();
            while (rowSet.next()) {
                employees.add(new Employee(
                    rowSet.getInt("EID"),
                    rowSet.getString("NAME"),
                    rowSet.getInt("AGE"),
                    rowSet.getInt("SALARY"),
                    rowSet.getString("DESIGNATION"),
                    rowSet.getString("DEPARTMENT")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching employees: " + e.getMessage());
        }
        return employees;
    }

    public boolean applyAppraisal(int eid, int amount) {
        String sql = "UPDATE EMPLOYEE SET SALARY = SALARY + ? WHERE EID = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, amount);
            pstmt.setInt(2, eid);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error applying appraisal: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteEmployee(int eid) {
        String sql = "DELETE FROM EMPLOYEE WHERE EID = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eid);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting employee: " + e.getMessage());
            return false;
        }
    }

    public List<Employee> searchEmployees(String field, String value) {
        List<Employee> results = new ArrayList<>();
        String sql = "SELECT * FROM EMPLOYEE WHERE " + field + " = ?";
        try (JdbcRowSet rowSet = RowSetProvider.newFactory().createJdbcRowSet()) {
            rowSet.setUrl(DBConfig.URL);
            rowSet.setUsername(DBConfig.USER);
            rowSet.setPassword(DBConfig.PASSWORD);
            rowSet.setCommand(sql);
            rowSet.setString(1, value);
            rowSet.execute();
            while (rowSet.next()) {
                results.add(new Employee(
                    rowSet.getInt("EID"),
                    rowSet.getString("NAME"),
                    rowSet.getInt("AGE"),
                    rowSet.getInt("SALARY"),
                    rowSet.getString("DESIGNATION"),
                    rowSet.getString("DEPARTMENT")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error searching employees: " + e.getMessage());
        }
        return results;
    }

    public boolean isEidExists(int eid) {
        String sql = "SELECT EID FROM EMPLOYEE WHERE EID = ?";
        try (JdbcRowSet rowSet = RowSetProvider.newFactory().createJdbcRowSet()) {
            rowSet.setUrl(DBConfig.URL);
            rowSet.setUsername(DBConfig.USER);
            rowSet.setPassword(DBConfig.PASSWORD);
            rowSet.setCommand(sql);
            rowSet.setInt(1, eid);
            rowSet.execute();
            return rowSet.next();
        } catch (SQLException e) {
            System.out.println("Error checking EID: " + e.getMessage());
            return false;
        }
    }
}

class InputHelper {
    private static Scanner scanner = new Scanner(System.in);

    public static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    public static String getStringInput(String prompt, String pattern) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.matches(pattern)) return input;
            System.out.println("Invalid format! Please try again.");
        }
    }

    public static int getEid() {
        return getIntInput("Enter Employee ID: ");
    }

    public static String getName() {
        return getStringInput("Enter Name: ", "^[A-Z][a-z]+ [A-Z][a-z]*$");
    }

    public static int getAge() {
        return getIntInput("Enter Age (21-60): ");
    }

    public static int getSalary() {
        return getIntInput("Enter Salary: ");
    }
}

public class EmployeeJDBC {
    private static DatabaseManager dbManager = new DatabaseManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeDatabase();
        mainMenu();
    }

    private static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS EMPLOYEE (" +
                "EID INT PRIMARY KEY, " +
                "NAME VARCHAR(255) NOT NULL, " +
                "AGE INT NOT NULL, " +
                "SALARY INT NOT NULL, " +
                "DESIGNATION VARCHAR(255) NOT NULL, " +
                "DEPARTMENT VARCHAR(255) NOT NULL)");
        } catch (SQLException e) {
            System.out.println("Database initialization failed: " + e.getMessage());
        }
    }

    private static void mainMenu() {
        while (true) {
            System.out.println("\n--- Employee Management System ---");
            System.out.println("1. Create Employee");
            System.out.println("2. Display Employees");
            System.out.println("3. Apply Appraisal");
            System.out.println("4. Search Employees");
            System.out.println("5. Remove Employee");
            System.out.println("6. Exit");
            int choice = InputHelper.getIntInput("Enter choice: ");

            switch (choice) {
                case 1: createEmployee(); break;
                case 2: displayEmployees(); break;
                case 3: applyAppraisal(); break;
                case 4: searchEmployees(); break;
                case 5: removeEmployee(); break;
                case 6: System.exit(0);
                default: System.out.println("Invalid choice!");
            }
        }
    }

    private static void createEmployee() {
        System.out.println("\n--- Create New Employee ---");
        System.out.println("1. Clerk");
        System.out.println("2. Programmer");
        System.out.println("3. Manager");
        System.out.println("4. Others");
        System.out.println("5. Back to Main Menu");
        int choice = InputHelper.getIntInput("Enter choice: ");

        if (choice == 5) return;

        int eid = InputHelper.getEid();
        if (dbManager.isEidExists(eid)) {
            System.out.println("Employee ID already exists!");
            return;
        }

        String name = InputHelper.getName();
        int age = InputHelper.getAge();
        int salary = InputHelper.getSalary();
        String department = InputHelper.getStringInput("Enter Department: ", "^[A-Za-z ]+$");
        String designation = "";

        switch (choice) {
            case 1: designation = "Clerk"; break;
            case 2: designation = "Programmer"; break;
            case 3: designation = "Manager"; break;
            case 4: designation = InputHelper.getStringInput("Enter Designation: ", "^[A-Za-z ]+$"); break;
            default: System.out.println("Invalid choice!"); return;
        }

        Employee emp = new Employee(eid, name, age, salary, designation, department);
        if (dbManager.addEmployee(emp)) {
            System.out.println("Employee created successfully!");
        }
    }

    private static void displayEmployees() {
        System.out.println("\n--- Display Employees ---");
        System.out.println("1. By ID");
        System.out.println("2. By Name");
        System.out.println("3. By Designation");
        System.out.println("4. By Age");
        System.out.println("5. By Salary");
        System.out.println("6. Back to Main Menu");
        int choice = InputHelper.getIntInput("Enter choice: ");

        String sortBy = switch (choice) {
            case 1 -> "EID";
            case 2 -> "NAME";
            case 3 -> "DESIGNATION";
            case 4 -> "AGE";
            case 5 -> "SALARY";
            case 6 -> { yield ""; }
            default -> { System.out.println("Invalid choice!"); yield "EID"; }
        };

        if (choice != 6) {
            List<Employee> employees = dbManager.getAllEmployees(sortBy);
            employees.forEach(Employee::display);
        }
    }

    private static void applyAppraisal() {
        System.out.println("\n--- Apply Appraisal ---");
        int eid = InputHelper.getEid();
        int amount = InputHelper.getIntInput("Enter appraisal amount: ");
        boolean success = dbManager.applyAppraisal(eid, amount);
        if (success) {
            System.out.println("Appraisal applied successfully!");
        } else {
            System.out.println("Employee not found!");
        }
    }

    private static void searchEmployees() {
        System.out.println("\n--- Search Employees ---");
        System.out.println("1. By ID");
        System.out.println("2. By Name");
        System.out.println("3. By Designation");
        System.out.println("4. Back to Main Menu");
        int choice = InputHelper.getIntInput("Enter choice: ");

        if (choice == 4) return;

        String field = switch (choice) {
            case 1 -> "EID";
            case 2 -> "NAME";
            case 3 -> "DESIGNATION";
            default -> { System.out.println("Invalid choice!"); yield ""; }
        };

        if (!field.isEmpty()) {
            String value = InputHelper.getStringInput("Enter search value: ", ".*");
            List<Employee> results = dbManager.searchEmployees(field, value);
            results.forEach(Employee::display);
        }
    }

    private static void removeEmployee() {
        int eid = InputHelper.getEid();
        if (dbManager.deleteEmployee(eid)) {
            System.out.println("Employee removed successfully!");
        } else {
            System.out.println("Employee not found!");
        }
    }
}
