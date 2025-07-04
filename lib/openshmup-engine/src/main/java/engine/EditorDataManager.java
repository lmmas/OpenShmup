package engine;

import engine.entity.Entity;
import engine.entity.extraComponent.ExtraComponent;
import engine.entity.trajectory.Trajectory;
import engine.render.RenderInfo;
import engine.assets.Texture;
import engine.scene.LevelTimeline;
import engine.scene.display.SceneDisplay;
import engine.scene.spawnable.Spawnable;
import json.EditorDataLoader;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class EditorDataManager {
    final private Engine engine;
    private final HashMap<Integer, SceneDisplay> customDisplays;
    private final HashMap<Integer, Trajectory> customTrajectories;
    private final HashMap<Integer, Entity> customEntities;
    private final ArrayList<LevelTimeline> customTimelines;

    public EditorDataManager(Engine engine){
        this.engine = engine;
        this.customDisplays = new HashMap<>();
        this.customEntities = new HashMap<>();
        this.customTrajectories = new HashMap<>();
        this.customTimelines = new ArrayList<>();
    }

    void loadGameParameters(){
        EditorDataLoader editorDataLoader = new EditorDataLoader(engine);
        try {
            editorDataLoader.loadGameParameters(GlobalVars.Paths.customGameParametersFile);
        } catch (FileNotFoundException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    void loadGameContents(){
        EditorDataLoader editorDataLoader = new EditorDataLoader(engine);
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

    public Optional<RenderInfo> getRenderInfoOfDisplay(int id){
        return customDisplays.get(id).getRenderInfo();
    }

    public SceneDisplay buildCustomDisplay(int id){
        return customDisplays.get(id).copy();
    }

    public Optional<Texture> getTextureOfDisplay(int id){
        return customDisplays.get(id).getTexture();
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

    public ArrayList<RenderInfo> getRenderInfoOfEntity(int id){
        ArrayList<RenderInfo> renderInfoList = new ArrayList<>();
        Entity entity = customEntities.get(id);
        Optional<RenderInfo> renderInfoOptional = entity.getSprite().getRenderInfo();
        if(renderInfoOptional.isPresent()){
            renderInfoList.add(renderInfoOptional.orElseThrow());
        }
        return renderInfoList;
    }

    public Optional<Texture> getTextureOfEntity(int id){
        return customEntities.get(id).getSprite().getTexture();
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
