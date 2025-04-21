package engine.graphics;

import engine.assets.Shader;
import engine.assets.Texture;

public final class DynamicImage extends Image2D{
    public DynamicImage(Texture texture, int layer, float sizeX, float sizeY){
        super(texture, layer, GraphicType.DYNAMIC_IMAGE, sizeX, sizeY);
    }
    public DynamicImage(Texture texture, int layer, Shader shader, float sizeX, float sizeY){
        super(texture, layer, GraphicType.DYNAMIC_IMAGE, sizeX, sizeY, shader);
    }
    public DynamicImage(Texture texture, int layer, Shader shader, ImagePrimitive primitive){
        //this constructor is only used for deep copying
        super(texture, layer, GraphicType.DYNAMIC_IMAGE, shader, primitive);
    }

    @Override
    public DynamicImage copy() {
        return new DynamicImage(texture, renderInfo.layer(), shader, primitive.copy());
    }
}
