package json.readers;

import edition.EditionData;
import json.JsonNodeReader;
import types.IVec2D;
import types.Vec2D;

import static edition.EditionData.SpritesheetInfo;

@FunctionalInterface
public interface VisualReader {

    EditionData fromJson(JsonNodeReader node);

    static EditionData ScrollingImage(JsonNodeReader node) {
        int id = node.readInt(EditionData.Keys.Visual.ScrollingImage.id);
        int layer = node.readInt(EditionData.Keys.Visual.ScrollingImage.layer);
        Vec2D size = node.readVec2D(EditionData.Keys.Visual.ScrollingImage.size);

        String imageFilename = node.readString(EditionData.Keys.Visual.ScrollingImage.fileName);
        boolean horizontalScrolling = node.readBoolean(EditionData.Keys.Visual.ScrollingImage.horizontalScrolling);
        float speed = node.readFloat(EditionData.Keys.Visual.ScrollingImage.speed);

        return EditionData.Visual.ScrollingImage(id, layer, size, imageFilename, speed, horizontalScrolling);
    }

    static EditionData Animation(JsonNodeReader node) {
        int id = node.readInt(EditionData.Keys.Visual.Animation.id);
        int layer = node.readInt(EditionData.Keys.Visual.Animation.layer);
        Vec2D size = node.readVec2D(EditionData.Keys.Visual.Animation.size);

        JsonNodeReader spritesheetInfoNode = node.readObject(EditionData.Keys.Visual.Animation.spritesheetInfo);
        String textureFileName = spritesheetInfoNode.readString(EditionData.Keys.SpritesheetInfo.fileName);
        int frameCount = spritesheetInfoNode.readInt(EditionData.Keys.SpritesheetInfo.frameCount);
        IVec2D frameSize = spritesheetInfoNode.readIVec2D(EditionData.Keys.SpritesheetInfo.frameSize);
        IVec2D startingPosition = spritesheetInfoNode.readIVec2D(EditionData.Keys.SpritesheetInfo.startingPosition);
        IVec2D stride = spritesheetInfoNode.readIVec2D(EditionData.Keys.SpritesheetInfo.stride);

        double framePeriodSeconds = node.readDouble(EditionData.Keys.Visual.Animation.framePeriodSeconds);
        boolean looping = node.readBoolean(EditionData.Keys.Visual.Animation.looping);

        EditionData spritesheetInfo = SpritesheetInfo(
            textureFileName,
            frameCount,
            frameSize,
            startingPosition,
            stride);

        return EditionData.Visual.Animation(id, layer, size, framePeriodSeconds, looping, spritesheetInfo);
    }
}
