package engine.entity.sprite;

import engine.graphics.Graphic;
import engine.render.RenderInfo;
import engine.render.Texture;

import java.util.Optional;

public interface EntitySprite{
    void setPosition(float positionX, float positionY);
    void setSize(float sizeX, float sizeY);
    void setOrientation(float orientation);
    Optional<RenderInfo> getRenderInfo();
    Optional<Graphic<?,?>> getGraphic();
    Optional<Texture> getTexture();
    void update(float currentTimeSeconds);
    EntitySprite copy();
    static EntitySprite DEFAULT(){
        return EmptySprite.getInstance();
    }
}
