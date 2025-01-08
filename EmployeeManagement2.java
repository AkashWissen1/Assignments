
import java.util.*;

public class EmployeeManagement2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Employee> employees = new ArrayList<>();
        boolean mainFlag = true;

        while (mainFlag) {
            System.out.println("Employee Management App");
            System.out.println("-----------------------");
            System.out.println("1. Create");
            System.out.println("2. Display");
            System.out.println("3. Raise Salary");
            System.out.println("4. Exit");
            System.out.println("-----------------------");
            System.out.print("Enter choice: ");
            int choiceMain = sc.nextInt();

            switch (choiceMain) {
                case 1:
                    boolean roleFlag = true;
                    while (roleFlag) {
                        System.out.println("-----------------------");
                        System.out.println("1. Clerk");
                        System.out.println("2. Programmer");
                        System.out.println("3. Manager");
                        System.out.println("4. Back to Main Menu");
                        System.out.println("-----------------------");
                        System.out.print("Enter the choice: ");
                        int choiceRole = sc.nextInt();
                        sc.nextLine(); 

                        if (choiceRole >= 1 && choiceRole <= 3) {
                            System.out.print("Enter name: ");
                            String name = sc.nextLine();
                            System.out.print("Enter age: ");
                            int age = sc.nextInt();
                            if(age < 21 || age > 60) {
                            	System.out.println("Invalid age, Try again");
                            	continue;
                            }

                            if (choiceRole == 1) {
                                employees.add(new Clerk(name, age, 20000));
                                System.out.println("Clerk created");
                            } else if (choiceRole == 2) {
                                employees.add(new Programmer(name, age, 30000));
                                System.out.println("Programmer created");
                            } else {
                                employees.add(new Manager(name, age, 100000));
                                System.out.println("Manager created");
                            }
                        } else if (choiceRole == 4) {
                            roleFlag = false; 
                        } else {
                            System.out.println("Invalid choice. Try again.");
                        }
                    }
                    break;

                case 2:
                    if (employees.isEmpty()) {
                        System.out.println("No employees to display.");
                    } else {
                        System.out.println("Employee Details:");
                        for (Employee emp : employees) {
                            emp.display();
                            System.out.println("-----------------------");
                        }
                    }
                    break;

                case 3:
                    if (employees.isEmpty()) {
                        System.out.println("No employees available to raise salary.");
                    } else {
                        System.out.println("Raising salaries for all employees...");
                        for (Employee emp : employees) {
                            if (emp.getRole() == "Clerk") {
                                emp.raiseSalary(2000);
                            } else if (emp.getRole() == "Programmer") {
                                emp.raiseSalary(5000);
                            } else if (emp.getRole() == "Manager") {
                                emp.raiseSalary(15000);
                            }
                        }
                    }
                    break;

                case 4:
                    mainFlag = false;
                    System.out.println("Exiting application");
                    System.out.println("Total employees created: " + employees.size());
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        sc.close();
    }
}

class Employee {
    private String employeeName;
    private int employeeAge;
    private double employeeSalary;
    private String employeeRole;

    public Employee(String employeeName, int employeeAge, double employeeSalary, String employeeRole) {
        this.employeeName = employeeName;
        this.employeeAge = employeeAge;
        this.employeeSalary = employeeSalary;
        this.employeeRole = employeeRole;
    }
    
    public String getRole() {
        return employeeRole;
    }
    
    public String getName() {
        return employeeName;
    }

    public void display() {
        System.out.println("Name: " + employeeName);
        System.out.println("Age: " + employeeAge);
        System.out.println("Salary: " + employeeSalary);
        System.out.println("Role: " + employeeRole);
    }

    public void raiseSalary(double amount) {
        this.employeeSalary += amount;
        System.out.println("Salary of " + employeeName + " raised by " + amount);
        System.out.println("New Salary: " + this.employeeSalary);
    }
}

class Clerk extends Employee {
    public Clerk(String employeeName, int employeeAge, double employeeSalary) {
        super(employeeName, employeeAge, employeeSalary, "Clerk");
    }
}

class Programmer extends Employee {
    public Programmer(String employeeName, int employeeAge, double employeeSalary) {
        super(employeeName, employeeAge, employeeSalary, "Programmer");
    }
}

class Manager extends Employee {
    public Manager(String employeeName, int employeeAge, double employeeSalary) {
        super(employeeName, employeeAge, employeeSalary, "Manager");
    }
}
