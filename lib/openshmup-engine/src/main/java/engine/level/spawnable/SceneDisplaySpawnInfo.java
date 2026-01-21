package engine.level.spawnable;

import engine.types.Vec2D;

public record SceneDisplaySpawnInfo(
    int id,
    Vec2D position
) implements Spawnable {

    public SceneDisplaySpawnInfo(int id, float positionX, float positionY) {
        this(id, new Vec2D(positionX, positionY));
    }

    @Override
    public Spawnable copy() {
        return new SceneDisplaySpawnInfo(id, position.x, position.y);
    }

    @Override
    public Spawnable copyWithOffset(float offsetX, float offsetY) {
        return new SceneDisplaySpawnInfo(id, position.x + offsetX, position.y + offsetY);
    }


}
