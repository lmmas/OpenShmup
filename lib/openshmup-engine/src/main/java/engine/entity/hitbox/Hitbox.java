package engine.entity.hitbox;

import java.util.List;

public interface Hitbox {
    void setPosition(float positionX, float positionY);
    void setSize(float sizeX, float sizeY);
    void setOrientation(float orientationRadians);
    boolean intersects(Hitbox otherHitbox);
    List<HitboxRectangle> getRectangles();
    Hitbox copy();
    static Hitbox DEFAULT(){
        return EmptyHitbox.getInstance();
    }
}
