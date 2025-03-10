package engine.visual;

import engine.render.RenderType;
import engine.render.Shader;

public class StaticImage extends Image2D {
    public StaticImage(String textureFilepath, int layer){
        super(textureFilepath, layer, RenderType.STATIC_IMAGE);
    }
    public StaticImage(String textureFilepath, int layer, Shader shader){
        super(textureFilepath, layer, RenderType.STATIC_IMAGE, shader);
    }
}
