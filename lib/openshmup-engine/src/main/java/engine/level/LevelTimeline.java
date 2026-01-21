package engine.level;

import engine.assets.Texture;
import engine.gameData.GameDataManager;
import engine.level.spawnable.EntitySpawnInfo;
import engine.level.spawnable.SceneDisplaySpawnInfo;
import engine.level.spawnable.Spawnable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

final public class LevelTimeline {
    @Getter
    final private GameDataManager gameDataManager;

    private final float levelDuration;

    private final TreeMap<Float, ArrayList<Spawnable>> spawnList;

    private Float nextSpawnTime;

    public LevelTimeline(GameDataManager gameDataManager, float levelDuration) {
        this.gameDataManager = gameDataManager;
        this.levelDuration = levelDuration;
        this.spawnList = new TreeMap<>();
        this.nextSpawnTime = null;
    }

    public void updateSpawning(Level level) {
        double currentTime = level.getLevelTimeSeconds();
        while (nextSpawnTime != null && currentTime >= nextSpawnTime && currentTime < levelDuration) {
            ArrayList<Spawnable> spawnables = spawnList.get(nextSpawnTime);
            for (Spawnable spawnable : spawnables) {
                level.addSpawnable(spawnable);
            }
            nextSpawnTime = spawnList.higherKey(nextSpawnTime);
        }
    }

    private HashSet<Spawnable> getAllSpawnables() {
        HashSet<Spawnable> allSpawnablesSet = new HashSet<>(spawnList.size());
        for (ArrayList<Spawnable> spawnEntry : spawnList.values()) {
            allSpawnablesSet.addAll(spawnEntry);
        }
        return allSpawnablesSet;
    }

    /*public HashSet<RenderInfo> getAllRenderInfos(){
        HashSet<RenderInfo> allRenderInfos = new HashSet<>();
        HashSet<Spawnable> allSpawnablesSet = getAllSpawnables();
        for(var spawnable: allSpawnablesSet){
            if(spawnable instanceof EntitySpawnInfo entitySpawnInfo){
                allRenderInfos.addAll(gameDataManager.getRenderInfosOfEntity(entitySpawnInfo.id()));
            }
            if(spawnable instanceof SceneDisplaySpawnInfo sceneDisplaySpawnInfo){
                allRenderInfos.addAll(gameDataManager.getRenderInfosOfVisual(sceneDisplaySpawnInfo.id()));
            }
        }
        return allRenderInfos;
    }*/

    public HashSet<Texture> getAllTextures() {
        HashSet<Texture> allTextures = new HashSet<>();
        HashSet<Spawnable> allSpawnables = getAllSpawnables();
        for (var spawnable : allSpawnables) {
            if (spawnable instanceof SceneDisplaySpawnInfo sceneDisplaySpawnInfo) {
                List<Texture> textures = gameDataManager.getTexturesOfDisplay(sceneDisplaySpawnInfo.id());
                allTextures.addAll(textures);
            }
            if (spawnable instanceof EntitySpawnInfo entitySpawnInfo) {
                List<Texture> textures = gameDataManager.getTexturesOfEntity(entitySpawnInfo.id());
                allTextures.addAll(textures);
            }
        }
        return allTextures;
    }

    public void addSpawnable(float time, Spawnable spawnable) {
        this.spawnList.computeIfAbsent(time, k -> new ArrayList<Spawnable>());
        this.spawnList.get(time).add(spawnable);
        nextSpawnTime = spawnList.higherKey(-1.0f);
    }

    public void addEntity(float time, int id, float startingPositionX, float startingPositionY, int trajectoryId) {
        EntitySpawnInfo entitySpawnInfo = new EntitySpawnInfo(id, startingPositionX, startingPositionY, trajectoryId);
        addSpawnable(time, entitySpawnInfo);
    }

    public void addEntity(float time, int id, float startingPositionX, float startingPositionY) {
        EntitySpawnInfo entitySpawnInfo = new EntitySpawnInfo(id, startingPositionX, startingPositionY, -1);
        addSpawnable(time, entitySpawnInfo);
    }

    public void resetTime() {
        this.nextSpawnTime = spawnList.higherKey(-1.0f);
    }

}
