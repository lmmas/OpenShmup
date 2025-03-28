package engine.entity.sprite;

import engine.graphics.Graphic;
import engine.render.RenderInfo;
import engine.graphics.DynamicImage;
import engine.render.Texture;

import java.util.Optional;

public class SimpleSprite implements EntitySprite{
    final private DynamicImage image;
    public SimpleSprite(DynamicImage image){
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
