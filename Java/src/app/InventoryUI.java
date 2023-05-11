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
		System.out.println(" insert '/' to cancel operation");

		if (!ProductUI.search(input)) {
			return;
		}

		int idInt;
		String ProductId = getString("id", input);
		if (ProductId.contains("/") || !verifyId(ProductId)) {
			return;
		} else {
			idInt = Integer.parseInt(ProductId);
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

		InventoryRecord record = new InventoryRecord.InventoryRecordBuilder(idInt, quantityInt).notes(notes).build();
		InventoryRecord result = null;

		if((quantityInt<0)&&(getQuantity(idInt)+quantityInt<0)) {
			System.out.println(" low stock !");
		}
		
		try {
			result = InventoryManager.updateStock(record);
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getLocalizedMessage());
		}

		if (result != null) {
			printRecord(result);
			System.out.println(" added !");
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
		}else {
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
		
		if (rec!=null) {
			printRecord(rec);
			return rec.getQuantity();
		}
		else {
			return 0;
		}
		
	}

	private static boolean verifyId(String s) {
		if (s.isEmpty() || s.matches("\\s*")) {
			System.out.println(" field cant be empty !");
			return false;
		} else if (!s.matches("\\d*")) {
			System.out.println(" field must be a number !");
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
			System.out.println(" field cant be empty !");
			return false;
		} else {
			return true;
		}
	}

	private static void printRecord(InventoryRecord result) {
		System.out.format(" |%15s | %15s | %15s | %15s | %30s |%n%n", "id", "product_id", "quantity", "date", "note");
		System.out.format(" |%15d | %15d | %15d | %15s | %30s |%n", result.getId(),
				result.getProductId(), result.getQuantity(), result.getDate().toString(), result.getNotes());

		// id productID quantity date note
	}

	private static void printRecord(List<InventoryRecord> results) {
		System.out.format(" |%15s | %15s | %15s | %15s | %30s |%n%n", "id", "product_id", "quantity", "date", "note");
		for (InventoryRecord p : results) {
			System.out.format(" |%15d | %15d | %15d | %15s | %30s |%n", p.getId(), p.getProductId(),
					p.getQuantity(), p.getDate().toString(), p.getNotes());
		}

	}
}