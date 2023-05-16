package app;

import java.sql.SQLException;
import java.util.ArrayList;
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

		String name = getString("name", input);
		if (name.contains("/") || !verifyString(name)) {
			return;
		}

		Category category = new Category.CategoryBuilder(name).id(idInt).build();
		Category result = null;

		try {
			result = CategoryManager.update(category);
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getLocalizedMessage());
		}

		if (result != null) {
			printCategory(result);
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
		if (id.contains("/") || !verifyId(id)) {
			return;
		} else {
			idInt = Integer.parseInt(id);
		}

		Category result = null;
		try {
			result = CategoryManager.delete(idInt);
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getLocalizedMessage());
		}

		if (result != null) {
			printCategory(result);
			System.out.println(" deleted !");
		} else {
			System.out.println(" error , please try again !");
		}
	}

	public static boolean search(Scanner input) {

		System.out.println(" insert '/' to cancel operation");

		String noid = getString("name or id", input);
		if (noid.contains("/") || !verifyString(noid)) {
			return false;
		}
		if (noid.matches("\\d*")) {
			return searchInt(Integer.parseInt(noid));
		} else {
			return searchString(noid);
		}

	}

	private static boolean searchString(String name) {

		List<Category> categories = new ArrayList<>();
		try {
			categories = CategoryManager.search(name);
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getLocalizedMessage());
		}

		if (!categories.isEmpty()) {
			printCategory(categories);
			return true;
		} else {
			System.out.println(" not found !");
			return false;
		}
	}

	private static boolean searchInt(int id) {
		Category c = null;
		try {
			c = CategoryManager.search(id);
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getLocalizedMessage());
		}

		if (c != null) {
			printCategory(c);
			return true;
		} else {
			System.out.println(" not found !");
			return false;
		}
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
			System.out.println(" Enter a value !");
			return false;
		} else {
			return true;
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
}
