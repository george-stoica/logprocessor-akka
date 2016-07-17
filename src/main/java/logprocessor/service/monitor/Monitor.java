package logprocessor.service.monitor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import logprocessor.message.LogProcessingMessage;
import logprocessor.service.FileScanner;
import org.apache.log4j.Logger;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by munca on 17/7/2016.
 */
public abstract class Monitor extends AbstractActor {
    private final static Logger logger = Logger.getLogger(FileScanner.class);

    private List<ActorRef> watched = new ArrayList<>();

    /**
     * Do cleanup once all the actors finished their work
     */
    protected abstract void cleanup();

    public PartialFunction<Object, BoxedUnit> receive() {
        return ReceiveBuilder
                .match(LogProcessingMessage.MonitorOperation.class, message -> {
                    logger.debug("Start monitoring " + message.getRef().toString());
                    watched.add(message.getRef());
                })
                .match(LogProcessingMessage.StoppedOperation.class, message -> {
                    // remove actor from monitoring
                    watched.remove(message.getRef());
                    logger.debug("Removed " + message.getRef().toString() + " from monitor");

                    // if no other actors are active shutdown the system
                    if (watched.isEmpty()) {
                        cleanup();
                        logger.debug("Cleanup...");
                    }
                })
                .build();
    }
}
