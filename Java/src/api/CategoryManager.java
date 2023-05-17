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
import models.Category;

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
	public static Category insert(Category category) throws SQLException {

		ResultSet resultSet = null;
		Category result = null;

		String sqlString = "INSERT INTO categories (name) VALUES (?)";

		try (Connection connection = DBconnector.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);

		) {

			statement.setString(1, category.getName());
			int rowAffected = statement.executeUpdate();

			if (rowAffected == 1) {
				resultSet = statement.getGeneratedKeys();
				if (resultSet.next()) {
					result = new Category.CategoryBuilder(category.getName()).id(resultSet.getInt(1)).build();
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

		return result;
	}

	/**
	 * updates a category in the database.
	 * 
	 * @parameter id of the category
	 * @parameter name for updating
	 * @return either 1 if successful or 0 if unsuccessful
	 * @throws InvalidInputException
	 */
	public static Category update(Category category) throws SQLException {

		int rowAffected = 0;

		String sqlString = "UPDATE categories SET name = ? WHERE id = ?";

		try (Connection connection = DBconnector.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(sqlString);) {
			statement.setString(1, category.getName());
			statement.setInt(2, category.getId());

			rowAffected = statement.executeUpdate();

		} catch (SQLException e) {

			if (e.getClass() == SQLIntegrityConstraintViolationException.class) {
				throw new SQLIntegrityConstraintViolationException("same category name exists !");
			}

			throw e;

		}

		if (rowAffected == 1) {
			return search(category.getId());
		} else {
			return null;
		}

	}

	/**
	 * deletes a category in the database.
	 * 
	 * @parameter id of the category
	 *
	 * @return either 1 if successful or 0 if unsuccessful
	 * @throws SQLException
	 */

	public static Category delete(int id) throws SQLException {

		int rowAffected = 0;
		String sqlString = "DELETE FROM Categories WHERE id= ?";
		Category category = search(id);

		try (Connection connection = DBconnector.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(sqlString);) {

			statement.setInt(1, id);
			rowAffected = statement.executeUpdate();

		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}

		if (rowAffected == 1) {
			return category;
		} else {
			return null;
		}
	}

	/**
	 * search for categories with name in database.
	 * 
	 * @parameter id
	 * @return a <code>List</code> of <code>categories</code>
	 * @throws SQLException
	 */

	public static Category search(int id) throws SQLException {

		Category category = null;

		String sqlString = "SELECT id,name FROM categories WHERE id=" + id;

		try (Connection connection = DBconnector.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlString);) {

			while (resultSet.next()) {
				category = new Category.CategoryBuilder(resultSet.getString("name")).id(resultSet.getInt("id")).build();
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
	public static List<Category> search(String name) throws SQLException {

		List<Category> categories = new ArrayList<>();

		String sqlString = "SELECT id,name FROM categories WHERE name LIKE '%" + name + "%'";

		try (Connection connection = DBconnector.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlString);) {

			while (resultSet.next()) {
				categories.add(
						new Category.CategoryBuilder(resultSet.getString("name")).id(resultSet.getInt("id")).build());
			}

		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}

		return categories;

	}

}
