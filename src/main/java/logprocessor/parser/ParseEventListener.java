package logprocessor.parser;

/**
 * Lambda for parse events
 */
@FunctionalInterface
public interface ParseEventListener {
    void fireEvent(String fileName, ParseEvent e);
}
