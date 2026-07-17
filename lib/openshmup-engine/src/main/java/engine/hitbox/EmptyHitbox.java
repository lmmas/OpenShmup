package engine.hitbox;

import types.Vec2D;

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
    public void setPosition(Vec2D position) {

    }

    @Override
    public void setSize(Vec2D size) {

    }

    @Override
    public void setOrientation(float orientationRadians) {

    }

    @Override
    public Hitbox copy() {
        return this;
    }
}
