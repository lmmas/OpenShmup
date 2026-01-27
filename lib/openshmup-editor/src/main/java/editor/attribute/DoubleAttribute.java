package editor.attribute;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
final public class DoubleAttribute extends Attribute {

    private double value;

    public DoubleAttribute(String name, String jsonFieldName, double value) {
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
