package engine.graphics;

import engine.render.RenderType;
import engine.render.Shader;
import engine.render.Texture;
import engine.scene.Scene;

public class StaticImage extends Image2D {
    public StaticImage(Texture texture, int layer, Shader shader){
        super(texture, layer, RenderType.STATIC_IMAGE, shader);
    }
    public StaticImage(String textureFilepath, int layer){
        super(textureFilepath, layer, RenderType.STATIC_IMAGE);
    }
    public StaticImage(String textureFilepath, int layer, Shader shader){
        super(textureFilepath, layer, RenderType.STATIC_IMAGE, shader);
    }

    @Override
    public StaticImage copy() {
        return new StaticImage(texture, layer, shader);
    }
}
