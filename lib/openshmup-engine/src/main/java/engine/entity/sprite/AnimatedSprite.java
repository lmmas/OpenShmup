package engine.entity.sprite;

import engine.assets.Texture;
import engine.scene.display.Animation;
import engine.graphics.Graphic;
import engine.render.RenderInfo;

import java.util.Optional;

public class AnimatedSprite implements EntitySprite{
    final private Animation animation;

    public AnimatedSprite(Animation animation) {
        this.animation = animation;
    }

    @Override
    public void setPosition(float positionX, float positionY) {
        animation.setPosition(positionX, positionY);
    }

    @Override
    public void setSize(float sizeX, float sizeY) {
        animation.setSize(sizeX, sizeY);
    }

    @Override
    public void setOrientation(float orientation) {

    }

    @Override
    public Optional<RenderInfo> getRenderInfo() {
        return Optional.of(animation.getImage().getRenderInfo());
    }

    @Override
    public Optional<Graphic<?, ?>> getGraphic() {
        return Optional.of(animation.getImage());
    }

    @Override
    public Optional<Texture> getTexture() {
        return Optional.of(animation.getTexture().orElseThrow());
    }

    @Override
    public void update(float currentTimeSeconds) {
        animation.update(currentTimeSeconds);
    }

    @Override
    public EntitySprite copy() {
        return new AnimatedSprite(animation.copy());
    }

}
