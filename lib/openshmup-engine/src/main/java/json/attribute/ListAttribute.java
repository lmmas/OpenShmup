package json.attribute;

import json.editionData.EditionData;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter final public class ListAttribute extends Attribute {

    private ArrayList<EditionData> dataList;

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

    public void setDataList(List<EditionData> dataList) {
        this.dataList = new ArrayList<>(dataList);
    }
}
