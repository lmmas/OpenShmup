package engine.level.spawnable;

import engine.types.Vec2D;

public record SceneDisplaySpawnInfo(
    int id,
    Vec2D position
) implements Spawnable {

    @Override
    public Spawnable copy() {
        return new SceneDisplaySpawnInfo(id, position);
    }

    @Override
    public Spawnable copyWithOffset(Vec2D offset) {
        return new SceneDisplaySpawnInfo(id, position.add(offset));
    }
}
