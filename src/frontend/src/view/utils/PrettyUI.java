package view.utils;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import view.CommandView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PrettyUI {
    private static final String DEFAULT_FONT = "consolas";

    private static final String CARET = "<<:";
    private static final String SPACE = " ";
    private static final String EMPTY = "";

    private static final String CONSTANT = "-?[0-9]+\\.?[0-9]*";
    private static final String VARIABLE = ":[a-zA-Z_]+";

    private static final String BG_THEME = "bg-theme";
    private static final String LIGHT_BG_THEME = "light-bg-theme";
    private static final String LIT_BG_THEME = "lit-bg-theme";
    private static final String H1 = "text-header";
    private static final String P = "text-body";

    private static final Color SYNTAX_PLAIN = Color.BLACK;
    private static final Color SYNTAX_KEYWORD = Color.valueOf("#125bd1");
    private static final Color SYNTAX_VARIABLE = Color.valueOf("#01a362");
    private static final Color SYNTAX_NUMBER = Color.valueOf("#800020");
    private static final Color SYNTAX_COMMAND = Color.valueOf("#661bad");
    private static final Color SYNTAX_COMMENT = Color.valueOf("#afafaf");
    private static final Color SYNTAX_ERROR = Color.valueOf("#d81c22");

    private static final Set<String> keywords = Language.keywords();
    private static final Set<String> commands = Language.nonKeywords();


    private static Text textWithStyle(String text, String styleClass) {
        var ret = new Text(text);
        ret.getStyleClass().add(styleClass);
        return ret;
    }

    private static List<Text> textWithColor(String text, Color color, int size) {
        var ret = new Text(text);
        ret.setFill(color);
        ret.setFont(new Font(DEFAULT_FONT, size));
        return List.of(ret);
    }

    private static List<Text> textWithColorSplit(String text, String split, Color color, int size) {
        var idx = text.indexOf(split);
        if(idx < 0) return textWithColor(text+SPACE, color, size);
        else {
            var ret = new ArrayList<Text>();
            var prefix = text.substring(0, idx);
            var suffix = text.substring(idx+split.length());
            if(prefix.length() > 0) ret.addAll(textWithColor(prefix, color, size));
            ret.addAll(textWithColor(split, SYNTAX_PLAIN, size));
            ret.addAll(textWithColor(suffix+SPACE, color, size));
            return ret;
        }
    }

    public static List<Text> plain(String text, String split, int size) {
        return textWithColorSplit(text, split, SYNTAX_PLAIN, size);
    }
    public static List<Text> keyword(String text, String split, int size) {
        return textWithColorSplit(text, split, SYNTAX_KEYWORD, size);
    }
    public static List<Text> variable(String text, String split, int size) {
        return textWithColorSplit(text, split, SYNTAX_VARIABLE, size);
    }
    public static List<Text> number(String text, String split, int size) {
        return textWithColorSplit(text, split, SYNTAX_NUMBER, size);
    }
    public static List<Text> command(String text, String split, int size) {
        return textWithColorSplit(text, split, SYNTAX_COMMAND, size);
    }
    public static List<Text> comment(String text, String split, int size) {
        return textWithColorSplit(text, split, SYNTAX_COMMENT, size);
    }
    public static List<Text> error(String text, int size) {
        return textWithColor(text, SYNTAX_ERROR, size);
    }

    public static List<Node> highlight(String text, int size) {
        var elms = new ArrayList<Node>();
        for(var line : text.split("\\n+")) {
            if(line.startsWith("#")) {
                elms.addAll(PrettyUI.comment(line, CARET, size));
            } else {
                for(var token : line.split("\\s+")) {
                    var test = token.replace(CARET, EMPTY);
                    if(keywords.contains(test)) elms.addAll(PrettyUI.keyword(token, CARET, size));
                    else if(commands.contains(test)) elms.addAll(PrettyUI.command(token, CARET, size));
                    else if(test.matches(CONSTANT)) elms.addAll(PrettyUI.number(token, CARET, size));
                    else if(test.matches(VARIABLE)) elms.addAll(PrettyUI.variable(token, CARET, size));
                    else elms.addAll(PrettyUI.plain(token, CARET, size));
                } elms.add(new Text("\n"));
            }
        }
        return elms;
    }

    public static Text h1(String text) { return textWithStyle(text, H1); }
    public static Text p(String text) { return textWithStyle(text, P); }
    public static Text p(String text, double wrapWidth) {
        var ret = p(text);
        ret.setWrappingWidth(wrapWidth);
        return ret;
    }

    public static void bgTheme(Node ugly) { ugly.getStyleClass().add(BG_THEME); }
    public static void lightBgTheme(Node ugly) { ugly.getStyleClass().add(LIGHT_BG_THEME); }
    public static void litBgTheme(Node ugly) { ugly.getStyleClass().add(LIT_BG_THEME); }
    public static void alternateBgTheme(int idx, Node ugly) {
        ugly.getStyleClass().add(idx % 2 == 0 ? LIGHT_BG_THEME : BG_THEME);
    }
}
