package json.attribute;

import com.fasterxml.jackson.databind.node.ObjectNode;
import json.editionData.EditionData;

final public class EditionDataAttribute extends Attribute {

    private EditionData editionData;

    public EditionDataAttribute(EditionData.Key key) {
        super(key);
        this.editionData = null;
    }

    public EditionDataAttribute(EditionData.Key key, EditionData editionData) {
        super(key);
        this.editionData = editionData;
    }

    public EditionData getData() {
        return editionData;
    }

    public void setData(EditionData data) {
        this.editionData = data;
    }
    @Override
    public void addToNode(ObjectNode node) {
        var arrayNode = node.putArray(key.name());
    }

    public boolean hasTypeSelect() {
        return editionData.hasTypeSelect();
    }
}
