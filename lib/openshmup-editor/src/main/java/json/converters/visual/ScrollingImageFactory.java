package json.converters.visual;

import engine.assets.Texture;
import engine.types.Vec2D;
import engine.visual.SceneVisual;
import engine.visual.ScrollingImage;
import json.SafeJsonNode;

import java.nio.file.Path;

import static engine.Engine.assetManager;

public class ScrollingImageFactory implements VisualFactory {

    @Override
    public SceneVisual fromJson(SafeJsonNode node, Path textureFolderPath) {
        int layer = node.checkAndGetInt("layer");
        Vec2D size = node.checkAndGetVec2D("size");
        String imagePath = textureFolderPath.resolve(node.checkAndGetString("fileName")).toString();
        Texture texture = assetManager.getTexture(imagePath);
        boolean horizontalScrolling = node.checkAndGetBoolean("horizontalScrolling");
        float speed = node.checkAndGetFloat("speed");

        return new ScrollingImage(texture, layer, size.x, size.y, speed, horizontalScrolling);
    }
}
