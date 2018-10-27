package view;

import app.SLogoApp;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import model.TurtleModel;
import view.utils.BackgroundUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *  Manages multiple Turtle Views, listening to how their view list changes
 */
public class CanvasView {
    public static final int TURTLE_VIEW_WIDTH = SLogoApp.APP_SCREEN_HEIGHT;

    private Pane root;
    private SimpleDoubleProperty duration;
    private Map<Integer, TurtleView> turtleViews;

    public CanvasView() {
        root = new Pane();
        setBackgroundColor(Color.WHITE);
        duration = new SimpleDoubleProperty();
        turtleViews = new HashMap<>();
    }
    public void addTurtle(int id, TurtleModel model) {
        var newView = new TurtleView(model, duration);
        turtleViews.put(id, newView);
        root.getChildren().addAll(newView.views());
    }
    public DoubleProperty durationProperty() { return duration; }
    public void setImage(int idx, Image img) { turtleViews.get(idx).setTurtleImage(img); }
    public void setBackgroundColor(Color c) { root.setBackground(BackgroundUtils.coloredBackground(c)); }
    public void setPenColor(int idx, Color c) { turtleViews.get(idx).setPenColor(c); }
    public Pane view() { return root; }
}
