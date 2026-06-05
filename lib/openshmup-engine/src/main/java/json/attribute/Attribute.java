package json.attribute;

import com.fasterxml.jackson.databind.node.ObjectNode;
import json.editionData.EditionData;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public sealed abstract class Attribute permits BooleanAttribute, DoubleAttribute, EditionDataAttribute, FloatAttribute, IVec2DAttribute, IntegerAttribute, ListAttribute, StringAttribute, Vec2DAttribute {

    protected EditionData.Key key;

    public Attribute(EditionData.Key key) {

        this.key = key;
    }

    abstract public void addToNode(ObjectNode node);

}
