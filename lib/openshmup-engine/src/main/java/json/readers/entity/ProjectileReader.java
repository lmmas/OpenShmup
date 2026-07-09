package json.readers.entity;

import engine.types.Vec2D;
import json.JsonFieldNames;
import json.SafeJsonNode;
import json.editionData.EditionData;
import json.readers.JsonDataReader;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static json.editionData.EditionData.Entity;

final public class ProjectileReader implements EntityReader {

    @Override
    public EditionData fromJson(SafeJsonNode node, JsonDataReader jsonDataReader, Path textureFolderPath) {
        int id = node.safeGetInt(JsonFieldNames.Projectile.id);
        boolean evil = node.safeGetBoolean(JsonFieldNames.Projectile.evil);
        Vec2D size = node.safeGetVec2D(JsonFieldNames.Projectile.size);
        SafeJsonNode hitboxNode = node.safeGetObject(JsonFieldNames.Projectile.hitbox);
        EditionData hitboxData = jsonDataReader.hitboxEditionDataFromJSON(hitboxNode, textureFolderPath);
        SafeJsonNode deathSpawnNode = node.safeGetArray(JsonFieldNames.Projectile.deathSpawn);
        List<SafeJsonNode> spawnableNodes = deathSpawnNode.safeGetObjectListFromArray();
        List<EditionData> deathSpawn = spawnableNodes.stream().map(
            jsonDataReader::spawnEditionDataFromJSON
        ).toList();
        int spriteVisualId = node.safeGetInt(JsonFieldNames.Projectile.spriteVisualId);
        int defaultTrajectoryID = node.safeGetInt(JsonFieldNames.Ship.defaultTrajectoryId);
        ArrayList<EditionData> shots = new ArrayList<>();
        if (node.hasField(JsonFieldNames.Ship.shots)) {
            SafeJsonNode shotsArray = node.safeGetArray(JsonFieldNames.Ship.shots);
            List<SafeJsonNode> shotNodes = shotsArray.safeGetObjectListFromArray();
            for (var extraComponentNode : shotNodes) {
                shots.add(jsonDataReader.shotEditionDataFromJSON(extraComponentNode, jsonDataReader, textureFolderPath));
            }
        }
        return Entity.Projectile(id, evil, size, spriteVisualId, hitboxData, defaultTrajectoryID, deathSpawn, shots);
    }

}
