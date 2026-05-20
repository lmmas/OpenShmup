package engine.level.spawnable;

import engine.types.Vec2D;

public interface Spawnable {

    Spawnable copy();

    Spawnable copyWithOffset(Vec2D offset);
}
