package logprocessor.service;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import logprocessor.message.LogProcessingMessage;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * Created by georgestoica on 15/7/16.
 */
public class FileParserService extends AbstractActor {

    public PartialFunction<Object, BoxedUnit> receive() {
        return ReceiveBuilder.match(LogProcessingMessage.Parse.class, message -> System.out.println(message)).build();
    }
}
