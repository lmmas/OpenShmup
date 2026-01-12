package editor.attribute;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
final public class FloatAttribute extends Attribute {

    private float value;

    public FloatAttribute(String name, float value) {
        super(name);
        this.value = value;
    }

    @Override
    public String toString() {
        return name + ": " + value;
    }
}
