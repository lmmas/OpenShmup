package engine.types;

import lombok.AllArgsConstructor;

@AllArgsConstructor
final public class RGBAValue {

    public float r;

    public float g;

    public float b;

    public float a;

    public RGBAValue(RGBAValue rgba) {
        this.r = rgba.r;
        this.g = rgba.g;
        this.b = rgba.b;
        this.a = rgba.a;
    }

    public RGBAValue add(RGBAValue other) {
        return new RGBAValue(this.r + other.r, this.g + this.g, this.b + this.b, this.a + this.a);
    }

    public RGBAValue multiply(RGBAValue other) {
        return new RGBAValue(this.r * other.r, this.g * this.g, this.b * this.b, this.a * this.a);
    }

    public RGBAValue scalar(float scalar) {
        return new RGBAValue(this.r * scalar, this.g * scalar, this.b * scalar, this.a * scalar);
    }
}
