package api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import config.DBconnector;

public class CategoryManager {

	private CategoryManager() {

	}

	/**
	 * inserts a new category in the database.
	 * 
	 * @parameter name of the category
	 * @return generated category id or 0 if unsuccessful
	 * @throws SQLIntegrityConstraintViolationException
	 */
	public static int insert(String name) throws SQLException {

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

			if (e.getClass() == SQLIntegrityConstraintViolationException.class) {
				throw new SQLIntegrityConstraintViolationException("same category name exists !");
			}

			throw e;
		}

		finally {
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
	 * @parameter id of the category
	 * @parameter name for updating
	 * @return either 1 if successful or 0 if unsuccessful
	 * @throws InvalidInputException
	 */
	public static int update(int id, String name) throws SQLException {

		int rowAffected = 0;

		String sqlString = "UPDATE categories SET name = ? WHERE id = ?";

		try (Connection connection = DBconnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(sqlString);) {
			statement.setString(1, name);
			statement.setInt(2, id);

			rowAffected = statement.executeUpdate();

		} catch (SQLException e) {

			if (e.getClass() == SQLIntegrityConstraintViolationException.class) {
				throw new SQLIntegrityConstraintViolationException("same category name exists !");
			}

			throw e;

		}

		return rowAffected;

	}

	/**
	 * deletes a category in the database.
	 * 
	 * @parameter id of the category
	 *
	 * @return either 1 if successful or 0 if unsuccessful
	 * @throws SQLException
	 */

	public static int delete(int id) throws SQLException {

		int rowAffected = 0;
		String sqlString = "DELETE FROM Categories WHERE id= ?";

		try (Connection connection = DBconnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(sqlString);) {

			statement.setInt(1, id);
			rowAffected = statement.executeUpdate();

		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}

		return rowAffected;

	}

	/**
	 * search for categories with name in database.
	 * 
	 * @parameter id
	 * @return a <code>List</code> of <code>categories</code>
	 * @throws SQLException
	 */

	public static String search(int id) throws SQLException {

		String category = null;

		String sqlString = "SELECT id,name FROM categories WHERE id=" + id;

		try (Connection connection = DBconnector.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlString);) {

			while (resultSet.next()) {
				category = Integer.toString(resultSet.getInt(1)) + " | " + resultSet.getString(2);
			}

		}

		catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}

		return category;

	}

	/**
	 * search for categories with name in database.
	 * 
	 * @parameter category name
	 * @return a <code>List</code> of <code>categories</code>
	 * @throws SQLException
	 */
	public static List<String> search(String name) throws SQLException {

		List<String> categories = new ArrayList<>();

		if (" ".equals(name)) {
			return categories;
		}

		String sqlString = "SELECT id,name FROM categories WHERE name LIKE '%" + name + "%'";

		try (Connection connection = DBconnector.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlString);) {

			while (resultSet.next()) {
				categories.add(Integer.toString(resultSet.getInt(1)) + " | " + resultSet.getString(2));
			}

		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}

		return categories;

	}

}
