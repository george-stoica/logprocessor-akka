package logprocessor.service;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import logprocessor.message.LogProcessingMessage;
import org.apache.log4j.Logger;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.util.HashMap;
import java.util.Map;

/**
 * Aggregates parsed log file data
 */
public class Aggregator extends AbstractActor {
    private final static Logger logger = Logger.getLogger(Aggregator.class);

    private Map<String, Integer> processedLogs = new HashMap<>();

    public static Props props() {
        return Props.create(Aggregator.class, () -> new Aggregator());
    }

    public PartialFunction<Object, BoxedUnit> receive() {
        return ReceiveBuilder
                .match(LogProcessingMessage.StartOfFile.class, m -> {
                    logger.debug("Start aggregating data from " + m.getFileName());
                    processedLogs.put(m.getFileName(), 0);
                })
                .match(LogProcessingMessage.Line.class, m -> {
                    int currentLineNumber = processedLogs.get(m.getFileName()) + 1;
                    processedLogs.put(m.getFileName(), currentLineNumber);
                })
                .match(LogProcessingMessage.EndOfFile.class, m -> {
                    logger.debug("Final data from " + m.getFileName() + ": " + processedLogs.get(m.getFileName()) + " lines");
                    System.out.println(m.getFileName() + ": " + processedLogs.get(m.getFileName()) + " lines");
                })
                .build();
    }
}
