package json.attribute;

import com.fasterxml.jackson.databind.node.ObjectNode;
import engine.types.Vec2D;
import json.editionData.EditionData;
import lombok.Getter;
import lombok.Setter;

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
    @Override
    public void addToNode(ObjectNode node) {
        var arrayNode = node.putArray(key.name());
        arrayNode.add(value.x);
        arrayNode.add(value.y);
    }
}
