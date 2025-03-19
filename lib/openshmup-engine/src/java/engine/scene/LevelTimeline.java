package engine.scene;

import engine.EditorDataManager;
import engine.Game;
import engine.entity.EntitySpawnInfo;
import engine.entity.Trajectory;

import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeMap;

public class LevelTimeline {
    final private EditorDataManager editorDataManager;
    private final float levelDuration;
    private TreeMap<Float, ArrayList<Spawnable>> spawnList;
    private float nextSpawnTime;
    public LevelTimeline(EditorDataManager editorDataManager, float levelDuration){
        this.editorDataManager = editorDataManager;
        this.levelDuration= levelDuration;
        this.spawnList = new TreeMap<>();
        this.nextSpawnTime = -1.0f;
    }

    public void updateSpawning(LevelScene scene){
        float currentTime = scene.getSceneTime();
        if(currentTime >= nextSpawnTime && currentTime < levelDuration){
            while(currentTime >= nextSpawnTime){
                ArrayList<Spawnable> spawnables = spawnList.get(nextSpawnTime);
                assert spawnables != null :"bad spawnList access";
                for(Spawnable spawnable: spawnables){
                    spawnable.spawn(scene);
                }
                if(spawnList.higherKey(nextSpawnTime) != null){
                    nextSpawnTime = spawnList.higherKey(nextSpawnTime);
                }
                else{
                    nextSpawnTime = levelDuration;
                }
            }
        }
    }

    public Optional<Float> getNextSpawnTime(float currentTime){
        return spawnList.higherKey(currentTime).describeConstable();
    }

    public void addSpawnable(float time, Spawnable spawnable){
        this.spawnList.computeIfAbsent(time, k -> new ArrayList<Spawnable>());
        this.spawnList.get(time).add(spawnable);
        nextSpawnTime = spawnList.higherKey(-1.0f);
    }

    public void addEntity(float time, int id, float startingPositionX, float startingPositionY, Trajectory trajectory){
        EntitySpawnInfo entitySpawnInfo = new EntitySpawnInfo(editorDataManager, id, startingPositionX, startingPositionY, trajectory);
        addSpawnable(time, entitySpawnInfo);
    }

    public void addEntity(float time, int id, float startingPositionX, float startingPositionY){
        EntitySpawnInfo entitySpawnInfo = new EntitySpawnInfo(editorDataManager, id, startingPositionX, startingPositionY);
        addSpawnable(time, entitySpawnInfo);
    }
}
