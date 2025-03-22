package engine.graphics;

import engine.entity.EntitySprite;
import engine.render.RenderType;
import engine.render.Shader;
import engine.render.Texture;

public final class MovingImage extends Image2D implements EntitySprite {
    public MovingImage(Texture texture, int layer, Shader shader, ImagePrimitive primitive){
        super(texture, layer, RenderType.MOVING_IMAGE, shader, primitive);
    }
    public MovingImage(String textureFilepath, int layer){
        super(textureFilepath, layer, RenderType.MOVING_IMAGE);
    }
    public MovingImage(String textureFilepath, int layer, Shader shader){
        super(textureFilepath, layer, RenderType.MOVING_IMAGE, shader);
    }

    @Override
    public MovingImage copy() {
        return new MovingImage(texture, layer, shader, primitive.copy());
    }

    @Override
    public Graphic<?, ?> getGraphic() {
        return this;
    }

    public void update(float currentTimeSeconds){

    }
}
