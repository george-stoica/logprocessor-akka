package logprocessor.service.monitor;

import akka.actor.Props;

/**
 * Created by munca on 17/7/2016.
 */
public class LogFileParserMonitor extends Monitor {
    public static Props props() {
        return Props.create(LogFileParserMonitor.class, () -> new LogFileParserMonitor());
    }

    protected void cleanup() {
        context().system().shutdown();
    }
}
