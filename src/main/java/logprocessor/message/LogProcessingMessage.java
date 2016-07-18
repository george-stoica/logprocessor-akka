package logprocessor.message;

import akka.actor.ActorRef;

/**
 * Messages passed between actors
 */
public class LogProcessingMessage {
    // File Parsing Messages

    public static class Scan implements Message {
        private String dirName;

        public Scan(String dirName) {
            this.dirName = dirName;
        }

        public String getDirName() {
            return dirName;
        }
    }

    public static class Parse implements Message {
        private String fileName;

        public Parse(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }
    }

    public static class LogDetails implements Message {
        private int lineCount;
        private String fileName;

        public LogDetails(String fileName, int lineCount) {
            this.fileName = fileName;
            this.lineCount = lineCount;
        }

        public int getLineCount() {
            return lineCount;
        }

        public String getFileName() {
            return fileName;
        }
    }

    public static class StartOfFile implements Message {
        private String fileName;

        public StartOfFile(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }
    }

    public static class Line implements Message {
        private String fileName;

        public Line(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }
    }

    public static class EndOfFile implements Message {
        private String fileName;

        public EndOfFile(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }
    }

    public static class ParseError implements Message {
        private String fileName;

        public ParseError(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }
    }

    // Actor status messages

    public static class MonitorOperation implements Message {
        private ActorRef ref;

        public MonitorOperation(ActorRef ref) {
            this.ref = ref;
        }

        public ActorRef getRef() {
            return ref;
        }
    }

    public static class StoppedOperation implements Message {
        private ActorRef ref;

        public StoppedOperation(ActorRef ref) {
            this.ref = ref;
        }

        public ActorRef getRef() {
            return ref;
        }
    }
}
