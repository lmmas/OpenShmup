package engine.level.spawnable;

import engine.types.Vec2D;

public record DisplaySpawnInfo(
    int id,
    Vec2D position
) implements Spawnable {

    @Override
    public Spawnable copy() {
        return new DisplaySpawnInfo(id, position);
    }

    @Override
    public Spawnable copyWithOffset(Vec2D offset) {
        return new DisplaySpawnInfo(id, position.add(offset));
    }
}
