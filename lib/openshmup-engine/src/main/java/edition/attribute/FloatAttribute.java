package edition.attribute;

import edition.EditionData;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
final public class FloatAttribute extends Attribute {

    private float value;

    public FloatAttribute(EditionData.Key key) {
        super(key);
        this.value = 0.0f;
    }

    public FloatAttribute(EditionData.Key key, float value) {
        super(key);
        this.value = value;
    }
}
