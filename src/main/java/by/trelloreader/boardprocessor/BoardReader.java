package by.trelloreader.boardprocessor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import by.trelloreader.entity.TrelloList;
import by.trelloreader.reader.DataFileReader;
import by.trelloreader.restcall.RestCaller;
import by.trelloreader.util.FileUtil;
import by.trelloreader.util.StringUtil;
import by.trelloreader.util.ZipUtil;
import by.trelloreader.writer.DataFileWriter;

public class BoardReader {
	private final static Logger LOGGER = LogManager.getLogger();
	private static ObjectMapper mapper = new ObjectMapper();

	private boolean isInited;
	private RestCaller restCaller;
	private String outputZipPath;
	private String tempFolderName;
	private Properties properties;

	private int invalidKeys;

	public boolean init(String propertiesFilePath) {
		properties = DataFileReader.readProperties(propertiesFilePath);

		if (properties == null) {
			return false;
		}

		System.out.println("Initializing...");

		String key = properties.getProperty("app_key");
		String token = properties.getProperty("app_token");
		outputZipPath = properties.getProperty("app_output_path");
		if (StringUtil.isAnyEmpty(key, token, outputZipPath)) {
			System.out.println("Initialization failed. Incorrect Key, Token or OutputPath.");
			return false;
		}

		File parent = new File(outputZipPath).getParentFile();
		parent.mkdirs();

		tempFolderName = parent.getAbsolutePath() + "/" + String.valueOf(System.nanoTime());
		File temp = new File(tempFolderName);
		temp.mkdir();

		restCaller = new RestCaller(key, token);
		isInited = true;
		System.out.println("Successfully initialized.");
		return true;
	}

	public void read() {
		if (!isInited) {
			throw new IllegalArgumentException("The Reader wasn't initialized.");
		}

		System.out.println("Reading...");

		Set<String> keys = properties.stringPropertyNames();
		List<String> paths = new ArrayList<>();
		for (String key : keys) {
			if (StringUtil.isEmpty(key)) {
				invalidKeys++;
				continue;
			}
			if (key.startsWith("app_")) {
				continue;
			}

			String boardId = properties.getProperty(key);
			String writePath = processBoard(boardId);
			paths.add(writePath);
		}

		if (invalidKeys > 0) {
			System.out.println("Invalid keys: " + invalidKeys);
		}

		ZipUtil.zipMultipleFiles(paths, outputZipPath);
		FileUtil.deleteFolder(tempFolderName);
	}

	/**
	 * Fetches lists of cards from a board and then writes it into a file
	 *
	 * @param boardId ID of a board to fetch info from
	 */
	private String processBoard(String boardId) {
		String boardName = restCaller.fetchBoardName(boardId)
				.replaceAll("\\.|/", "");

		String writePath = new StringBuilder().append(tempFolderName)
				.append("/")
				.append(boardName)
				.append(".json")
				.toString();

		List<TrelloList> list = restCaller.fetchLists(boardId);

		try {
			DataFileWriter.writeToFile(writePath, mapper.writeValueAsString(list));
		} catch (Exception e) {
			LOGGER.error("Error while writing to path: " + writePath, e);
		}

		return writePath;
	}
}