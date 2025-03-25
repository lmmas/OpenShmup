package engine.entity.sprite;

import engine.graphics.Graphic;

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
    public Graphic<?, ?> getGraphic() {
        return null;
    }

    @Override
    public void update(float currentTimeSeconds) {

    }
}
