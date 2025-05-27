package engine.graphics;

import engine.assets.Shader;
import engine.assets.Texture;

public class StaticImage extends Image2D {
    public StaticImage(Texture texture, int layer, float sizeX, float sizeY){
        super(texture, layer, GraphicType.STATIC_IMAGE, sizeX, sizeY);
    }
    public StaticImage(Texture texture, int layer, float sizeX, float sizeY, Shader shader){
        super(texture, layer, GraphicType.STATIC_IMAGE, sizeX, sizeY, shader);
    }
    public StaticImage(Texture texture, int layer, Shader shader, ImagePrimitive primitive){
        //this constructor is only used for deep copying
        super(texture, layer, GraphicType.STATIC_IMAGE, shader, primitive);
    }
    @Override
    public StaticImage copy() {
        return new StaticImage(texture, renderInfo.layer(), shader, primitive);
    }
}
