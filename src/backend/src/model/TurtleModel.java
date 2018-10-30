package model;

import engine.compiler.storage.StateMachine;
import engine.errors.InterpretationException;
import engine.errors.UndefinedKeywordException;
import javafx.beans.property.SimpleBooleanProperty;

public interface TurtleModel {
    double TRUE = 1;
    double FALSE = 0;

    void equipMemory(StateMachine memory);

    double setPenDown(boolean down) throws InterpretationException, UndefinedKeywordException;
    double setVisible(boolean visible) throws InterpretationException, UndefinedKeywordException;
    double forward(double by) throws InterpretationException, UndefinedKeywordException;
    double moveTo(double x, double y, boolean forcePenUp) throws InterpretationException, UndefinedKeywordException;
    double setAngle(double angle) throws InterpretationException, UndefinedKeywordException;
    double leftBy(double angle) throws InterpretationException, UndefinedKeywordException;
    double getX() throws InterpretationException, UndefinedKeywordException;
    double getY() throws InterpretationException, UndefinedKeywordException;
    double getAngle() throws InterpretationException, UndefinedKeywordException;

    boolean isPenDown() throws InterpretationException, UndefinedKeywordException;
    boolean isVisible() throws InterpretationException, UndefinedKeywordException;
    SimpleBooleanProperty isPenDownModel();
    SimpleBooleanProperty isVisibleModel();
    PosAndAngle posAndAngleModel();

    /**
     * I honestly feel like this shouldn't be here ... but it's easy to do
     * @blame inchan hwang
     */
    int setBackground(int index) throws InterpretationException, UndefinedKeywordException;
    int setPenColor(int index) throws InterpretationException, UndefinedKeywordException;
    int setPenSize(int pixels) throws InterpretationException, UndefinedKeywordException;
    int setShape(int index) throws InterpretationException, UndefinedKeywordException;

    StateMachine memory();
    void registerClearListener(ClearListener cl);
    void registerUIListener(UIListener ul);
    double clear() throws InterpretationException, UndefinedKeywordException;
}
