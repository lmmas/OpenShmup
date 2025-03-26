package engine.scene.spawnable;

import engine.Vec2D;
import engine.scene.LevelScene;

public record EntitySpawnInfo(int id, Vec2D startingPosition, int trajectoryId) implements Spawnable {

    public EntitySpawnInfo(int id, float startingPositionX, float startingPositionY, int trajectoryId) {
        this(id, new Vec2D(startingPositionX, startingPositionY), trajectoryId);
    }
@Override
    public void spawn(LevelScene scene) {
        scene.addEntitySpawn(this);
    }

    @Override
    public Spawnable copy() {
        return new EntitySpawnInfo(id, startingPosition.x, startingPosition.y, trajectoryId);
    }

    @Override
    public Spawnable copyWithOffset(float offsetX, float offsetY) {
        return new EntitySpawnInfo(id, startingPosition.x + offsetX, startingPosition.y + offsetY, trajectoryId);
    }

}
