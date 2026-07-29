import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {

        Connection con = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/school";
            String user = "root";
            String password = "Astin321";

            con = DriverManager.getConnection(url,user,password);

        } catch(Exception e) {

            e.printStackTrace();
        }

        return con;
    }
}