package engine.entity.sprite;

import engine.graphics.Graphic;
import engine.graphics.Image2D;
import engine.render.RenderInfo;
import engine.assets.Texture;

import java.util.Optional;

final public class SimpleSprite implements EntitySprite{
    final private Image2D image;
    public SimpleSprite(Image2D image){
        this.image = image;
    }
    @Override
    public void setPosition(float positionX, float positionY) {
        image.setPosition(positionX, positionY);
    }

    @Override
    public void setSize(float sizeX, float sizeY) {
        image.setSize(sizeX, sizeY);
    }

    @Override
    public void setOrientation(float orientation) {
    }

    @Override
    public Optional<RenderInfo> getRenderInfo() {
        return Optional.of(image.getRenderInfo());
    }

    @Override
    public Optional<Graphic<?, ?>> getGraphic() {
        return Optional.of(image);
    }

    @Override
    public Optional<Texture> getTexture() {
        return Optional.of(image.getTexture());
    }

    @Override
    public void update(float currentTimeSeconds) {
    }

    @Override
    public EntitySprite copy() {
        return new SimpleSprite(image.copy());
    }
}
