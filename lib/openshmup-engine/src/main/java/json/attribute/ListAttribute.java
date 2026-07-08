package json.attribute;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import json.editionData.EditionData;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter final public class ListAttribute extends Attribute {

    private ArrayList<EditionData> dataList;
    @Getter
    final private EditionData.Category category;

    public ListAttribute(EditionData.Category category, EditionData.Key key) {
        super(key);
        this.category = category;
        this.dataList = new ArrayList<>();
    }

    public ListAttribute(EditionData.Category category, EditionData.Key key, List<EditionData> dataList) {
        super(key);
        this.dataList = new ArrayList<>(dataList);
        this.category = category;
    }
    @Override
    public void addToNode(ObjectNode node) {
        ArrayNode arrayNode = node.putArray(key.name());
        for (EditionData data : dataList) {
            ObjectNode dataNode = arrayNode.addObject();
            if (data.hasTypeSelect()) {
                dataNode.put(EditionData.Keys.type.name(), data.getType().name());
            }
            for (Attribute attribute : data.getAttributesList()) {
                attribute.addToNode(dataNode);
            }
        }
    }

    public void setDataList(List<EditionData> dataList) {
        this.dataList = new ArrayList<>(dataList);
    }
}
