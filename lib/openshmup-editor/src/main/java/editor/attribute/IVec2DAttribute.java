package editor.attribute;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
final public class IVec2DAttribute extends Attribute {

    private int x;

    private int y;

    public IVec2DAttribute(String name, int x, int y) {
        super(name);
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return name + " : " + x + ", " + y;
    }
}
