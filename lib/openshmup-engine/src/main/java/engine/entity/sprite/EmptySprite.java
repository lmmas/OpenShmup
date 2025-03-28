package engine.entity.sprite;

import engine.graphics.Graphic;
import engine.render.RenderInfo;
import engine.render.Texture;

import java.util.Optional;

public class EmptySprite implements EntitySprite{
    private static EmptySprite instance = null;
    private EmptySprite(){

    }

    public static EmptySprite getInstance() {
        if(instance == null){
            instance = new EmptySprite();
        }
        return instance;
    }

    @Override
    public void setPosition(float positionX, float positionY) {

    }

    @Override
    public void setSize(float sizeX, float sizeY) {

    }

    @Override
    public void setOrientation(float orientation) {

    }

    @Override
    public Optional<RenderInfo> getRenderInfo() {
        return Optional.empty();
    }

    @Override
    public Optional<Graphic<?, ?>> getGraphic() {
        return Optional.empty();
    }

    @Override
    public Optional<Texture> getTexture() {
        return Optional.empty();
    }

    @Override
    public void update(float currentTimeSeconds) {

    }

    @Override
    public EntitySprite copy() {
        return this;
    }
}
