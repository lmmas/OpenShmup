package json.converters.spawnable;

import com.fasterxml.jackson.databind.node.ObjectNode;
import editor.attribute.Attribute;
import editor.editionData.EntitySpawnInfoEditionData;
import editor.editionData.SpawnableEditionData;
import engine.types.Vec2D;
import json.JsonFieldNames;
import json.SafeJsonNode;

import java.util.List;

public class EntitySpawnInfoConverter implements SpawnableConverter {

    @Override
    public SpawnableEditionData fromJson(SafeJsonNode node) {
        int id = node.safeGetInt(JsonFieldNames.EntitySpawnInfo.id);
        Vec2D startingPositionVec = node.safeGetVec2D(JsonFieldNames.EntitySpawnInfo.startingPosition);
        int trajectoryId = node.safeGetInt(JsonFieldNames.EntitySpawnInfo.trajectory);
        return new EntitySpawnInfoEditionData(id, startingPositionVec.x, startingPositionVec.y, trajectoryId);
    }

    @Override
    public ObjectNode toJson(SpawnableEditionData spawnableData, ObjectNode node) {
        EntitySpawnInfoEditionData entitySpawnInfoData = (EntitySpawnInfoEditionData) spawnableData;

        List<Attribute> attributes = List.of(entitySpawnInfoData.getEntityID(), entitySpawnInfoData.getPosition(), entitySpawnInfoData.getTrajectoryID());
        attributes.forEach(attribute -> attribute.addToNode(node));

        return node;
    }
}
