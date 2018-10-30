package engine.compiler;

import java.util.*;

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
        expression = expression.trim();
        Deque<Character> stack = new ArrayDeque<>();
        List<Character> traverse = new LinkedList<>();
        for (char c : expression.toCharArray()) {
            if (Character.isWhitespace(c) || c == ',') {
                continue;
            }
            traverse.add(c);
        }
        StringBuilder sb = new StringBuilder();
        for (ListIterator<Character> iter = traverse.listIterator(); iter.hasNext(); ) {
            char c = iter.next();
            iter.previous();
            char last = '\0';
            if (iter.hasPrevious()) {
                last = iter.previous();
                iter.next();
                iter.next();
            } else {
                iter.next();
            }
            if (c == '(' || c == '[' || c == '{') {
                /*if (last != '\n') {
                    sb.append('\n');
                }
                sb.append(charMultiplier('\t', stack.size()));
                sb.append(c);*/
                stack.push(c);
            } else if (c == ')' || c == ']' || c == '}') {
                stack.pop();
                /*if (last != '\n') {
                    sb.append('\n');
                }
                sb.append(charMultiplier('\t', stack.size()));
                sb.append(c);*/
            } else if (Character.isWhitespace(c) || c == ',') {
                continue;
            } else {
                if (last == '\n') {
                    sb.append(charMultiplier('\t', stack.size()));
                } else if (last == '[' || last == ']' || last == '(' || last == ')' || last == '{' || last == '}') {
                    sb.append('\n');
                    sb.append(charMultiplier('\t', stack.size()));
                }
                sb.append(c);
            }
        }
        String raw = sb.toString();
        return removeTrails(raw);
    }

    /**
     * This method removes the newlines in front and back. It also removes indentation that all lines have in common.
     *
     * @param raw: The input raw String.
     * @return A processed String.
     */
    private static String removeTrails(String raw) {
        raw = raw.replaceAll("\\s+$", "");
        raw = raw.replaceAll("^\\n+", "");
        String[] lines = raw.split("\n");
        int min = Integer.MAX_VALUE;
        for (String line : lines) {
            int numOfTabs = line.length() - line.replaceAll("^\t+", "").length();
            if (numOfTabs < min) {
                min = numOfTabs;
            }
        }
        String ret = "";
        for (String line : lines) {
            ret += line.substring(min) +"\n";
        }
        ret = ret.substring(0, ret.length() - 1);
        return ret;
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
