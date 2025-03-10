package engine.visual;

import engine.render.RenderType;

abstract public class Animation extends Image2D{

    public Animation(String textureFilepath, int layer, RenderType type) {
        super(textureFilepath, layer, type);
    }
}
