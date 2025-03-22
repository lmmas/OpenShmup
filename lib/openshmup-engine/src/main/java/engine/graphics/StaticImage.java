package engine.graphics;

import engine.render.RenderType;
import engine.render.Shader;
import engine.render.Texture;
import engine.scene.Scene;

public class StaticImage extends Image2D {
    public StaticImage(String textureFilepath, int layer, float sizeX, float sizeY){
        super(textureFilepath, layer, RenderType.STATIC_IMAGE, sizeX, sizeY);
    }
    public StaticImage(String textureFilepath, int layer, float sizeX, float sizeY, Shader shader){
        super(textureFilepath, layer, RenderType.STATIC_IMAGE, sizeX, sizeY, shader);
    }
    public StaticImage(Texture texture, int layer, Shader shader, ImagePrimitive primitive){
        super(texture, layer, RenderType.STATIC_IMAGE, shader, primitive);
    }

    @Override
    public StaticImage copy() {
        return new StaticImage(texture, layer, shader, primitive.copy());
    }
}
