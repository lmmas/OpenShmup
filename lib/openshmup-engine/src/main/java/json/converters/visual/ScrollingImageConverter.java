package json.converters.visual;

import engine.types.Vec2D;
import json.JsonFieldNames;
import json.SafeJsonNode;
import json.editionData.EditionData;

import java.nio.file.Path;

import static json.editionData.EditionData.Visual;

final public class ScrollingImageConverter implements VisualConverter {

    @Override
    public EditionData fromJson(SafeJsonNode node, Path textureFolderPath) {
        int id = node.safeGetInt(JsonFieldNames.ScrollingImage.id);
        int layer = node.safeGetInt(JsonFieldNames.ScrollingImage.layer);
        Vec2D size = node.safeGetVec2D(JsonFieldNames.ScrollingImage.size);

        String imageFilename = node.safeGetString(JsonFieldNames.ScrollingImage.fileName);
        boolean horizontalScrolling = node.safeGetBoolean(JsonFieldNames.ScrollingImage.horizontalScrolling);
        float speed = node.safeGetFloat(JsonFieldNames.ScrollingImage.speed);

        return Visual.ScrollingImage(id, layer, size, imageFilename, speed, horizontalScrolling);
    }
}
