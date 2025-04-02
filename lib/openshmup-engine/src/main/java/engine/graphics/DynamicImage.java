package engine.graphics;

import engine.render.GraphicType;
import engine.render.Shader;
import engine.render.Texture;

public final class DynamicImage extends Image2D{
    public DynamicImage(String textureFilepath, int layer, float sizeX, float sizeY){
        super(textureFilepath, layer, GraphicType.DYNAMIC_IMAGE, sizeX, sizeY);
    }
    public DynamicImage(String textureFilepath, int layer, Shader shader, float sizeX, float sizeY){
        super(textureFilepath, layer, GraphicType.DYNAMIC_IMAGE, sizeX, sizeY, shader);
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
