package editor.attribute;

import lombok.Data;

@Data
public abstract class Attribute {

    protected String name;

    public Attribute(String name) {
        this.name = name;
    }

    abstract public String toString();
}
