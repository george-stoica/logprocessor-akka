package logprocessor.service;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import logprocessor.message.LogProcessingMessage;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by georgestoica on 15/7/16.
 */
public class Aggregator extends AbstractActor {
    private Map<String, Integer> processedLogs = new HashMap<>();

    static Props props() {
        return Props.create(Aggregator.class, () -> new Aggregator());
    }

    public PartialFunction<Object, BoxedUnit> receive() {
        return ReceiveBuilder
                .match(LogProcessingMessage.StartOfFile.class, m -> processedLogs.put(m.getFileName(), 0))
                .match(LogProcessingMessage.Line.class, m -> {
                    int currentLineNumber = processedLogs.get(m.getFileName()) + 1;
                    processedLogs.put(m.getFileName(), currentLineNumber);
                })
                .match(LogProcessingMessage.EndOfFile.class, m ->
                    System.out.println(m.getFileName() + ": " + processedLogs.get(m.getFileName()) + " lines")
                )
                .build();
    }
}
