package json.converters.hitbox;

import com.fasterxml.jackson.databind.node.ObjectNode;
import engine.types.Vec2D;
import json.JsonFieldNames;
import json.SafeJsonNode;
import json.attribute.Attribute;
import json.editionData.CustomHitboxEditionData;
import json.editionData.HitboxEditionData;

import java.nio.file.Path;
import java.util.List;

final public class CompositeHitboxConverter implements HitboxConverter {

    @Override
    public HitboxEditionData fromJson(SafeJsonNode node, Path textureFolderPath) {
        String textureFileName = node.safeGetString(JsonFieldNames.CompositeHitbox.fileName);
        Vec2D size = node.safeGetVec2D(JsonFieldNames.CompositeHitbox.size);
        return new CustomHitboxEditionData(size, textureFileName);
    }

    @Override
    public ObjectNode toJson(HitboxEditionData hitboxData, ObjectNode node) {
        CustomHitboxEditionData customHitboxEditionData = (CustomHitboxEditionData) hitboxData;

        List<Attribute> attributes = List.of(customHitboxEditionData.getSize(), customHitboxEditionData.getFileName());
        attributes.forEach(attribute -> attribute.addToNode(node));

        return node;
    }
}
