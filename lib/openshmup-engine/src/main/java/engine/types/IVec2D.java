package engine.types;

final public class IVec2D {

    public int x;

    public int y;

    public IVec2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public IVec2D(IVec2D vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    public IVec2D add(IVec2D other) {
        return new IVec2D(this.x + other.x, this.y + other.y);
    }

    public IVec2D multiply(IVec2D other) {
        return new IVec2D(this.x * other.x, this.y * other.y);
    }

    public Vec2D scalar(float scalar) {
        return new Vec2D(this.x * scalar, this.y * scalar);
    }
}
