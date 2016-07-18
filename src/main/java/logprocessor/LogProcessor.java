package logprocessor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import logprocessor.message.LogProcessingMessage;
import logprocessor.service.Aggregator;
import logprocessor.service.FileScanner;
import logprocessor.service.monitor.LogFileParserMonitor;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Created by munca on 17/7/2016.
 */
public class LogProcessor {
    private final static Logger logger = Logger.getLogger(LogProcessor.class);

    public static void main(String[] args) {
        String logFileDir = "./logs";

        if(args.length > 0) {
            logFileDir = args[0];
        }

        // exit with error if directory does not exist
        if(!(new File(logFileDir).exists())) {
            System.out.println("Log file directory does not exist");
            System.exit(1);
        }

        logger.info("Parsing log files");

        logger.debug("creating actor system...");
        final ActorSystem system = ActorSystem.create("LogProcessor");

        // create FileScanner actor
        logger.debug("creating file scanner...");
        final ActorRef fileScanner = system.actorOf(FileScanner.props(), "fileScanner");

        // create Aggregator actor
        logger.debug("creating file aggregator...");
        final ActorRef aggregator = system.actorOf(Aggregator.props(), "aggregator");

        logger.debug("creating monitor...");
        final ActorRef monitor = system.actorOf(LogFileParserMonitor.props(), "monitor");

        // send SCAN message to the actor system
        final Inbox inbox = Inbox.create(system);

        logger.debug("start scanning for log files...");
        inbox.send(fileScanner, new LogProcessingMessage.Scan(logFileDir));
    }
}
