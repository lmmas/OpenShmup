package engine.gameData;

import engine.assets.Texture;
import engine.entity.Entity;
import engine.entity.extraComponent.ExtraComponent;
import engine.entity.trajectory.Trajectory;
import engine.scene.LevelTimeline;
import engine.scene.spawnable.Spawnable;
import engine.visual.SceneVisual;
import json.factories.GameFactory;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

final public class GameDataManager {

    final private String gameFolderName;

    final public GamePaths paths;

    public GameConfig config;

    @Getter
    final private HashMap<Integer, SceneVisual> visuals;

    @Getter
    final private HashMap<Integer, Trajectory> trajectories;

    @Getter
    final private HashMap<Integer, Entity> entities;

    @Getter
    final private ArrayList<LevelTimeline> timelines;

    public GameDataManager(String gameFolderName) {
        this.gameFolderName = gameFolderName;
        this.paths = new GamePaths(gameFolderName);
        this.config = new GameConfig();
        this.visuals = new HashMap<>();
        this.entities = new HashMap<>();
        this.trajectories = new HashMap<>();
        this.timelines = new ArrayList<>();
    }

    public void loadGameConfig() {
        GameFactory gameFactory = new GameFactory();
        try {
            //gameDataLoader.loadGameConfig(paths.gameConfigFile);
            gameFactory.loadGameConfig(this);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadGameContents() {
        GameFactory gameFactory = new GameFactory();
        try {
            //gameDataLoader.loadGameVisuals(paths.gameVisualsFile);
            gameFactory.loadGameVisuals(this);
            //gameDataLoader.loadGameTrajectories(paths.gameTrajectoriesFile);
            gameFactory.loadGameTrajectories(this);
            //gameDataLoader.loadGameEntities(paths.gameEntitiesFile);
            gameFactory.loadGameEntities(this);
            //gameDataLoader.loadGameTimeline(paths.gameTimelineFile);
            gameFactory.loadGameTimelines(this);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public void addCustomVisual(int id, SceneVisual visual) {
        visuals.put(id, visual);
    }

    public SceneVisual getGameVisual(int id) {
        return visuals.get(id).copy();
    }

    public List<Texture> getTexturesOfDisplay(int id) {
        return visuals.get(id).getTextures();
    }

    public void addCustomEntity(int id, Entity entity) {
        entities.put(id, entity);
    }

    public Entity buildCustomEntity(int id) {
        return entities.get(id).copy();
    }

    public ArrayList<Spawnable> getSpawnablesOfEntity(int id) {
        Entity entity = entities.get(id);
        ArrayList<Spawnable> spawnablesList = new ArrayList<>();
        for (ExtraComponent component : entity.getExtraComponents()) {
            spawnablesList.addAll(component.getSpawnables());
        }
        spawnablesList.addAll(entity.getDeathSpawn());
        return spawnablesList;
    }

    public List<Texture> getTexturesOfEntity(int id) {
        return entities.get(id).getSprite().getTextures();
    }

    public void addTrajectory(int id, Trajectory trajectory) {
        trajectories.put(id, trajectory);
    }

    public Trajectory getTrajectory(int id) {
        Trajectory trajectory = trajectories.get(id);
        return trajectory.copyIfNotReusable();
    }

    public void addTimeline(LevelTimeline timeline) {
        timelines.add(timeline);
    }

    public LevelTimeline getTimeline(int index) {
        return timelines.get(index);
    }

    public String getGameName() {
        return gameFolderName;
    }

}
