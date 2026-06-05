package json.attribute;

import com.fasterxml.jackson.databind.node.ObjectNode;
import json.editionData.EditionData;
import lombok.Setter;

@Setter
final public class BooleanAttribute extends Attribute {

    private boolean value;

    public BooleanAttribute(EditionData.Key key) {
        super(key);
        this.value = false;
    }

    public BooleanAttribute(EditionData.Key key, boolean value) {
        super(key);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
    @Override
    public void addToNode(ObjectNode node) {
        node.put(key.name(), value);
    }
}
