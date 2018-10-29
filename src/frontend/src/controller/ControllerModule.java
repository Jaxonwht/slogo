package controller;

import app.TabbedApp;
import engine.api.EngineAPI;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import model.ModelModule;
import view.ViewModule;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ControllerModule {
    public static final List<String> LANGUAGES = Collections.unmodifiableList(
            List.of(
                    "Chinese",
                    "English",
                    "French",
                    "German",
                    "Italian",
                    "Portuguese",
                    "Russian",
                    "Spanish",
                    "Urdu"
            )
    );

    private static final String DEFAULT_LANG = "English";

    private CanvasController canvasController;
    private EditorController editorController;
    private SidebarController sidebarController;

    public ControllerModule(
            TabbedApp app,
            ModelModule modelModule,
            EngineAPI engineApi,
            ViewModule viewModule,
            Consumer<String> setEngineLanguage
    ) {
        editorController = new EditorController(viewModule.commandView(), viewModule.historyView(), engineApi);
        sidebarController = new SidebarController(DEFAULT_LANG, app, viewModule.sidebarView(), modelModule.turtleManager(), setEngineLanguage);
        canvasController = new CanvasController(modelModule.turtleManager(), viewModule.canvasView());
        assemble();
    }

    private void assemble() { sidebarController.registerControllers(editorController, canvasController); }
}
