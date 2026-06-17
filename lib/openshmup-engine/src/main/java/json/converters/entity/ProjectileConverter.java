package json.converters.entity;

import engine.types.Vec2D;
import json.JsonFieldNames;
import json.SafeJsonNode;
import json.converters.JsonDataConverter;
import json.editionData.EditionData;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static json.editionData.EditionData.Entity;

final public class ProjectileConverter implements EntityConverter {

    @Override
    public EditionData fromJson(SafeJsonNode node, JsonDataConverter jsonDataConverter, Path textureFolderPath) {
        int id = node.safeGetInt(JsonFieldNames.Projectile.id);
        boolean evil = node.safeGetBoolean(JsonFieldNames.Projectile.evil);
        Vec2D size = node.safeGetVec2D(JsonFieldNames.Projectile.size);
        SafeJsonNode hitboxNode = node.safeGetObject(JsonFieldNames.Projectile.hitbox);
        EditionData hitboxData = jsonDataConverter.hitboxEditionDataFromJSON(hitboxNode, textureFolderPath);
        SafeJsonNode deathSpawnNode = node.safeGetArray(JsonFieldNames.Projectile.deathSpawn);
        List<SafeJsonNode> spawnableNodes = deathSpawnNode.safeGetObjectListFromArray();
        List<EditionData> deathSpawn = spawnableNodes.stream().map(
            jsonDataConverter::spawnEditionDataFromJSON
        ).toList();
        int spriteVisualId = node.safeGetInt(JsonFieldNames.Projectile.spriteVisualId);
        int defaultTrajectoryID = node.safeGetInt(JsonFieldNames.Ship.defaultTrajectoryId);
        ArrayList<EditionData> shots = new ArrayList<>();
        if (node.hasField(JsonFieldNames.Ship.shots)) {
            SafeJsonNode shotsArray = node.safeGetArray(JsonFieldNames.Ship.shots);
            List<SafeJsonNode> shotNodes = shotsArray.safeGetObjectListFromArray();
            for (var extraComponentNode : shotNodes) {
                shots.add(jsonDataConverter.shotEditionDataFromJSON(extraComponentNode, jsonDataConverter, textureFolderPath));
            }
        }
        return Entity.Projectile(id, evil, size, spriteVisualId, hitboxData, defaultTrajectoryID, deathSpawn, shots);
    }

}
