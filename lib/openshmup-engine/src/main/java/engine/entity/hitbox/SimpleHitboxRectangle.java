package engine.entity.hitbox;

import engine.Vec2D;

public class SimpleHitboxRectangle {
    protected float leftBound;
    protected float rightBound;
    protected float upBound;
    protected float downBound;

    public SimpleHitboxRectangle(Vec2D position, Vec2D size) {
        this.leftBound = position.x - (size.x / 2);
        this.rightBound = position.x + (size.x / 2);
        this.upBound = position.y + (size.y / 2);
        this.downBound = position.y - (size.y / 2);
    }

    public SimpleHitboxRectangle(float leftBound, float rightBound, float upBound, float downBound) {
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.upBound = upBound;
        this.downBound = downBound;
    }

    public void setBounds(float leftBound, float rightBound, float upBound, float downBound) {
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.upBound = upBound;
        this.downBound = downBound;
    }

    public boolean intersects(SimpleHitboxRectangle otherRectangle) {
        return !(this.downBound > otherRectangle.upBound) && !(this.upBound < otherRectangle.downBound) && !(this.rightBound < otherRectangle.leftBound) && !(this.leftBound > otherRectangle.rightBound);
    }
}
