package engine;

import engine.entity.Entity;
import engine.entity.trajectory.Trajectory;
import engine.scene.LevelTimeline;
import engine.scene.display.SceneDisplay;
import json.EditorDataLoader;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class EditorDataManager {
    private final HashMap<Integer, SceneDisplay> customDisplays;
    private final HashMap<Integer, Trajectory> customTrajectories;
    private final HashMap<Integer, Entity> customEntities;
    private final ArrayList<LevelTimeline> customTimelines;

    public EditorDataManager(){
        this.customDisplays = new HashMap<>();
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
            editorDataLoader.loadCustomDisplays(GlobalVars.Paths.editorCustomDisplaysFile, this);
            editorDataLoader.loadCustomTrajectories(GlobalVars.Paths.editorCustomTrajectoriesFile, this);
            editorDataLoader.loadCustomEntities(GlobalVars.Paths.editorCustomEntitiesFile, this);
            editorDataLoader.loadCustomTimeline(GlobalVars.Paths.editorCustomTimelineFile, this);
        } catch (FileNotFoundException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public void addCustomDisplays(int id, SceneDisplay display){
        customDisplays.put(id, display);
    }

    public SceneDisplay buildCustomDisplay(int id){
        return customDisplays.get(id).copy();
    }

    public void addCustomEntity(int id, Entity constructor){
        customEntities.put(id, constructor);
    }

    public Entity buildCustomEntity(int id){
        return customEntities.get(id).copy();
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
