package engine.entity.hitbox;

import engine.types.Vec2D;

final public class SimpleRectangleHitbox implements Hitbox{
    final public Vec2D position;
    final public Vec2D size;
    public float leftBound;
    public float rightBound;
    public float upBound;
    public float downBound;

    public SimpleRectangleHitbox(float positionX, float positionY, float sizeX, float sizeY) {
        this.position = new Vec2D(positionX, positionY);
        this.size = new Vec2D(sizeX, sizeY);
        updateBounds();
    }

    @Override
    public SimpleRectangleHitbox copy() {
        return new SimpleRectangleHitbox(position.x, position.y, size.x, size.y);
    }

    @Override
    public void setPosition(float positionX, float positionY){
        this.position.x = positionX;
        this.position.y = positionY;
        updateBounds();
    }

    @Override
    public void setSize(float sizeX, float sizeY){
        this.size.x = sizeX;
        this.size.y = sizeY;
        updateBounds();
    }

    @Override
    public void setOrientation(float orientationRadians) {

    }

    private void updateBounds(){
        this.leftBound = position.x - (size.x / 2);
        this.rightBound = position.x + (size.x / 2);
        this.upBound = position.y + (size.y / 2);
        this.downBound = position.y - (size.y / 2);
    }

    public boolean intersects(SimpleRectangleHitbox otherRectangle) {
        return !(this.downBound > otherRectangle.upBound) && !(this.upBound < otherRectangle.downBound) && !(this.rightBound < otherRectangle.leftBound) && !(this.leftBound > otherRectangle.rightBound);
    }
}
