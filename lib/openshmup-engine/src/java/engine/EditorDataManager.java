package engine;

import engine.entity.Entity;
import engine.entity.Trajectory;
import engine.graphics.AnimationInfo;
import engine.scene.LevelScene;
import engine.scene.LevelTimeline;
import json.GameDataLoader;
import pl.joegreen.lambdaFromString.LambdaCreationException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class EditorDataManager {
    private HashMap<Integer, Function<LevelScene, Entity>> customEntities;
    private HashMap<Integer, Trajectory> customTrajectories;
    private ArrayList<LevelTimeline> customLevels;

    public EditorDataManager(){
        this.customEntities = new HashMap<>();
        Function<LevelScene, Entity> entityConstructor1 = (scene) ->{
            AnimationInfo testAnimInfo = new AnimationInfo("src/resources/textures/enemy-medium.png", 2, 32, 16, 0, 0, 32, 0);
            return new Entity.Builder().setScene(scene)
                    .setSize(0.5f,0.5f)
                    .createSprite(3,testAnimInfo, 0.25f, true, false)
                    .createFixedTrajectory(t-> 0.3f * (float) cos(t) + 0.5f, t-> 0.3f * (float) sin(t) + 0.5f, false)
                    .setId(1)
                    .build();
        };
        addCustomEntity(1, entityConstructor1);

        this.customTrajectories = new HashMap<>();
        GameDataLoader gameDataLoader = new GameDataLoader();
        this.customLevels = new ArrayList<>();
        try {
            gameDataLoader.loadCustomTrajectories(GlobalVars.Paths.editorCustomTrajectoriesFile, this);
            gameDataLoader.loadCustomEntities(GlobalVars.Paths.editorCustomEntitiesFile, this);
            gameDataLoader.loadCustomTimeline(GlobalVars.Paths.editorCustomTimelineFile, this);
        } catch (FileNotFoundException | IllegalArgumentException | LambdaCreationException e) {
            throw new RuntimeException(e);
        }
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
        customLevels.add(timeline);
    }

    public LevelTimeline getTimeline(int index){
        return customLevels.get(index);
    }
}
