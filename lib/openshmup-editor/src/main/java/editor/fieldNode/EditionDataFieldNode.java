package editor.fieldNode;

import engine.types.Vec2D;
import json.editionData.EditionData;

public sealed interface EditionDataFieldNode extends FieldNode permits EditionDataFields, EditionDataTypeSelect {

    EditionData getEditionData();

    void setEditionData(EditionData data);

    static EditionDataFieldNode createFromEtitionData(EditionData data, Vec2D startPosition) {
        if (data.hasTypeSelect()) {
            return new EditionDataTypeSelect(data, startPosition);
        }
        else {
            return new EditionDataFields(data, startPosition);
        }
    }
}
