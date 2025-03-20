package engine.scene;

import engine.Game;
import engine.InputHandler;
import engine.EditorDataManager;
import engine.Vec2D;
import engine.entity.EntityType;
import engine.entity.Ship;
import engine.entity.hitbox.EntitySprite;
import engine.entity.hitbox.SimpleHitBox;
import engine.graphics.Graphic;
import engine.entity.Entity;
import engine.entity.PlayerShip;
import engine.scene.spawnable.EntitySpawnInfo;

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

    public void addEntity(EntitySpawnInfo spawnInfo){
        Entity newEntity = editorDataManager.buildCustomEntity(this, spawnInfo.id());
        addEntity(newEntity);
        Vec2D startingPosition = spawnInfo.startingPosition();
        newEntity.setStartingPosition(startingPosition.x, startingPosition.y);
        int trajectoryId = spawnInfo.trajectoryId();
        if(trajectoryId != -1){
            newEntity.setTrajectory(editorDataManager.getTrajectory(trajectoryId));
        }
    }

    public void deleteEntity(Entity entity){
        if(entity.isEvil()){
            evilEntities.remove(entity);
        }
        else{
            goodEntities.remove(entity);
        }
        entity.getSprite().getGraphic().delete();
    }

    @Override
    public void update() {
        super.update();
        if(timer.isPaused()){
            return;
        }
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

    public boolean getControlState(GameControl control){
        return controlStates[control.ordinal()];
    }

    public boolean getControlActivation(GameControl control){
        return controlStates[control.ordinal()] && !lastControlStates[control.ordinal()];
    }

    public boolean getControlDeactivation(GameControl control){
        return (!controlStates[control.ordinal()]) && lastControlStates[control.ordinal()];
    }

    public void handleCollisions(Entity entity, HashSet<Entity> ennemyList) {
        if (entity.isInvincible()) {
            return;
        }
        SimpleHitBox entityHitbox = entity.getHitbox();
        if (entity.getType() == EntityType.PROJECTILE) {
            for (Entity ennemy : ennemyList) {
                if(ennemy.getType() == EntityType.PROJECTILE){
                    continue;
                }
                Ship ennemyShip = (Ship) ennemy;
                SimpleHitBox ennemyHitbox = ennemyShip.getHitbox();
                if (entityHitbox.intersects(ennemyHitbox)) {
                    entity.deathEvent(this);
                    deleteEntity(entity);
                }
            }
        } else if (entity.getType() == EntityType.SHIP) {
            Ship shipEntity = (Ship) entity;
            for (Entity ennemy : ennemyList) {
                SimpleHitBox ennemyHitbox = ennemy.getHitbox();
                if (entityHitbox.intersects(ennemyHitbox)) {
                    shipEntity.takeDamage(1);
                    if(shipEntity.isDead()){
                        shipEntity.deathEvent();
                        deleteEntity(shipEntity);
                    }
                }
            }
        }
    }
}
