package engine.graphics;

import engine.render.RenderType;
import engine.scene.Scene;

abstract public class Animation extends Image2D{

    public Animation(String textureFilepath, Scene scene, int layer, RenderType type) {
        super(textureFilepath, scene, layer, type);
    }
}
