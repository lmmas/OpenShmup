package engine.gameData;

import engine.assets.Texture;
import engine.entity.Entity;
import engine.entity.extraComponent.ExtraComponent;
import engine.entity.trajectory.Trajectory;
import engine.scene.LevelTimeline;
import engine.scene.spawnable.Spawnable;
import engine.visual.SceneVisual;
import json.GameDataLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

final public class GameDataManager {
    final private String gameFolderName;
    final public GamePaths paths;
    final public GameConfig config;
    final private HashMap<Integer, SceneVisual> customVisuals;
    final private HashMap<Integer, Trajectory> customTrajectories;
    final private HashMap<Integer, Entity> customEntities;
    final private ArrayList<LevelTimeline> customTimelines;

    public GameDataManager(String gameFolderName) {
        this.gameFolderName = gameFolderName;
        this.paths = new GamePaths(gameFolderName);
        this.config = new GameConfig();
        this.customVisuals = new HashMap<>();
        this.customEntities = new HashMap<>();
        this.customTrajectories = new HashMap<>();
        this.customTimelines = new ArrayList<>();
    }

    public void loadGameConfig() {
        GameDataLoader gameDataLoader = new GameDataLoader(this);
        try {
            gameDataLoader.loadGameConfig(paths.customGameConfigFile);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadGameContents() {
        GameDataLoader gameDataLoader = new GameDataLoader(this);
        try {
            gameDataLoader.loadCustomDisplays(paths.editorCustomDisplaysFile);
            gameDataLoader.loadCustomTrajectories(paths.editorCustomTrajectoriesFile);
            gameDataLoader.loadCustomEntities(paths.editorCustomEntitiesFile);
            gameDataLoader.loadCustomTimeline(paths.editorCustomTimelineFile);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public void addCustomVisual(int id, SceneVisual visual) {
        customVisuals.put(id, visual);
    }

    public SceneVisual buildCustomDisplay(int id) {
        return customVisuals.get(id).copy();
    }

    public List<Texture> getTexturesOfDisplay(int id) {
        return customVisuals.get(id).getTextures();
    }

    public void addCustomEntity(int id, Entity entity) {
        customEntities.put(id, entity);
    }

    public Entity buildCustomEntity(int id) {
        return customEntities.get(id).copy();
    }

    public ArrayList<Spawnable> getSpawnablesOfEntity(int id) {
        Entity entity = customEntities.get(id);
        ArrayList<Spawnable> spawnablesList = new ArrayList<>();
        for (ExtraComponent component : entity.getExtraComponents()) {
            spawnablesList.add(component.getSpawnable());
        }
        spawnablesList.add(entity.getDeathSpawn());
        return spawnablesList;
    }

    public List<Texture> getTexturesOfEntity(int id) {
        return customEntities.get(id).getSprite().getTextures();
    }

    public void addTrajectory(int id, Trajectory trajectory) {
        customTrajectories.put(id, trajectory);
    }

    public Trajectory getTrajectory(int id) {
        Trajectory trajectory = customTrajectories.get(id);
        return trajectory.copyIfNotReusable();
    }

    public void addTimeline(LevelTimeline timeline) {
        customTimelines.add(timeline);
    }

    public LevelTimeline getTimeline(int index) {
        return customTimelines.get(index);
    }
}
