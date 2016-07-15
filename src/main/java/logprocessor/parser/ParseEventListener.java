package logprocessor.parser;

/**
 * Created by georgestoica on 15/7/16.
 */
@FunctionalInterface
public interface ParseEventListener {
    void fireEvent(String fileName, ParseEvent e);
}
