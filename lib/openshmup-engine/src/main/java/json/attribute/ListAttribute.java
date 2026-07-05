package json.attribute;

import com.fasterxml.jackson.databind.node.ObjectNode;
import json.editionData.EditionData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter final public class ListAttribute extends Attribute {

    private List<EditionData> dataList;
    @Getter
    final private EditionData.Category category;

    public ListAttribute(EditionData.Category category, EditionData.Key key) {
        super(key);
        this.category = category;
        this.dataList = List.of();
    }

    public ListAttribute(EditionData.Category category, EditionData.Key key, List<EditionData> dataList) {
        super(key);
        this.dataList = dataList;
        this.category = category;
    }
    @Override
    public void addToNode(ObjectNode node) {
        var arrayNode = node.putArray(key.name());
        //dataList.forEach(data ->);
    }
}
