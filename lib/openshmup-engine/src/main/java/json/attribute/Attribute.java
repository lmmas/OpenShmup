package json.attribute;

import json.editionData.EditionData;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public sealed abstract class Attribute implements Serializable permits BooleanAttribute, DoubleAttribute, EditionDataAttribute, FloatAttribute, IVec2DAttribute, IntegerAttribute, ListAttribute, StringAttribute, Vec2DAttribute {

    protected EditionData.Key key;

    public Attribute(EditionData.Key key) {

        this.key = key;
    }

}
