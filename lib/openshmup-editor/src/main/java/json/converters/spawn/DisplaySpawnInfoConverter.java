package json.converters.spawn;

import com.fasterxml.jackson.databind.node.ObjectNode;
import editor.attribute.Attribute;
import editor.editionData.DisplaySpawnEditionData;
import editor.editionData.SpawnEditionData;
import engine.types.Vec2D;
import json.JsonFieldNames;
import json.SafeJsonNode;

import java.util.List;

final public class DisplaySpawnInfoConverter implements SpawnConverter {

    @Override
    public SpawnEditionData fromJson(SafeJsonNode node) {
        int id = node.safeGetInt(JsonFieldNames.DisplaySpawnInfo.id);
        Vec2D position = node.safeGetVec2D(JsonFieldNames.DisplaySpawnInfo.position);
        return new DisplaySpawnEditionData(id, position);
    }

    @Override
    public ObjectNode toJson(SpawnEditionData spawnableData, ObjectNode node) {
        DisplaySpawnEditionData displaySpawnInfoData = (DisplaySpawnEditionData) spawnableData;

        List<Attribute> attributes = List.of(displaySpawnInfoData.getVisualID(), displaySpawnInfoData.getPosition());
        attributes.forEach(attribute -> attribute.addToNode(node));

        return node;
    }
}
