package json.readers;

import edition.EditionData;
import engine.types.Vec2D;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
public interface EntityReader {

    EditionData fromJson(JsonNodeReader node, GameDataReader gameDataReader, Path textureFolderPath);

    static EditionData Projectile(JsonNodeReader node, GameDataReader gameDataReader, Path textureFolderPath) {
        int id = node.readInt(EditionData.Keys.Entity.Projectile.id);
        boolean evil = node.readBoolean(EditionData.Keys.Entity.Projectile.evil);
        Vec2D size = node.readVec2D(EditionData.Keys.Entity.Projectile.size);
        JsonNodeReader hitboxNode = node.readObject(EditionData.Keys.Entity.Projectile.hitbox);
        EditionData hitboxData = gameDataReader.hitboxEditionDataFromJSON(hitboxNode, textureFolderPath);
        JsonNodeReader deathSpawnNode = node.readArray(EditionData.Keys.Entity.Projectile.deathSpawn);
        List<JsonNodeReader> spawnableNodes = deathSpawnNode.getObjectListFromArray();
        List<EditionData> deathSpawn = spawnableNodes.stream().map(
            gameDataReader::spawnEditionDataFromJSON
        ).toList();
        int spriteVisualId = node.readInt(EditionData.Keys.Entity.Projectile.spriteVisualId);
        int defaultTrajectoryID = node.readInt(EditionData.Keys.Entity.Ship.defaultTrajectoryId);
        ArrayList<EditionData> shots = new ArrayList<>();
        if (node.hasField(EditionData.Keys.Entity.Ship.shots)) {
            JsonNodeReader shotsArray = node.readArray(EditionData.Keys.Entity.Ship.shots);
            List<JsonNodeReader> shotNodes = shotsArray.getObjectListFromArray();
            for (var extraComponentNode : shotNodes) {
                shots.add(gameDataReader.shotEditionDataFromJSON(extraComponentNode, gameDataReader));
            }
        }
        return EditionData.Entity.Projectile(id, evil, size, spriteVisualId, hitboxData, defaultTrajectoryID, deathSpawn, shots);
    }

    static EditionData Ship(JsonNodeReader node, GameDataReader gameDataReader, Path textureFolderPath) {
        int id = node.readInt(EditionData.Keys.Entity.Ship.id);
        boolean evil = node.readBoolean(EditionData.Keys.Entity.Ship.evil);
        Vec2D size = node.readVec2D(EditionData.Keys.Entity.Ship.size);
        JsonNodeReader hitboxNode = node.readObject(EditionData.Keys.Entity.Ship.hitbox);
        EditionData hitboxData = gameDataReader.hitboxEditionDataFromJSON(hitboxNode, textureFolderPath);
        JsonNodeReader deathSpawnNode = node.readArray(EditionData.Keys.Entity.Ship.deathSpawn);
        List<JsonNodeReader> spawnableNodes = deathSpawnNode.getObjectListFromArray();
        List<EditionData> deathSpawn = spawnableNodes.stream().map(gameDataReader::spawnEditionDataFromJSON).toList();
        int spriteVisualId = node.readInt(EditionData.Keys.Entity.Ship.spriteVisualId);
        int defaultTrajectoryID = node.readInt(EditionData.Keys.Entity.Ship.defaultTrajectoryId);
        ArrayList<EditionData> shots = new ArrayList<>();
        if (node.hasField(EditionData.Keys.Entity.Ship.shots)) {
            JsonNodeReader shotsArray = node.readArray(EditionData.Keys.Entity.Ship.shots);
            List<JsonNodeReader> shotNodes = shotsArray.getObjectListFromArray();
            for (var extraComponentNode : shotNodes) {
                shots.add(gameDataReader.shotEditionDataFromJSON(extraComponentNode, gameDataReader));
            }
        }

        int hp = node.readInt(EditionData.Keys.Entity.Ship.hp);
        return EditionData.Entity.Ship(id, evil, hp, size, spriteVisualId, hitboxData, defaultTrajectoryID, deathSpawn, shots);
    }
}
