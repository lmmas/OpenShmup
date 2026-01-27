package json.converters.hitbox;

import com.fasterxml.jackson.databind.node.ObjectNode;
import editor.editionData.HitboxEditionData;
import editor.editionData.SimpleRectangleHitboxEditionData;
import engine.types.Vec2D;
import json.JsonFieldNames;
import json.SafeJsonNode;

import java.nio.file.Path;

public class SimpleRectangleHitboxConverter implements HitboxConverter {

    @Override
    public HitboxEditionData fromJson(SafeJsonNode node, Path textureFolderPath) {
        Vec2D size = node.safeGetVec2D(JsonFieldNames.SimpleRectangleHitbox.size);
        return new SimpleRectangleHitboxEditionData(size.x, size.y);
    }

    @Override
    public ObjectNode toJson(HitboxEditionData hitboxData, ObjectNode node) {
        SimpleRectangleHitboxEditionData simpleRectangleHitboxData = (SimpleRectangleHitboxEditionData) hitboxData;

        simpleRectangleHitboxData.getSize().addToNode(node);

        return node;
    }
}
