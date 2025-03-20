package engine.scene.spawnable;

import engine.Vec2D;
import engine.entity.Entity;
import engine.scene.LevelScene;

public record EntitySpawnInfo(int id, Vec2D startingPosition, int trajectoryId) implements Spawnable {

    public EntitySpawnInfo(int id, float startingPositionX, float startingPositionY, int trajectoryId) {
        this(id, new Vec2D(startingPositionX, startingPositionY), trajectoryId);
    }
@Override
    public void spawn(LevelScene scene) {
        scene.addEntity(this);
    }
}
