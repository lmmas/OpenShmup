package json.converters.hitbox;

import engine.types.Vec2D;
import json.JsonFieldNames;
import json.SafeJsonNode;
import json.editionData.EditionData;

import java.nio.file.Path;

import static json.editionData.EditionData.Hitbox;

final public class SimpleRectangleHitboxConverter implements HitboxConverter {

    @Override
    public EditionData fromJson(SafeJsonNode node, Path textureFolderPath) {
        Vec2D size = node.safeGetVec2D(JsonFieldNames.SimpleRectangleHitbox.size);
        return Hitbox.RectangleHitbox(size);
    }

}
