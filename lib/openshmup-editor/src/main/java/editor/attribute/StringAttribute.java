package editor.attribute;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
final public class StringAttribute extends Attribute {

    private String value;

    public StringAttribute(String name, String value) {
        super(name);
        this.value = value;
    }

    @Override
    public String toString() {
        return name + " : " + value;
    }
}
