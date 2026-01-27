package json.converters.hitbox;

import com.fasterxml.jackson.databind.node.ObjectNode;
import editor.attribute.Attribute;
import editor.editionData.CompositeHitboxEditionData;
import editor.editionData.HitboxEditionData;
import engine.types.Vec2D;
import json.JsonFieldNames;
import json.SafeJsonNode;

import java.nio.file.Path;
import java.util.List;

public class CompositeHitboxConverter implements HitboxConverter {

    @Override
    public HitboxEditionData fromJson(SafeJsonNode node, Path textureFolderPath) {
        String textureFileName = node.safeGetString(JsonFieldNames.CompositeHitbox.fileName);
        Vec2D size = node.safeGetVec2D(JsonFieldNames.CompositeHitbox.size);
        return new CompositeHitboxEditionData(size.x, size.y, textureFileName);
    }

    @Override
    public ObjectNode toJson(HitboxEditionData hitboxData, ObjectNode node) {
        CompositeHitboxEditionData compositeHitboxEditionData = (CompositeHitboxEditionData) hitboxData;

        List<Attribute> attributes = List.of(compositeHitboxEditionData.getSize(), compositeHitboxEditionData.getFileName());
        attributes.forEach(attribute -> attribute.addToNode(node));

        return node;
    }
}
