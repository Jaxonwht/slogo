package model.impl;
import engine.compiler.storage.StateMachine;
import engine.errors.InterpretationException;
import javafx.beans.property.SimpleBooleanProperty;
import model.ClearListener;
import model.PosAndAngle;
import model.TurtleModel;
import model.UIListener;

import java.util.ArrayList;
import java.util.List;

public class TurtleModelImpl implements TurtleModel {
    private SimpleBooleanProperty visible;
    private SimpleBooleanProperty penDown;
    private PosAndAngle pa;
    private List<ClearListener> listeners;
    private List<UIListener> uiListeners;
    private StateMachine memory;

    public TurtleModelImpl(StateMachine memory){
        listeners = new ArrayList<>();
        uiListeners = new ArrayList<>();
        pa = new PosAndAngle(0, 0, 0);
        visible = new SimpleBooleanProperty(true);
        penDown = new SimpleBooleanProperty(true);
        this.memory = memory;
    }

    @Override
    public void equipMemory(StateMachine memory) { this.memory = memory; }

    public double setPenDown(boolean down){
        penDown.setValue(down);
        return down ? TRUE : FALSE;
    }
    public double setVisible(boolean visible){
        this.visible.setValue(visible);
        return visible ? TRUE : FALSE;
    }

    @Override
    public double forward(double by) {
        double stepX = by*Math.cos(Math.toRadians(pa.angle()));
        double stepY = by*Math.sin(Math.toRadians(pa.angle()));
        return moveTo(pa.x()+stepX, pa.y()+stepY, false);
    }

    public double moveTo(double x, double y, boolean forcePenUp){
        var dx = x - pa.x();
        var dy = y - pa.y();
        double dist = Math.sqrt(dx*dx + dy*dy);
        var prevPen = penDown;
        if(forcePenUp) setPenDown(false);
        pa.setXY(x, y);
        setPenDown(prevPen.getValue());
        return dist;
    }

    public double setAngle(double angle){
        var dAngle = (angle - pa.angle()+360)%360;
        pa.setAngle(angle);
        return dAngle;
    }

    @Override
    public double leftBy(double angle) throws InterpretationException {
        var toAngle = (pa.angle()-angle+360)%360;
        pa.setAngle(toAngle);
        return angle;
    }

    public double getX(){return pa.x();}
    public double getY(){return pa.y();}
    public double getAngle(){return pa.angle();}
    public boolean isPenDown(){ return penDown.getValue(); }
    public boolean isVisible(){ return visible.getValue(); }
    public SimpleBooleanProperty isPenDownModel(){ return penDown; }
    public SimpleBooleanProperty isVisibleModel(){ return visible; }
    public PosAndAngle posAndAngleModel() { return pa;}

    @Override
    public int setBackground(int index) throws InterpretationException {
        var colorStr = memory.getValue("ColorIndex" + String.valueOf(index)).toString();
        uiListeners.forEach(listener -> listener.setBackground(colorStr));
        return index;
    }

    @Override
    public int setPenColor(int index) throws InterpretationException {
        var colorStr = memory.getValue("ColorIndex"+String.valueOf(index)).toString();
        uiListeners.forEach(listener -> listener.setPenColor(colorStr));
        return index;
    }

    @Override
    public int setPenSize(int pixels) {
        uiListeners.forEach(listener -> listener.setPenSize(pixels));
        return pixels;
    }

    @Override
    public int setShape(int index) throws InterpretationException {
        var shapeStr = memory.getValue("ShapeIndex"+String.valueOf(index)).toString();
        uiListeners.forEach(listener -> listener.setShape(shapeStr));
        return index;
    }

    @Override
    public StateMachine memory() { return memory; }

    @Override
    public void registerClearListener(ClearListener cl) { listeners.add(cl); }

    @Override
    public void registerUIListener(UIListener ul) { uiListeners.add(ul); }

    @Override
    public double clear() { listeners.forEach(ClearListener::clear); return 0; }
}
