package engine.scene;

import engine.Game;
import engine.InputHandler;
import engine.EditorDataManager;
import engine.entity.hitbox.SimpleHitBox;
import engine.graphics.Graphic;
import engine.entity.Entity;
import engine.entity.PlayerShip;

import java.util.HashSet;
import java.util.List;

public class LevelScene extends Scene{
    final protected InputHandler inputHandler;
    final protected EditorDataManager editorDataManager;
    protected PlayerShip playerShip;
    protected HashSet<Entity> goodEntities;
    protected HashSet<Entity> evilEntities;
    protected boolean[] controlStates;
    protected boolean[] lastControlStates;
    protected LevelTimeline timeline;

    public LevelScene(Game game, LevelTimeline timeline) {
        super(game);
        this.inputHandler = game.getInputHandler();
        this.editorDataManager = game.getEditorDataManager();
        this.goodEntities = new HashSet<>();
        this.evilEntities = new HashSet<>();
        this.controlStates = new boolean[GameControl.values().length];
        this.lastControlStates = new boolean[GameControl.values().length];
        this.timeline = timeline;
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
            timeline.updateSpawning(this);
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
                handleCollisions(entity, goodEntities);
            }
            for(Entity entity: goodEntities){
                handleCollisions(entity, evilEntities);
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

    public void handleCollisions(Entity entity, HashSet<Entity> ennemyList){
        SimpleHitBox hitBox = entity.getHitbox();
        for(Entity ennemy: ennemyList){
            SimpleHitBox ennemyHitbox = ennemy.getHitbox();
            if(hitBox.intersects(ennemyHitbox)){
                entity.damage(1);
            }
        }
    }
}
