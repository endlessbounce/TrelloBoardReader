package by.trelloreader.reader;

import by.trelloreader.constant.AppConst;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.Scanner;

/**
 * Class to read user input data
 */
public class DataFileReader {
	
	private static Scanner scanner = new Scanner(System.in); 
	
	/**
	 * Reads the properties file specified by a user
	 *
	 * @return optional of properties if data was read successfully or an empty
	 *         Optional
	 */
	public static Optional<Properties> readProperties() {
		int attempts = 5;
		Properties properties = new Properties();
		Optional<Properties> result = Optional.empty();

		while (!result.isPresent() && attempts > 0) {

			try {
				properties.load(new FileInputStream(readData()));
				System.out.println("Reading file...");
				result = Optional.of(properties);
			} catch (IOException e) {
				System.out.println(
						"Error while reading the file. Please specify another file.\nAttempts left: " + --attempts);

				if (attempts == 0) {
					System.out.println("Exiting application.");
				}
			}

		}

		return result;
	}

	/**
	 * Simple reader of user's input data
	 *
	 * @return input data
	 */
	public static String readData() {
        String input = AppConst.EMPTY;

        while (input.trim().isEmpty()) {
            input = scanner.nextLine();
        }

        return input;
	}
	
	public static void close() {
		scanner.close();
	}
}
