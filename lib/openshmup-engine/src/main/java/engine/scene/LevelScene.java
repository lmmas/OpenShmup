package engine.scene;

import engine.*;
import engine.entity.*;
import engine.entity.extraComponent.ExtraComponent;
import engine.entity.extraComponent.HitboxDebugDisplay;
import engine.entity.hitbox.CompositeHitbox;
import engine.entity.hitbox.EmptyHitbox;
import engine.entity.hitbox.Hitbox;
import engine.entity.hitbox.SimpleRectangleHitbox;
import engine.graphics.Graphic;
import engine.graphics.StaticImage;
import engine.render.RenderInfo;
import engine.graphics.GraphicType;
import engine.render.Texture;
import engine.scene.spawnable.EntitySpawnInfo;
import engine.scene.spawnable.SceneDisplaySpawnInfo;
import engine.scene.display.SceneDisplay;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class LevelScene extends Scene{
    final protected InputHandler inputHandler;
    protected Ship playerShip;
    protected ArrayList<StaticImage> playerLives;
    protected HashSet<Entity> goodEntities;
    protected HashSet<Entity> evilEntities;
    protected HashSet<EntitySpawnInfo> entitiesToSpawn;
    protected HashSet<Entity> entitiesToRemove;
    protected HashSet<SceneDisplaySpawnInfo> displaysToSpawn;
    protected boolean[] controlStates;
    protected boolean[] lastControlStates;
    protected LevelTimeline timeline;

    public LevelScene(Game game, LevelTimeline timeline, boolean debugMode) {
        super(game, debugMode);
        this.inputHandler = game.getInputHandler();
        this.playerLives = new ArrayList<>();
        this.goodEntities = new HashSet<>();
        this.evilEntities = new HashSet<>();
        this.entitiesToSpawn = new HashSet<>();
        this.entitiesToRemove = new HashSet<>();
        this.displaysToSpawn = new HashSet<>();
        this.controlStates = new boolean[GameControl.values().length];
        this.lastControlStates = new boolean[GameControl.values().length];
        this.timeline = timeline;
        HashSet<RenderInfo> allRenderInfos = timeline.getAllRenderInfos();
        allRenderInfos.add(new RenderInfo(GameConfig.LevelUI.upperLayer, GraphicType.STATIC_IMAGE));
        if(debugMode){
            allRenderInfos.add(new RenderInfo(GlobalVars.debugDisplayLayer, GraphicType.COLOR_RECTANGLE));
        }
        constructRenderers(allRenderInfos);
        HashSet<Texture> allTextures = timeline.getAllTextures();
        allTextures.add(Texture.getTexture(GameConfig.LevelUI.Lives.textureFilepath));
        for(var texture: allTextures){
            texture.loadInGPU();
        }
        this.timer.start();
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
            Entity newEntity = editorDataManager.buildCustomEntity(entitySpawn.id());
            if(entitySpawn.trajectoryId() != -1){
                newEntity.setTrajectory(editorDataManager.getTrajectory(entitySpawn.trajectoryId()));
            }
            newEntity.setTrajectoryStartingPosition(entitySpawn.startingPosition().x, entitySpawn.startingPosition().y);
            newEntity.setPosition(entitySpawn.startingPosition().x, entitySpawn.startingPosition().y);
            addEntity(newEntity);
            if(entitySpawn.id() == 0){
                playerShip = (Ship) newEntity;
            }
        }
        entitiesToSpawn.clear();

        for(var displaySpawn: displaysToSpawn){
            SceneDisplay newDisplay = editorDataManager.buildCustomDisplay(displaySpawn.id());
            newDisplay.setPosition(displaySpawn.position().x, displaySpawn.position().y);
            addDisplay(newDisplay);
        }
        displaysToSpawn.clear();

        for(Entity entity: goodEntities){
            entity.update(sceneTime);
        }
        for(Entity entity: evilEntities){
            entity.update(sceneTime);
        }
        removeFarAwayEntities(goodEntities);
        removeFarAwayEntities(evilEntities);
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
        updatePlayerLives();
        super.update();
    }

    public void addEntitySpawn(EntitySpawnInfo entitySpawnInfo){
        entitiesToSpawn.add(entitySpawnInfo);
    }

    public void addDisplaySpawn(SceneDisplaySpawnInfo displaySpawnInfo){
        displaysToSpawn.add(displaySpawnInfo);
    }

    public void addEntity(Entity entity){
        if(debugMode){
            RGBAValue hitboxColor;
            if(entity.getType() == EntityType.SHIP){
                if(entity.isEvil()){
                    hitboxColor = new RGBAValue(1.0f, 0.0f, 0.0f, 1.0f);
                }
                else{
                    hitboxColor = new RGBAValue(0.0f, 1.0f, 0.0f, 1.0f);
                }
            } else if (entity.getType() == EntityType.PROJECTILE) {
                if(entity.isEvil()){
                    hitboxColor = new RGBAValue(1.0f, 1.0f, 0.0f, 1.0f);
                }
                else{
                    hitboxColor = new RGBAValue(0.0f, 1.0f, 1.0f, 1.0f);
                }
            }else {
                hitboxColor = new RGBAValue(1.0f, 1.0f, 1.0f, 1.0f);
            }
            Hitbox entityHitbox = entity.getHitbox();
            switch (entityHitbox){
                case EmptyHitbox ignored -> {
                }
                case SimpleRectangleHitbox simpleRectangleHitbox -> entity.addExtraComponent(new HitboxDebugDisplay(simpleRectangleHitbox, hitboxColor.r, hitboxColor.g, hitboxColor.b, hitboxColor.a));
                case CompositeHitbox compositeHitbox -> {
                    for(Hitbox rectangle: compositeHitbox.getRectangleList()){
                        if(rectangle instanceof SimpleRectangleHitbox simpleRectangle){
                            entity.addExtraComponent(new HitboxDebugDisplay(simpleRectangle, hitboxColor.r, hitboxColor.g, hitboxColor.b, hitboxColor.a));
                        }
                    }
                }
            }
        }
        Optional<Graphic<?, ?>> entityGraphic = entity.getSprite().getGraphic();
        if(entityGraphic.isPresent()){
            Graphic<?, ?> newGraphic = entityGraphic.orElseThrow();
            addGraphic(newGraphic);
        }
        List<ExtraComponent> extraComponentsList = entity.getExtraComponents();
        for(var component: extraComponentsList){
            List<Graphic<?,?>> graphicsList = component.getGraphics();
            for(var graphic: graphicsList){
                addGraphic(graphic);
            }
        }
        if(entity.isEvil()){
            evilEntities.add(entity);
        }
        else{
            goodEntities.add(entity);
        }
        entity.setScene(this);
    }

    public void deleteEntity(Entity entity){
        if(entity.isEvil()){
            evilEntities.remove(entity);
        }
        else{
            goodEntities.remove(entity);
        }
        entity.getSprite().getGraphic().ifPresent(Graphic::delete);
        List<ExtraComponent> extraComponentsList = entity.getExtraComponents();
        for(var component: extraComponentsList){
            List<Graphic<?,?>> graphicsList = component.getGraphics();
            for(var graphic: graphicsList){
                graphic.delete();
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

    public void removeFarAwayEntities(HashSet<Entity> entityList){
        for(Entity entity: entityList){
            Vec2D position = entity.getPosition();
            if(position.x < -0.5f || position.x > 1.5f || position.y < -0.5f || position.y > 2.0f){
                entitiesToRemove.add(entity);
            }
        }
    }

    public void handleCollisions(Entity entity, HashSet<Entity> ennemyList) {
        if (entity.isInvincible()) {
            return;
        }
        Hitbox entityHitbox = entity.getHitbox();
        if(entityHitbox == EmptyHitbox.getInstance()){
            return;
        }
        if (entity.getType() == EntityType.PROJECTILE) {
            for (Entity ennemy : ennemyList) {
                if(ennemy.getType() == EntityType.PROJECTILE){
                    continue;
                }
                Ship ennemyShip = (Ship) ennemy;
                Hitbox ennemyHitbox = ennemyShip.getHitbox();
                if (Hitbox.intersection(entityHitbox, ennemyHitbox)) {
                    entity.deathEvent();
                    entitiesToRemove.add(entity);
                }
            }
        } else if(entity.getType() == EntityType.SHIP){
            Ship shipEntity = (Ship) entity;
            for (Entity ennemy : ennemyList) {
                Hitbox ennemyHitbox = ennemy.getHitbox();
                if (Hitbox.intersection(entityHitbox, ennemyHitbox)) {
                    shipEntity.takeDamage(1);
                    if(shipEntity.isDead()){
                        shipEntity.deathEvent();
                        entitiesToRemove.add(entity);
                    }
                }
            }
        }
    }

    protected void updatePlayerLives(){
        if(playerShip != null){
            int playerHP = playerShip.getHP();
            if(playerLives.size() != playerHP){
                while(playerLives.size() < playerHP){
                    Vec2D size = GameConfig.LevelUI.Lives.size;
                    StaticImage hpPoint = new StaticImage(GameConfig.LevelUI.Lives.textureFilepath, GameConfig.LevelUI.upperLayer, size.x, size.y);
                    Vec2D position = GameConfig.LevelUI.Lives.position;
                    Vec2D stride = GameConfig.LevelUI.Lives.stride;
                    float pointPositionX = position.x + stride.x * playerLives.size();
                    float pointPositionY = position.y + stride.y * playerLives.size();
                    hpPoint.setPosition(pointPositionX, pointPositionY);
                    addGraphic(hpPoint);
                    playerLives.add(hpPoint);
                }
                while(playerLives.size() > playerHP){
                    playerLives.getLast().delete();
                    playerLives.removeLast();
                }
            }
        }
    }
}
