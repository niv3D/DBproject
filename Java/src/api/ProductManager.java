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
	 * @return generated product
	 * @throws SQLException
	 */
	public static Product insert(Product product) throws SQLException {

		ResultSet resultSet = null;
		Product result = null;

		String sqlString = "INSERT INTO products (name,category_id,price,description) VALUES (?,?,?,?)";

		try (Connection connection = DBconnector.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);

		) {
			int i = 0;
			statement.setString(++i, product.getName());

			if (product.getCategoryId() == 0) {
				statement.setNull(++i, Types.INTEGER);
			} else {
				statement.setInt(++i, product.getCategoryId());
			}

			statement.setFloat(++i, product.getPrice());
			statement.setString(++i, product.getDescription());

			int rowAffected = statement.executeUpdate();

			if (rowAffected == 1) {
				resultSet = statement.getGeneratedKeys();
				if (resultSet.next()) {

					result = new Product.ProductBuilder(product.getName(), product.getPrice(), product.getDescription())
							.categoryId(product.getCategoryId()).id(resultSet.getInt(1)).build();
				}
			}

		} catch (SQLException e) {

			if (e.getClass() == SQLIntegrityConstraintViolationException.class) {
				throw new SQLIntegrityConstraintViolationException(
						"Product name already exists or category id not found");
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

		return result;
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
	public static Product update(Product product) throws SQLException {

		int rowAffected = 0;
		String sqlString = "UPDATE products SET ";

		if (product.getName() != null) {
			sqlString += "name = ?,";
		}
		if (product.getCategoryId() != 0) {
			sqlString += "category_id = ?,";
		}

		if (product.getPrice() != 0f) {
			sqlString += "price = ?, ";
		}
		if (product.getPrice() != 0) {
			sqlString += "price = ?,";

		}
		if (product.getDescription() != null) {
			sqlString += "description = ?,";
		}
		sqlString = sqlString.substring(0, sqlString.lastIndexOf(","));
		sqlString += " WHERE id = ?";

		System.out.println(sqlString);

		try (Connection connection = DBconnector.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(sqlString);) {

			int i = 0;

			if (product.getName() != null) {
				statement.setString(++i, product.getName());
			}
			if (product.getCategoryId() != 0) {
				statement.setInt(++i, product.getCategoryId());
			}
			if (product.getPrice() != 0) {
				statement.setFloat(++i, product.getPrice());
			}
			if (product.getDescription() != null) {
				statement.setString(++i, product.getDescription());
			}

			// if all field are null return 0
			if (i == 0) {
				return null;
			}

			statement.setInt(++i, product.getId());

			rowAffected = statement.executeUpdate();

		} catch (SQLException e) {
			if (e.getClass() == SQLIntegrityConstraintViolationException.class) {
				throw new SQLIntegrityConstraintViolationException(
						"Product name already exists or category id not found");
			}

			throw e;
		}

		if (rowAffected == 1) {
			return search(product.getId());
		} else {
			return null;
		}

	}

	/**
	 * deletes a product from the database.
	 * 
	 * @param id product id
	 * @return rowAffected, either 1 if deletion was successful or 0
	 */
	public static Product delete(int id) throws SQLException {

		String sqlString = "DELETE FROM products WHERE id = ?";
		int rowAffected = 0;
		Product product = search(id);

		try (Connection connection = DBconnector.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(sqlString)) {

			statement.setInt(1, id);
			rowAffected = statement.executeUpdate();

		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}

		if (rowAffected == 1) {
			return product;
		} else {
			return null;
		}
	}

	/**
	 * search for a product with id in database.
	 * 
	 * @param id product id
	 * @return a <code>models.Product<code> object or null if no product is found
	 * @throws SQLException
	 */
	public static Product search(int id) throws SQLException {

		Product product = null;
		String sqlString = "SELECT id,name,category_id,price,description FROM products WHERE id = " + id;

		try (Connection connection = DBconnector.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlString);

		) {

			if (resultSet.next()) {
				product = new Product.ProductBuilder(resultSet.getString("name"), resultSet.getFloat("price"),
						resultSet.getString("description")).categoryId(resultSet.getInt("category_id"))
						.id(resultSet.getInt("id")).build();
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

		String sqlString = "SELECT id,name,category_id,price,description FROM products WHERE name LIKE '%" + name
				+ "%'";

		try (Connection connection = DBconnector.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlString);) {

			while (resultSet.next()) {
				products.add(new Product.ProductBuilder(resultSet.getString("name"), resultSet.getFloat("price"),
						resultSet.getString("description")).categoryId(resultSet.getInt("category_id"))
						.id(resultSet.getInt("id")).build());
			}

		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}

		return products;
	}
}