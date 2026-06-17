package json.attribute;

import com.fasterxml.jackson.databind.node.ObjectNode;
import json.editionData.EditionData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter final public class ListAttribute extends Attribute {

    private List<EditionData> dataList;

    public ListAttribute(EditionData.Key key) {
        super(key);
        this.dataList = List.of();
    }

    public ListAttribute(EditionData.Key key, List<EditionData> dataList) {
        super(key);
        this.dataList = dataList;
    }
    @Override
    public void addToNode(ObjectNode node) {
        var arrayNode = node.putArray(key.name());
        //dataList.forEach(data ->);
    }
}
