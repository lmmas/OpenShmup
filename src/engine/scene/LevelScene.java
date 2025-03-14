package engine.scene;

import engine.InputHandler;
import engine.graphics.Graphic;
import engine.entity.NonPlayerEntity;
import engine.entity.PlayerShip;

import java.util.HashSet;

public class LevelScene extends Scene{
    protected PlayerShip playerShip;
    protected HashSet<NonPlayerEntity> goodEntities = new HashSet<>();
    protected HashSet<NonPlayerEntity> evilEntities = new HashSet<>();
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
        Graphic<?,?> entityGraphic = entity.getSprite().getGraphic();
        addGraphic(entityGraphic);
        if(entity.isEvil()){
            evilEntities.add(entity);
        }
        else{
            goodEntities.add(entity);
        }
    }
    @Override
    public void update() {
        super.update();
        if(!timer.isPaused()){
            for(NonPlayerEntity entity: goodEntities){
                entity.update();
            }
            for(NonPlayerEntity entity: evilEntities){
                entity.update();
            }
            if(playerShip != null){
                playerShip.actionsAndMoves(window);
            }
            for(NonPlayerEntity entity: evilEntities){
                entity.handleCollisions();
            }
            for(NonPlayerEntity entity: goodEntities){
                entity.handleCollisions();
            }
            if(playerShip!=null){
                playerShip.handleCollisions();
            }
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

    public void deleteEntity(NonPlayerEntity entity){
        if(entity.isEvil()){
            evilEntities.remove(entity);
        }
        else{
            goodEntities.remove(entity);
        }
        entity.delete();
    }
}
