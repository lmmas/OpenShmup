package engine.scene;

import engine.EditorDataManager;
import engine.render.RenderInfo;
import engine.render.Texture;
import engine.scene.spawnable.*;

import java.util.*;

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

    private HashSet<Spawnable> getAllSpawnables(){
        HashSet<Spawnable> allSpawnablesSet = new HashSet<>(spawnList.size());
        for(ArrayList<Spawnable> spawnEntry: spawnList.values()){
            for(Spawnable spawnable: spawnEntry){
                if(!(spawnable instanceof EmptySpawnable)){
                    HashSet<Spawnable> spawnablesToCheck = new HashSet<>();
                    spawnablesToCheck.add(spawnable);
                    while(!spawnablesToCheck.isEmpty()){
                        Spawnable currentSpawnable = spawnablesToCheck.iterator().next();
                        spawnablesToCheck.remove(currentSpawnable);
                        if(!(currentSpawnable instanceof EmptySpawnable) && !(currentSpawnable instanceof MultiSpawnable)){
                            allSpawnablesSet.add(currentSpawnable);
                        }
                        if(currentSpawnable instanceof EntitySpawnInfo entitySpawnInfo){
                            ArrayList<Spawnable> entitySpawnables = editorDataManager.getSpawnablesOfEntity(entitySpawnInfo.id());
                            spawnablesToCheck.addAll(entitySpawnables);
                        }
                        if(currentSpawnable instanceof MultiSpawnable multiSpawnable){
                            ArrayList<Spawnable> entitySpawnables = multiSpawnable.spawnables();
                            spawnablesToCheck.addAll(entitySpawnables);
                        }//fixme still vulnerable to circular references in shots and deathSpawns
                    }
                }
            }
        }
        return allSpawnablesSet;
    }

    public HashSet<RenderInfo> getAllRenderInfos(){
        HashSet<RenderInfo> allRenderInfos = new HashSet<>();
        HashSet<Spawnable> allSpawnablesSet = getAllSpawnables();
        for(var spawnable: allSpawnablesSet){
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

    public HashSet<Texture> getAllTextures(){
        HashSet<Texture> allTextures = new HashSet<>();
        HashSet<Spawnable> allSpawnables = getAllSpawnables();
        for(var spawnable: allSpawnables){
            if(spawnable instanceof SceneDisplaySpawnInfo sceneDisplaySpawnInfo){
                Optional<Texture> textureOptional = editorDataManager.getTextureOfDisplay(sceneDisplaySpawnInfo.id());
                textureOptional.ifPresent(allTextures::add);
            }
            if(spawnable instanceof EntitySpawnInfo entitySpawnInfo){
                Optional<Texture> textureOptional = editorDataManager.getTextureOfEntity(entitySpawnInfo.id());
                textureOptional.ifPresent(allTextures::add);
            }
        }
        return allTextures;
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
