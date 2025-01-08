public class EmployeeManagement {
    public static void main(String[] args) {
        Clerk clerk = new Clerk("Akash", 30, 50000);
        Programmer programmer = new Programmer("Abhay", 25, 70000);
        Manager manager = new Manager("Alice", 40, 100000);

        System.out.println("Employee Details:");
        clerk.display();
        programmer.display();
        manager.display();
        System.out.println();

        System.out.println("Raising salary for Abhay by 5000");
        programmer.raiseSalary(5000);
        System.out.println();

        System.out.println("Updated Employee Details:");
        clerk.display();
        programmer.display();
        manager.display();
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

    public void display() {
        System.out.println("Name: " + employeeName);
        System.out.println("Age: " + employeeAge);
        System.out.println("Salary: " + employeeSalary);
        System.out.println("Role: " + employeeRole);
    }

    public void raiseSalary(double amount) {
        System.out.println("Raising salary of " + employeeName + " by " + amount);
        this.employeeSalary += amount;
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