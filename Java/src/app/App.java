package app;

import java.util.Scanner;

public class App {

	public static void main(String[] args) {

		String mainMenuString = "%n------------ commands ------------%n%n";
		mainMenuString += " insert -[p,c]%n";
		mainMenuString += " update -[p,c]%n";
		mainMenuString += " delete -[p,c]%n";
		mainMenuString += " search -[p,c]%n";
		mainMenuString += " exit%n";
		mainMenuString += "                  p - product%n";
		mainMenuString += "                  c - category%n";

		boolean exitStatus = false;
		Scanner input = new Scanner(System.in);
		System.out.format(mainMenuString);

		do {

			System.out.format(">");
			String[] inputStrings = input.nextLine().split(" -");
			String command = inputStrings[0];

			switch (command) {

			case "":
				break;

			case "insert":
				if (inputStrings.length == 1) {
					System.out.println("no argument !");
					break;
				}
				insert(inputStrings[1], input);
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

	private static void insert(String arg, Scanner input) {
		if ("p".equals(arg)) {
			ProductUI.insert(input);
		} else if ("c".equals(arg)) {
			CategoryUI.insert(input);
		} else {
			System.out.println("invalid argument !");
		}
	}

}
