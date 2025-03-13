package engine.graphics;

import engine.render.RenderType;
import engine.render.Shader;
import engine.scene.Scene;

public final class MovingImage extends Image2D implements EntitySprite {
    public MovingImage(String textureFilepath, Scene scene, int layer){
        super(textureFilepath, scene, layer, RenderType.MOVING_IMAGE);
    }
    public MovingImage(String textureFilepath, Scene scene, int layer, Shader shader){
        super(textureFilepath, scene, layer, RenderType.MOVING_IMAGE, shader);
    }

    public void update(){

    }
}
