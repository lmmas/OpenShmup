package engine.graphics;

import engine.render.RenderType;
import engine.render.Shader;
import engine.scene.Scene;

public final class SimpleSprite extends Image2D implements EntitySprite {
    public SimpleSprite(String textureFilepath, Scene scene, int layer){
        super(textureFilepath, scene, layer, RenderType.SIMPLE_SPRITE);
    }
    public SimpleSprite(String textureFilepath, Scene scene, int layer, Shader shader){
        super(textureFilepath, scene, layer, RenderType.SIMPLE_SPRITE, shader);
    }
}
