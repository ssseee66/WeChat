package WeChat;
import java.sql.*;
public class GetConnection {
	public static Connection connection(String databaseName, String password) {
		Connection con = null;
		String username = "root";
		String url = "jdbc:mysql://localhost:3307/" + databaseName + 
			"?useSSL=true&serverTimezone=GMT&characterEncoding=utf8";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception ignored) {}
		try {
			con = DriverManager.getConnection(url, username, password);
		} catch (SQLException ignored) {}
		return con;
	}
}