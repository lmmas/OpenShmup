package editor.attribute;

import com.fasterxml.jackson.databind.node.ObjectNode;
import editor.editionData.EditionData;

final public class EditionDataAttribute<D extends EditionData> extends Attribute {

    private D editionData;

    public EditionDataAttribute(String name, String jsonFieldName, D editionData) {
        super(name, jsonFieldName);
        this.editionData = editionData;
    }

    public D getData() {
        return editionData;
    }

    @Override public String toString() {
        return "";
    }

    @Override public void addToNode(ObjectNode node) {
        var arrayNode = node.putArray(jsonFieldName);
    }
}
