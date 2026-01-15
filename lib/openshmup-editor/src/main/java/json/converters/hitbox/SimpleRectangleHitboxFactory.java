package json.converters.hitbox;

import engine.gameData.GamePaths;
import engine.level.entity.hitbox.Hitbox;
import engine.level.entity.hitbox.SimpleRectangleHitbox;
import engine.types.Vec2D;
import json.SafeJsonNode;

public class SimpleRectangleHitboxFactory implements HitboxFactory {

    @Override
    public Hitbox fromJson(SafeJsonNode node, GamePaths paths) {
        Vec2D size = node.checkAndGetVec2D("size");
        return new SimpleRectangleHitbox(0f, 0f, size.x, size.y);
    }
}
