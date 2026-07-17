package json.readers;

import edition.EditionData;
import json.JsonNodeReader;
import types.Vec2D;

@FunctionalInterface
public interface HitboxReader {

    EditionData fromJson(JsonNodeReader node);

    static EditionData RectangleHitbox(JsonNodeReader node) {
        Vec2D size = node.readVec2D(EditionData.Keys.Hitbox.RectangleHitbox.size);
        return EditionData.Hitbox.RectangleHitbox(size);
    }

    static EditionData CompositeHitbox(JsonNodeReader node) {
        String textureFileName = node.readString(EditionData.Keys.Hitbox.CustomHitbox.fileName);
        Vec2D size = node.readVec2D(EditionData.Keys.Hitbox.CustomHitbox.size);
        return EditionData.Hitbox.CustomHitbox(size, textureFileName);
    }
}
