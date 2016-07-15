package logprocessor.service;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import logprocessor.message.LogProcessingMessage;
import logprocessor.message.Message;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * Created by georgestoica on 15/7/16.
 */
public class FileScanner extends AbstractActor {
    public PartialFunction<Object, BoxedUnit> receive() {
        return ReceiveBuilder.match(LogProcessingMessage.Scan.class, message -> {
            // prepare message
            Message parseFileMessage = new LogProcessingMessage.Parse(message.getFileName());

            // create a Parser Actor
            ActorRef fileParser = context().actorOf(FileParser.props(message.getFileName()), "fileParser");
            fileParser.tell(parseFileMessage, self());

        }).build();
    }
}
