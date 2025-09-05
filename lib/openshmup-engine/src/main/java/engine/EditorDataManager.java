package engine;

import engine.entity.Entity;
import engine.entity.extraComponent.ExtraComponent;
import engine.entity.trajectory.Trajectory;
import engine.render.RenderInfo;
import engine.assets.Texture;
import engine.scene.LevelTimeline;
import engine.scene.display.SceneVisual;
import engine.scene.spawnable.Spawnable;
import json.EditorDataLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

final public class EditorDataManager {
    private final HashMap<Integer, SceneVisual> customDisplays;
    private final HashMap<Integer, Trajectory> customTrajectories;
    private final HashMap<Integer, Entity> customEntities;
    private final ArrayList<LevelTimeline> customTimelines;

    public EditorDataManager(Engine engine){
        this.customDisplays = new HashMap<>();
        this.customEntities = new HashMap<>();
        this.customTrajectories = new HashMap<>();
        this.customTimelines = new ArrayList<>();
    }

    void loadGameParameters(){
        EditorDataLoader editorDataLoader = new EditorDataLoader();
        try {
            editorDataLoader.loadGameParameters(GlobalVars.Paths.customGameParametersFile);
        } catch (IllegalArgumentException e) {
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
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public void addCustomDisplays(int id, SceneVisual display){
        customDisplays.put(id, display);
    }

    public List<RenderInfo> getRenderInfosOfDisplay(int id){
        return customDisplays.get(id).getRenderInfos();
    }

    public SceneVisual buildCustomDisplay(int id){
        return customDisplays.get(id).copy();
    }

    public List<Texture> getTexturesOfDisplay(int id){
        return customDisplays.get(id).getTextures();
    }

    public void addCustomEntity(int id, Entity entity){
        customEntities.put(id, entity);
    }

    public Entity buildCustomEntity(int id){
        return customEntities.get(id).copy();
    }

    public ArrayList<Spawnable> getSpawnablesOfEntity(int id){
        Entity entity = customEntities.get(id);
        ArrayList<Spawnable> spawnablesList = new ArrayList<>();
        for (ExtraComponent component: entity.getExtraComponents()){
            spawnablesList.add(component.getSpawnable());
        }
        spawnablesList.add(entity.getDeathSpawn());
        return spawnablesList;
    }

    public List<RenderInfo> getRenderInfosOfEntity(int id){
        Entity entity = customEntities.get(id);
        return entity.getSprite().getRenderInfos();
    }

    public List<Texture> getTexturesOfEntity(int id){
        return customEntities.get(id).getSprite().getTextures();
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
