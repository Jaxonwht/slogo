package engine.compiler;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * This class contains a single static method that makes the String representation of an Expression prettier.
 *
 * @author Haotian Wang
 */
public class PrettierPresentation {
    /**
     * This method makes the String representation of an Expression more presentable.
     *
     * @param expression: The String representation of an Expression expression given by expression.toString()
     * @return A more readable representation of the String.
     */
    public static String prettify(String expression) {
        Deque<Character> stack = new ArrayDeque<>();
        if (expression.charAt(0) == '[' || expression.charAt(0) == '(' || expression.charAt(0) == '{') {
            expression = expression.substring(1, expression.length() - 1);
        }
        StringBuilder sb = new StringBuilder();
        for (char c : expression.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                if (sb.length() != 0 && sb.charAt(sb.length() - 1) != '\n') {
                    sb.append('\n');
                }
                sb.append(charMultiplier('\t', stack.size()));
                sb.append(c);
                sb.append('\n');
                stack.push(c);
            } else if (c == ')' || c == ']' || c == '}') {
                stack.pop();
                if (sb.length() != 0 && sb.charAt(sb.length() - 1) != '\n') {
                    sb.append('\n');
                }
                sb.append(charMultiplier('\t', stack.size()));
                sb.append(c);
                sb.append('\n');
            } else if (Character.isWhitespace(c) || c == ',') {
                continue;
            } else {
                if (sb.length() != 0 && sb.charAt(sb.length() - 1) == '\n') {
                    sb.append(charMultiplier('\t', stack.size()));
                }
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * This method returns a String by repeating a character n times. for example charMultiplier('b', 3) returns a String "bbb".
     *
     * @param c: The char to be repeated.
     * @param times: The int number of times to be repeated.
     * @return An out put String.
     */
    private static String charMultiplier(char c, int times) {
        String ret = "";
        for (int i = 0 ; i < times; i++) {
            ret += c;
        }
        return ret;
    }
}
