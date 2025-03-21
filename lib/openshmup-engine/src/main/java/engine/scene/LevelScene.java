package engine.scene;

import engine.Game;
import engine.InputHandler;
import engine.EditorDataManager;
import engine.entity.*;
import engine.entity.hitbox.SimpleHitBox;
import engine.graphics.Graphic;
import engine.scene.spawnable.EntitySpawnInfo;
import engine.scene.spawnable.SceneVisualSpawnInfo;
import engine.scene.visual.SceneVisual;

import java.util.HashSet;

public class LevelScene extends Scene{
    final protected InputHandler inputHandler;
    final protected EditorDataManager editorDataManager;
    protected ShootingShip playerShip;
    protected HashSet<Entity> goodEntities;
    protected HashSet<Entity> evilEntities;
    protected HashSet<EntitySpawnInfo> entitiesToSpawn;
    protected HashSet<Entity> entitiesToRemove;
    protected HashSet<SceneVisualSpawnInfo> visualsToSpawn;
    protected boolean[] controlStates;
    protected boolean[] lastControlStates;
    protected LevelTimeline timeline;

    public LevelScene(Game game, LevelTimeline timeline) {
        super(game);
        this.inputHandler = game.getInputHandler();
        this.editorDataManager = game.getEditorDataManager();
        this.goodEntities = new HashSet<>();
        this.evilEntities = new HashSet<>();
        this.entitiesToSpawn = new HashSet<>();
        this.entitiesToRemove = new HashSet<>();
        this.visualsToSpawn = new HashSet<>();
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

    @Override
    public void update() {
        if(timer.isPaused()){
            return;
        }

        timeline.updateSpawning(this);
        for(var entitySpawn: entitiesToSpawn){
            Entity newEntity = editorDataManager.buildCustomEntity(this, entitySpawn.id());
            if(entitySpawn.trajectoryId() != -1){
                newEntity.setTrajectory(editorDataManager.getTrajectory(entitySpawn.trajectoryId()));
            }
            newEntity.setStartingPosition(entitySpawn.startingPosition().x, entitySpawn.startingPosition().y);
            newEntity.setPosition(entitySpawn.startingPosition().x, entitySpawn.startingPosition().y);
            addEntity(newEntity);
        }
        entitiesToSpawn.clear();

        for(var visualSpawn: visualsToSpawn){
            SceneVisual newVisual = editorDataManager.buildCustomVisual(this, visualSpawn.id());
            newVisual.setPosition(visualSpawn.position().x, visualSpawn.position().y);
            addVisual(newVisual);
        }
        visualsToSpawn.clear();

        for(Entity entity: goodEntities){
            entity.update();
        }
        for(Entity entity: evilEntities){
            entity.update();
        }
        for(Entity entity: evilEntities){
            handleCollisions(entity, goodEntities);
        }
        for(Entity entity: goodEntities){
            handleCollisions(entity, evilEntities);
        }
        for(Entity entity: entitiesToRemove){
            deleteEntity(entity);
        }
        entitiesToRemove.clear();
        super.update();
    }

    public void addEntitySpawn(EntitySpawnInfo entitySpawnInfo){
        entitiesToSpawn.add(entitySpawnInfo);
    }

    public void addVisualSpawn(SceneVisualSpawnInfo visualSpawnInfo){
        visualsToSpawn.add(visualSpawnInfo);
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

    public void deleteEntity(Entity entity){
        if(entity.isEvil()){
            evilEntities.remove(entity);
        }
        else{
            goodEntities.remove(entity);
        }
        entity.getSprite().getGraphic().delete();
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
                    entity.deathEvent();
                    entitiesToRemove.add(entity);
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
                        entitiesToRemove.add(entity);
                    }
                }
            }
        }
    }
}
