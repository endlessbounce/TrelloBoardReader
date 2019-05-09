package by.trelloreader.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class FileUtil {

	private final static Logger LOGGER = LogManager.getLogger();

	/*
	 * Files.walk() returns a Stream of Path that we sort in reverse order. This
	 * places the paths denoting the contents of directories before directories
	 * itself. Thereafter it maps Path to File and deletes each File.
	 * https://www.baeldung.com/java-delete-directory
	 */
	public static void deleteFolder(String outputPath) {
		try {
			Files.walk(Paths.get(outputPath))
					.sorted(Comparator.reverseOrder())
					.map(Path::toFile)
					.forEach(File::delete);
		} catch (IOException e) {
			LOGGER.error("Couldn't delete folder: " + outputPath, e);
			System.out.println("Couldn't delete folder: " + outputPath);
		}
	}

	public static File newDirectory(String path) {
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
		return f;
	}

	public static File newFile(String path) {
		File f = new File(path);
		f.getParentFile()
				.mkdirs();
		return f;
	}
}
