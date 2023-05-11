package app;

import java.util.Scanner;

public class App {

	static final String NO_ARG = "no argument !";
	static final String INV_ARG = "invalid argument !";

	public static void main(String[] args) {

		String mainMenuString = "%n------------ commands ------------%n%n";
		mainMenuString += " insert -[p,c,s]%n";
		mainMenuString += " update -[p,c]%n";
		mainMenuString += " delete -[p,c,s]%n";
		mainMenuString += " search -[p,c]%n";
		mainMenuString += " status%n";
		mainMenuString += " exit%n";
		mainMenuString += "                  p - product%n";
		mainMenuString += "                  c - category%n";
		mainMenuString += "                  s - stock%n";

		boolean exitStatus = false;
		Scanner input = new Scanner(System.in);
		System.out.format(mainMenuString);

		do {

			System.out.print(">");
			String[] inputStrings = input.nextLine().split(" -");
			String command = inputStrings[0];

			switch (command) {

			case "":
				break;

			case "insert":
				if (inputStrings.length == 1) {
					System.out.println(NO_ARG);
					break;
				}
				insert(inputStrings[1], input);
				break;

			case "update":
				if (inputStrings.length == 1) {
					System.out.println(NO_ARG);
					break;
				}
				update(inputStrings[1], input);
				break;

			case "search":
				if (inputStrings.length == 1) {
					System.out.println(NO_ARG);
					break;
				}
				search(inputStrings[1], input);
				break;

			case "delete":
				if (inputStrings.length == 1) {
					System.out.println(NO_ARG);
					break;
				}
				delete(inputStrings[1], input);
				break;

			case "status":
				InventoryUI.getStockStatus();
				break;

			case "exit":
				System.out.println("bye");
				exitStatus = true;
				break;

			default:
				System.out.println("invalid command !");
				break;
			}

		} while (!exitStatus);

		input.close();

	}

	private static void delete(String arg, Scanner input) {
		if ("p".equals(arg)) {
			ProductUI.delete(input);
		} else if ("c".equals(arg)) {
			CategoryUI.delete(input);
		} else if ("s".equals(arg)) {
			InventoryUI.delete(input);
		} else {
			System.out.println(INV_ARG);
		}

	}

	private static void search(String arg, Scanner input) {
		if ("p".equals(arg)) {
			ProductUI.search(input);
		} else if ("c".equals(arg)) {
			CategoryUI.search(input);
		} else {
			System.out.println(INV_ARG);
		}

	}

	private static void update(String arg, Scanner input) {
		if ("p".equals(arg)) {
			ProductUI.update(input);
		} else if ("c".equals(arg)) {
			CategoryUI.update(input);
		} else {
			System.out.println(INV_ARG);
		}
	}

	private static void insert(String arg, Scanner input) {
		if ("p".equals(arg)) {
			ProductUI.insert(input);
		} else if ("c".equals(arg)) {
			CategoryUI.insert(input);
		} else if ("s".equals(arg)) {
			InventoryUI.insert(input);
		} else {
			System.out.println(INV_ARG);
		}
	}

}
