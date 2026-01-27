package editor.attribute;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
final public class FloatAttribute extends Attribute {

    private float value;

    public FloatAttribute(String name, String jsonFieldName, float value) {
        super(name, jsonFieldName);
        this.value = value;
    }

    @Override
    public String toString() {
        return name + ": " + value;
    }

    @Override public void addToNode(ObjectNode node) {
        node.put(jsonFieldName, value);
    }
}
