package employee_application;

import java.util.*;
import java.util.regex.*;

class InvalidNameException extends Exception {
    public InvalidNameException(String message) {
        super(message);
    }
}

class InvalidIdException extends Exception {
    public InvalidIdException(String message) {
        super(message);
    }
}

class InvalidChoiceException extends Exception {
    public InvalidChoiceException(String message) {
        super(message);
    }
}

class InvalidAgeException extends Exception {
    public InvalidAgeException(String message) {
        super(message);
    }
}

class EmployeeIterator implements Iterator<Employee> {
    private Employee[] employees;
    private int currentIndex = 0;

    public EmployeeIterator(Employee[] employees) {
        this.employees = employees;
    }

    public boolean hasNext() {
        while (currentIndex < employees.length && employees[currentIndex] == null) {
            currentIndex++;
        }
        return currentIndex < employees.length;
    }

    public Employee next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more employees.");
        }
        return employees[currentIndex++];
    }
}

class EmployeeFactory {
    public static Employee createEmployee(String type, String name, int age, int empId) {
        switch (type.toLowerCase()) {
            case "clerk":
                return Clerk.create(name, age, empId);
            case "programmer":
                return Programmer.create(name, age, empId);
            case "manager":
                return Manager.create(name, age, empId);
            case "ceo":
                return CEO.createInstance(name, age);
            default:
                throw new IllegalArgumentException("Invalid employee type: " + type);
        }
    }
}

public class EmployeeManagement {
    public static void main(String[] args) {
        Employee[] employees = new Employee[100];
        Scanner sc = new Scanner(System.in);

        boolean mainFlag = true;
        while (mainFlag) {
            Menu.displayMainMenu();
            int choice = Menu.readMenuChoice(sc, 1, 5);

            switch (choice) {
                case 1:
                    Employee.createEmployee(employees, sc);
                    break;
                case 2:
                    Employee.displayAll(employees);
                    break;
                case 3:
                    Employee.raiseSalary(employees);
                    break;
                case 4:
                    Employee.removeEmployee(employees, sc);
                    break;
                case 5:
                    mainFlag = false;
                    System.out.println("The number of users in the system are: " + Employee.getEmpCount());
                    System.out.println("Exiting application.");
                    break;
            }
        }

        sc.close();
    }
}

class Menu {
    public static void displayMainMenu() {
        System.out.println("Employee Management App");
        System.out.println("-----------------------");
        System.out.println("1. Create");
        System.out.println("2. Display");
        System.out.println("3. Raise Salary");
        System.out.println("4. Remove Employee");
        System.out.println("5. Exit");
        System.out.println("-----------------------");
        System.out.print("Enter choice: ");
    }

    public static int readMenuChoice(Scanner sc, int min, int max) {
        int choice = 0;
        while (true) {
            try {
                choice = sc.nextInt();
                if (choice < min || choice > max) {
                    throw new InvalidChoiceException("Invalid menu choice, please retry.");
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Error: Provide a valid input.");
                sc.nextLine();
            } catch (InvalidChoiceException e) {
                System.out.println(e.getMessage());
            }
        }
        return choice;
    }
}

class EmployeeDetailsReader {
    public static int readAge(Scanner sc) {
        int age = 0;
        while (true) {
            try {
                System.out.print("Enter age: ");
                age = sc.nextInt();
                if (age < 21 || age > 60) {
                    throw new InvalidAgeException("Invalid age! Age should be between 21 and 60.");
                }
                return age;
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a valid number for age.");
                sc.nextLine();
            } catch (InvalidAgeException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static String readName(Scanner sc) {
        String name;
        while (true) {
            try {
                System.out.print("Enter name (FirstName LastName): ");
                name = sc.nextLine().trim();

                if (name.isEmpty()) {
                    throw new InvalidNameException("Name cannot be empty. Please try again.");
                }

                Pattern pattern = Pattern.compile("^[A-Z][a-z]*\\s[A-Z][a-z]*$");
                Matcher matcher = pattern.matcher(name);

                if (!matcher.matches()) {
                    throw new InvalidNameException("Invalid name! Name must be in the format 'FirstName LastName'.");
                }
                return name;
            } catch (InvalidNameException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

abstract class Employee {
    private static int employeeCount = 0;
    private final int empId;
    private final String name;
    private final int age;
    protected double salary;

    private static boolean isCeoCreated = false;

    protected Employee(String name, int age, double salary, int empId) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.empId = empId;
    }

    public int getEmpId() {
        return empId;
    }

    public String getName() {
        return name;
    }

    public abstract String getRole();

    public abstract void raiseSalary(double amount);

    public static int getEmpCount() {
        return employeeCount;
    }

    public static void incrementEmpCount() {
        employeeCount++;
    }

    public static void decrementEmpCount() {
        employeeCount--;
    }

    public static boolean isCeoCreated() {
        return isCeoCreated;
    }

    public static void setCeoCreated(boolean created) {
        isCeoCreated = created;
    }

    public void display() {
        System.out.println("ID: " + empId);
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Salary: " + salary);
        System.out.println("Role: " + getRole());
    }

    public static void createEmployee(Employee[] employees, Scanner sc) {
        if (!isCeoCreated()) {
            System.out.println("No CEO exists. A CEO must be created first.");
            System.out.print("Do you want to create the CEO? (yes/no): ");
            String response = sc.next().toLowerCase();
            sc.nextLine();
            if (!response.equals("yes")) {
                System.out.println("Returning to main menu...");
                return;
            }

            String name = EmployeeDetailsReader.readName(sc);
            int age = EmployeeDetailsReader.readAge(sc);
            employees[employeeCount] = CEO.createInstance(name, age);
            return;
        }

        if (employeeCount >= employees.length) {
            System.out.println("Cannot add more employees. Maximum limit reached.");
            return;
        }

        while (true) {
            System.out.println("1. Clerk");
            System.out.println("2. Programmer");
            System.out.println("3. Manager");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int choice = Menu.readMenuChoice(sc, 1, 4);
            sc.nextLine();
            if (choice == 4) return;

            String type = switch (choice) {
                case 1 -> "clerk";
                case 2 -> "programmer";
                case 3 -> "manager";
                default -> throw new IllegalArgumentException("Invalid type");
            };

            String name = EmployeeDetailsReader.readName(sc);
            int age = EmployeeDetailsReader.readAge(sc);
            employees[employeeCount++] = EmployeeFactory.createEmployee(type, name, age, employeeCount);
        }
    }

    public static void displayAll(Employee[] employees) {
        if (employeeCount == 0) {
            System.out.println("No employees to display.");
            return;
        }

        EmployeeIterator iterator = new EmployeeIterator(employees);

        while (iterator.hasNext()) {
            Employee emp = iterator.next();
            if (emp != null) {
                emp.display();
                System.out.println("-----------------------");
            }
        }
    }

    public static void raiseSalary(Employee[] employees) {
        if (employeeCount == 0) {
            System.out.println("No employees to raise salary for.");
            return;
        }

        for (Employee emp : employees) {
            if (emp != null) {
                if (emp.getRole().equals("Clerk")) {
                    emp.raiseSalary(2000);
                } else if (emp.getRole().equals("Programmer")) {
                    emp.raiseSalary(5000);
                } else if (emp.getRole().equals("Manager")) {
                    emp.raiseSalary(15000);
                }
            }
        }
    }

    public static void removeEmployee(Employee[] employees, Scanner sc) {
        System.out.print("Enter employee ID to remove: ");
        int id = sc.nextInt();

        for (int i = 0; i < employeeCount; i++) {
            if (employees[i].getEmpId() == id) {
                if (employees[i] instanceof CEO) {
                    System.out.println("Cannot remove the CEO.");
                    return;
                }

                System.out.print("Are you sure you want to remove employee with ID " + id + " (yes/no)? ");
                sc.nextLine();
                String confirmation = sc.nextLine().toLowerCase();

                if (confirmation.equals("yes")) {
                    System.out.println("Removing " + employees[i].getRole() + " with ID: " + id);
                    employees[i] = employees[--employeeCount];
                    employees[employeeCount] = null;
                } else {
                    System.out.println("Employee removal canceled.");
                }
                return;
            }
        }

        System.out.println("Employee with ID " + id + " not found.");
    }
}

class Clerk extends Employee {
    private Clerk(String name, int age, double salary, int empId) {
        super(name, age, salary, empId);
    }

    public static Clerk create(String name, int age, int empId) {
        return new Clerk(name, age, 20000, empId);
    }

    public String getRole() {
        return "Clerk";
    }

    public void raiseSalary(double amount) {
        super.salary += amount;
        System.out.println("Salary for Clerk " + super.getName() + " raised by " + amount + ". New Salary: " + super.salary);
    }
}

class Programmer extends Employee {
    private Programmer(String name, int age, double salary, int empId) {
        super(name, age, salary, empId);
    }

    public static Programmer create(String name, int age, int empId) {
        return new Programmer(name, age, 30000, empId);
    }

    public String getRole() {
        return "Programmer";
    }

    public void raiseSalary(double amount) {
        super.salary += amount;
        System.out.println("Salary for Programmer " + super.getName() + " raised by " + amount + ". New Salary: " + super.salary);
    }
}

class Manager extends Employee {
    private Manager(String name, int age, double salary, int empId) {
        super(name, age, salary, empId);
    }

    public static Manager create(String name, int age, int empId) {
        return new Manager(name, age, 100000, empId);
    }

    public String getRole() {
        return "Manager";
    }

    public void raiseSalary(double amount) {
        super.salary += amount;
        System.out.println("Salary for Manager " + super.getName() + " raised by " + amount + ". New Salary: " + super.salary);
    }
}

class CEO extends Employee {
    private static CEO instance;

    private CEO(String name, int age, double salary, int empId) {
        super(name, age, salary, empId);
    }

    public static CEO createInstance(String name, int age) {
        if (instance == null) {
            instance = new CEO(name, age, 500000, 1);
            setCeoCreated(true);
            incrementEmpCount();
            System.out.println("CEO created successfully.");
        } else {
            System.out.println("CEO already exists. Cannot create another.");
        }
        return instance;
    }

    public String getRole() {
        return "CEO";
    }

    public void raiseSalary(double amount) {
        super.salary += amount;
        System.out.println("Salary for CEO " + super.getName() + " raised by " + amount + ". New Salary: " + super.salary);
    }
}
