package app;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import api.InventoryManager;
import models.InventoryRecord;

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
		InventoryRecord result = null;

		try {
			result = InventoryManager.updateStock(rec);
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getMessage());
		}

		if (result != null) {
			printRecord(result);
			System.out.println(" added !");
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
		InventoryRecord result = null;

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
			printRecord(result);
			System.out.println(" removed !");
		} else {
			System.out.println(" error , please try again !");
		}

	}

	public static void getStockStatus() {

		List<InventoryRecord> records = new ArrayList<>();

		try {
			records = InventoryManager.productStock();
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getLocalizedMessage());
		}

		if (!records.isEmpty()) {
			printRecord(records);
		} else {
			System.out.println(" all stocks empty !");
		}

	}

	private static int getQuantity(int id) {

		InventoryRecord rec = null;
		try {
			rec = InventoryManager.getProductQuantity(id);
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getLocalizedMessage());
		}

		if (rec != null) {
			return rec.getQuantity();
		} else {
			return 0;
		}

	}

	private static boolean verifyId(String s) {
		if (s.isEmpty() || s.matches("\\s*")) {
			System.out.println(" Enter a value !");
			return false;
		} else if (!s.matches("\\d*")) {
			System.out.println(" Enter a number !");
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

	private static void printRecord(InventoryRecord result) {
		System.out.format(" |%15s | %15s | %15s | %15s | %30s |%n%n", "id", "product_id", "quantity", "date", "note");
		System.out.format(" |%15d | %15d | %15d | %15s | %30s |%n", result.getId(), result.getProductId(),
				result.getQuantity(), result.getDate(), result.getNotes());

		// id productID quantity date note
	}

	private static void printRecord(List<InventoryRecord> results) {
		System.out.format(" |%15s | %15s | %15s | %15s | %30s |%n%n", "id", "product_id", "quantity", "date", "note");
		for (InventoryRecord p : results) {
			System.out.format(" |%15d | %15d | %15d | %15s | %30s |%n", p.getId(), p.getProductId(), p.getQuantity(),
					p.getDate(), p.getNotes());
		}

	}
}
