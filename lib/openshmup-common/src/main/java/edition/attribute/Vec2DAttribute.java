package edition.attribute;

import edition.EditionData;
import lombok.Getter;
import lombok.Setter;
import types.Vec2D;

@Getter @Setter
final public class Vec2DAttribute extends Attribute {

    private Vec2D value;

    public Vec2DAttribute(EditionData.Key key) {
        super(key);
        this.value = Vec2D.ZERO;
    }

    public Vec2DAttribute(EditionData.Key key, Vec2D value) {
        super(key);
        this.value = value;
    }
}
