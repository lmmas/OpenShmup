package editor.attribute;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
final public class Vec2DAttribute extends Attribute {

    private float x;

    private float y;

    public Vec2DAttribute(String name, String jsonFieldName, float x, float y) {
        super(name, jsonFieldName);
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return name + ": " + x + ", " + y;
    }

    @Override public void addToNode(ObjectNode node) {
        var arrayNode = node.putArray(jsonFieldName);
        arrayNode.add(x);
        arrayNode.add(y);
    }
}
