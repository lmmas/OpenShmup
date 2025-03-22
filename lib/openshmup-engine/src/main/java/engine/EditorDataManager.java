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
import java.util.function.Supplier;

public class EditorDataManager {
    private HashMap<Integer, SceneVisual> customVisuals;
    private HashMap<Integer, Trajectory> customTrajectories;
    private HashMap<Integer, Supplier<Entity>> customEntities;
    private ArrayList<LevelTimeline> customTimelines;

    public EditorDataManager(){
        this.customVisuals = new HashMap<>();
        this.customEntities = new HashMap<>();
        this.customTrajectories = new HashMap<>();
        this.customTimelines = new ArrayList<>();
    }

    void loadGameParameters(){
        EditorDataLoader editorDataLoader = new EditorDataLoader();
        try {
            editorDataLoader.loadGameParameters(GlobalVars.Paths.customGameParametersFile);
        } catch (FileNotFoundException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    void loadGameContents(){
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

    public void addCustomVisual(int id, SceneVisual visual){
        customVisuals.put(id, visual);
    }

    public SceneVisual buildCustomVisual(int id){
        return customVisuals.get(id).copy();
    }

    public void addCustomEntity(int id, Supplier<Entity> constructor){
        customEntities.put(id, constructor);
    }

    public Entity buildCustomEntity(int id){
        return customEntities.get(id).get();
    }

    public void addTrajectory(int id, Trajectory trajectory){
        customTrajectories.put(id, trajectory);
    }

    public Trajectory getTrajectory(int id){
        Trajectory trajectory = customTrajectories.get(id);
        return trajectory.copyIfNotReusable();
    }

    public void addTimeline(LevelTimeline timeline){
        customTimelines.add(timeline);
    }

    public LevelTimeline getTimeline(int index){
        return customTimelines.get(index);
    }
}
