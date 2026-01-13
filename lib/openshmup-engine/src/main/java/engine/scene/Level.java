package engine.scene;

import engine.Engine;
import engine.EngineSystem;
import engine.Game;
import engine.Timer;
import engine.assets.Texture;
import engine.entity.Entity;
import engine.entity.EntityType;
import engine.entity.Ship;
import engine.entity.extraComponent.ExtraComponent;
import engine.entity.extraComponent.HitboxDebugRectangle;
import engine.entity.hitbox.CompositeHitbox;
import engine.entity.hitbox.EmptyHitbox;
import engine.entity.hitbox.Hitbox;
import engine.entity.hitbox.SimpleRectangleHitbox;
import engine.gameData.GameConfig;
import engine.gameData.GameDataManager;
import engine.scene.menu.GameMenus;
import engine.scene.menu.MenuScreen;
import engine.scene.spawnable.EntitySpawnInfo;
import engine.scene.spawnable.SceneDisplaySpawnInfo;
import engine.types.GameControl;
import engine.types.RGBAValue;
import engine.types.Vec2D;
import engine.visual.SceneVisual;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static engine.Engine.assetManager;
import static engine.Engine.inputStatesManager;

final public class Level implements EngineSystem {

    private Scene scene;

    final private HashSet<Entity> goodEntities;

    final private HashSet<Entity> evilEntities;

    final private HashSet<EntitySpawnInfo> entitiesToSpawn;

    final private HashSet<Entity> entitiesToRemove;

    final private HashSet<SceneDisplaySpawnInfo> displaysToSpawn;

    private List<Boolean> controlStates;

    private List<Boolean> lastControlStates;
    @Getter
    final private GameDataManager gameDataManager;

    final private GameConfig gameConfig;

    private final LevelTimeline timeline;

    final private LevelUI levelUI;

    final private MenuScreen pauseMenu;

    final private MenuScreen gameOverScreen;

    final private LevelDebug levelDebug;

    private boolean debugModeEnabled;

    private Timer timer;

    public Level(Scene scene, LevelTimeline timeline) {
        this.scene = scene;
        this.timeline = timeline;
        this.gameDataManager = timeline.getGameDataManager();
        this.gameConfig = gameDataManager.config;
        this.goodEntities = new HashSet<>();
        this.evilEntities = new HashSet<>();
        this.entitiesToSpawn = new HashSet<>();
        this.entitiesToRemove = new HashSet<>();
        this.displaysToSpawn = new HashSet<>();
        this.controlStates = new ArrayList<Boolean>(Collections.nCopies(GameControl.values().length, Boolean.FALSE));
        this.lastControlStates = new ArrayList<Boolean>(Collections.nCopies(GameControl.values().length, Boolean.FALSE));
        this.levelUI = new LevelUI(gameConfig.levelUI, scene);

        this.pauseMenu = GameMenus.PauseMenu(gameConfig.pauseMenuLayer);
        this.gameOverScreen = pauseMenu;
        this.levelDebug = new LevelDebug(false);
        this.debugModeEnabled = false;
        this.timer = new Timer();
    }

    public void start() {
        loadAssets();
        scene.start();
        timer.start();
    }

    void loadAssets() {
        //graphicsManager.constructRenderers(timelineRenderInfos);
        HashSet<Texture> allTextures = timeline.getAllTextures();
        allTextures.add(assetManager.getTexture(gameConfig.levelUI.lives.textureFilepath));
        for (var texture : allTextures) {
            if (!texture.isLoadedInGPU()) {
                texture.loadInGPU();
            }
        }
    }

    public void handleInputs() {
        controlStates = inputStatesManager.getGameControlStates();
        if (getControlPress(GameControl.PAUSE)) {
            if (timer.isPaused()) {
                timer.resume();
                scene.removeMenu(pauseMenu);
            }
            else {
                timer.pause();
                scene.addMenu(pauseMenu);
            }
        }
        if (getControlPress(GameControl.SLOWDOWN)) {
            timer.setSpeed(0.5f);
        }
        if (getControlUnpress(GameControl.SLOWDOWN)) {
            timer.setSpeed(1.0f);
        }
        if (getControlPress(GameControl.TOGGLE_DEBUG)) {
            this.toggleDebug();
        }
        this.lastControlStates = controlStates;
    }

    private void toggleDebug() {
        levelDebug.toggle();
        this.debugModeEnabled = !debugModeEnabled;
    }

    @Override
    public void update() {
        if (timer.isPaused()) {
            return;
        }
        Game.setLevelTime(timer.getTimeSeconds());
        handleInputs();
        timeline.updateSpawning(this);
        spawnEntities();
        spawnDisplays();
        double levelTime = timer.getTimeSeconds();
        for (Entity entity : goodEntities) {
            entity.update(levelTime);
        }
        for (Entity entity : evilEntities) {
            entity.update(levelTime);
        }
        removeFarAwayEntities(goodEntities);
        removeFarAwayEntities(evilEntities);
        for (Entity entity : evilEntities) {
            handleCollisions(entity, goodEntities);
        }
        for (Entity entity : goodEntities) {
            handleCollisions(entity, evilEntities);
        }
        for (Entity entity : entitiesToRemove) {
            deleteEntity(entity);
        }
        entitiesToRemove.clear();
        levelUI.update();
    }

    public void addDisplaySpawn(SceneDisplaySpawnInfo displaySpawnInfo) {
        displaysToSpawn.add(displaySpawnInfo);
    }

    private void spawnDisplays() {
        for (var displaySpawn : displaysToSpawn) {
            SceneVisual newDisplay = gameDataManager.getGameVisual(displaySpawn.id());
            newDisplay.setPosition(displaySpawn.position().x, displaySpawn.position().y);
            scene.addVisual(newDisplay);
        }
        displaysToSpawn.clear();
    }

    public void addEntitySpawn(EntitySpawnInfo entitySpawnInfo) {
        entitiesToSpawn.add(entitySpawnInfo);
    }

    public void addEntity(Entity entity) {
        scene.addVisual(entity.getSprite());

        for (ExtraComponent component : entity.getExtraComponents()) {
            component.init();
        }
        if (debugModeEnabled) {
            LevelDebug.addHitboxDebugDisplay(entity);
        }
        if (entity.isEvil()) {
            evilEntities.add(entity);
        }
        else {
            goodEntities.add(entity);
        }
        entity.setLevel(this);
    }

    public void deleteEntity(Entity entity) {
        if (entity.isEvil()) {
            evilEntities.remove(entity);
        }
        else {
            goodEntities.remove(entity);
        }
        entity.getSprite().setShouldBeRemoved();
        List<ExtraComponent> extraComponentsList = entity.getExtraComponents();
        for (var component : extraComponentsList) {
            component.onRemove();
        }
    }

    private void spawnEntities() {
        for (var entitySpawn : entitiesToSpawn) {
            Entity newEntity = gameDataManager.buildCustomEntity(entitySpawn.id());
            if (entitySpawn.trajectoryId() != -1) {
                newEntity.setTrajectory(gameDataManager.getTrajectory(entitySpawn.trajectoryId()));
            }
            newEntity.setTrajectoryStartingPosition(entitySpawn.startingPosition().x, entitySpawn.startingPosition().y);
            newEntity.setPosition(entitySpawn.startingPosition().x, entitySpawn.startingPosition().y);
            addEntity(newEntity);
            if (entitySpawn.id() == gameDataManager.config.playerEntityId) {
                levelUI.setPlayerShip((Ship) newEntity);
            }
        }
        entitiesToSpawn.clear();
    }

    public boolean getControlState(GameControl control) {
        return controlStates.get(control.ordinal());
    }

    public boolean getControlPress(GameControl control) {
        return controlStates.get(control.ordinal()) && !lastControlStates.get(control.ordinal());
    }

    public boolean getControlUnpress(GameControl control) {
        return (!controlStates.get(control.ordinal())) && lastControlStates.get(control.ordinal());
    }

    public void removeFarAwayEntities(HashSet<Entity> entityList) {
        Vec2D resolution = new Vec2D(Engine.getNativeWidth(), Engine.getNativeHeight());
        for (Entity entity : entityList) {
            Vec2D position = entity.getPosition();
            if (position.x < -0.5f * resolution.x || position.x > 1.5f * resolution.x || position.y < -0.5f * resolution.y || position.y > 2.0f * resolution.y) {
                entitiesToRemove.add(entity);
            }
        }
    }

    public void handleCollisions(Entity entity, HashSet<Entity> ennemyList) {
        if (entity.isInvincible()) {
            return;
        }
        Hitbox entityHitbox = entity.getHitbox();
        if (entityHitbox == Hitbox.DEFAULT_EMPTY()) {
            return;
        }
        if (entity.getType() == EntityType.PROJECTILE) {
            for (Entity ennemy : ennemyList) {
                if (ennemy.getType() == EntityType.PROJECTILE) {
                    continue;
                }
                Ship ennemyShip = (Ship) ennemy;
                Hitbox ennemyHitbox = ennemyShip.getHitbox();
                if (Hitbox.intersection(entityHitbox, ennemyHitbox)) {
                    entity.deathEvent();
                    entitiesToRemove.add(entity);
                }
            }
        }
        else if (entity.getType() == EntityType.SHIP) {
            Ship shipEntity = (Ship) entity;
            for (Entity ennemy : ennemyList) {
                Hitbox ennemyHitbox = ennemy.getHitbox();
                if (Hitbox.intersection(entityHitbox, ennemyHitbox)) {
                    shipEntity.takeDamage(1);
                    if (shipEntity.isDead()) {
                        shipEntity.deathEvent();
                        entitiesToRemove.add(entity);
                        if (!shipEntity.isEvil()) {
                            scene.addMenu(gameOverScreen);
                            timer.pause();
                        }
                    }
                }
            }
        }
    }

    private static void addComponent(Entity entity, ExtraComponent component) {
        component.init();
        entity.addExtraComponent(component);
    }

    private static void deleteComponent(Entity entity, ExtraComponent extraComponent) {
        extraComponent.onRemove();
        entity.getExtraComponents().remove(extraComponent);
    }

    public double getLevelTimeSeconds() {
        return timer.getTimeSeconds();
    }

    private class LevelDebug {

        public LevelDebug(boolean debugModeEnabled) {
            if (debugModeEnabled) {
                this.enable();
            }
        }

        private static void addHitboxDebugDisplay(Entity entity) {
            RGBAValue hitboxColor = getHitboxDebugDisplayColor(entity);
            Hitbox entityHitbox = entity.getHitbox();
            switch (entityHitbox) {
                case EmptyHitbox ignored -> {
                }
                case SimpleRectangleHitbox simpleRectangleHitbox -> {
                    HitboxDebugRectangle debugDisplay = new HitboxDebugRectangle(simpleRectangleHitbox, hitboxColor);
                    addComponent(entity, debugDisplay);
                }
                case CompositeHitbox compositeHitbox -> {
                    for (Hitbox rectangle : compositeHitbox.getRectangleList()) {
                        if (rectangle instanceof SimpleRectangleHitbox simpleRectangle) {
                            HitboxDebugRectangle debugDisplay = new HitboxDebugRectangle(simpleRectangle, hitboxColor);
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
                }
                else {
                    hitboxColor = new RGBAValue(0.0f, 1.0f, 0.0f, 1.0f);
                }
            }
            else if (entity.getType() == EntityType.PROJECTILE) {
                if (entity.isEvil()) {
                    hitboxColor = new RGBAValue(1.0f, 1.0f, 0.0f, 1.0f);
                }
                else {
                    hitboxColor = new RGBAValue(0.0f, 1.0f, 1.0f, 1.0f);
                }
            }
            else {
                hitboxColor = new RGBAValue(1.0f, 1.0f, 1.0f, 1.0f);
            }
            return hitboxColor;
        }

        private void enable() {
            for (Entity entity : goodEntities) {
                addHitboxDebugDisplay(entity);
            }
            for (Entity entity : evilEntities) {
                addHitboxDebugDisplay(entity);
            }
        }

        private void disable() {
            for (Entity entity : evilEntities) {
                ArrayList<HitboxDebugRectangle> debugDisplaysToRemove = new ArrayList<>();
                for (ExtraComponent extraComponent : entity.getExtraComponents()) {
                    if (extraComponent instanceof HitboxDebugRectangle hitboxDebugRectangle) {
                        debugDisplaysToRemove.add(hitboxDebugRectangle);
                    }
                }
                debugDisplaysToRemove.forEach(hitboxDebugRectangle -> Level.deleteComponent(entity, hitboxDebugRectangle));
            }
            for (Entity entity : goodEntities) {
                ArrayList<HitboxDebugRectangle> debugDisplaysToRemove = new ArrayList<>();
                for (ExtraComponent extraComponent : entity.getExtraComponents()) {
                    if (extraComponent instanceof HitboxDebugRectangle hitboxDebugRectangle) {
                        debugDisplaysToRemove.add(hitboxDebugRectangle);
                    }
                }
                debugDisplaysToRemove.forEach(hitboxDebugRectangle -> Level.deleteComponent(entity, hitboxDebugRectangle));
            }
        }

        public void toggle() {
            if (debugModeEnabled) {
                this.disable();
            }
            else {
                this.enable();
            }
        }
    }

}
