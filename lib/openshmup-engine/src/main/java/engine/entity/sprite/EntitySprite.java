package engine.entity.sprite;

import engine.graphics.Graphic;

public interface EntitySprite{
    void setPosition(float positionX, float positionY);
    void setSize(float sizeX, float sizeY);
    void setOrientation(float orientation);
    Graphic<?,?> getGraphic();
    void update(float currentTimeSeconds);
    static EntitySprite DEFAULT(){
        return EmptySprite.getInstance();
    }
}
