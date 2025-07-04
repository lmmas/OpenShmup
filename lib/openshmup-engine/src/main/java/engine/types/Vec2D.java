package engine.types;

final public class Vec2D {
    public float x;
    public float y;

    public Vec2D(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vec2D (Vec2D vec){
        this.x = vec.x;
        this.y = vec.y;
    }
}
