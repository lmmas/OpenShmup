package editor.attribute;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
final public class BooleanAttribute extends Attribute {

    private boolean value;

    public BooleanAttribute(String name, boolean value) {
        super(name);
        this.value = value;
    }

    @Override
    public String toString() {
        return name + " : " + value;
    }
}
