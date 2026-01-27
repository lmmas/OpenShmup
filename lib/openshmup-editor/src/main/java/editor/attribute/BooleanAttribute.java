package editor.attribute;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Setter;

@Setter
final public class BooleanAttribute extends Attribute {

    private boolean value;

    public BooleanAttribute(String name, String jsonFieldName, boolean value) {
        super(name, jsonFieldName);
        this.value = value;
    }

    @Override
    public String toString() {
        return name + ": " + value;
    }

    public boolean getValue() {
        return value;
    }

    @Override public void addToNode(ObjectNode node) {
        node.put(jsonFieldName, value);
    }
}
