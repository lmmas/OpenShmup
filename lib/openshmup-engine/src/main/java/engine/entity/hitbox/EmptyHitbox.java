package engine.entity.hitbox;

import engine.types.Vec2D;

final public class EmptyHitbox implements Hitbox {

    private static EmptyHitbox instance = null;

    private EmptyHitbox() {

    }

    public static EmptyHitbox getInstance() {
        if (instance == null) {
            instance = new EmptyHitbox();
        }
        return instance;
    }

    @Override
    public boolean containsPoint(Vec2D position) {
        return false;
    }

    @Override
    public void setPosition(float positionX, float positionY) {

    }

    @Override
    public void setSize(float sizeX, float sizeY) {

    }

    @Override
    public void setOrientation(float orientationRadians) {

    }

    @Override
    public Hitbox copy() {
        return this;
    }
}
