package api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
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
	 */
	public static int insert(String name) throws InvalidInputException {

		if ("".equals(name) || "null".equalsIgnoreCase(name)) {
			throw new InvalidInputException();

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

		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println("Duplicate entries are not allowed");
		} catch (Exception e) {
			System.out.println("Something went Wrong");
		}

		finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					System.out.println("Something went Wrong");
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
	public static int update(int id, String name) throws InvalidInputException {

		int rowAffected = 0;

		if (name == "null" || " ".equals(name) || id == 0) {
			throw new InvalidInputException();

		}

		String sqlString = "UPDATE categories SET name = ? WHERE id = ?";

		try (Connection connection = DBconnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(sqlString);) {
			statement.setString(1, name);
			statement.setInt(2, id);

			rowAffected = statement.executeUpdate();

		} catch (SQLDataException e) {
			System.out.println("Enter an integer value as id");

		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println("Duplicate entries are not allowed");

		} catch (Exception e) {
			System.out.println("Something went wrong");
		}

		return rowAffected;

	}

	/**
	 * deletes a category in the database.
	 * 
	 * @parameter id of the category
	 *
	 * @return either 1 if successful or 0 if unsuccessful
	 */

	public static int delete(int id) {

		int rowAffected = 0;
		String sqlString = "DELETE FROM Categories WHERE id= ?";

		try (Connection connection = DBconnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(sqlString);) {

			statement.setInt(1, id);
			rowAffected = statement.executeUpdate();

		} catch (SQLDataException e) {
			System.out.println("Enter an integer value as id");

		}

		catch (Exception e) {
			System.out.println("Something went wrong");
		}

		return rowAffected;

	}

	/**
	 * search for categories with name in database.
	 * 
	 * @parameter id
	 * @return a <code>List</code> of <code>categories</code>
	 */

	public static List<String> search(int id) {

		List<String> categories = new ArrayList<>();

		String sqlString = "SELECT id,name FROM categories WHERE id=" + id;

		try (Connection connection = DBconnector.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlString);) {

			while (resultSet.next()) {
				categories.add(Integer.toString(resultSet.getInt(1)) + " " + resultSet.getString(2));
			}

		} catch (SQLDataException e) {
			System.out.println("Enter an integer value as id");

		} catch (Exception e) {
			System.out.println("Something went wrong");
		}

		return categories;

	}

	/**
	 * search for categories with name in database.
	 * 
	 * @parameter category name
	 * @return a <code>List</code> of <code>categories</code>
	 */
	public static List<String> search(String name) {

		List<String> categories = new ArrayList<>();

		if (" ".equals(name)) {
			return categories;
		}

		String sqlString = "SELECT id,name FROM categories WHERE name LIKE '%" + name + "%'";

		try (Connection connection = DBconnector.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlString);) {

			while (resultSet.next()) {
				categories.add(Integer.toString(resultSet.getInt(1)) + " " + resultSet.getString(2));
			}

		} catch (Exception e) {
			System.out.println("Something went wrong");
		}

		return categories;

	}

}
