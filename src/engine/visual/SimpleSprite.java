package engine.visual;

import engine.render.RenderType;
import engine.render.Shader;

public final class SimpleSprite extends Image2D implements EntitySprite {
    public SimpleSprite(String textureFilepath, int layer){
        super(textureFilepath, layer, RenderType.SIMPLE_SPRITE);
    }
    public SimpleSprite(String textureFilepath, int layer, Shader shader){
        super(textureFilepath, layer, RenderType.SIMPLE_SPRITE, shader);
    }
}
