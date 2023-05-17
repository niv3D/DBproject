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
import models.InventoryRecord;
import models.Product;

public class InventoryManager {

	private InventoryManager() {
	}

	/**
	 * insert new inventory record in the database.
	 * 
	 * @param inventory <code>models.inventoryRecords</code> object
	 * @return id of the record or null if low stock
	 * @throws SQLException
	 */
	public static Product updateStock(InventoryRecord inventory) throws SQLException {

		Connection connection = null;
		PreparedStatement statement = null;
		PreparedStatement stmt = null;

		String sqlString = "INSERT INTO inventoryrecords (product_id,quantity,date,notes) VALUES (?,?,CURDATE(),?)";
		String updateString = "UPDATE products SET quantity_in_stock = ? WHERE id = ?";

		try {

			connection = DBconnector.getInstance().getConnection();
			connection.setAutoCommit(false);

			stmt = connection.prepareStatement(updateString);

			statement = connection.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);

			statement.setInt(1, inventory.getProductId());

			statement.setInt(2, inventory.getQuantity());

			if (inventory.getNotes() == null) {
				statement.setNull(3, Types.VARCHAR);
			} else {
				statement.setString(3, inventory.getNotes());
			}
			
			statement.executeUpdate();
			Product p = productStock(inventory.getProductId());

			if ( p != null) {

				int quantityInStock = p.getQuantityInStock() + inventory.getQuantity();

				stmt.setInt(1, quantityInStock);
				stmt.setInt(2, p.getId());

				stmt.executeUpdate();

				connection.commit();

			}

		} catch (SQLException e) {
			connection.rollback();
			if (e.getClass() == SQLIntegrityConstraintViolationException.class) {
				throw new SQLIntegrityConstraintViolationException("Product Id not found !");
			}

			throw e;

		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return productStock(inventory.getId());
	}

	/**
	 * To get quantity of individual product in the database.
	 * 
	 * @param productId
	 * @return quantity of the product
	 * @throws SQLException
	 */

	public static InventoryRecord getRecord(int id) throws SQLException {
		if (id == 0) {
			return null;
		}
		InventoryRecord rec = null;
		String sqlString = "SELECT id,product_id,quantity,date,notes FROM inventoryrecords WHERE id = " + id;

		try (Connection connection = DBconnector.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet r = statement.executeQuery(sqlString);

		) {

			if (r.next()) {
				rec = new InventoryRecord.InventoryRecordBuilder(r.getInt("product_id"), r.getInt("quantity"))
						.id(r.getInt("id")).date(r.getDate("date")).notes(r.getString("notes")).build();
			}

		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}

		return rec;

	}

	/**
	 * To get all available stock of all products in the database.
	 * 
	 * @return all the product id and available stock
	 * @throws SQLException
	 */

	public static Product productStock(int id) throws SQLException {
		Product product = null;
		String sqlString = "SELECT id, name, category_id, price, description quantity_in_stock FROM products WHERE id = " + id;

		try (Connection connection = DBconnector.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlString);) {

			if (resultSet.next()) {
				product = new Product.ProductBuilder(resultSet.getString("name"), resultSet.getInt("price"),
						resultSet.getString("description")).id(resultSet.getInt("id"))
						.quantityInStock(resultSet.getInt("quantity_in_stock")).categoryId(resultSet.getInt("category_id")).build();
			}

		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}

		return product;

	}

	public static List<Product> productStock() throws SQLException {
		List<Product> stock = new ArrayList<>();
		String sqlString = "SELECT id, name, price, description quantity_in_stock FROM products";
		try (Connection connection = DBconnector.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlString);) {

			while (resultSet.next()) {
				stock.add(new Product.ProductBuilder(resultSet.getString("name"), resultSet.getInt("price"),
						resultSet.getString("description")).id(resultSet.getInt("id"))
						.quantityInStock(resultSet.getInt("quantity_in_stock")).build());
			}

		} catch (SQLException e) {
			throw new SQLException(e.getMessage());

		}

		return stock;

	}

}
