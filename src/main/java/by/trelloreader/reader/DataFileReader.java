package by.trelloreader.reader;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.trelloreader.constant.AppConst;
import by.trelloreader.writer.DataFileWriter;

/**
 * Class to read user input data
 */
public class DataFileReader {
	private final static Logger LOGGER = LogManager.getLogger();
	private static Scanner scanner = new Scanner(System.in);

	// https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java
	public static String readInputStream(InputStream is) {
		try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			DataFileWriter.writeToOutputStream(os, is);
			return os.toString("UTF-8");
		} catch (IOException e) {
			LOGGER.error(e);
		}
		return AppConst.EMPTY;
	}

	public static String readFile(URI uri) {
		try {
			return Files.readAllLines(Paths.get(uri))
					.stream()
					.collect(Collectors.joining("\n"));
		} catch (IOException e) {
			LOGGER.error("Couldn't read file: " + uri, e);
			System.out.println("Couldn't read file: " + uri);
		}
		return AppConst.EMPTY;
	}

	/**
	 * 
	 * @param propertiesFilePath
	 * @return
	 */
	public static Properties readProperties(String propertiesFilePath) {
		Properties properties = new Properties();

		try {
			System.out.println("Loading properties file: " + propertiesFilePath);
			properties.load(new FileInputStream(propertiesFilePath));
			System.out.println("Successfully loaded.");
			return properties;
		} catch (IOException e) {
			LOGGER.error("Error loading: " + propertiesFilePath, e);
			System.out.println("Error loading: " + propertiesFilePath + ". Please specify another file.");
		}

		return null;
	}

	/**
	 * Simple reader of user's input data
	 *
	 * @return input data
	 */
	public static String readData() {
		String input = AppConst.EMPTY;

		while (input.trim()
				.isEmpty()) {
			input = scanner.nextLine();
		}

		return input;
	}

	public static void close() {
		scanner.close();
	}
}
