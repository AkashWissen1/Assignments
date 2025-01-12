package employee_application;

import java.util.Scanner;

public class EmployeeManagement {
    public static void main(String[] args) {
        Employee[] employees = new Employee[100];
        Scanner sc = new Scanner(System.in);
        
        boolean mainFlag = true;
        while (mainFlag) {
            System.out.println("Employee Management App");
            System.out.println("-----------------------");
            System.out.println("1. Create");
            System.out.println("2. Display");
            System.out.println("3. Raise Salary");
            System.out.println("4. Remove Employee");
            System.out.println("5. Exit");
            System.out.println("-----------------------");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

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
                    System.out.println("The number of users in the System are :-" + Employee.getEmpCount());
                    System.out.println("Exiting application.");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        sc.close();
    }
}

abstract class Employee {
    private static int employeeCount = 0;
    private final int empId;
    private final String name;
    private final int age;
    protected double salary;

    public Employee(String name, int age, double salary, int empId) {
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

    public void display() {
        System.out.println("ID: " + empId);
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Salary: " + salary);
        System.out.println("Role: " + getRole());
    }

    public static boolean isEmpIdDuplicate(Employee[] employees, int empId) {
        for (int i = 0; i < employeeCount; i++) {
            if (employees[i].getEmpId() == empId) {
                return true;
            }
        }
        return false;
    }

    public static void createEmployee(Employee[] employees, Scanner sc) {
        if (employeeCount >= employees.length) {
            System.out.println("Cannot add more employees. Maximum limit reached.");
            return;
        }

        System.out.println("1. Clerk");
        System.out.println("2. Programmer");
        System.out.println("3. Manager");
        System.out.print("Enter choice: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice >= 1 && choice <= 3) {
            System.out.print("Enter name: ");
            String name = sc.nextLine();
            System.out.print("Enter age: ");
            int age = sc.nextInt();

            if (age < 21 || age > 60) {
                System.out.println("Invalid age. Try again.");
                return;
            }

            int empId = employeeCount + 1; // Automatically assign empId starting from 1

            Employee emp = null;
            switch (choice) {
                case 1:
                    emp = new Clerk(name, age, 20000, empId);
                    break;
                case 2:
                    emp = new Programmer(name, age, 30000, empId);
                    break;
                case 3:
                    emp = new Manager(name, age, 100000, empId);
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

            if (emp != null) {
                employees[employeeCount++] = emp;
                System.out.println(emp.getRole() + " created with ID: " + emp.getEmpId());
            }
        } else {
            System.out.println("Invalid choice. Try again.");
        }
    }

    public static void displayAll(Employee[] employees) {
        if (employeeCount == 0) {
            System.out.println("No employees to display.");
            return;
        }

        for (int i = 0; i < employeeCount; i++) {
            employees[i].display();
            System.out.println("-----------------------");
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
                System.out.print("Are you sure you want to remove employee with ID " + id + " (yes/no)? ");
                sc.nextLine();  // Consume the newline
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
    public Clerk(String name, int age, double salary, int empId) {
        super(name, age, salary, empId);
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
    public Programmer(String name, int age, double salary, int empId) {
        super(name, age, salary, empId);
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
    public Manager(String name, int age, double salary, int empId) {
        super(name, age, salary, empId);
    }

    public String getRole() {
        return "Manager";
    }

    public void raiseSalary(double amount) {
        super.salary += amount;
        System.out.println("Salary for Manager " + super.getName() + " raised by " + amount + ". New Salary: " + super.salary);
    }
}
