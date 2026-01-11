package editor.attribute;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
final public class IntegerAttribute extends Attribute {

    private int value;

    public IntegerAttribute(String name, int value) {
        super(name);
        this.value = value;
    }

    @Override
    public String toString() {
        return name + " : " + value;
    }
}
