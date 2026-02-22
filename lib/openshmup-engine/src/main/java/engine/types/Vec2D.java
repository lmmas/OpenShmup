package engine.types;

import lombok.AllArgsConstructor;

@AllArgsConstructor
final public class Vec2D {

    public float x;

    public float y;

    public Vec2D(Vec2D vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    public Vec2D add(Vec2D other) {
        return new Vec2D(this.x + other.x, this.y + other.y);
    }

    public Vec2D multiply(Vec2D other) {
        return new Vec2D(this.x * other.x, this.y * other.y);
    }

    public Vec2D scalar(float scalar) {
        return new Vec2D(this.x * scalar, this.y * scalar);
    }
}
