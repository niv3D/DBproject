package api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import config.DBconnector;
import models.Product;

public class ProductManager {

	private ProductManager() {
	}

	/**
	 * inserts a new product in the database.
	 * 
	 * @param product an object of <code>models.Product</code> which contains
	 *                product data without id
	 * @return generated product id or 0 if unsuccessful
	 * @throws SQLException 
	 */
	public static int insert(Product product) throws SQLException {

		int id = 0;
		ResultSet resultSet = null;

		String sqlString = "INSERT INTO products (name,category_id,price,description) VALUES (?,?,?,?)";

		try (Connection connection = DBconnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);

		) {

			statement.setString(1, product.name);

			if (product.categoryId == null) {
				statement.setNull(2, Types.INTEGER);
			} else {
				statement.setInt(2, product.categoryId);
			}

			if (product.price == null) {
				statement.setNull(3, Types.FLOAT);
			} else {
				statement.setFloat(3, product.price);
			}

			if (product.description == null) {
				statement.setNull(4, Types.VARCHAR);
			} else {
				statement.setString(4, product.description);
			}
			int rowAffected = statement.executeUpdate();

			if (rowAffected == 1) {

				resultSet = statement.getGeneratedKeys();
				if (resultSet.next()) {
					id = resultSet.getInt(1);
				}
			}

		} catch (SQLException e) {

			if (e.getClass() == SQLIntegrityConstraintViolationException.class) {
				throw new SQLIntegrityConstraintViolationException("product with same name exists !");
			}

			throw e;

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
	 * updates an existing product's data from the database.
	 * 
	 * @param product an object of <code>models.Product</code> with id of the
	 *                updating product and fields set to the updated values, fields
	 *                set to null wont be updated
	 * @return rowAffected, either 1 if update was successful or 0
	 * @throws SQLException 
	 */
	public static int update(Product product) throws SQLException {

		int rowAffected = 0;
		String sqlString = "UPDATE products SET ";

		if (product.name != null) {
			sqlString += "name = ?, ";
		}
		if (product.categoryId != null) {
			sqlString += "category_id = ?, ";
		}
		if (product.price != null) {
			sqlString += "price = ?, ";
		}
		if (product.description != null) {
			sqlString += "description = ? ";
		}

		sqlString += "WHERE id = ?";

		try (Connection connection = DBconnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(sqlString);) {

			int i = 0;

			if (product.name != null) {
				statement.setString(++i, product.name);
			}
			if (product.categoryId != null) {
				statement.setInt(++i, product.categoryId);
			}
			if (product.price != null) {
				statement.setFloat(++i, product.price);
			}
			if (product.description != null) {
				statement.setString(++i, product.description);
			}

			// if all field are null return 0
			if (i == 0) {
				return 0;
			}

			statement.setInt(++i, product.id);

			rowAffected = statement.executeUpdate();

		} catch (SQLException e) {
			if (e.getClass() == SQLIntegrityConstraintViolationException.class) {
				throw new SQLIntegrityConstraintViolationException("product with same name exists !");
			}

			throw e;
		}

		return rowAffected;
	}

	/**
	 * deletes a product from the database.
	 * 
	 * @param id product id
	 * @return rowAffected, either 1 if deletion was successful or 0
	 */
	public static int delete(int id) {

		String sqlString = "DELETE FROM products WHERE id = ?";
		int rowAffected = 0;

		try (Connection connection = DBconnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(sqlString)) {

			statement.setInt(1, id);
			rowAffected = statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return rowAffected;

	}

	/**
	 * search for a product with id in database.
	 * 
	 * @param id product id
	 * @return a <code>models.Product<code> object or null if no product is found
	 * @throws SQLException 
	 */
	public static Product search(int id) throws SQLException {

		if (id == 0) {
			return null;
		}

		Product product = null;
		String sqlString = "SELECT id,name,category_id,price,description FROM products WHERE id = " + id;

		try (Connection connection = DBconnector.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlString);

		) {

			if (resultSet.next()) {
				product = new Product(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3),
						resultSet.getFloat(3), resultSet.getString(4));
			}

		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}

		return product;
	}

	/**
	 * search for products with name in database.
	 * 
	 * @param name product name
	 * @return a <code>List</code> of <code>models.Product</code>
	 * @throws SQLException 
	 */
	public static List<Product> search(String name) throws SQLException {

		List<Product> products = new ArrayList<>();

		if (" ".equals(name)) {
			return products;
		}

		String sqlString = "SELECT id,name,category_id,price,description FROM products WHERE name LIKE '%" + name
				+ "%'";

		try (Connection connection = DBconnector.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlString);) {

			while (resultSet.next()) {
				products.add(new Product(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3),
						resultSet.getFloat(3), resultSet.getString(4)));
			}

		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}

		return products;
	}
}