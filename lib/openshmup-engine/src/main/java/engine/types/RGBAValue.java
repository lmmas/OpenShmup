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

    public RGBAValue copy(){
        return new RGBAValue(r, g, b, a);
    }
}
