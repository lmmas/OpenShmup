package engine.hitbox;

import lombok.Getter;
import types.Vec2D;

@Getter
final public class RectangleBox implements Hitbox {

    private Vec2D position;

    private Vec2D size;

    private float leftBound;

    private float rightBound;

    private float upBound;

    private float downBound;

    public RectangleBox(Vec2D position, Vec2D size) {
        this.position = position;
        this.size = size;
        updateBounds();
    }

    @Override
    public RectangleBox copy() {
        return new RectangleBox(position, size);
    }

    @Override
    public boolean containsPoint(Vec2D position) {
        return position.x > leftBound && position.x < rightBound && position.y > downBound && position.y < upBound;
    }

    @Override
    public void setPosition(Vec2D position) {
        this.position = position;
        updateBounds();
    }

    @Override
    public void setSize(Vec2D size) {
        this.size = size;
        updateBounds();
    }

    @Override
    public void setOrientation(float orientationRadians) {

    }

    private void updateBounds() {
        this.leftBound = position.x - (size.x / 2);
        this.rightBound = position.x + (size.x / 2);
        this.upBound = position.y + (size.y / 2);
        this.downBound = position.y - (size.y / 2);
    }
}
