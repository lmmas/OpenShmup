package engine.scene;

import engine.EditorDataManager;
import engine.entity.Entity;
import engine.render.RenderInfo;
import engine.scene.spawnable.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class LevelTimeline {
    final private EditorDataManager editorDataManager;
    private final float levelDuration;
    private final TreeMap<Float, ArrayList<Spawnable>> spawnList;
    private Float nextSpawnTime;
    public LevelTimeline(EditorDataManager editorDataManager, float levelDuration){
        this.editorDataManager = editorDataManager;
        this.levelDuration= levelDuration;
        this.spawnList = new TreeMap<>();
        this.nextSpawnTime = -1.0f;
    }

    public void updateSpawning(LevelScene scene){
        float currentTime = scene.getSceneTimeSeconds();
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

    private ArrayList<Spawnable> getAllSpawnables(){
        ArrayList<Spawnable> allSpawnablesList = new ArrayList<>(spawnList.size());
        for(ArrayList<Spawnable> spawnEntry: spawnList.values()){
            for(Spawnable spawnable: spawnEntry){
                if(!(spawnable instanceof EmptySpawnable) && !(spawnable instanceof MultiSpawnable)){
                    allSpawnablesList.add(spawnable);
                }
                if(spawnable instanceof EntitySpawnInfo entitySpawnInfo){
                    ArrayList<Spawnable> entitySpawnables = editorDataManager.getSpawnablesOfEntity(entitySpawnInfo.id());
                    for (var spawnableOfEntity : entitySpawnables){
                        if(!(spawnableOfEntity instanceof EmptySpawnable)){
                            allSpawnablesList.add(spawnableOfEntity);
                        }
                    }
                }
                if(spawnable instanceof MultiSpawnable multiSpawnable){
                    for(Spawnable element: multiSpawnable.spawnables()){
                        if(!(element instanceof EmptySpawnable)){
                            allSpawnablesList.add(element);
                        }
                        if(element instanceof EntitySpawnInfo entitySpawnInfo){
                            ArrayList<Spawnable> entitySpawnables = editorDataManager.getSpawnablesOfEntity(entitySpawnInfo.id());
                            for (var spawnableOfEntity : entitySpawnables){
                                if(!(spawnableOfEntity instanceof EmptySpawnable)){
                                    allSpawnablesList.add(spawnableOfEntity);//TODO there are edge cases that are not handled correctly
                                }
                            }
                        }
                    }
                }
            }
        }
        return allSpawnablesList;
    }

    public ArrayList<RenderInfo> getAllRenderInfos(){
        ArrayList<RenderInfo> allRenderInfos = new ArrayList<>();
        ArrayList<Spawnable> allSpawnablesList = getAllSpawnables();
        for(var spawnable: allSpawnablesList){
            if(spawnable instanceof EntitySpawnInfo entitySpawnInfo){
                allRenderInfos.addAll(editorDataManager.getRenderInfoOfEntity(entitySpawnInfo.id()));
            }
            if(spawnable instanceof SceneDisplaySpawnInfo sceneDisplaySpawnInfo){
                Optional<RenderInfo> renderInfoOptional = editorDataManager.getRenderInfoOfDisplay(sceneDisplaySpawnInfo.id());
                renderInfoOptional.ifPresent(allRenderInfos::add);
            }
        }
        return allRenderInfos;
    }

    public Optional<Float> getNextSpawnTime(float currentTime){
        return spawnList.higherKey(currentTime).describeConstable();
    }

    public void addSpawnable(float time, Spawnable spawnable){
        this.spawnList.computeIfAbsent(time, k -> new ArrayList<Spawnable>());
        this.spawnList.get(time).add(spawnable);
        nextSpawnTime = spawnList.higherKey(-1.0f);
    }

    public void addEntity(float time, int id, float startingPositionX, float startingPositionY, int trajectoryId){
        EntitySpawnInfo entitySpawnInfo = new EntitySpawnInfo(id, startingPositionX, startingPositionY, trajectoryId);
        addSpawnable(time, entitySpawnInfo);
    }

    public void addEntity(float time, int id, float startingPositionX, float startingPositionY){
        EntitySpawnInfo entitySpawnInfo = new EntitySpawnInfo(id, startingPositionX, startingPositionY, -1);
        addSpawnable(time, entitySpawnInfo);
    }
}
