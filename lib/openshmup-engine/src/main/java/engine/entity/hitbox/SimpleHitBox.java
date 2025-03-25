package engine.entity.hitbox;

import engine.Vec2D;

public class SimpleHitBox implements Hitbox{
    final protected Vec2D position;
    final protected Vec2D size;
    HitboxRectangle rectangle;

    public SimpleHitBox(float positionX, float positionY, float sizeX, float sizeY) {
        this.position = new Vec2D(positionX, positionY);
        this.size = new Vec2D(sizeX, sizeY);
        this.rectangle = new HitboxRectangle(new Vec2D(positionX, positionY), new Vec2D(sizeX, sizeY));
    }

    protected void updateBounds(){
        this.rectangle.leftBound = this.position.x - (this.size.x / 2);
        this.rectangle.rightBound = this.position.x + (this.size.x / 2);
        this.rectangle.upBound = this.position.y + (this.size.y / 2);
        this.rectangle.downBound = this.position.y - (this.size.y / 2);
    }
    protected HitboxRectangle getRectangle(){
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

    protected class HitboxRectangle{
        protected float leftBound;
        protected float rightBound;
        protected float upBound;
        protected float downBound;
        public HitboxRectangle(Vec2D position, Vec2D size){
            this.leftBound = position.x - (size.x / 2);
            this.rightBound = position.x + (size.x / 2);
            this.upBound = position.y + (size.y / 2);
            this.downBound = position.y - (size.y / 2);
        }

        public HitboxRectangle(float leftBound, float rightBound, float upBound, float downBound) {
            this.leftBound = leftBound;
            this.rightBound = rightBound;
            this.upBound = upBound;
            this.downBound = downBound;
        }

        public void setBounds(float leftBound, float rightBound, float upBound, float downBound){
            this.leftBound = leftBound;
            this.rightBound = rightBound;
            this.upBound = upBound;
            this.downBound = downBound;
        }

        public boolean intersects(HitboxRectangle otherRectangle){
            return !(this.downBound > otherRectangle.upBound) && !(this.upBound < otherRectangle.downBound) && !(this.rightBound < otherRectangle.leftBound) && !(this.leftBound > otherRectangle.rightBound);
        }
    }
}
