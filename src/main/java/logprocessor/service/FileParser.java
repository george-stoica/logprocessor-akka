package logprocessor.service;

import akka.actor.*;
import akka.japi.pf.ReceiveBuilder;
import logprocessor.message.LogProcessingMessage;
import logprocessor.message.Message;
import logprocessor.parser.LogFileParser;
import logprocessor.parser.ParseEvent;
import logprocessor.parser.Parser;
import org.apache.log4j.Logger;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * Parses files
 */
public class FileParser extends AbstractActor {
    private final static Logger logger = Logger.getLogger(FileParser.class);

    private ActorRef aggregator;
    private ActorSelection monitor;
    private Parser parser;
    private String fileToParse;

    static Props props(String fileToParse) {
        return Props.create(FileParser.class, () -> new FileParser(fileToParse, new LogFileParser()));
    }

    public FileParser(String fileToParse, Parser parser) {
        this.parser = parser;
        this.fileToParse = fileToParse;

//        ActorSelection aggregator = context().actorSelection("/user/aggregator");
//        aggregator.tell(new Identify(1), null);

        monitor = context().actorSelection("/user/monitor");
        monitor.tell(new LogProcessingMessage.MonitorOperation(self()), self());
    }

    public PartialFunction<Object, BoxedUnit> receive() {
        return ReceiveBuilder
                .match(LogProcessingMessage.Parse.class, message -> {
                    logger.debug("Parse message received for " + message.getFileName());

                    fileToParse = message.getFileName();

                    // start parsing file
                    this.parser.parse(message.getFileName(), (fileName, parseEvent) -> {
                        //prepare message
                        Message parseEventMessage = mapEvent(fileName, parseEvent);

                        // send data to aggregator
                        ActorSelection aggregator = context().actorSelection("/user/aggregator");

                        // check if aggregator exists
                        //                aggregator.tell(new Identify());

                        //                ActorRef aggregator = context().actorOf(Aggregator.props(), "aggregator");
                        aggregator.tell(parseEventMessage, self());

                        // shut down actor
                        if (parseEvent == ParseEvent.EOF) {
                            logger.debug("Finished parsing " + message.getFileName() + ". Cleanup");
//                            ActorSelection monitor = context().actorSelection("/user/monitor");
                            monitor.tell(new LogProcessingMessage.StoppedOperation(self()), self());
                            context().stop(self());
                        }
                    });

                })
                .match(ActorIdentity.class, actorIdentity -> {
                    this.aggregator = actorIdentity.getRef();
                })
                .build();
    }

    private Message mapEvent(String fileName, ParseEvent evt) {
        Message msg;

        switch (evt) {
            case START:
                msg = new LogProcessingMessage.StartOfFile(fileName);
                break;
            case LINE:
                msg = new LogProcessingMessage.Line(fileName);
                break;
            case EOF:
                msg = new LogProcessingMessage.EndOfFile(fileName);
                break;
            default:
                msg = null; // unhandled
        }

        return msg;
    }
}
