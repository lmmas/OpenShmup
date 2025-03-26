package engine.entity.hitbox;

public interface Hitbox {
    void setPosition(float positionX, float positionY);
    void setSize(float sizeX, float sizeY);
    void setOrientation(float orientationRadians);
    boolean intersects(Hitbox otherHitbox);
    Hitbox copy();
    static Hitbox DEFAULT(){
        return EmptyHitbox.getInstance();
    }
}
