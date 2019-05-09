package by.trelloreader.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.trelloreader.writer.DataFileWriter;

// Zip Utils
public final class ZipUtil {
	private final static Logger LOGGER = LogManager.getLogger();

	public static void zipMultipleFiles(List<String> srcFiles, String outputZip) {
		try (FileOutputStream fos = new FileOutputStream(outputZip)) {
			ZipOutputStream zipOut = new ZipOutputStream(fos);
			for (String srcFile : srcFiles) {
				File fileToZip = new File(srcFile);
				FileInputStream fis = new FileInputStream(fileToZip);
				ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
				zipOut.putNextEntry(zipEntry);

				DataFileWriter.writeToOutputStream(zipOut, fis);
				fis.close();
			}
			zipOut.close();
		} catch (IOException e) {
			LOGGER.error("Couldn't zip.", e);
			System.out.println("Couldn't zip.");
		}

	}
}
