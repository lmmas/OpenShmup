package engine.entity.hitbox;

import engine.Vec2D;

public class SimpleHitBox extends Hitbox{
    HitboxRectangle rectangle;

    public SimpleHitBox(float positionX, float positionY, float sizeX, float sizeY) {
        super(positionX, positionY, sizeX, sizeY);
        this.rectangle = new HitboxRectangle(new Vec2D(positionX, positionY), new Vec2D(sizeX, sizeY));
    }

    protected void updateBounds(){
        this.rectangle.leftBound = this.position.x - (this.size.x / 2);
        this.rectangle.rightBound = this.position.x + (this.size.x / 2);
        this.rectangle.upBound = this.position.y + (this.size.y / 2);
        this.rectangle.downBound = this.position.y - (this.size.y / 2);
    }
    @Override
    public boolean intersects(SimpleHitBox otherHitBox) {
        return rectangle.intersects(otherHitBox.getRectangle());
    }
    protected HitboxRectangle getRectangle(){
        return rectangle;
    }
}
