package json.attribute;

import json.editionData.EditionData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
final public class DoubleAttribute extends Attribute {

    private double value;

    public DoubleAttribute(EditionData.Key key) {
        super(key);
        this.value = 0.0d;
    }

    public DoubleAttribute(EditionData.Key key, double value) {
        super(key);
        this.value = value;
    }
}
