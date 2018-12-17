package controller;

import engine.errors.InterpretationException;
import engine.errors.UndefinedKeywordException;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.*;
import view.CanvasView;
import view.utils.ImageUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *  Interfaces between the Turtle Manager and Canvas View
 */
public class CanvasController implements SelectionListener, StampListener {
    private TurtleManager turtleManager;
    private CanvasView canvasView;
    private double selectionX, selectionY;
    private double pivotX, pivotY;
    private Image turtleImage;
    private static final double TURTLE_SIZE = 50;
    private Set<ImageView> stamps;

    public CanvasController(TurtleManager turtleManager, CanvasView canvasView) {
        this.turtleManager = turtleManager;
        this.turtleManager.registerSelectionListener(this);
        this.canvasView = canvasView;
        this.turtleManager.setStampListener(this);
        turtleImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("turtle_1.png")));
        stamps = new HashSet<>();
        setupTurtleManager();
        selectionX = selectionY = 0;
        setupSelectionRectangle();
        setupZoom();
    }

    private void setupSelectionRectangle() {
        canvasView.view().setOnMousePressed(e -> {
            selectionX = e.getX();
            selectionY = e.getY();
            canvasView.selection().setX(e.getX());
            canvasView.selection().setY(e.getY());
        });
        canvasView.view().setOnMouseDragged(e -> {
            if(e.getX() < selectionX) {
                canvasView.selection().setX(e.getX());
                canvasView.selection().setWidth(selectionX-e.getX());
            } else canvasView.selection().setWidth(e.getX()-selectionX);
            if(e.getY() < selectionY) {
                canvasView.selection().setY(e.getY());
                canvasView.selection().setHeight(selectionY-e.getY());
            } else canvasView.selection().setHeight(e.getY()-selectionY);
        });
        canvasView.view().setOnMouseReleased(e -> {
            turtleManager.tell(canvasView.turtlesInSelection());
            canvasView.selection().setWidth(0);
            canvasView.selection().setHeight(0);
        });
    }

    private void setupZoom(){
        canvasView.view().setOnScroll(e ->{
            double delta = 1.1;
            if (e.getDeltaY()<0)
                delta = 1/delta;
            else{
                pivotX = e.getSceneX();
                pivotY = e.getSceneY();
            }
            for (Node i :canvasView.view().getChildren())
            {
                if(i.getClass().getSimpleName().equals("Rectangle")){
                    continue;
                }
                i.setScaleX(i.getScaleX()*delta);
                i.setScaleY(i.getScaleY()*delta);
                adjust(i, pivotX, pivotY, delta-1);
            }
        });
    }

    private void adjust(Node n, double sceneX, double sceneY, double f){
        double dx = (sceneX - (n.getBoundsInParent().getWidth()/2 + n.getBoundsInParent().getMinX()));
        double dy = (sceneY - (n.getBoundsInParent().getHeight()/2 + n.getBoundsInParent().getMinY()));
        n.setTranslateX(n.getTranslateX()-f*dx);
        n.setTranslateY(n.getTranslateY()-f*dy);
    }

    private void setupTurtleManager() {
        canvasView.addTurtle(
                ModelModule.INITIAL_TURTLE_ID,
                turtleManager.turtleModels().get(ModelModule.INITIAL_TURTLE_ID)
        );
        canvasView.highlightSelected(turtleManager.selected());
        turtleManager.turtleModels().addListener((MapChangeListener<Integer, TurtleModel>) c -> {
            if(c.wasAdded()) {
                canvasView.addTurtle(c.getKey(), c.getValueAdded());
                canvasView.highlightSelected(turtleManager.selected());
            }
        });
    }

    public void setPenColor(Color c) { turtleManager.selected().forEach(idx -> canvasView.setPenColor(idx, c)); }
    public void setTurtleImage(Image img) {
        turtleManager.selected().forEach(idx -> canvasView.setImage(idx, img));
        turtleImage = img;
    }
    public void bindDuration(DoubleProperty model) { canvasView.durationProperty().bind(model); }
    public void bindStroke(DoubleProperty stroke) { canvasView.strokeProperty().bindBidirectional(stroke); }
    public void setBackgroundColor(Color c) { canvasView.setBackgroundColor(c); }

    @Override
    public void selectionUpdated(List<Integer> selected) {
        canvasView.highlightSelected(selected);
    }

    @Override
    public double updateOnStamp() {
        stamps.clear();
        turtleManager.turtleModels().values().forEach(turtleModel -> {
            ImageView stamp = new ImageView(turtleImage);
            try {
                stamp.setX(turtleModel.getX());
                stamp.setY(turtleModel.getY());
            } catch (InterpretationException | UndefinedKeywordException ignored) {
            }
            stamp.setFitWidth(TURTLE_SIZE);
            stamp.setFitHeight(TURTLE_SIZE);
            stamps.add(stamp);
        });
        canvasView.addStamps(stamps);
        return 0;
    }

    @Override
    public double updateOnClearStamps() {
        canvasView.removeStamps(stamps);
        if (stamps.isEmpty()) {
            return 0;
        } else {
            stamps.clear();
            return 1;
        }
    }
}
