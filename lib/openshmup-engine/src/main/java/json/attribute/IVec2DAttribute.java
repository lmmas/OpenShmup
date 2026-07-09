package json.attribute;

import engine.types.IVec2D;
import json.editionData.EditionData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
final public class IVec2DAttribute extends Attribute {

    private IVec2D value;

    public IVec2DAttribute(EditionData.Key key) {
        super(key);
        this.value = IVec2D.ZERO;
    }

    public IVec2DAttribute(EditionData.Key key, IVec2D value) {
        super(key);
        this.value = value;
    }

}
