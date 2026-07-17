package edition.attribute;

import edition.EditionData;
import lombok.Setter;

@Setter
final public class BooleanAttribute extends Attribute {

    private boolean value;

    public BooleanAttribute(EditionData.Key key) {
        super(key);
        this.value = false;
    }

    public BooleanAttribute(EditionData.Key key, boolean value) {
        super(key);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
