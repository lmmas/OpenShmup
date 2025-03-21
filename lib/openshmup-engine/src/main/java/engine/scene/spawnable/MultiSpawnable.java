package engine.scene.spawnable;

import engine.scene.LevelScene;

public record MultiSpawnable(
        Spawnable[] spawnables
) implements Spawnable{
    @Override
    public void spawn(LevelScene scene) {
        for(var spawnable: spawnables){
            spawnable.spawn(scene);
        }
    }

    @Override
    public Spawnable copyWithOffset(float offsetX, float offsetY) {
        Spawnable[] newSpawnables = new Spawnable[spawnables().length];
        for(int i = 0; i < newSpawnables.length; i++){
            newSpawnables[i] = spawnables[i].copyWithOffset(offsetX, offsetY);
        }
        return new MultiSpawnable(newSpawnables);
    }
}
