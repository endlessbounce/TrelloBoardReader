package by.trelloreader.app;

import java.net.URI;
import java.net.URISyntaxException;

import by.trelloreader.boardprocessor.BoardReader;
import by.trelloreader.exception.ThreadDefaultHandler;
import by.trelloreader.reader.DataFileReader;

public class TrelloClient {

	/**
	 * Reads user input of path to write to and file to read from. Reads properties
	 * file and processes each board by its ID.
	 *
	 * @param args
	 * @throws URISyntaxException 
	 */
	public static void main(String[] args) throws URISyntaxException {
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
	 * @throws URISyntaxException
	 */
	private static void printIntro() throws URISyntaxException {
		URI uri = TrelloClient.class.getClassLoader()
				.getResource("readme.txt")
				.toURI()
				.normalize();
		System.out.println(DataFileReader.readFile(uri));
	}
}
