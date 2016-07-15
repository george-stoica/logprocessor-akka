package logprocessor.service;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import logprocessor.message.LogProcessingMessage;
import logprocessor.message.Message;
import logprocessor.parser.LogFileParser;
import logprocessor.parser.ParseEvent;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * Created by georgestoica on 15/7/16.
 */
public class FileParser extends AbstractActor {
    private String fileToParse;

    static Props props(String fileToParse) {
        return Props.create(FileParser.class, () -> new FileParser(fileToParse));
    }

    public FileParser(String fileToParse) {
        this.fileToParse = fileToParse;
    }

    public PartialFunction<Object, BoxedUnit> receive() {
        return ReceiveBuilder.match(LogProcessingMessage.Parse.class, message -> {
            fileToParse = message.getFileName();
            System.out.println(message.getFileName());

            // start parsing file
            LogFileParser logFileParser = new LogFileParser();
            logFileParser.parse(message.getFileName(), (fileName, parseEvent) -> {
                //prepare message
                Message parseEventMessage = mapEvent(fileName, parseEvent);

                // send data to aggregator
                ActorRef aggregator = context().actorOf(Aggregator.props(), "aggregator");
                aggregator.tell(parseEventMessage, self());
            });

        }).build();
    }

    public String getFileToParse() {
        return fileToParse;
    }

    private Message mapEvent(String fileName, ParseEvent evt) {
        Message msg;

        switch(evt) {
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
