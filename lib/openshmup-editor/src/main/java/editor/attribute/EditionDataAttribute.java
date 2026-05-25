package editor.attribute;

import com.fasterxml.jackson.databind.node.ObjectNode;
import editor.editionData.*;

final public class EditionDataAttribute<D extends EditionData> extends Attribute {

    private D editionData;

    public EditionDataAttribute(String name, String jsonFieldName) {
        super(name, jsonFieldName);
        this.editionData = null;
    }

    public EditionDataAttribute(String name, String jsonFieldName, D editionData) {
        super(name, jsonFieldName);
        this.editionData = editionData;
    }

    public D getData() {
        return editionData;
    }

    public void setData(EditionData data) {
        this.editionData = (D) data;
    }

    @Override public String toString() {
        return "";
    }

    @Override public void addToNode(ObjectNode node) {
        var arrayNode = node.putArray(jsonFieldName);
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
