package app;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import api.ProductManager;
import models.Product;

public class ProductUI {

	private ProductUI() {
	}

	public static void insert(Scanner input) {

		System.out.println(" insert '/' to cancel operation");

		System.out.print(" name : ");
		String name = input.nextLine();
		if (name.contains("/")) {
			return;
		}

		System.out.print(" category_id : ");
		String categoryId = input.nextLine();
		if (categoryId.contains("/")) {
			return;
		}

		System.out.print(" price : ");
		String price = input.nextLine();
		if (price.contains("/")) {
			return;
		}

		System.out.print(" description : ");
		String description = input.nextLine();
		if (description.contains("/")) {
			return;
		}

		Product product = getProduct(name, categoryId, price, description);

		if (product == null) {
			System.out.println(" input format error !");
			return;
		}

		int id = 0;

		try {
			id = ProductManager.insert(product);
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getLocalizedMessage());
		}

		if (id != 0) {
			System.out.format(" product added : id - %d ;%n", id);
		} else {
			System.out.println(" error , please try again !");
		}
	}

	public static void update(Scanner input) {
		// TODO document why this method is empty
	}

	public static void search(Scanner input) {
		System.out.println(" insert '/' to cancel operation");
		System.out.print(" name or id : ");
		String noid = input.nextLine();
		if (noid.contains("/")) {
			return;
		}

		if (noid.isEmpty() || noid.matches("\\s*")) {
			System.out.println(" input format error !");
			return;
		}

		if (noid.matches("\\d*")) {
			searchInt(noid);
		} else {
			searchString(noid);
		}

	}

	private static void searchString(String noid) {
		List<Product> products = null;
		try {
			products = ProductManager.search(noid);
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getLocalizedMessage());
		}

		if (products != null) {
			for (Product p : products) {
				System.out.format(" %4d | %-20s | %4d | %4f | %30s |%n", p.id, p.name, p.categoryId, p.price,
						p.description);
			}
		} else {
			System.out.println(" not found !");
		}
	}

	private static void searchInt(String noid) {
		int id = Integer.parseInt(noid);
		Product p = null;

		try {
			p = ProductManager.search(id);
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getLocalizedMessage());
		}

		if (p != null) {
			System.out.format(" %4d | %-20s | %4d | %4f | %30s |%n", p.id, p.name, p.categoryId, p.price,
					p.description);
		} else {
			System.out.println(" not found !");
		}
	}

	private static Product getProduct(String name, String categoryId, String price, String description) {

		if (name.isEmpty() || name.matches("\\s*")) {
			return null;
		}

		Integer cid;
		if (categoryId.matches("\\d*")) {
			cid = Integer.parseInt(categoryId);
		} else if (categoryId.isEmpty()) {
			cid = null;
		} else {
			return null;
		}

		Float p;
		if (price.isEmpty()) {
			p = null;
		} else if (price.matches("\\d*\\.?\\d*")) {
			p = Float.parseFloat(price);
		} else {
			return null;
		}

		if (description.isEmpty()) {
			description = null;
		}

		return new Product(name, cid, p, description);

	}

}
