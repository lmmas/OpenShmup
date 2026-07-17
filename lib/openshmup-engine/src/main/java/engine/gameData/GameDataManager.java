package engine.gameData;

import engine.assets.Texture;
import engine.level.LevelTimeline;
import engine.level.entity.Entity;
import engine.level.entity.extraComponent.ExtraComponent;
import engine.level.entity.trajectory.Trajectory;
import engine.level.spawnable.Spawnable;
import engine.scene.visual.SceneVisual;
import lombok.Getter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

final public class GameDataManager {

    @Getter
    final private String gameName;

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

    public GameDataManager(Path gameFolderPath, String gameName) {
        this.gameName = gameName;
        this.paths = new GamePaths(gameFolderPath);
        this.config = new GameConfig();
        this.visuals = new HashMap<>();
        this.entities = new HashMap<>();
        this.trajectories = new HashMap<>();
        this.timelines = new ArrayList<>();
    }

    public void addVisual(int id, SceneVisual visual) {
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

}
