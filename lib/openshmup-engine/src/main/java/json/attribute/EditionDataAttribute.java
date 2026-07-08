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
        var dataNode = node.putObject(key.name());
        if (editionData.hasTypeSelect()) {
            dataNode.put(EditionData.Keys.type.name(), editionData.getType().name());
        }
        editionData.getAttributesList().forEach(attribute -> attribute.addToNode(dataNode));
    }

    public boolean hasTypeSelect() {
        return editionData.hasTypeSelect();
    }
}
