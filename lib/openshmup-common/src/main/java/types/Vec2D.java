package types;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor final public class Vec2D implements Serializable {

    final public float x;

    final public float y;

    final public static Vec2D ZERO = new Vec2D(0.0f, 0.0f);

    final public static Vec2D ONE = new Vec2D(1.0f, 1.0f);

    public Vec2D(Vec2D vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    public Vec2D add(float xOffset, float yOffset) {
        return new Vec2D(this.x + xOffset, this.y + yOffset);
    }

    public Vec2D add(Vec2D other) {
        return new Vec2D(this.x + other.x, this.y + other.y);
    }

    public Vec2D multiply(float xCoef, float yCoef) {
        return new Vec2D(this.x * xCoef, this.y * yCoef);
    }

    public Vec2D multiply(Vec2D other) {
        return new Vec2D(this.x * other.x, this.y * other.y);
    }

    public Vec2D scalar(float scalar) {
        return new Vec2D(this.x * scalar, this.y * scalar);
    }
}
