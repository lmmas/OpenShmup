package engine.level.spawnable;

import types.Vec2D;

public record EntitySpawnInfo(int id, Vec2D startingPosition, int trajectoryId) implements Spawnable {

    @Override
    public Spawnable copy() {
        return new EntitySpawnInfo(id, startingPosition, trajectoryId);
    }

    @Override
    public Spawnable copyWithOffset(Vec2D offset) {
        return new EntitySpawnInfo(id, startingPosition.add(offset), trajectoryId);
    }

}
