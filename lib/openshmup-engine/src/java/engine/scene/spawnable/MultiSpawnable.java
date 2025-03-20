package engine.scene.spawnable;

import engine.scene.LevelScene;

public record MultiSpawnable(
        Spawnable[] spawnables
) implements Spawnable{
    @Override
    public void spawn(LevelScene scene) {
        for(Spawnable spawnable: spawnables){
            spawnable.spawn(scene);
        }
    }
}
