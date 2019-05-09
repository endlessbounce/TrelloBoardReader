package by.trelloreader.app;

import java.io.IOException;
import java.io.InputStream;

import by.trelloreader.boardprocessor.BoardReader;
import by.trelloreader.exception.ThreadDefaultHandler;
import by.trelloreader.reader.DataFileReader;

public class TrelloClient {

	/**
	 * Reads user input of path to write to and file to read from. Reads properties
	 * file and processes each board by its ID.
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Thread.currentThread()
				.setUncaughtExceptionHandler(new ThreadDefaultHandler());
		printIntro();
		System.out.println("Please enter the path name of a properties file: ");

		String propertiesFilePath;
		BoardReader trelloReader = new BoardReader();
		do {
			propertiesFilePath = DataFileReader.readData();
		} while (!trelloReader.init(propertiesFilePath));
		DataFileReader.close();

		trelloReader.read();
		System.out.println("Job done.");
	}

	/**
	 * Usage information about this program.
	 * 
	 * @throws IOException
	 */
	private static void printIntro() throws IOException {
		InputStream is = TrelloClient.class.getClassLoader()
				.getResourceAsStream("readme.txt");
		if (is != null) {
			System.out.println(DataFileReader.readInputStream(is));
			is.close();
		}
	}
}
