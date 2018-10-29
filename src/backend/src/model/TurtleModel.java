package model;

import engine.compiler.storage.StateMachine;
import engine.errors.InterpretationException;
import javafx.beans.property.SimpleBooleanProperty;

public interface TurtleModel {
    double TRUE = 1;
    double FALSE = 0;

    void equipMemory(StateMachine memory);

    double setPenDown(boolean down) throws InterpretationException;
    double setVisible(boolean visible) throws InterpretationException;
    double forward(double by) throws InterpretationException;
    double moveTo(double x, double y, boolean forcePenUp) throws InterpretationException;
    double setAngle(double angle) throws InterpretationException;
    double leftBy(double angle) throws InterpretationException;
    double getX() throws InterpretationException;
    double getY() throws InterpretationException;
    double getAngle() throws InterpretationException;

    boolean isPenDown() throws InterpretationException;
    boolean isVisible() throws InterpretationException;
    SimpleBooleanProperty isPenDownModel();
    SimpleBooleanProperty isVisibleModel();
    PosAndAngle posAndAngleModel();

    /**
     * I honestly feel like this shouldn't be here ... but it's easy to do
     * @blame inchan hwang
     */
    int setBackground(int index) throws InterpretationException;
    int setPenColor(int index) throws InterpretationException;
    int setPenSize(int pixels) throws InterpretationException;
    int setShape(int index) throws InterpretationException;

    StateMachine memory();
    void registerClearListener(ClearListener cl);
    void registerUIListener(UIListener ul);
    double clear() throws InterpretationException;
}
