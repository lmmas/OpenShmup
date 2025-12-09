package engine.scene.spawnable;

import engine.scene.LevelScene;

import java.util.ArrayList;

public record MultiSpawnable(
    ArrayList<Spawnable> spawnables
) implements Spawnable {
    @Override
    public void spawn(LevelScene scene) {
        for (var spawnable : spawnables) {
            spawnable.spawn(scene);
        }
    }

    @Override
    public Spawnable copy() {
        ArrayList<Spawnable> newSpawnables = new ArrayList<>(spawnables.size());
        for (Spawnable spawnable : spawnables) {
            newSpawnables.add(spawnable.copy());
        }
        return new MultiSpawnable(newSpawnables);
    }

    @Override
    public Spawnable copyWithOffset(float offsetX, float offsetY) {
        ArrayList<Spawnable> newSpawnables = new ArrayList<>(spawnables.size());
        for (Spawnable spawnable : spawnables) {
            newSpawnables.add(spawnable.copyWithOffset(offsetX, offsetY));
        }
        return new MultiSpawnable(newSpawnables);
    }
}
