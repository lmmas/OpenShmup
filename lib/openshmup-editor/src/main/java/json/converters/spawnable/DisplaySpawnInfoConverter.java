package json.converters.spawnable;

import com.fasterxml.jackson.databind.node.ObjectNode;
import editor.attribute.Attribute;
import editor.editionData.DisplaySpawnInfoEditionData;
import editor.editionData.SpawnableEditionData;
import engine.types.Vec2D;
import json.JsonFieldNames;
import json.SafeJsonNode;

import java.util.List;

public class DisplaySpawnInfoConverter implements SpawnableConverter {

    @Override
    public SpawnableEditionData fromJson(SafeJsonNode node) {
        int id = node.safeGetInt(JsonFieldNames.DisplaySpawnInfo.id);
        Vec2D position = node.safeGetVec2D(JsonFieldNames.DisplaySpawnInfo.position);
        return new DisplaySpawnInfoEditionData(id, position.x, position.y);
    }

    @Override
    public ObjectNode toJson(SpawnableEditionData spawnableData, ObjectNode node) {
        DisplaySpawnInfoEditionData displaySpawnInfoData = (DisplaySpawnInfoEditionData) spawnableData;

        List<Attribute> attributes = List.of(displaySpawnInfoData.getVisualID(), displaySpawnInfoData.getPosition());
        attributes.forEach(attribute -> attribute.addToNode(node));

        return node;
    }
}
