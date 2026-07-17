package edition.attribute;

import edition.EditionData;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
final public class StringAttribute extends Attribute {

    private String value;

    public StringAttribute(EditionData.Key key) {
        super(key);
        this.value = "";
    }

    public StringAttribute(EditionData.Key key, String value) {
        super(key);
        this.value = value;
    }
}
