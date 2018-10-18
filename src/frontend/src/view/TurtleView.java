package view;

import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import fake_model.TurtleModel;
import javafx.util.Duration;
import view.utils.BackgroundUtils;
import view.utils.ImageUtils;

import java.util.EventListener;

public class TurtleView {
    public static final int TURTLE_SIZE = 50;
    public static final int TURTLE_VIEW_WIDTH = MainView.SCREEN_HEIGHT;
    public static final int DURATION_MILLIS = 4000;
    //private static final Image DEFAULT_TURTLE_IMG =
         //   ImageUtils.getImageFromUrl("turtle_image_button.png", TURTLE_SIZE, TURTLE_SIZE);
    private double x;
    private double y;
    private boolean penDown;
    private boolean move;
    private double angle;
    private GridPane root;
    private ImageView turtle;
    private Color penColor;

    public TurtleView(TurtleModel turtleModel) {
        turtle = new ImageView();
        root = new GridPane();
        root.getStyleClass().add("canvas");
        root.setPrefWidth(200);
        root.setPrefHeight(200);


        penDown = true;
        turtle.visibleProperty().bind(turtleModel.isVisible());

        bindObservable(turtleModel);

    }

    private void bindObservable(TurtleModel turtleModel) {
        turtleModel.isMove().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                move = newValue;
            }
        });
        turtleModel.isPenDown().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                penDown = newValue;
            }
        });

        turtleModel.getPoints().addListener(new ListChangeListener<Double>() {
            @Override
            public void onChanged(Change<? extends Double> c) {
                x = c.getList().get(0);
                y = c.getList().get(1);
                if (move){
                    var animation = makeAnimation(turtle);
                    animation.play();
                }
            }
        });
    }

    private Animation makeAnimation(ImageView turtle){
        var path = new Path();
        path.getElements().add(new MoveTo(x,y));
        if(penDown){
            path.getElements().add(new LineTo(x, y));
        }
        var pt = new PathTransition(Duration.millis(DURATION_MILLIS),path,turtle);
        var rt = new RotateTransition(Duration.millis(DURATION_MILLIS));
        rt.setToAngle(angle);
        return new SequentialTransition(turtle,pt,rt);
    }

    public Pane view() { return root; }
    public void setBackgroundColor(Color c) { root.setBackground(BackgroundUtils.coloredBackground(c)); }
    public void setTurtleImage(int x, int y, Image v) {
        System.out.println(x + " " + y);
        turtle.setImage(v);
        turtle.setX(x);
        turtle.setY(y);
        turtle.setRotate(0);
    }
    public void setPenColor(Color c) {penColor = c;}







}
