package engine.entity.hitbox;

import engine.Vec2D;

public class HitboxRectangle {
    final public Vec2D position;
    final public Vec2D size;
    public float leftBound;
    public float rightBound;
    public float upBound;
    public float downBound;

    public HitboxRectangle(float positionX, float positionY, float sizeX, float sizeY) {
        this.position = new Vec2D(positionX, positionY);
        this.size = new Vec2D(sizeX, sizeY);
        updateBounds();
    }

    public HitboxRectangle copy() {
        return new HitboxRectangle(position.x, position.y, size.x, size.y);
    }

    public void setPosition(float positionX, float positionY){
        this.position.x = positionX;
        this.position.y = positionY;
        updateBounds();
    }

    public void setSize(float sizeX, float sizeY){
        this.size.x = sizeX;
        this.size.y = sizeY;
        updateBounds();
    }

    private void updateBounds(){
        this.leftBound = position.x - (size.x / 2);
        this.rightBound = position.x + (size.x / 2);
        this.upBound = position.y + (size.y / 2);
        this.downBound = position.y - (size.y / 2);
    }

    public boolean intersects(HitboxRectangle otherRectangle) {
        return !(this.downBound > otherRectangle.upBound) && !(this.upBound < otherRectangle.downBound) && !(this.rightBound < otherRectangle.leftBound) && !(this.leftBound > otherRectangle.rightBound);
    }
}
