package engine.scene;

import engine.Game;
import engine.InputHandler;
import engine.entity.CustomEntityManager;
import engine.graphics.Graphic;
import engine.entity.Entity;
import engine.entity.PlayerShip;

import java.util.HashSet;

public class LevelScene extends Scene{
    final protected InputHandler inputHandler;
    final protected CustomEntityManager customEntityManager;
    protected PlayerShip playerShip;
    protected HashSet<Entity> goodEntities;
    protected HashSet<Entity> evilEntities;
    protected boolean[] controlStates;
    protected boolean[] lastControlStates;
    protected LevelTimeline timeline;
    public LevelScene(Game game) {
        super(game);
        this.inputHandler = game.getInputHandler();
        this.customEntityManager = game.getCustomEntityManager();
        this.goodEntities = new HashSet<>();
        this.evilEntities = new HashSet<>();
        this.controlStates = new boolean[GameControl.values().length];
        this.lastControlStates = new boolean[GameControl.values().length];
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

    public void addEntity(Entity entity){
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
            for(Entity entity: goodEntities){
                entity.update();
            }
            for(Entity entity: evilEntities){
                entity.update();
            }
            if(playerShip != null){
                playerShip.actionsAndMoves(window);
            }
            for(Entity entity: evilEntities){
                entity.handleCollisions();
            }
            for(Entity entity: goodEntities){
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

    public void deleteEntity(Entity entity){
        if(entity.isEvil()){
            evilEntities.remove(entity);
        }
        else{
            goodEntities.remove(entity);
        }
        entity.delete();
    }

    public CustomEntityManager getCustomEntityManager() {
        return customEntityManager;
    }
}
