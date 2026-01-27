package json.converters.visual;

import com.fasterxml.jackson.databind.node.ObjectNode;
import editor.attribute.Attribute;
import editor.editionData.AnimationEditionData;
import editor.editionData.VisualEditionData;
import engine.types.IVec2D;
import engine.types.Vec2D;
import json.JsonFieldNames;
import json.SafeJsonNode;

import java.nio.file.Path;
import java.util.List;

public class AnimationConverter implements VisualConverter {

    @Override
    public VisualEditionData fromJson(SafeJsonNode node, Path textureFolderPath) {
        int id = node.safeGetInt(JsonFieldNames.Animation.id);
        int layer = node.safeGetInt(JsonFieldNames.Animation.layer);
        Vec2D size = node.safeGetVec2D(JsonFieldNames.Animation.size);

        SafeJsonNode spritesheetInfoNode = node.safeGetObject(JsonFieldNames.Animation.spritesheetInfo);
        String textureFileName = spritesheetInfoNode.safeGetString(JsonFieldNames.Animation.SpritesheetInfo.fileName);
        int frameCount = spritesheetInfoNode.safeGetInt(JsonFieldNames.Animation.SpritesheetInfo.frameCount);
        IVec2D frameSize = spritesheetInfoNode.safeGetIVec2D(JsonFieldNames.Animation.SpritesheetInfo.frameSize);
        IVec2D startingPosition = spritesheetInfoNode.safeGetIVec2D(JsonFieldNames.Animation.SpritesheetInfo.startingPosition);
        IVec2D stride = spritesheetInfoNode.safeGetIVec2D(JsonFieldNames.Animation.SpritesheetInfo.stride);

        float framePeriodSeconds = node.safeGetFloat(JsonFieldNames.Animation.framePeriodSeconds);
        boolean looping = node.safeGetBoolean(JsonFieldNames.Animation.looping);

        AnimationEditionData.SpritesheetInfoData spritesheetInfo = new AnimationEditionData.SpritesheetInfoData(
            textureFileName,
            frameCount,
            frameSize.x, frameSize.y,
            startingPosition.x, startingPosition.y,
            stride.x, stride.y);

        return new AnimationEditionData(id, layer, size.x, size.y, framePeriodSeconds, looping, spritesheetInfo);
    }

    @Override
    public ObjectNode toJson(VisualEditionData visualData, ObjectNode node) {
        AnimationEditionData animationEditionData = (AnimationEditionData) visualData;
        animationEditionData.getIdAttribute().addToNode(node);
        animationEditionData.getLayer().addToNode(node);
        animationEditionData.getSize().addToNode(node);
        AnimationEditionData.SpritesheetInfoData spritesheetInfoData = animationEditionData.getSpritesheetInfo();

        ObjectNode spritesheetInfoNode = node.putObject("spritesheetInfo");
        List<Attribute> attributes = List.of(spritesheetInfoData.getFileName(), spritesheetInfoData.getFrameCount(), spritesheetInfoData.getFrameSize(), spritesheetInfoData.getStartPosition(), spritesheetInfoData.getStride());
        attributes.forEach(attribute -> attribute.addToNode(spritesheetInfoNode));

        animationEditionData.getFramePeriodSeconds().addToNode(node);
        animationEditionData.getLooping().addToNode(node);

        return node;
    }
}
