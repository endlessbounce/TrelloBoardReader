package by.trelloreader.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class to write data into a file
 */
public class DataFileWriter {

    // Actions ------------------------------------------------------------------------------------

    /**
     * Writes data fetched from Trello into a file
     *
     * @param writePath a path to create file and write into
     * @param data      to write
     * @throws IOException 
     */
    public static void writeToFile(String writePath, String data) throws IOException {

        System.out.println("Writing to " + writePath + "...");

        try (FileWriter writer = new FileWriter(new File(writePath))) {
            writer.write(data);
            writer.flush();
        }
    }
}
