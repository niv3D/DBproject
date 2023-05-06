package api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import config.DBconnector;

public class CategoryManager {

	private CategoryManager() {

	}

	/**
	 * inserts a new category in the database.
	 * 
	 * @param name of the category
	 * @return generated category id or 0 if unsuccessful
	 */
	public static int insert(String name) {

		if (" ".equals(name)) {
			return 0;
		}
		if (name == null) {
			return 0;
		}

		int id = 0;
		ResultSet resultSet = null;
		String sqlString = "INSERT INTO categories (name) VALUES (?)";

		try (Connection connection = DBconnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);

		) {

			statement.setString(1, name);
			int rowAffected = statement.executeUpdate();

			if (rowAffected == 1) {
				resultSet = statement.getGeneratedKeys();
				if (resultSet.next()) {
					id = resultSet.getInt(1);
				}
			}

		} catch (SQLException e) {
			// Implement user defined Exception later to re-throw
			// catch SQLIntegrityConstraint.. exception for duplicate product name
			e.printStackTrace();

		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return id;
	}

	/**
	 * updates a category in the database.
	 * 
	 * @param id   of the category
	 * @param name for updating
	 * @return either 1 if successful or 0 if unsuccessful
	 */
	public static int update(int id, String name) {

		int rowAffected = 0;

		if (name == null || " ".equals(name) || id == 0) {
			return 0;
		}

		String sqlString = "UPDATE categories SET name = ? WHERE id = ?";

		try (Connection connection = DBconnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(sqlString);) {
			statement.setString(1, name);
			statement.setInt(2, id);

			rowAffected = statement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();

		}

		return rowAffected;

	}

	public static int delete() {
		int rowAffected = 0;

		// TO-DO

		return rowAffected;
	}

}
