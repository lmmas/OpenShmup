package json.factories;

import engine.assets.Texture;
import engine.level.entity.hitbox.CompositeHitbox;
import engine.level.entity.hitbox.Hitbox;
import engine.level.entity.hitbox.SimpleRectangleHitbox;
import engine.types.Vec2D;
import json.SafeJsonNode;

import java.nio.file.Path;
import java.util.function.BiFunction;

import static engine.Engine.assetManager;

public class HitboxFactories {

    final public static BiFunction<SafeJsonNode, Path, Hitbox> simpleRectangleHitboxFactory = (node, path) -> {
        Vec2D size = node.checkAndGetVec2D("size");
        return new SimpleRectangleHitbox(0f, 0f, size.x, size.y);
    };

    final public static BiFunction<SafeJsonNode, Path, Hitbox> compositeHitboxFactory = (node, path) -> {
        String textureFileName = node.checkAndGetString("fileName");
        Texture texture = assetManager.getTexture(path.resolve(textureFileName));
        Vec2D size = node.checkAndGetVec2D("size");
        return new CompositeHitbox(texture, size.x, size.y);
    };
}
