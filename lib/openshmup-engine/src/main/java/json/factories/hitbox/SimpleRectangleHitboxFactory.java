package json.factories.hitbox;

import engine.entity.hitbox.Hitbox;
import engine.entity.hitbox.SimpleRectangleHitbox;
import engine.types.Vec2D;
import json.SafeJsonNode;

public class SimpleRectangleHitboxFactory implements HitboxFactory {

    @Override
    public Hitbox fromJson(SafeJsonNode node) {
        Vec2D size = node.checkAndGetVec2D("size");
        return new SimpleRectangleHitbox(0f, 0f, size.x, size.y);
    }
}
