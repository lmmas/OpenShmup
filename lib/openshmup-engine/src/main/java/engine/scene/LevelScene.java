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
import engine.render.RenderInfo;
import engine.graphics.RenderType;
import engine.assets.Texture;
import engine.scene.spawnable.EntitySpawnInfo;
import engine.scene.spawnable.SceneDisplaySpawnInfo;
import engine.scene.display.SceneDisplay;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import java.util.*;

import static engine.Engine.*;

public class LevelScene extends Scene{
    protected HashSet<Entity> goodEntities;
    protected HashSet<Entity> evilEntities;
    protected HashSet<EntitySpawnInfo> entitiesToSpawn;
    protected HashSet<Entity> entitiesToRemove;
    protected HashSet<SceneDisplaySpawnInfo> displaysToSpawn;
    protected List<Boolean> controlStates;
    protected List<Boolean> lastControlStates;
    protected LevelTimeline timeline;
    final protected LevelUI levelUI;
    final private LevelDebug levelDebug;

    public LevelScene(LevelTimeline timeline, boolean debugMode) {
        super(debugMode);
        this.goodEntities = new HashSet<>();
        this.evilEntities = new HashSet<>();
        this.entitiesToSpawn = new HashSet<>();
        this.entitiesToRemove = new HashSet<>();
        this.displaysToSpawn = new HashSet<>();
        this.controlStates = new ArrayList<Boolean>(Collections.nCopies(GameControl.values().length, Boolean.FALSE));
        this.lastControlStates = new ArrayList<Boolean>(Collections.nCopies(GameControl.values().length, Boolean.FALSE));
        this.levelUI = new LevelUI(this);
        this.timeline = timeline;
        this.levelDebug = new LevelDebug(debugMode);
        loadAssets();
        this.timer.start();
    }

    void loadAssets(){
        HashSet<RenderInfo> timelineRenderInfos = timeline.getAllRenderInfos();
        timelineRenderInfos.add(new RenderInfo(GameConfig.LevelUI.contentsLayer, RenderType.STATIC_IMAGE));
        graphicsManager.constructRenderers(timelineRenderInfos);
        HashSet<Texture> allTextures = timeline.getAllTextures();
        allTextures.add(assetManager.getTexture(GameConfig.LevelUI.Lives.textureFilepath));
        for(var texture: allTextures){
            if(!texture.isLoadedInGPU()){
                texture.loadInGPU();
            }
        }
    }

    @Override
    public void handleInputs() {
        controlStates = inputStatesManager.getControlStates();
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
        if(getControlActivation(GameControl.TOGGLE_DEBUG)){
            this.toggleDebug();
        }
        this.lastControlStates = controlStates;
    }

    private void toggleDebug() {
        sceneDebug.toggle();
        levelDebug.toggle();
        this.debugModeEnabled = !debugModeEnabled;
    }

    @Override
    public void update() {
        handleInputs();
        if(timer.isPaused()){
            return;
        }
        timeline.updateSpawning(this);
        spawnEntities();
        spawnDisplays();

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
        levelUI.update();
        super.update();
    }

    public void addDisplaySpawn(SceneDisplaySpawnInfo displaySpawnInfo){
        displaysToSpawn.add(displaySpawnInfo);
    }

    private void spawnDisplays(){
        for(var displaySpawn: displaysToSpawn){
            SceneDisplay newDisplay = editorDataManager.buildCustomDisplay(displaySpawn.id());
            newDisplay.setPosition(displaySpawn.position().x, displaySpawn.position().y);
            addDisplay(newDisplay);
        }
        displaysToSpawn.clear();
    }

    public void addEntitySpawn(EntitySpawnInfo entitySpawnInfo){
        entitiesToSpawn.add(entitySpawnInfo);
    }

    public void addEntity(Entity entity){
        Optional<Graphic<?, ?>> spriteGraphic = entity.getSprite().getGraphic();
        if(spriteGraphic.isPresent()){
            Graphic<?, ?> newGraphic = spriteGraphic.orElseThrow();
            graphicsManager.addGraphic(newGraphic);
        }

        for(ExtraComponent component: entity.getExtraComponents()){
            List<Graphic<?,?>> graphicsList = component.getGraphics();
            for(var graphic: graphicsList){
                graphicsManager.addGraphic(graphic);
            }
        }
        if(debugModeEnabled){
            LevelDebug.addHitboxDebugDisplay(entity);
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

    private void spawnEntities(){
        for(var entitySpawn: entitiesToSpawn){
            Entity newEntity = editorDataManager.buildCustomEntity(entitySpawn.id());
            if(entitySpawn.trajectoryId() != -1){
                newEntity.setTrajectory(editorDataManager.getTrajectory(entitySpawn.trajectoryId()));
            }
            newEntity.setTrajectoryStartingPosition(entitySpawn.startingPosition().x, entitySpawn.startingPosition().y);
            newEntity.setPosition(entitySpawn.startingPosition().x, entitySpawn.startingPosition().y);
            addEntity(newEntity);
            if(entitySpawn.id() == 0){
                levelUI.setPlayerShip((Ship) newEntity);
            }
        }
        entitiesToSpawn.clear();
    }

    public boolean getControlState(GameControl control){
        return controlStates.get(control.ordinal());
    }

    public boolean getControlActivation(GameControl control){
        return controlStates.get(control.ordinal()) && !lastControlStates.get(control.ordinal());
    }

    public boolean getControlDeactivation(GameControl control){
        return (!controlStates.get(control.ordinal())) && lastControlStates.get(control.ordinal());
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

    private static void addComponent(Entity entity, ExtraComponent component){
        component.getGraphics().forEach(graphic -> graphicsManager.addGraphic(graphic));
        entity.addExtraComponent(component);
    }

    private static void deleteComponent(Entity entity, ExtraComponent extraComponent) {
        extraComponent.getGraphics().forEach(Graphic::delete);
        entity.getExtraComponents().remove(extraComponent);
    }

    private class LevelDebug{

        public LevelDebug(boolean debugModeEnabled){
            if(debugModeEnabled){
                this.enable();
            }
        }

        private static void addHitboxDebugDisplay(Entity entity){
            RGBAValue hitboxColor = getHitboxDebugDisplayColor(entity);
            Hitbox entityHitbox = entity.getHitbox();
            switch (entityHitbox) {
                case EmptyHitbox ignored -> {
                }
                case SimpleRectangleHitbox simpleRectangleHitbox -> {
                    HitboxDebugDisplay debugDisplay = new HitboxDebugDisplay(simpleRectangleHitbox, hitboxColor.r, hitboxColor.g, hitboxColor.b, hitboxColor.a);
                    addComponent(entity, debugDisplay);
                }
                case CompositeHitbox compositeHitbox -> {
                    for (Hitbox rectangle : compositeHitbox.getRectangleList()) {
                        if (rectangle instanceof SimpleRectangleHitbox simpleRectangle) {
                            HitboxDebugDisplay debugDisplay = new HitboxDebugDisplay(simpleRectangle, hitboxColor.r, hitboxColor.g, hitboxColor.b, hitboxColor.a);
                            addComponent(entity, debugDisplay);
                        }
                    }
                }
            }
        }

        private static RGBAValue getHitboxDebugDisplayColor(Entity entity) {
            RGBAValue hitboxColor;
            if (entity.getType() == EntityType.SHIP) {
                if (entity.isEvil()) {
                    hitboxColor = new RGBAValue(1.0f, 0.0f, 0.0f, 1.0f);
                } else {
                    hitboxColor = new RGBAValue(0.0f, 1.0f, 0.0f, 1.0f);
                }
            } else if (entity.getType() == EntityType.PROJECTILE) {
                if (entity.isEvil()) {
                    hitboxColor = new RGBAValue(1.0f, 1.0f, 0.0f, 1.0f);
                } else {
                    hitboxColor = new RGBAValue(0.0f, 1.0f, 1.0f, 1.0f);
                }
            } else {
                hitboxColor = new RGBAValue(1.0f, 1.0f, 1.0f, 1.0f);
            }
            return hitboxColor;
        }

        private void enable() {
            for(Entity entity: goodEntities){
                addHitboxDebugDisplay(entity);
            }
            for (Entity entity: evilEntities){
                addHitboxDebugDisplay(entity);
            }
        }

        private void disable() {
            for(Entity entity: evilEntities) {
                ArrayList<HitboxDebugDisplay> debugDisplaysToRemove = new ArrayList<>();
                for(ExtraComponent extraComponent: entity.getExtraComponents()){
                    if(extraComponent instanceof  HitboxDebugDisplay hitboxDebugDisplay){
                        debugDisplaysToRemove.add(hitboxDebugDisplay);
                    }
                }
                debugDisplaysToRemove.forEach(hitboxDebugDisplay -> LevelScene.deleteComponent(entity,hitboxDebugDisplay));
            }
            for(Entity entity: goodEntities){
                ArrayList<HitboxDebugDisplay> debugDisplaysToRemove = new ArrayList<>();
                for(ExtraComponent extraComponent: entity.getExtraComponents()){
                    if(extraComponent instanceof  HitboxDebugDisplay hitboxDebugDisplay){
                        debugDisplaysToRemove.add(hitboxDebugDisplay);
                    }
                }
                debugDisplaysToRemove.forEach(hitboxDebugDisplay -> LevelScene.deleteComponent(entity,hitboxDebugDisplay));
            }
        }

        public void toggle() {
            if(debugModeEnabled){
                this.disable();
            }
            else{
                this.enable();
            }
        }
    }

}
