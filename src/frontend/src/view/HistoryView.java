package view;

import app.SLogoApp;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import view.utils.PrettyUI;

import java.util.function.Consumer;
import java.util.function.Function;

public class HistoryView {
    static final int HISTORY_VIEW_WIDTH =
            SLogoApp.APP_SCREEN_WIDTH - SidebarView.SIDEBAR_VIEW_WIDTH - CanvasView.TURTLE_VIEW_WIDTH;
    private final int FONT_SIZE = 12;

    private ScrollPane root;
    private VBox display;
    private Consumer<String> onHistoryClick;

    HistoryView() {
        display = new VBox(10);
        display.getStyleClass().add("history-view");
        display.setMinWidth(HISTORY_VIEW_WIDTH);
        display.setMaxWidth(HISTORY_VIEW_WIDTH);
        display.heightProperty().addListener(ob -> root.setVvalue(root.getVmax()));

        root = new ScrollPane();
        root.getStyleClass().add("history-view-bg");
        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setContent(display);
    }

    public void registerOnHistoryClick(Consumer<String> handler) { onHistoryClick = handler; }

    public ScrollPane view() { return root; }

    public void addText(String text, double retVal) {
        display.getChildren().add(new HistoryUnit(text, retVal).flow());
    }

    public void displayError(String text, Exception e) {
        display.getChildren().add(new HistoryUnit(text, e).flow());
    }

    private class HistoryUnit {
        private TextFlow flow;

        private HistoryUnit(String command) {
            flow = new TextFlow();
            PrettyUI.litBgTheme(flow);
            flow.getChildren().addAll(PrettyUI.highlight(command.trim(), FONT_SIZE));
        }

        private HistoryUnit(String command, Exception e) {
            this(command);
            flow.getChildren().add(new Text("\n"));
            flow.getChildren().addAll(PrettyUI.error(e.toString().trim(), FONT_SIZE));
        }

        private HistoryUnit(String command, double retVal) {
            this(command);
            flow.getChildren().add(new Text("\n>> "));
            flow.getChildren().addAll(PrettyUI.number(String.valueOf(retVal), " ", FONT_SIZE));
            flow.setOnMousePressed(ev -> onHistoryClick.accept(command));
        }

        private TextFlow flow() { return flow; }
    }
}
