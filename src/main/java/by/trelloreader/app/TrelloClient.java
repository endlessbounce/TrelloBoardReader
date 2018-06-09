package by.trelloreader.app;

import by.trelloreader.constant.AppConst;
import by.trelloreader.exception.ThreadDefaultHandler;
import by.trelloreader.reader.DataFileReader;
import by.trelloreader.restcall.RestCaller;
import by.trelloreader.writer.DataFileWriter;

import java.util.stream.Collectors;

public class TrelloClient {

    // Constants ----------------------------------------------------------------------------------
    private static final String EXTENSION = ".txt";

    // Vars ---------------------------------------------------------------------------------------
    private static RestCaller restCaller;
    private static String outputPath;

    // Actions ------------------------------------------------------------------------------------

    /**
     * Reads user input of path to write to and file to read from.
     * Reads properties file and processes each board by its ID.
     *
     * @param args
     */
    public static void main(String[] args) {
        Thread.currentThread().setUncaughtExceptionHandler(new ThreadDefaultHandler());
        printIntro();

        System.out.println("Please enter output path (excluding file name): ");
        outputPath = DataFileReader.readData();

        System.out.println("Please enter valid full path name for a file to read: ");
        DataFileReader.readProperties().ifPresent(properties -> {
            System.out.println("Loading data...");
            restCaller = new RestCaller(properties.getProperty(AppConst.KEY), properties.getProperty(AppConst.TOKEN));
            properties.stringPropertyNames()
                    .stream()
                    .filter(s -> !AppConst.KEY.equals(s) && !AppConst.TOKEN.equals(s))
                    .map(s -> properties.getProperty(s))
                    .forEach(s -> processBoard(s));
        });
    }

    /**
     * Fetches lists of cards from a board and then writes it into a file
     *
     * @param boardId ID of a board to fetch info from
     */
    private static void processBoard(String boardId) {
        String writePath = new StringBuilder().append(outputPath)
                .append(restCaller.fetchBoardName(boardId).replaceAll("\\.|/", ""))
                .append(EXTENSION)
                .toString();

        String data = restCaller.fetchLists(boardId)
                .stream()
                .map(trelloList -> trelloList.toString())
                .collect(Collectors.joining());

        DataFileWriter.writeIntoFile(writePath, data);
    }

    /**
     * Usage information about this program.
     */
    private static void printIntro() {
        System.out.println(new StringBuilder()
                .append("**************************************************\n")
                .append("Trello.com data reader by endlessbouce. V1.0-2018/06/08\n")
                .append("**************************************************\n")
                .append("The program requires from you to provide a *.properties file.\n")
                .append("It fetches cards (names, descriptions and comments) of all lists from boards listed in the file,\n")
                .append("and writes it into a file, destination of which you specify.\n")
                .append("The file must contain your key and token to access necessary account (key=***, token=***),\n")
                .append("and a list of boards' IDs (e.g. board1=***).\n")
                .append("**************************************************\n"));
    }
}
