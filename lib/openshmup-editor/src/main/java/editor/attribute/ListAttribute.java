package editor.attribute;

import com.fasterxml.jackson.databind.node.ObjectNode;
import editor.editionData.EditionData;
import lombok.Getter;

import java.util.List;

final public class ListAttribute<D extends EditionData> extends Attribute {

    @Getter final private List<D> dataList;

    public ListAttribute(String name, String jsonFieldName, List<D> dataList) {
        super(name, jsonFieldName);
        this.dataList = dataList;
    }

    @Override public String toString() {
        return "";
    }

    @Override public void addToNode(ObjectNode node) {
        var arrayNode = node.putArray(jsonFieldName);
        //dataList.forEach(data ->);
    }
}
