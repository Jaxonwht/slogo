package model;

import engine.errors.InterpretationException;
import engine.errors.UndefinedKeywordException;

public interface TurtleOperations<T> {
    T op(TurtleModel turtleModel) throws InterpretationException, UndefinedKeywordException;
}
