package fr._42.blinnea.computorv1;

import java.util.logging.*;

public interface Loggable {
    Logger logger = Logger.getLogger("simpleFrame");

    static void setupDefaultLogger() {
        if (System.getProperty("java.util.logging.config.class") == null
                && System.getProperty("java.util.logging.config.file") == null)
        {
            logger.setLevel(Level.ALL);
            Handler handler = new Handler(){
                @Override
                public void publish(LogRecord record)
                {
                    if (getFormatter() == null) setFormatter(new SimpleFormatter());
                    String message = getFormatter().format(record);
                    if (record.getLevel().intValue() >= Level.WARNING.intValue())
                        System.err.print(message);
                    else
                        System.out.print(message);
                }

                @Override
                public void close() {
                    flush();
                }

                @Override
                public void flush() {
                    System.err.flush();
                    System.out.flush();
                }
            };
            logger.addHandler(handler);
        }
    }
}