package controller;

import engine.api.EngineAPI;
import javafx.scene.input.KeyCode;
import view.CommandView;
import view.HistoryView;

public class EditorController {
    private static final String NEWLINE = "\n";
    private static final String SPACE = " ";

    private EngineAPI engineApi;
    private CommandView commandView;
    private HistoryView historyView;

    public EditorController(CommandView commandView, HistoryView historyView, EngineAPI engineApi) {
        this.commandView = commandView;
        this.historyView = historyView;
        this.historyView.registerOnHistoryClick(s -> commandView.view().setText(s));
        this.engineApi = engineApi;

        setupHandlers();
    }

    private void setupHandlers() {
        commandView.view().setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER ) {
                if(!e.isShiftDown()) submitCommand();
                else commandView.view().insertText(commandView.view().getCaretPosition(), NEWLINE);
            }
        });
    }

    private void submitCommand() {
        String cmd = commandView.view().getText().replaceAll("[ ]+", SPACE).trim();
        commandView.view().clear();
        try {
            double ret = engineApi.processString(cmd);
            historyView.addText(cmd, ret);
        }  catch (Exception e) {
            e.printStackTrace();
            historyView.displayError(cmd, e);
        }
    }
}
