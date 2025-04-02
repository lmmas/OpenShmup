package engine.entity.hitbox;

import java.util.List;

public class SimpleHitBox implements Hitbox{
    HitboxRectangle rectangle;

    public SimpleHitBox(float positionX, float positionY, float sizeX, float sizeY) {
        this.rectangle = new HitboxRectangle(positionX, positionY, sizeX, sizeY);
    }
    public SimpleHitBox(HitboxRectangle rectangle){
        //this constructor should only be used to copy objects
        this.rectangle = rectangle;
    }

    @Override
    public Hitbox copy() {
        return new SimpleHitBox(rectangle.copy());
    }

    @Override
    public void setPosition(float positionX, float positionY){
        rectangle.setPosition(positionX, positionY);
    }
    @Override
    public void setOrientation(float orientationRadians) {

    }

    @Override
    public void setSize(float sizeX, float sizeY){
        rectangle.setSize(sizeX, sizeY);
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
    public List<HitboxRectangle> getRectangles() {
        return List.of(rectangle);
    }

    protected HitboxRectangle getRectangle(){
        return rectangle;
    }

}
