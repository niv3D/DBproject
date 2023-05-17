package app;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import api.InventoryManager;
import models.InventoryRecord;
import models.Product;

public class InventoryUI {
	private InventoryUI() {
	}

	public static void insert(Scanner input) {

		if (!ProductUI.search(input)) {
			return;
		}

		int idInt;
		String productId = getString("select-id", input);
		if (productId.contains("/") || !verifyId(productId)) {
			return;
		} else {
			idInt = Integer.parseInt(productId);
		}

		int quantityInt;
		String quantity = getString("quantity", input);
		if (quantity.contains("/") || !verifyId(quantity)) {
			return;
		} else {
			quantityInt = Integer.parseInt(quantity);
		}

		String notes = getString("notes", input);
		if (notes.isEmpty()) {
			notes = null;
		} else if (notes.contains("/") || !verifyString(notes)) {
			return;
		}

		InventoryRecord rec = new InventoryRecord.InventoryRecordBuilder(idInt, quantityInt).notes(notes).build();
		Product result = null;

		try {
			result = InventoryManager.updateStock(rec);
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getMessage());
		}

		if (result != null) {
			System.out.println(" added !");
			printStatus(result);
		} else {
			System.out.println(" error , please try again !");
		}

	}

	public static void delete(Scanner input) {

		if (!ProductUI.search(input)) {
			return;
		}

		int idInt;
		String productId = getString("select-id", input);
		if (productId.contains("/") || !verifyId(productId)) {
			return;
		} else {
			idInt = Integer.parseInt(productId);
		}

		int quantityInt;
		String quantity = getString("quantity", input);
		if (quantity.contains("/") || !verifyId(quantity)) {
			return;
		} else {
			quantityInt = -Integer.parseInt(quantity);
		}

		String notes = getString("notes", input);
		if (notes.isEmpty()) {
			notes = null;
		} else if (notes.contains("/") || !verifyString(notes)) {
			return;
		}

		InventoryRecord rec = new InventoryRecord.InventoryRecordBuilder(idInt, quantityInt).notes(notes).build();
		Product result = null;

		if ((quantityInt < 0) && (getQuantity(idInt) + quantityInt < 0)) {
			System.out.println(" low stock !");
			System.out.println(" stock not removed !");
			return;
		}

		try {
			result = InventoryManager.updateStock(rec);
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getLocalizedMessage());
		}

		if (result != null) {
			System.out.println(" removed !");
			printStatus(result);
		} else {
			System.out.println(" error , please try again !");
		}

	}

	public static void getStockStatus() {

		List<Product> records = new ArrayList<>();

		try {
			records = InventoryManager.productStock();
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getLocalizedMessage());
		}

		if (!records.isEmpty()) {
			printStatus(records);
		} else {
			System.out.println(" all stocks empty !");
		}

	}

	private static int getQuantity(int id) {

		Product rec = null;
		try {
			rec = InventoryManager.productStock(id);
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getLocalizedMessage());
		}

		if (rec != null) {
			return rec.getQuantityInStock();
		} else {
			return 0;
		}

	}

	private static boolean verifyId(String s) {
		if (s.isEmpty() || s.matches("\\s*")) {
			System.out.println(" Enter a value !");
			return false;
		} else if (!s.matches("\\d*")) {
			System.out.println(" Enter a positive number !");
			return false;
		} else {
			return true;
		}
	}

	private static String getString(String label, Scanner input) {
		System.out.format(" %s : ", label);
		return input.nextLine();
	}

	private static boolean verifyString(String s) {

		if (s.isEmpty() || s.matches("\\s*")) {
			System.out.println(" Enter a value !");
			return false;
		} else {
			return true;
		}
	}

	private static void printStatus(Product result) {
		System.out.format(" |%15s | %30s | %15s | %15s | | %15s |  %30s |%n%n", "id", "name", "category_id", "price",
				"total_quantity", "description");
		System.out.format(" |%15d | %30s | %15d | %15.2f | | %15d |  %30s |%n", result.getId(), result.getName(),
				result.getCategoryId(), result.getPrice(), result.getQuantityInStock(), result.getDescription());

		// id productID quantity date note
	}

	private static void printStatus(List<Product> results) {
		System.out.format(" |%15s | %30s | %15s |%n%n", "id", "name", "total_quantity");
		for (Product p : results) {
			System.out.format(" |%15d | %30s | %15d |%n", p.getId(), p.getName(), p.getQuantityInStock());
		}

	}
}
