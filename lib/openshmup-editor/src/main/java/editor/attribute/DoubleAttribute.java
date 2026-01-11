package editor.attribute;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
final public class DoubleAttribute extends Attribute {

    private double value;

    public DoubleAttribute(String name, double value) {
        super(name);
        this.value = value;
    }

    @Override
    public String toString() {
        return name + " : " + value;
    }
}
