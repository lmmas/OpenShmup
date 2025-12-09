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
}
