package logprocessor.parser;

/**
 * Created by georgestoica on 15/7/16.
 */
public class LogFileParser implements Parser {
    public void parse(String fileName, ParseEventListener eventListener) {
        eventListener.fireEvent(fileName, ParseEvent.START);
    }
}
