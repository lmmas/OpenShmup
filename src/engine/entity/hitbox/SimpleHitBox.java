package engine.entity.hitbox;

public class SimpleHitBox extends Hitbox{
    HitboxRectangle rectangle;

    public SimpleHitBox(float positionX, float positionY, float sizeX, float sizeY) {
        super(positionX, positionY, sizeX, sizeY);
        updateBounds();
    }

    protected void updateBounds(){
        this.rectangle.leftBound = this.position.x - (this.size.x / 2);
        this.rectangle.rightBound = this.position.x + (this.size.x / 2);
        this.rectangle.upBound = this.position.y + (this.size.y / 2);
        this.rectangle.downBound = this.position.y - (this.size.y / 2);
    }
    @Override
    boolean intersects(SimpleHitBox otherHitBox) {
        return rectangle.intersects(otherHitBox.getRectangle());
    }
    protected HitboxRectangle getRectangle(){
        return rectangle;
    }
}
