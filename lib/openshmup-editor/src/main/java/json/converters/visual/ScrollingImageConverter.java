package json.converters.visual;

import com.fasterxml.jackson.databind.node.ObjectNode;
import editor.attribute.Attribute;
import editor.editionData.ScrollingImageEditionData;
import editor.editionData.VisualEditionData;
import engine.types.Vec2D;
import json.JsonFieldNames;
import json.SafeJsonNode;

import java.nio.file.Path;
import java.util.List;

public class ScrollingImageConverter implements VisualConverter {

    @Override
    public VisualEditionData fromJson(SafeJsonNode node, Path textureFolderPath) {
        int id = node.safeGetInt(JsonFieldNames.ScrollingImage.id);
        int layer = node.safeGetInt(JsonFieldNames.ScrollingImage.layer);
        Vec2D size = node.safeGetVec2D(JsonFieldNames.ScrollingImage.size);

        String imageFilename = node.safeGetString(JsonFieldNames.ScrollingImage.fileName);
        boolean horizontalScrolling = node.safeGetBoolean(JsonFieldNames.ScrollingImage.horizontalScrolling);
        float speed = node.safeGetFloat(JsonFieldNames.ScrollingImage.speed);

        return new ScrollingImageEditionData(id, layer, size.x, size.y, imageFilename, speed, horizontalScrolling);
    }

    @Override
    public ObjectNode toJson(VisualEditionData visualData, ObjectNode node) {
        ScrollingImageEditionData scrollingImageData = (ScrollingImageEditionData) visualData;

        List<Attribute> attributes = List.of(scrollingImageData.getIdAttribute(), scrollingImageData.getLayer(), scrollingImageData.getSize(), scrollingImageData.getSpeed(), scrollingImageData.getFileName(), scrollingImageData.getHorizontalScrolling());
        attributes.forEach(attribute -> attribute.addToNode(node));

        return node;
    }
}
