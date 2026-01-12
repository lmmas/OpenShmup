package json.converters.visual;

import editor.editionData.visual.AnimationEditionData;
import editor.editionData.visual.VisualEditionData;
import engine.types.IVec2D;
import engine.types.Vec2D;
import json.SafeJsonNode;

import java.nio.file.Path;

public class AnimationConverter implements VisualConverter {

    @Override
    public VisualEditionData fromJson(SafeJsonNode node, Path textureFolderPath) {
        int id = node.checkAndGetInt("id");
        int layer = node.checkAndGetInt("layer");
        Vec2D size = node.checkAndGetVec2D("size");

        SafeJsonNode animationInfoNode = node.checkAndGetObject("animationInfo");
        String textureFilepath = textureFolderPath.resolve(animationInfoNode.checkAndGetString("fileName")).toString();
        int frameCount = animationInfoNode.checkAndGetInt("frameCount");
        IVec2D frameSize = animationInfoNode.checkAndGetIVec2D("frameSize");
        IVec2D startingPosition = animationInfoNode.checkAndGetIVec2D("startingPosition");
        IVec2D stride = animationInfoNode.checkAndGetIVec2D("stride");

        float framePeriodSeconds = node.checkAndGetFloat("framePeriodSeconds");
        boolean looping = node.checkAndGetBoolean("looping");

        AnimationEditionData.AnimationInfoAttributes animationInfo = new AnimationEditionData.AnimationInfoAttributes(
            textureFilepath,
            frameCount,
            frameSize.x, frameSize.y,
            startingPosition.x, startingPosition.y,
            stride.x, stride.y);

        return new AnimationEditionData(id, layer, size.x, size.y, framePeriodSeconds, looping, animationInfo);
    }
}
