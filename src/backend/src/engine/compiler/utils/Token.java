package engine.compiler.utils;

/**
 * This class represents a Token object used by the
 *
 * @author Haotian Wang
 */
public class Token {
    private final String myString;
    private final String myType;

    public Token(String string, String type) {
        myString = string;
        myType = type;
    }

    public String getString() {
        return myString;
    }

    public String getType() {
        return myType;
    }

    @Override
    public String toString() { return myType + " : " + myString; }
}
