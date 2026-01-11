package editor.attribute;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
final public class Vec2DAttribute extends Attribute {

    private float x;

    private float y;

    public Vec2DAttribute(String name, float x, float y) {
        super(name);
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return name + " : " + x + ", " + y;
    }
}
