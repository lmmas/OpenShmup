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
}
