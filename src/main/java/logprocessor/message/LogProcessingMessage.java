package logprocessor.message;

import java.io.Serializable;

/**
 * Created by georgestoica on 15/7/16.
 */
public class LogProcessingMessage {
    public static class Scan implements Message {
        private String fileName;

        public Scan(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
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
}
