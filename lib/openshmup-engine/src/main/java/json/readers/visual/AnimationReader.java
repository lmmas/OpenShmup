package json.readers.visual;

import engine.types.IVec2D;
import engine.types.Vec2D;
import json.JsonFieldNames;
import json.SafeJsonNode;
import json.editionData.EditionData;

import java.nio.file.Path;

import static json.editionData.EditionData.SpritesheetInfo;
import static json.editionData.EditionData.Visual;

final public class AnimationReader implements VisualReader {

    @Override
    public EditionData fromJson(SafeJsonNode node, Path textureFolderPath) {
        int id = node.safeGetInt(JsonFieldNames.Animation.id);
        int layer = node.safeGetInt(JsonFieldNames.Animation.layer);
        Vec2D size = node.safeGetVec2D(JsonFieldNames.Animation.size);

        SafeJsonNode spritesheetInfoNode = node.safeGetObject(JsonFieldNames.Animation.spritesheetInfo);
        String textureFileName = spritesheetInfoNode.safeGetString(JsonFieldNames.Animation.SpritesheetInfo.fileName);
        int frameCount = spritesheetInfoNode.safeGetInt(JsonFieldNames.Animation.SpritesheetInfo.frameCount);
        IVec2D frameSize = spritesheetInfoNode.safeGetIVec2D(JsonFieldNames.Animation.SpritesheetInfo.frameSize);
        IVec2D startingPosition = spritesheetInfoNode.safeGetIVec2D(JsonFieldNames.Animation.SpritesheetInfo.startingPosition);
        IVec2D stride = spritesheetInfoNode.safeGetIVec2D(JsonFieldNames.Animation.SpritesheetInfo.stride);

        double framePeriodSeconds = node.safeGetDouble(JsonFieldNames.Animation.framePeriodSeconds);
        boolean looping = node.safeGetBoolean(JsonFieldNames.Animation.looping);

        EditionData spritesheetInfo = SpritesheetInfo(
            textureFileName,
            frameCount,
            frameSize,
            startingPosition,
            stride);

        return Visual.Animation(id, layer, size, framePeriodSeconds, looping, spritesheetInfo);
    }
}
