package json.factories;

import engine.assets.Texture;
import engine.scene.visual.Animation;
import engine.scene.visual.AnimationInfo;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.ScrollingImage;
import engine.scene.visual.style.TimeReference;
import engine.types.IVec2D;
import engine.types.Vec2D;
import json.SafeJsonNode;

import java.nio.file.Path;
import java.util.function.BiFunction;

import static engine.Engine.assetManager;

final public class VisualFactories {

    final public static BiFunction<SafeJsonNode, Path, SceneVisual> scrollingImageFactory = (node, textureFolderPath) -> {
        int layer = node.checkAndGetInt("layer");
        Vec2D size = node.checkAndGetVec2D("size");
        Path imagePath = textureFolderPath.resolve(node.checkAndGetString("fileName"));
        Texture texture = assetManager.getTexture(imagePath);
        boolean horizontalScrolling = node.checkAndGetBoolean("horizontalScrolling");
        float speed = node.checkAndGetFloat("speed");

        return new ScrollingImage(texture, layer, size.x, size.y, speed, horizontalScrolling, TimeReference.LEVEL);
    };

    final public static BiFunction<SafeJsonNode, Path, SceneVisual> animationFactory = (node, textureFolderPath) -> {
        int layer = node.checkAndGetInt("layer");
        Vec2D size = node.checkAndGetVec2D("size");

        SafeJsonNode animationInfoNode = node.checkAndGetObject("animationInfo");

        Path textureFilepath = textureFolderPath.resolve(animationInfoNode.checkAndGetString("fileName"));
        int frameCount = animationInfoNode.checkAndGetInt("frameCount");
        IVec2D frameSize = animationInfoNode.checkAndGetIVec2D("frameSize");
        IVec2D startingPosition = animationInfoNode.checkAndGetIVec2D("startingPosition");
        IVec2D stride = animationInfoNode.checkAndGetIVec2D("stride");

        float framePeriodSeconds = node.checkAndGetFloat("framePeriodSeconds");
        boolean looping = node.checkAndGetBoolean("looping");

        Texture texture = assetManager.getTexture(textureFilepath);
        int animationTextureWidth = texture.getWidth();
        int animationTextureHeight = texture.getHeight();

        AnimationInfo animationInfo = new AnimationInfo(textureFilepath, frameCount,
            (float) frameSize.x / animationTextureWidth,
            (float) frameSize.y / animationTextureHeight,
            (float) startingPosition.x / animationTextureWidth,
            (float) startingPosition.y / animationTextureHeight,
            (float) stride.x / animationTextureWidth,
            (float) stride.y / animationTextureHeight);

        return new Animation(layer, texture, animationInfo, framePeriodSeconds, looping, size.x, size.y, TimeReference.LEVEL);
    };
}
