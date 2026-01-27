package json.factories;

import engine.assets.Texture;
import engine.scene.visual.Animation;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.ScrollingImage;
import engine.scene.visual.SpritesheetInfo;
import engine.scene.visual.style.TimeReference;
import engine.types.IVec2D;
import engine.types.Vec2D;
import json.JsonFieldNames;
import json.SafeJsonNode;

import java.nio.file.Path;
import java.util.function.BiFunction;

import static engine.Engine.assetManager;

final public class VisualFactories {

    final public static BiFunction<SafeJsonNode, Path, SceneVisual> scrollingImageFactory = (node, textureFolderPath) -> {
        int layer = node.safeGetInt(JsonFieldNames.ScrollingImage.layer);
        Vec2D size = node.safeGetVec2D(JsonFieldNames.ScrollingImage.size);
        Path imagePath = textureFolderPath.resolve(node.safeGetString(JsonFieldNames.ScrollingImage.fileName));
        Texture texture = assetManager.getTexture(imagePath);
        boolean horizontalScrolling = node.safeGetBoolean(JsonFieldNames.ScrollingImage.horizontalScrolling);
        float speed = node.safeGetFloat(JsonFieldNames.ScrollingImage.speed);

        return new ScrollingImage(texture, layer, size.x, size.y, speed, horizontalScrolling, TimeReference.LEVEL);
    };

    final public static BiFunction<SafeJsonNode, Path, SceneVisual> animationFactory = (node, textureFolderPath) -> {
        int layer = node.safeGetInt(JsonFieldNames.Animation.layer);
        Vec2D size = node.safeGetVec2D(JsonFieldNames.Animation.size);

        SafeJsonNode spritesheetInfoNode = node.safeGetObject(JsonFieldNames.Animation.spritesheetInfo);

        Path textureFilepath = textureFolderPath.resolve(spritesheetInfoNode.safeGetString(JsonFieldNames.Animation.SpritesheetInfo.fileName));
        int frameCount = spritesheetInfoNode.safeGetInt(JsonFieldNames.Animation.SpritesheetInfo.frameCount);
        IVec2D frameSize = spritesheetInfoNode.safeGetIVec2D(JsonFieldNames.Animation.SpritesheetInfo.frameSize);
        IVec2D startingPosition = spritesheetInfoNode.safeGetIVec2D(JsonFieldNames.Animation.SpritesheetInfo.startingPosition);
        IVec2D stride = spritesheetInfoNode.safeGetIVec2D(JsonFieldNames.Animation.SpritesheetInfo.stride);

        float framePeriodSeconds = node.safeGetFloat(JsonFieldNames.Animation.framePeriodSeconds);
        boolean looping = node.safeGetBoolean(JsonFieldNames.Animation.looping);

        Texture texture = assetManager.getTexture(textureFilepath);
        int animationTextureWidth = texture.getWidth();
        int animationTextureHeight = texture.getHeight();

        SpritesheetInfo spritesheetInfo = new SpritesheetInfo(textureFilepath, frameCount,
            (float) frameSize.x / animationTextureWidth,
            (float) frameSize.y / animationTextureHeight,
            (float) startingPosition.x / animationTextureWidth,
            (float) startingPosition.y / animationTextureHeight,
            (float) stride.x / animationTextureWidth,
            (float) stride.y / animationTextureHeight);

        return new Animation(layer, texture, spritesheetInfo, framePeriodSeconds, looping, size.x, size.y, TimeReference.LEVEL);
    };
}
