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
	public static InventoryRecord updateStock(InventoryRecord inventory) throws SQLException {

		ResultSet resultSet = null;
		int id = 0;

		String sqlString = "INSERT INTO inventoryrecords (product_id,quantity,date,notes) VALUES (?,?,CURDATE(),?)";

		try (Connection connection = DBconnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);

		) {

			statement.setInt(1, inventory.getProductId());

			statement.setInt(2, inventory.getQuantity());

			if (inventory.getNotes() == null) {
				statement.setNull(3, Types.VARCHAR);
			} else {
				statement.setString(3, inventory.getNotes());
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
				throw new SQLIntegrityConstraintViolationException("Product Id not found !");
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

		return getRecord(id);
	}

	/**
	 * To get quantity of individual product in the database.
	 * 
	 * @param productId
	 * @return quantity of the product
	 * @throws SQLException
	 */

	public static InventoryRecord getProductQuantity(int productId) throws SQLException {
		InventoryRecord result = null;

		String sqlString = "SELECT SUM(quantity) AS TotalQuantity FROM inventoryrecords WHERE product_id =" + productId;

		try (Connection connection = DBconnector.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlString);) {

			if (resultSet.next()) {
				result = new InventoryRecord.InventoryRecordBuilder(resultSet.getInt("Product_id"),
						resultSet.getInt("TotalQuantity")).build();
			}

		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}

		return result;

	}

	public static InventoryRecord getRecord(int id) throws SQLException {
		if(id==0) {
			return null;
		}
		InventoryRecord rec = null;
		String sqlString = "SELECT id, product_id,quantity,date,notes FROM inventoryrecords WHERE id = " + id;

		try (Connection connection = DBconnector.getConnection();
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

	public static List<InventoryRecord> productStock() throws SQLException {
		List<InventoryRecord> stock = new ArrayList<>();
		String sqlString = "SELECT p.id, name, SUM(i.quantity) AS TotalQuantity FROM products p INNER JOIN inventoryrecords i ON p.id = i.product_id GROUP BY p.id ORDER BY p.id;";
		try (Connection connection = DBconnector.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlString);) {

			while (resultSet.next()) {
				stock.add(new InventoryRecord.InventoryRecordBuilder(resultSet.getInt("p.id"),
						resultSet.getInt("TotalQuantity")).notes(resultSet.getString("name")).build());
			}

		} catch (SQLException e) {
			throw new SQLException(e.getMessage());

		}

		return stock;

	}

}
