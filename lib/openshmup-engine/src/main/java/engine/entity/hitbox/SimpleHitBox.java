package engine.entity.hitbox;

import engine.Vec2D;

public class SimpleHitBox implements Hitbox{
    final protected Vec2D position;
    final protected Vec2D size;
    SimpleHitboxRectangle rectangle;

    public SimpleHitBox(float positionX, float positionY, float sizeX, float sizeY) {
        this.position = new Vec2D(positionX, positionY);
        this.size = new Vec2D(sizeX, sizeY);
        this.rectangle = new SimpleHitboxRectangle(new Vec2D(positionX, positionY), new Vec2D(sizeX, sizeY));
    }

    protected void updateBounds(){
        this.rectangle.leftBound = this.position.x - (this.size.x / 2);
        this.rectangle.rightBound = this.position.x + (this.size.x / 2);
        this.rectangle.upBound = this.position.y + (this.size.y / 2);
        this.rectangle.downBound = this.position.y - (this.size.y / 2);
    }
    protected SimpleHitboxRectangle getRectangle(){
        return rectangle;
    }

    @Override
    public void setPosition(float positionX, float positionY){
        this.position.x = positionX;
        this.position.y = positionY;
        updateBounds();
    }

    @Override
    public void setOrientation(float orientationRadians) {

    }
    @Override
    public void setSize(float sizeX, float sizeY){
        this.size.x = sizeX;
        this.size.y = sizeY;
        updateBounds();
    }

    @Override
    public boolean intersects(Hitbox otherHitbox) {
        if(otherHitbox == EmptyHitbox.getInstance()){
            return false;
        }
        SimpleHitBox otherSimpleHitbox = (SimpleHitBox) otherHitbox;
        return rectangle.intersects(otherSimpleHitbox.getRectangle());
    }

    @Override
    public Hitbox copy() {
        return new SimpleHitBox(position.x, position.y, size.x, size.y);
    }

}
