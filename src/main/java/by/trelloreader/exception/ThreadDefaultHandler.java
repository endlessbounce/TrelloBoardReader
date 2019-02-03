package by.trelloreader.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A handler for uncaught exceptions. Simply makes a log.
 * Used as a Thread's uncaught exception handler.
 */
public class ThreadDefaultHandler implements Thread.UncaughtExceptionHandler {

    // Constants ----------------------------------------------------------------------------------
    private final static Logger LOGGER = LogManager.getLogger();

    // Actions ------------------------------------------------------------------------------------
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LOGGER.error("Uncaught Exception:  ", e);
    }
}
