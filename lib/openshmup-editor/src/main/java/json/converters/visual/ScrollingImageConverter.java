package json.converters.visual;

import editor.editionData.visual.ScrollingImageEditionData;
import editor.editionData.visual.VisualEditionData;
import engine.types.Vec2D;
import json.SafeJsonNode;

import java.nio.file.Path;

public class ScrollingImageConverter implements VisualConverter {

    @Override
    public VisualEditionData fromJson(SafeJsonNode node, Path textureFolderPath) {
        int id = node.checkAndGetInt("id");
        int layer = node.checkAndGetInt("layer");
        Vec2D size = node.checkAndGetVec2D("size");

        String imagePath = textureFolderPath.resolve(node.checkAndGetString("fileName")).toString();
        boolean horizontalScrolling = node.checkAndGetBoolean("horizontalScrolling");
        float speed = node.checkAndGetFloat("speed");

        return new ScrollingImageEditionData(id, layer, size.x, size.y, imagePath, speed, horizontalScrolling);
    }
}
