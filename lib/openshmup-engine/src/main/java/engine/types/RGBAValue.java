package engine.types;

public class RGBAValue {
    public float r;
    public float g;
    public float b;
    public float a;
    public RGBAValue(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public RGBAValue(RGBAValue rgba){
        this.r = rgba.r;
        this.g = rgba.g;
        this.b = rgba.b;
        this.a = rgba.a;
    }
}
