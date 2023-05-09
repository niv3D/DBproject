package app;

import java.sql.SQLException;
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
		// TODO document why this method is empty
	}
}
