package engine.entity.sprite;

import engine.graphics.Graphic;
import engine.render.RenderInfo;
import engine.scene.display.DynamicImage;

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
    public Optional<Graphic<?, ?>> getGraphic() {
        return Optional.ofNullable(image);
    }

    @Override
    public Optional<RenderInfo> getRenderInfo() {
        return Optional.ofNullable(image.getRenderInfo());
    }

    @Override
    public void update(float currentTimeSeconds) {
    }

    @Override
    public EntitySprite copy() {
        return new SimpleSprite(image.copy());
    }
}
