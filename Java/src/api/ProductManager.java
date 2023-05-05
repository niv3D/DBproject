package api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import config.DBconnector;
import models.Product;

public class ProductManager {

	private ProductManager() {
	}

	/**
	 * inserts a new row in products table
	 * 
	 * @param product an object of <code>models.Product</code> which contains
	 *                product data
	 * @return generated product id or 0 if unsuccessful
	 */
	public static int insert(Product product) {

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
			// Implement user defined Exception later to re-throw
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

	public static void update() {

	}

	public static void delete() {

	}

	public static void search() {

	}
	
	public static void main(String[] args) {
		Product product = new Product("Bike", null, 12222.22f, null);
		
		System.out.println(ProductManager.insert(product)); 
	}
}