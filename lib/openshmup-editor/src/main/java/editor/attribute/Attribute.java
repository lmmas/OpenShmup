package editor.attribute;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class Attribute {

    protected String name;

    protected String jsonFieldName;

    public Attribute(String name, String jsonFieldName) {
        this.name = name;
        this.jsonFieldName = jsonFieldName;
    }

    abstract public String toString();

    abstract public void addToNode(ObjectNode node);
}
