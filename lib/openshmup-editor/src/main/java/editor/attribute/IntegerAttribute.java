package editor.attribute;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
final public class IntegerAttribute extends Attribute {

    private Integer value;

    public IntegerAttribute(String name, String jsonFieldName, Integer value) {
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
