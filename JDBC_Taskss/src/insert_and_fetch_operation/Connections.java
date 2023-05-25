package insert_and_fetch_operation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Connections {

	public static Connection connection ;
	public static PreparedStatement preparedStatement;
	public static String query ;

	public static Connection createDBConnection () {
		try {
			
			String url = "jdbc:postgresql://localhost:5432/postgres";
			String uname = "postgres";
			String pass = "root";
			
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, uname, pass);
			
			preparedStatement = connection.prepareStatement(query);
			
			return connection;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void close_Connection (){
		try {
			connection.close();	
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}

}
