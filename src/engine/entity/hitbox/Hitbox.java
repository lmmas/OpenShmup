package engine.entity.hitbox;

import engine.Vec2D;
import engine.entity.Entity;

public abstract class Hitbox {
    final protected Vec2D position;
    final protected Vec2D size;
    abstract public boolean intersects(SimpleHitBox otherHitBox);
    abstract protected void updateBounds();
    public Hitbox(float positionX, float positionY, float sizeX, float sizeY){
        this.position = new Vec2D(positionX, positionY);
        this.size = new Vec2D(sizeX, sizeY);
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
