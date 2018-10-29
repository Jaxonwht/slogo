package view;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import view.utils.ImageUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SidebarView {
    public static final int SIDEBAR_VIEW_WIDTH = 75;

    private static final int BUTTON_MARGIN = 15;
    private static final int BUTTON_SIZE = 40;
    private static final int SLIDER_HEIGHT = 110;

    private static final int NEW_INSTANCE_BUTTON = 0;
    private static final int BACKGROUND_BUTTON = 1;
    private static final int TURTLE_IMAGE_BUTTON = 2;
    private static final int PEN_COLOR_BUTTON = 3;
    private static final int LANGUAGE_BUTTON = 4;
    private static final int HELP_BUTTON = 5;
    private static final int MULTI_TURTLE_BUTTON = 6;

    private static final List<Image> ICONS = Collections.unmodifiableList(List.of(
            ImageUtils.getImageFromUrl("new_button.png", BUTTON_SIZE, BUTTON_SIZE),
            ImageUtils.getImageFromUrl("background_button.png", BUTTON_SIZE, BUTTON_SIZE),
            ImageUtils.getImageFromUrl("turtle_image_button.png", BUTTON_SIZE, BUTTON_SIZE),
            ImageUtils.getImageFromUrl("pen_color_button.png", BUTTON_SIZE, BUTTON_SIZE),
            ImageUtils.getImageFromUrl("language_button.png", BUTTON_SIZE, BUTTON_SIZE),
            ImageUtils.getImageFromUrl("help_button.png", BUTTON_SIZE, BUTTON_SIZE),
            ImageUtils.getImageFromUrl("multi_turtle_button.png", BUTTON_SIZE, BUTTON_SIZE)
    ));

    private static final List<String> TOOLTIPS = Collections.unmodifiableList(List.of(
            "Creates a new workspace",
            "Set background color",
            "Set turtle image",
            "Set pen color",
            "Set language",
            "Open documentation",
            "Add new turtle"
    ));

    private static final String DURATION_TOOLTIP = "Adjust the duration of single movement";
    private static final String STROKE_TOOLTIP = "Adjust the stroke of lines";
    private static final double ANIMATION_DURATION_MIN = 10;
    private static final double ANIMATION_DURATION_MAX = 2000;
    private static final double ANIMATION_DURATION_PRECISION = 400;
    private static final double STROKE_MIN = 1;
    private static final double STROKE_MAX = 10;
    private static final double STROKE_PRECISION = 2;

    private Pane root;
    private VBox icons;
    private List<StackPane> buttons;
    private List<Tooltip> tooltips;
    private ColorPicker backgroundColor, penColor;
    private VBox sliderWrapper;
    private Slider animationDuration;
    private Slider strokeSize;

    SidebarView() {
        root = new Pane();
        root.getStyleClass().add("sidebar");

        icons = new VBox(BUTTON_MARGIN);
        icons.getStyleClass().add("sidebar");

        setupColorPickers();
        setupButtons();
        setupSliders();

        icons.getChildren().addAll(buttons);
        icons.getChildren().add(sliderWrapper);
        root.getChildren().add(icons);
    }

    private void setupColorPickers() {
        backgroundColor = new ColorPicker();
        backgroundColor.getStyleClass().add("background-button");
        penColor = new ColorPicker();
        penColor.getStyleClass().add("pen-color-button");
    }

    private void setupButtons() {
        buttons = new ArrayList<>();
        tooltips = new ArrayList<>();
        for(int i = 0 ; i < ICONS.size() ; i ++) {
            StackPane button;
            Node image;
            if(i == BACKGROUND_BUTTON || i == PEN_COLOR_BUTTON) {
                image = i == BACKGROUND_BUTTON ? backgroundColor : penColor;
            } else image = new ImageView(ICONS.get(i));
            button = new StackPane(image);
            button.setAlignment(Pos.BASELINE_LEFT);
            button.getStyleClass().add("sidebar-box");
            var tooltip = setTooltip(button, TOOLTIPS.get(i));
            buttons.add(button);
            tooltips.add(tooltip);
        }
        appendLanguageTooltip("current: English");
        buttons = Collections.unmodifiableList(buttons);
    }

    private void setupSliders() {
        sliderWrapper = new VBox();
        sliderWrapper.setSpacing(BUTTON_MARGIN);

        animationDuration = new Slider(ANIMATION_DURATION_MIN, ANIMATION_DURATION_MAX, ANIMATION_DURATION_PRECISION);
        strokeSize = new Slider(STROKE_MIN, STROKE_MAX, STROKE_PRECISION);

        animationDuration.setOrientation(Orientation.VERTICAL);
        animationDuration.setShowTickMarks(true);
        animationDuration.setShowTickLabels(true);
        animationDuration.setMaxHeight(SLIDER_HEIGHT);

        strokeSize.setOrientation(Orientation.VERTICAL);
        strokeSize.setShowTickLabels(true);
        strokeSize.setShowTickMarks(true);
        strokeSize.setMaxHeight(SLIDER_HEIGHT);

        setTooltip(animationDuration, DURATION_TOOLTIP);
        setTooltip(strokeSize, STROKE_TOOLTIP);

        sliderWrapper.getChildren().add(animationDuration);
        sliderWrapper.getChildren().add(strokeSize);
        sliderWrapper.setAlignment(Pos.TOP_LEFT);
    }

    private Tooltip setTooltip(Node node, String text) {
        var tooltip = new Tooltip(text);
        tooltip.setShowDelay(Duration.ZERO);
        tooltip.setHideDelay(Duration.ZERO);
        Tooltip.install(node, tooltip);
        return tooltip;
    }

    public void appendLanguageTooltip(String appended) {
        tooltips.get(LANGUAGE_BUTTON).setText(TOOLTIPS.get(LANGUAGE_BUTTON) + "\n" + appended);
    }

    public Pane view() { return root; }

    public StackPane newButton() { return buttons.get(NEW_INSTANCE_BUTTON); }
    public ColorPicker backgroundColor() { return backgroundColor; }
    public StackPane turtleImageButton() { return buttons.get(TURTLE_IMAGE_BUTTON); }
    public ColorPicker penColor() { return penColor; }
    public StackPane languageButton() { return buttons.get(LANGUAGE_BUTTON); }
    public StackPane helpButton() { return buttons.get(HELP_BUTTON); }
    public Slider speedSlider() { return animationDuration; }
    public Slider strokeSlider(){ return strokeSize; }
    public StackPane multiTurtle(){return buttons.get(MULTI_TURTLE_BUTTON);}
}
