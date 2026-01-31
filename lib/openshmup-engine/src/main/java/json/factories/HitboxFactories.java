package json.factories;

import engine.assets.Texture;
import engine.hitbox.CompositeHitbox;
import engine.hitbox.Hitbox;
import engine.hitbox.SimpleRectangleHitbox;
import engine.types.Vec2D;
import json.JsonFieldNames;
import json.SafeJsonNode;

import java.nio.file.Path;
import java.util.function.BiFunction;

import static engine.Engine.assetManager;

public class HitboxFactories {

    final public static BiFunction<SafeJsonNode, Path, Hitbox> simpleRectangleHitboxFactory = (node, path) -> {
        Vec2D size = node.safeGetVec2D(JsonFieldNames.SimpleRectangleHitbox.size);
        return new SimpleRectangleHitbox(0f, 0f, size.x, size.y);
    };

    final public static BiFunction<SafeJsonNode, Path, Hitbox> compositeHitboxFactory = (node, path) -> {
        String textureFileName = node.safeGetString(JsonFieldNames.CompositeHitbox.fileName);
        Texture texture = assetManager.getTexture(path.resolve(textureFileName));
        Vec2D size = node.safeGetVec2D(JsonFieldNames.CompositeHitbox.size);
        return new CompositeHitbox(texture, size.x, size.y);
    };
}
