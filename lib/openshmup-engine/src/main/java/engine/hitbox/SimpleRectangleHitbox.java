package engine.hitbox;

import types.Vec2D;

final public class SimpleRectangleHitbox implements Hitbox {

    public Vec2D position;

    public Vec2D size;

    public float leftBound;

    public float rightBound;

    public float upBound;

    public float downBound;

    public SimpleRectangleHitbox(Vec2D position, Vec2D size) {
        this.position = position;
        this.size = size;
        updateBounds();
    }

    @Override
    public SimpleRectangleHitbox copy() {
        return new SimpleRectangleHitbox(position, size);
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

    public boolean intersects(SimpleRectangleHitbox otherRectangle) {
        return !(this.downBound > otherRectangle.upBound) && !(this.upBound < otherRectangle.downBound) && !(this.rightBound < otherRectangle.leftBound) && !(this.leftBound > otherRectangle.rightBound);
    }
}
