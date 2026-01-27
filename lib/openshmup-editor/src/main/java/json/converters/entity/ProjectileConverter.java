package json.converters.entity;

import com.fasterxml.jackson.databind.node.ObjectNode;
import editor.editionData.*;
import engine.types.Vec2D;
import json.JsonFieldNames;
import json.SafeJsonNode;
import json.converters.JsonDataConverter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ProjectileConverter implements EntityConverter {

    @Override
    public EntityEditionData fromJson(SafeJsonNode node, JsonDataConverter jsonDataConverter, Path textureFolderPath) {
        int id = node.safeGetInt(JsonFieldNames.Projectile.id);
        boolean evil = node.safeGetBoolean(JsonFieldNames.Projectile.evil);
        Vec2D size = node.safeGetVec2D(JsonFieldNames.Projectile.size);
        SafeJsonNode hitboxNode = node.safeGetObject(JsonFieldNames.Projectile.hitbox);
        HitboxEditionData hitboxData = jsonDataConverter.hitboxEditionDataFromJSON(hitboxNode, textureFolderPath);
        SafeJsonNode deathSpawnNode = node.safeGetArray(JsonFieldNames.Projectile.deathSpawn);
        List<SafeJsonNode> spawnableNodes = deathSpawnNode.safeGetObjectListFromArray();
        List<SpawnableEditionData> deathSpawn = spawnableNodes.stream().map(
            jsonDataConverter::spawnableEditionDataFromJSON
        ).toList();
        int spriteVisualId = node.safeGetInt(JsonFieldNames.Projectile.spriteVisualId);
        Integer defaultTrajectoryID = null;
        if (node.hasField(JsonFieldNames.Projectile.defaultTrajectoryId)) {
            defaultTrajectoryID = node.safeGetInt(JsonFieldNames.Projectile.defaultTrajectoryId);
        }
        ArrayList<ExtraComponentEditionData> extraComponents = new ArrayList<>();
        if (node.hasField(JsonFieldNames.Projectile.shot)) {
            SafeJsonNode shotNode = node.safeGetObject(JsonFieldNames.Projectile.shot);
            extraComponents.add(jsonDataConverter.extraComponentEditionDataFromJSON(shotNode, jsonDataConverter, textureFolderPath));
        }
        return new ProjectileEditionData(id, evil, size.x, size.y, spriteVisualId, hitboxData, defaultTrajectoryID, deathSpawn, extraComponents);
    }

    @Override
    public ObjectNode toJson(EntityEditionData entityData, ObjectNode node) {
        return null;
    }
}
