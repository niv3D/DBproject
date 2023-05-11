package app;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import api.CategoryManager;
import models.Category;

public class CategoryUI {
	private CategoryUI() {
	}

	public static void insert(Scanner input) {

		System.out.println(" insert '/' to cancel operation");

		String name = getString("name", input);
		if (name.contains("/") || !verifyString(name)) {
			return;
		}

		Category category = new Category.CategoryBuilder(name).build();
		Category result = null;

		try {
			result = CategoryManager.insert(category);
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getLocalizedMessage());
		}

		if (result != null) {
			printCategory(result);
			System.out.println(" added !");
		} else {
			System.out.println(" error , please try again !");
		}

	}

	public static void update(Scanner input) {

	}

	public static boolean search(Scanner input) {

	}

	private static boolean searchString(String noid) {

	}

	private static boolean searchInt(String noid) {

	}
	
	
	private static void printCategory(Category result) {
		System.out.format(" |%15s | %15s |%n%n", "id", "name");
		System.out.format(" |%15d | %15s |%n", result.getId(), result.getName());
	}

	private static void printCategory(List<Category> results) {
		System.out.format(" |%15s | %15s |%n%n", "id", "name");
		for (Category c : results) {
			System.out.format(" |%15d | %15s |%n", c.getId(), c.getName());
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
}
