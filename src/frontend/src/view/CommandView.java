package view;

import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

public class CommandView {
    private static final double COMMAND_WIDTH = CanvasView.TURTLE_VIEW_WIDTH;
    private static final String EMPTY = "";
    private static final int FONT_SIZE = 16;

    private TextArea root;

    CommandView() {
        root = new TextArea();
        root.setFont(new Font(FONT_SIZE));
    }

    public TextArea view() { return root; }
}
