package app;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import api.CategoryManager;

public class CategoryUI {
	private CategoryUI() {
	}

	public static void insert(Scanner input) {

		System.out.println(" insert '/' to cancel operation");
		System.out.print(" name : ");
		String name = input.nextLine();
		if (name.contains("/")) {
			return;
		}

		if (name.isEmpty() || !name.matches("\\s*")) {
			System.out.println(" invalid input !");
			return;
		}

		int id = 0;

		try {
			id = CategoryManager.insert(name);
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getLocalizedMessage());
		}

		if (id != 0) {
			System.out.format(" category added : id - %d ;%n", id);
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
		List<String> categories = new ArrayList<>();
		try {
			categories = CategoryManager.search(noid);
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getLocalizedMessage());
		}

		if (!categories.isEmpty()) {
			for (String c : categories) {
				System.out.format(" %s%n", c);
			}
		} else {
			System.out.println(" not found !");
		}
	}

	private static void searchInt(String noid) {
		int id = Integer.parseInt(noid);
		String c = null;
		
		try {
			c = CategoryManager.search(id);
		} catch (SQLException e) {
			System.out.format(" %s%n", e.getLocalizedMessage());
		}
		
		if(c!=null) {
			System.out.format(" %s%n", c);
		}else {
			System.out.println(" not found !");
		}

	}
}
