package json.readers;

import edition.EditionData;
import json.JsonNodeReader;

import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
public interface SpawnInfoReader {

    EditionData fromJSON(JsonNodeReader node, GameDataReader gameDataReader);

    static EditionData SingleSpawnInfo(JsonNodeReader node, GameDataReader reader) {
        double spawnTime = node.readDouble(EditionData.Keys.SpawnInfo.Single.time);
        JsonNodeReader spawnsNode = node.readArray(EditionData.Keys.SpawnInfo.Single.spawns);
        List<JsonNodeReader> spawnNodes = spawnsNode.getObjectListFromArray();
        List<EditionData> spawnList = new ArrayList<>(spawnNodes.size());
        for (var spawnNode : spawnNodes) {
            spawnList.add(reader.spawnEditionDataFromJSON(spawnNode));
        }
        return EditionData.SpawnInfo.SingleSpawnInfo(spawnTime, spawnList);
    }

    static EditionData RepeatSpawnInfo(JsonNodeReader node, GameDataReader gameDataReader) {
        double startTime = node.readDouble(EditionData.Keys.SpawnInfo.Repeat.startTime);
        int spawnCount = node.readInt(EditionData.Keys.SpawnInfo.Repeat.spawnCount);
        double interval = node.readDouble(EditionData.Keys.SpawnInfo.Repeat.interval);
        JsonNodeReader spawnsNode = node.readArray(EditionData.Keys.SpawnInfo.Repeat.spawns);
        List<JsonNodeReader> spawnNodes = spawnsNode.getObjectListFromArray();
        List<EditionData> spawnList = new ArrayList<>(spawnNodes.size());
        for (var spawnNode : spawnNodes) {
            spawnList.add(gameDataReader.spawnEditionDataFromJSON(spawnNode));
        }
        return EditionData.SpawnInfo.RepeatSpawnInfo(startTime, spawnCount, interval, spawnList);
    }
}
