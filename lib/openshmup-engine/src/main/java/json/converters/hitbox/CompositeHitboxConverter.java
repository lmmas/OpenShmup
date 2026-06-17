package json.converters.hitbox;

import engine.types.Vec2D;
import json.JsonFieldNames;
import json.SafeJsonNode;
import json.editionData.EditionData;

import java.nio.file.Path;

import static json.editionData.EditionData.Hitbox;

final public class CompositeHitboxConverter implements HitboxConverter {

    @Override
    public EditionData fromJson(SafeJsonNode node, Path textureFolderPath) {
        String textureFileName = node.safeGetString(JsonFieldNames.CompositeHitbox.fileName);
        Vec2D size = node.safeGetVec2D(JsonFieldNames.CompositeHitbox.size);
        return Hitbox.CustomHitbox(size, textureFileName);
    }

}
