package engine.scene.display;

import engine.entity.sprite.EntitySprite;
import engine.graphics.Graphic;
import engine.graphics.Image2D;
import engine.render.RenderType;
import engine.render.Shader;
import engine.render.Texture;

public final class DynamicImage extends Image2D implements EntitySprite {
    public DynamicImage(String textureFilepath, int layer, float sizeX, float sizeY){
        super(textureFilepath, layer, RenderType.MOVING_IMAGE, sizeX, sizeY);
    }
    public DynamicImage(String textureFilepath, int layer, Shader shader, float sizeX, float sizeY){
        super(textureFilepath, layer, RenderType.MOVING_IMAGE, sizeX, sizeY, shader);
    }
    public DynamicImage(Texture texture, int layer, Shader shader, ImagePrimitive primitive){
        //this constructore is only used for deep copying
        super(texture, layer, RenderType.MOVING_IMAGE, shader, primitive);
    }
    @Override
    public DynamicImage copy() {
        return new DynamicImage(texture, layer, shader, primitive.copy());
    }

    @Override
    public Graphic<?, ?> getGraphic() {
        return this;
    }

    public void update(float currentTimeSeconds){

    }
}
