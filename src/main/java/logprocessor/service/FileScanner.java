package logprocessor.service;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import logprocessor.message.LogProcessingMessage;
import logprocessor.message.Message;
import org.apache.log4j.Logger;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Scans given directory for files and starts parsing
 */
public class FileScanner extends AbstractActor {
    private final static Logger logger = Logger.getLogger(FileScanner.class);

    public static Props props() {
        return Props.create(FileScanner.class, () -> new FileScanner());
    }

    public PartialFunction<Object, BoxedUnit> receive() {
        return ReceiveBuilder.match(LogProcessingMessage.Scan.class, message -> {
            logger.debug("Scan request for " + message.getDirName());

            List<String> filesToParse = this.scanDirectory(message.getDirName());

            for (String fileToParse : filesToParse) {
                // create a Parser Actor
                ActorRef fileParser = context().actorOf(FileParser.props(fileToParse));

                // prepare message
                Message parseFileMessage = new LogProcessingMessage.Parse(message.getDirName() + '/' + fileToParse);

                logger.debug("Start parsing file: " + fileToParse);
                fileParser.tell(parseFileMessage, self());
            }

        }).build();
    }

    private List<String> scanDirectory(String dirName) {
        List<String> files = new ArrayList<>();

        Path scanDir = Paths.get(dirName);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(scanDir)) {
            for (Path file : stream) {
                files.add(file.getFileName().toString());
            }
        } catch (IOException | DirectoryIteratorException e) {

        }

        return files;
    }
}
