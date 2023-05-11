package app;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import api.ProductManager;
import models.Product;
import models.Product.ProductBuilder;

public class ProductUI {

	private ProductUI() {
	}

	public static void insert(Scanner input) {

		System.out.println(" insert '/' to cancel operation");

		String name = getString("name", input);
		if (name.contains("/") || !verifyString(name)) {
			return;
		}

		float priceFloat;
		String price = getString("price", input);
		if (price.contains("/") || !verifyFloat(price)) {
			return;
		} else {
			priceFloat = Float.parseFloat(price);
		}

		String description = getString("description", input);
		if (description.contains("/") || !verifyString(description)) {
			return;
		}

		int categoryIdInt;
		String categoryId = getString("[optional] categoryId", input);
		if (categoryId.isEmpty()) {
			categoryIdInt = 0;
		} else if (categoryId.contains("/") || !verifyId(categoryId)) {
			return;
		} else {
			categoryIdInt = Integer.parseInt(categoryId);
		}

		Product product = new ProductBuilder(name, priceFloat, description).categoryId(categoryIdInt).build();
		Product result = null;
		try {
			result = ProductManager.insert(product);
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getLocalizedMessage());
		}

		if (result != null) {
			printProduct(result);
			System.out.println(" added !");
		} else {
			System.out.println(" error , please try again !");
		}

	}

	public static boolean search(Scanner input) {

		System.out.println(" insert '/' to cancel operation");

		String noid = getString("name", input);
		if (noid.contains("/") || !verifyString(noid)) {
			return false;
		}

		if (noid.matches("\\d*")) {
			return searchInt(Integer.parseInt(noid));
		} else {
			return searchString(noid);
		}
	}

	public static void update(Scanner input) {

		if (!search(input)) {
			return;
		}

		int idInt;
		String id = getString("id", input);
		if (id.contains("/") || !verifyId(id)) {
			return;
		} else {
			idInt = Integer.parseInt(id);
		}

		String name = getString("[optional] name", input);
		if (name.isEmpty()) {
			name = null;
		} else if (name.contains("/") || !verifyString(name)) {
			return;
		}

		int categoryIdInt;
		String categoryId = getString("[optional] category_id", input);
		if (categoryId.isEmpty()) {
			categoryIdInt = 0;
		} else if (categoryId.contains("/") || !verifyId(categoryId)) {
			return;
		} else {
			categoryIdInt = Integer.parseInt(categoryId);
		}

		float priceFloat;

		String price = getString("[optional] price", input);
		if (price.isEmpty()) {
			priceFloat = 0;
		} else if (price.contains("/") || !verifyFloat(price)) {
			return;
		} else {
			priceFloat = Float.parseFloat(price);
		}

		String description = getString("[optional] description", input);
		if (description.isEmpty()) {
			description = null;
		} else if (description.contains("/") || !verifyString(description)) {
			return;
		}

		Product product = new ProductBuilder(name, priceFloat, description).categoryId(categoryIdInt).id(idInt).build();
		Product result = null;

		try {
			result = ProductManager.update(product);
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getLocalizedMessage());
		}

		if (result != null) {
			printProduct(result);
			System.out.println(" updated !");
		} else {
			System.out.println(" error , please try again !");
		}

	}
	
	public static void delete(Scanner input) {
		if (!search(input)) {
			return;
		}
		int idInt;
		String id = getString("id", input);
		if (id.contains("/")||!verifyId(id)) {
			return;
		}
		else {
			idInt = Integer.parseInt(id);
		}
		
		Product result=null;
		try {
			result = ProductManager.delete(idInt);
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getLocalizedMessage());
		}
		
		if (result != null) {
			printProduct(result);
			System.out.println(" deleted !");
		} else {
			System.out.println(" error , please try again !");
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

	private static boolean verifyFloat(String s) {
		if (s.isEmpty() || s.matches("\\s*")) {
			System.out.println(" field cant be empty !");
			return false;
		} else if (!s.matches("\\d*\\.?\\d*")) {
			System.out.println(" field must be a number !");
			return false;
		} else {
			return true;
		}
	}

	private static void printProduct(Product result) {
		System.out.format(" %15s | %15s | %15s | %15s | %30s |%n%n", "id", "name", "category_id", "price",
				"description");
		System.out.format(" %15d | %15s | %15d | %15.2f | %30s |%n", result.getId(), result.getName(),
				result.getCategoryId(), result.getPrice(), result.getDescription());

	}

	private static void printProduct(List<Product> results) {
		System.out.format(" %15s | %15s | %15s | %15s | %30s |%n%n", "id", "name", "category_id", "price",
				"description");
		for (Product p : results) {
			System.out.format(" %15d | %15s | %15d | %15.2f | %30s |%n", p.getId(), p.getName(), p.getCategoryId(),
					p.getPrice(), p.getDescription());
		}

	}

	private static boolean searchString(String name) {
		List<Product> products = new ArrayList<>();
		try {
			products = ProductManager.search(name);
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getLocalizedMessage());
		}

		if (!products.isEmpty()) {
			printProduct(products);
			return true;
		} else {
			System.out.println(" not found !");
			return false;
		}
	}

	private static boolean searchInt(int id) {

		Product p = null;
		try {
			p = ProductManager.search(id);
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getLocalizedMessage());
		}

		if (p != null) {
			printProduct(p);
			return true;
		} else {
			System.out.println(" not found !");
			return false;
		}
	}

}
