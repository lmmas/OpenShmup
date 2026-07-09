package json.attribute;

import json.editionData.EditionData;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
final public class IntegerAttribute extends Attribute {

    private Integer value;

    public IntegerAttribute(EditionData.Key key) {
        super(key);
        this.value = 0;
    }

    public IntegerAttribute(EditionData.Key key, int value) {
        super(key);
        this.value = value;
    }
}
