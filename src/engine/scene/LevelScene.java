package engine.scene;

import engine.InputHandler;
import engine.graphics.Graphic;
import engine.entity.NonPlayerEntity;
import engine.entity.PlayerShip;

import java.util.HashSet;

public class LevelScene extends Scene{
    protected PlayerShip playerShip;
    protected HashSet<NonPlayerEntity> goodEntityList = new HashSet<>();
    protected HashSet<NonPlayerEntity> evilEntityList = new HashSet<>();
    protected InputHandler inputHandler;
    protected boolean[] controlStates = new boolean[GameControl.values().length];
    protected boolean[] lastControlStates = new boolean[GameControl.values().length];
    public LevelScene(long window) {
        super(window);
        this.inputHandler = new InputHandler(window);
    }

    @Override
    public void handleInputs() {
        inputHandler.updateControls(controlStates);
        if(getControlActivation(GameControl.PAUSE)){
            if(timer.isPaused()){
                timer.resume();
            }
            else{
                timer.pause();
            }
        }
        if(getControlActivation(GameControl.SLOWDOWN)){
            setSpeed(0.5f);
        }
        if(getControlDeactivation(GameControl.SLOWDOWN)){
            setSpeed(1.0f);
        }
        System.arraycopy(controlStates, 0, lastControlStates, 0, controlStates.length);
    }

    public void addEntity(NonPlayerEntity entity){
        Graphic<?,?> sprite = (Graphic<?,?>)entity.getSprite();
        addGraphic(sprite);
        if(entity.isEvil()){
            evilEntityList.add(entity);
        }
        else{
            goodEntityList.add(entity);
        }
    }
    @Override
    public void update() {
        super.update();
        for(NonPlayerEntity entity: goodEntityList){
            entity.actionsAndMoves();
        }
        for(NonPlayerEntity entity: evilEntityList){
            entity.actionsAndMoves();
        }
        if(playerShip != null){
            playerShip.actionsAndMoves(window);
        }
        for(NonPlayerEntity entity: evilEntityList){
            entity.handleCollisions();
        }
        for(NonPlayerEntity entity: goodEntityList){
            entity.handleCollisions();
        }
        if(playerShip!=null){
            playerShip.handleCollisions();
        }
    }

    public boolean getControlState(GameControl control){
        return controlStates[control.ordinal()];
    }

    public boolean getControlActivation(GameControl control){
        return controlStates[control.ordinal()] && !lastControlStates[control.ordinal()];
    }

    public boolean getControlDeactivation(GameControl control){
        return (!controlStates[control.ordinal()]) && lastControlStates[control.ordinal()];
    }
}
