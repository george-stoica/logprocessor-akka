package logprocessor.message;

import java.io.Serializable;

/**
 * Created by georgestoica on 15/7/16.
 */
public class LogProcessingMessage {
    public static class Scan implements Serializable {
        private String dirPath;

        public Scan(String dirPath) {
            this.dirPath = dirPath;
        }

        public String getDirPath() {
            return dirPath;
        }
    }

    public static class Parse implements Serializable {
        private String fileName;
        public Parse(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }
    }

    public static class LogDetails implements Serializable {
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

    public static class StartOfFile implements Serializable {
        private String fileName;

        public StartOfFile(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }
    }

    public static class Line implements Serializable {
        private int number;

        public Line(int number) {
            this.number = number;
        }

        public int getLineNumber() {
            return number;
        }
    }

    public static class EndOfFile implements Serializable {

    }
}
