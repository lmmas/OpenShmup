package engine.graphics;

import engine.render.RenderType;
import engine.render.Shader;
import engine.scene.Scene;

public class StaticImage extends Image2D {
    public StaticImage(String textureFilepath, Scene scene, int layer){
        super(textureFilepath, scene, layer, RenderType.STATIC_IMAGE);
    }
    public StaticImage(String textureFilepath, Scene scene, int layer, Shader shader){
        super(textureFilepath, scene, layer, RenderType.STATIC_IMAGE, shader);
    }
}
