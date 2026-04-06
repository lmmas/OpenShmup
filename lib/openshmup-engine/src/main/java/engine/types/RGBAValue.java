package engine.types;

import lombok.AllArgsConstructor;

@AllArgsConstructor
final public class RGBAValue {

    final public float r;

    final public float g;

    final public float b;

    final public float a;

    public RGBAValue(RGBAValue rgba) {
        this.r = rgba.r;
        this.g = rgba.g;
        this.b = rgba.b;
        this.a = rgba.a;
    }

    public RGBAValue add(RGBAValue other) {
        return new RGBAValue(this.r + other.r, this.g + other.g, this.b + other.b, this.a + other.a);
    }

    public RGBAValue multiply(RGBAValue other) {
        return new RGBAValue(this.r * other.r, this.g * other.g, this.b * other.b, this.a * other.a);
    }

    public RGBAValue scalar(float scalar) {
        return new RGBAValue(this.r * scalar, this.g * scalar, this.b * scalar, this.a * scalar);
    }

    final public static RGBAValue SOLID_WHITE = new RGBAValue(1.0f, 1.0f, 1.0f, 1.0f);

    final public static RGBAValue SOLID_BLACK = new RGBAValue(0.0f, 0.0f, 0.0f, 1.0f);
}
