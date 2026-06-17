package json.attribute;

import com.fasterxml.jackson.databind.node.ObjectNode;
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

    @Override
    public void addToNode(ObjectNode node) {
        var arrayNode = node.putArray(key.name());
        arrayNode.add(value.x);
        arrayNode.add(value.y);
    }
}
