package employee_application;
import java.sql.*;
public class JDBCDemo {
    public static void main(String args[]) {
        try {
            //Class.forName("org.postgresql.Driver");  // Not needed for JDBC 4.0+
            Connection con = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/empdb", "postgres", "tiger");
            
            Statement stmt = con.createStatement();

            // Uncomment any of these lines to perform different SQL operations
            // stmt.executeUpdate("insert into EMPLOYEE values('Geeta', 22)");
            // stmt.executeUpdate("update EMPLOYEE set AGE=AGE+1");
            // stmt.executeUpdate("delete from EMPLOYEE where AGE<30");

            ResultSet rs = stmt.executeQuery("select * from temp");

            while (rs.next()) {
                System.out.println("Name: " + rs.getString(1));
                System.out.println("Age: " + rs.getInt(2));
                System.out.println();
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
 
