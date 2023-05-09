package app;

import java.sql.SQLException;
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

	private static Product getProduct(String name, String categoryId, String price, String description) {

		if (name.isEmpty()) {
			return null;
		}

		Integer cid;
		if (categoryId.matches("\\d")) {
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
