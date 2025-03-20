package engine;

import engine.entity.Entity;
import engine.entity.trajectory.Trajectory;
import engine.scene.LevelScene;
import engine.scene.LevelTimeline;
import engine.scene.Scene;
import engine.scene.visual.SceneVisual;
import json.EditorDataLoader;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public class EditorDataManager {
    private HashMap<Integer, Function<Scene, SceneVisual>> customVisuals;
    private HashMap<Integer, Trajectory> customTrajectories;
    private HashMap<Integer, Function<LevelScene, Entity>> customEntities;
    private ArrayList<LevelTimeline> customTimelines;

    public EditorDataManager(){
        this.customVisuals = new HashMap<>();
        this.customEntities = new HashMap<>();
        this.customTrajectories = new HashMap<>();
        this.customTimelines = new ArrayList<>();
        EditorDataLoader editorDataLoader = new EditorDataLoader();
        try {
            editorDataLoader.loadCustomVisuals(GlobalVars.Paths.editorCustomVisualsFile, this);
            editorDataLoader.loadCustomTrajectories(GlobalVars.Paths.editorCustomTrajectoriesFile, this);
            editorDataLoader.loadCustomEntities(GlobalVars.Paths.editorCustomEntitiesFile, this);
            editorDataLoader.loadCustomTimeline(GlobalVars.Paths.editorCustomTimelineFile, this);
        } catch (FileNotFoundException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public void addCustomVisual(int id, Function<Scene, SceneVisual> constructor){
        customVisuals.put(id, constructor);
    }

    public SceneVisual buildCustomVisual(Scene scene, int id){
        return customVisuals.get(id).apply(scene);
    }

    public void addCustomEntity(int id, Function<LevelScene, Entity> constructor){
        customEntities.put(id, constructor);
    }

    public Entity buildCustomEntity(LevelScene scene, int id){
        return customEntities.get(id).apply(scene);
    }

    public void addTrajectory(int id, Trajectory trajectory){
        customTrajectories.put(id, trajectory);
    }

    public Trajectory getTrajectory(int id){
        Trajectory trajectory = customTrajectories.get(id);
        return customTrajectories.get(id).copyIfNotReusable();
    }

    public void addTimeline(LevelTimeline timeline){
        customTimelines.add(timeline);
    }

    public LevelTimeline getTimeline(int index){
        return customTimelines.get(index);
    }
}
