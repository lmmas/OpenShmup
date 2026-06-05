package json.attribute;

import com.fasterxml.jackson.databind.node.ObjectNode;
import json.editionData.*;

final public class EditionDataAttribute<D extends EditionData> extends Attribute {

    private D editionData;

    public EditionDataAttribute(EditionData.Key key) {
        super(key);
        this.editionData = null;
    }

    public EditionDataAttribute(EditionData.Key key, D editionData) {
        super(key);
        this.editionData = editionData;
    }

    public D getData() {
        return editionData;
    }

    public void setData(EditionData data) {
        this.editionData = (D) data;
    }
    @Override
    public void addToNode(ObjectNode node) {
        var arrayNode = node.putArray(key.name());
    }

    public boolean hasTypeSelect() {
        return switch (editionData) {
            case VisualEditionData ignored -> true;
            case TrajectoryEditionData ignored -> true;
            case EntityEditionData ignored -> true;
            case HitboxEditionData ignored -> true;
            case SpawnEditionData ignored -> true;
            case ShotEditionData ignored -> false;
            case AnimationEditionData.SpritesheetInfoData ignored -> false;
        };
    }
}
