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
}
