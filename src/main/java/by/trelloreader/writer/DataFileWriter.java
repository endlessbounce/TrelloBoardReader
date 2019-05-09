package by.trelloreader.writer;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.trelloreader.util.FileUtil;

/**
 * Class to write data into a file
 */
public class DataFileWriter {

	private final static Logger LOGGER = LogManager.getLogger();

	/**
	 * Writes data fetched from Trello into a file
	 *
	 * @param writePath a path to create file and write into
	 * @param data      to write
	 * @throws IOException
	 */
	public static void writeToFile(String writePath, String data) throws IOException {

		System.out.println("Writing to " + writePath + "...");

		try (FileWriter writer = new FileWriter(FileUtil.newFile(writePath))) {
			writer.write(data);
			writer.flush();
		}
	}
}
