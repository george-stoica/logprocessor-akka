package logprocessor.parser;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Log files parser. Emits START, LINE and EOF events
 */
public class LogFileParser implements Parser {
    private final static Logger logger = Logger.getLogger(LogFileParser.class);

    public void parse(String fileName, ParseEventListener eventListener) {
        eventListener.fireEvent(fileName, ParseEvent.START);

        logger.debug("Start parsing file: " + fileName);

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(line -> eventListener.fireEvent(fileName, ParseEvent.LINE));
        } catch (IOException e) {
            logger.error("PARSE ERROR: Error parsing " + fileName);
        }

        eventListener.fireEvent(fileName, ParseEvent.EOF);

        logger.debug("Finished parsing " + fileName);
    }
}
