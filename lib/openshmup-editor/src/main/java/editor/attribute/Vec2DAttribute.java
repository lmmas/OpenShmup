package editor.attribute;

import com.fasterxml.jackson.databind.node.ObjectNode;
import engine.types.Vec2D;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
final public class Vec2DAttribute extends Attribute {

    private Vec2D value;

    public Vec2DAttribute(String name, String jsonFieldName) {
        super(name, jsonFieldName);
        this.value = Vec2D.ZERO;
    }

    public Vec2DAttribute(String name, String jsonFieldName, Vec2D value) {
        super(name, jsonFieldName);
        this.value = value;
    }

    @Override
    public String toString() {
        return name + ": " + value.x + ", " + value.y;
    }

    @Override public void addToNode(ObjectNode node) {
        var arrayNode = node.putArray(jsonFieldName);
        arrayNode.add(value.x);
        arrayNode.add(value.y);
    }
}
