package by.trellohacker.writer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class to write data into a file
 */
public class DataFileWriter {

    // Constants ----------------------------------------------------------------------------------
    private final static Logger LOGGER = LogManager.getLogger();

    // Actions ------------------------------------------------------------------------------------

    /**
     * Writes data fetched from Trello into a file
     *
     * @param writePath a path to create file and write into
     * @param data      to write
     */
    public static void writeIntoFile(String writePath, String data) {

        System.out.println("Writing to " + writePath + "...");

        try (FileWriter writer = new FileWriter(new File(writePath))) {
            writer.write(data);
            writer.flush();
        } catch (IOException e) {
            LOGGER.error("Error while writing to path: " + writePath, e);
        }
    }
}
