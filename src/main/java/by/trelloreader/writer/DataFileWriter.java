package by.trelloreader.writer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

	public static void writeToOutputStream(OutputStream os, InputStream is) {
		byte[] buffer = new byte[1024];
		int length;
		try {
			while ((length = is.read(buffer)) != -1) {
				os.write(buffer, 0, length);
			}
		} catch (IOException e) {
			LOGGER.error(e);
			System.out.println("Couldn't write to OutputStream.");
		}
	}
}
