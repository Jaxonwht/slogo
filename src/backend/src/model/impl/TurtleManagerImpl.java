package model.impl;

import engine.compiler.storage.StateMachine;
import engine.errors.IllegalParameterException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TurtleManagerImpl implements TurtleManager {
    private StateMachine memory;
    private ObservableMap<Integer, TurtleModel> turtleModels;
    private List<Integer> selected;

    public TurtleManagerImpl() {
        turtleModels = FXCollections.observableMap(new HashMap<>());
        selected = List.of(ModelModule.INITIAL_TURTLE_ID);
    }

    @Override
    public int id() { return selected.get(selected.size()-1); }

    @Override
    public List<Integer> selected() { return selected; }

    @Override
    public int size() { return turtleModels.size(); }

    @Override
    public int addTurtle(int id) throws IllegalParameterException {
        if(id <= 0) throw new IllegalParameterException("Turtle's ID must be STRICTLY bigger than 0");
        var newTurtle = new TurtleModelImpl(memory);
        turtleModels.put(id, newTurtle);
        return id;
    }

    @Override
    public ObservableMap<Integer, TurtleModel> turtleModels() { return turtleModels; }

    @Override
    public <T> T foreach(TurtleOperations<T> ops) {
        var results = selected
                .stream()
                .map(idx -> ops.op(turtleModels.get(idx)))
                .collect(Collectors.toList());
        return results.get(results.size()-1);
    }

    private List<Integer> checkWildcard(List<Integer> indices) {
        return indices.contains(ALL) ? new ArrayList<>(turtleModels.keySet()) : indices;
    }

    @Override
    public int tell(List<Integer> turtleIDs) {
        selected = checkWildcard(turtleIDs);
        return id();
    }

    @Override
    public <T> T ask(List<Integer> indices, TurtleOperations<T> ops) {
        var results = checkWildcard(indices)
                .stream()
                .map(idx -> ops.op(turtleModels.get(idx)))
                .collect(Collectors.toList());
        return results.get(results.size()-1);
    }

    @Override
    public <T> T askWith(Predicate<TurtleModel> p, TurtleOperations<T> ops) {
        var results = turtleModels
                .keySet()
                .stream()
                .map(turtleModels::get)
                .filter(p)
                .map(ops::op)
                .collect(Collectors.toList());
        return results.get(results.size()-1);
    }

    @Override
    public StateMachine memory() { return memory; }

    @Override
    public void equipMemory(StateMachine memory) { this.memory = memory; }

    private <T> T batchOperation(TurtleOperations<T> ops) {
        var results = selected
                .stream()
                .map(turtleModels::get)
                .map(ops::op)
                .collect(Collectors.toList());
        return results.get(results.size()-1);
    }

    @Override
    public double setPenDown(boolean down) { return batchOperation(t -> t.setPenDown(down)); }

    @Override
    public double setVisible(boolean visible) { return batchOperation(t -> t.setVisible(visible)); }


    @Override
    public double moveTo(double x, double y, boolean forcePenUp) { return batchOperation(t -> t.moveTo(x, y, forcePenUp)); }

    @Override
    public double setAngle(double angle) { return batchOperation(t -> t.setAngle(angle)); }

    @Override
    public double getX() { return batchOperation(TurtleModel::getX); }

    @Override
    public double getY() { return batchOperation(TurtleModel::getY); }

    @Override
    public double getAngle() { return batchOperation(TurtleModel::getAngle); }

    @Override
    public boolean isPenDown() { return batchOperation(TurtleModel::isPenDown);}

    @Override
    public boolean isVisible() { return batchOperation(TurtleModel::isVisible); }

    @Override
    public double clear() { return batchOperation(TurtleModel::clear); }

    /**
     *  Used for individual turtles only
     */

    @Override
    public SimpleBooleanProperty isPenDownModel() { return null; }
    @Override
    public SimpleBooleanProperty isVisibleModel() { return null; }
    @Override
    public PosAndAngle posAndAngleModel() { return null; }
    @Override
    public void registerClearListener(ClearListener cl) { }
}