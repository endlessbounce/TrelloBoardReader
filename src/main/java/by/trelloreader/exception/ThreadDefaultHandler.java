package by.trelloreader.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadDefaultHandler implements Thread.UncaughtExceptionHandler {

    // Constants ----------------------------------------------------------------------------------
    private final static Logger LOGGER = LogManager.getLogger();

    // Actions ------------------------------------------------------------------------------------
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LOGGER.error("Uncaught Exception:  ", e);
    }
}
