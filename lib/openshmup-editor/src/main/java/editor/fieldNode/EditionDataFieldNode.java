package editor.fieldNode;

import edition.EditionData;
import engine.types.Vec2D;

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
