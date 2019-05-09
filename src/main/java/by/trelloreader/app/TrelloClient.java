package by.trelloreader.app;

import java.io.IOException;
import java.io.InputStream;

import by.trelloreader.boardprocessor.BoardReader;
import by.trelloreader.exception.ThreadDefaultHandler;
import by.trelloreader.reader.DataFileReader;

public class TrelloClient {

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

	private static void printIntro() throws IOException {
		InputStream is = TrelloClient.class.getClassLoader()
				.getResourceAsStream("readme.txt");
		if (is != null) {
			System.out.println(DataFileReader.readInputStream(is));
			is.close();
		}
	}
}
