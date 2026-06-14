package json.attribute;

import com.fasterxml.jackson.databind.node.ObjectNode;
import json.editionData.EditionData;

final public class EditionDataAttribute<D extends EditionData> extends Attribute {

    private D editionData;

    public EditionDataAttribute(EditionData.Key key) {
        super(key);
        this.editionData = null;
    }

    public EditionDataAttribute(EditionData.Key key, D editionData) {
        super(key);
        this.editionData = editionData;
    }

    public D getData() {
        return editionData;
    }

    public void setData(EditionData data) {
        this.editionData = (D) data;
    }
    @Override
    public void addToNode(ObjectNode node) {
        var arrayNode = node.putArray(key.name());
    }

    public boolean hasTypeSelect() {
        return EditionData.hasTypeSelect(editionData);
    }
}
