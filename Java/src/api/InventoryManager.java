package api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	 */
	public static Integer updateStock(InventoryRecord inventory) {
		
		int totalStock = getProductQuantity(inventory.productId);
		
		if((totalStock + inventory.quantity)<0) {
			return null;
		}
		
		int id = 0;
		ResultSet resultSet = null;

		String sqlString = "INSERT INTO inventoryrecords (product_id,quantity,date,notes) VALUES (?,?,CURDATE(),?)";

		try (Connection connection = DBconnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);

		) {

			statement.setInt(1, inventory.productId);

			statement.setInt(2, inventory.quantity);

			if (inventory.notes == null) {
				statement.setNull(3, Types.VARCHAR);
			} else {
				statement.setString(3, inventory.notes);
			}
			int rowAffected = statement.executeUpdate();

			if (rowAffected == 1) {

				resultSet = statement.getGeneratedKeys();
				if (resultSet.next()) {
					id = resultSet.getInt(1);
				}
			}

		} catch (SQLException e) {

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
	 * To get quantity of individual product in the database.
	 * 
	 * @param productId
	 * @return quantity of the product 
	 */

	public static int getProductQuantity(int productId) {
		int sum = 0;

		String sqlString = "SELECT SUM(quantity) FROM inventoryrecords WHERE product_id =" + productId;

		try (Connection connection = DBconnector.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlString);
				) {
			
			if(resultSet.next()) {
				sum=resultSet.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return sum;

	}
	
	/**
	 * To get all available stock of all products in the database.
	 *  @return all the product id and available stock 
	 */
	
	public static List<String> productStock () {
		List<String> stock=new ArrayList<>();
		String sqlString="SELECT p.id, name, SUM(i.quantity) FROM products p INNER JOIN inventoryrecords i ON p.id = i.product_id GROUP BY p.id ORDER BY p.id;";
		try (Connection connection = DBconnector.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlString);
				) {
			
			while(resultSet.next()) {
				stock.add(Integer.toString(resultSet.getInt(1))+" "+Integer.toString(resultSet.getInt(2)));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return stock;
		
	}

}
