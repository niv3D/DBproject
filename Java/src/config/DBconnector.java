package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnector {

	private static DBconnector dBconnector;

	private DBconnector() {
	}

	public static DBconnector getInstance() {
		if (dBconnector == null) {
			dBconnector = new DBconnector();
		}
		return dBconnector;
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/InventoryDB", "root", "");
	}
}
