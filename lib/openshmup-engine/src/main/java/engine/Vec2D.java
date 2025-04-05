package engine;

final public class Vec2D {
    public float x;
    public float y;
    public Vec2D(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vec2D copy(){
        return new Vec2D(x,y);
    }
}
