package json.converters.hitbox;

import com.fasterxml.jackson.databind.node.ObjectNode;
import engine.types.Vec2D;
import json.JsonFieldNames;
import json.SafeJsonNode;
import json.editionData.HitboxEditionData;
import json.editionData.RectangleHitboxEditionData;

import java.nio.file.Path;

final public class SimpleRectangleHitboxConverter implements HitboxConverter {

    @Override
    public HitboxEditionData fromJson(SafeJsonNode node, Path textureFolderPath) {
        Vec2D size = node.safeGetVec2D(JsonFieldNames.SimpleRectangleHitbox.size);
        return new RectangleHitboxEditionData(size);
    }

    @Override
    public ObjectNode toJson(HitboxEditionData hitboxData, ObjectNode node) {
        RectangleHitboxEditionData simpleRectangleHitboxData = (RectangleHitboxEditionData) hitboxData;

        simpleRectangleHitboxData.getSize().addToNode(node);

        return node;
    }
}
