package engine.level.spawnable;

import types.Vec2D;

public interface Spawnable {

    Spawnable copy();

    Spawnable copyWithOffset(Vec2D offset);
}
