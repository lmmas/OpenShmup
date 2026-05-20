package editor.attribute;

import com.fasterxml.jackson.databind.node.ObjectNode;
import engine.types.IVec2D;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
final public class IVec2DAttribute extends Attribute {

    private IVec2D value;

    public IVec2DAttribute(String name, String jsonFieldName, IVec2D value) {
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
