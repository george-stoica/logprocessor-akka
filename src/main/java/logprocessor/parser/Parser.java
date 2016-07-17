package logprocessor.parser;

/**
 * Created by georgestoica on 15/7/16.
 */
public interface Parser {
    void parse(String fileName, ParseEventListener eventListener);
}
