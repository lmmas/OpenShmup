package json.readers.spawnInfo;

import json.SafeJsonNode;
import json.editionData.EditionData;
import json.readers.JsonDataReader;

import java.util.ArrayList;
import java.util.List;

import static json.editionData.EditionData.Keys;
import static json.editionData.EditionData.SpawnInfo;

public class RepeatSpawnInfoReader implements SpawnInfoReader {

    @Override
    public EditionData fromJSON(SafeJsonNode node, JsonDataReader jsonDataReader) {
        double startTime = node.safeGetDouble(Keys.SpawnInfo.Repeat.startTime.name());
        int spawnCount = node.safeGetInt(Keys.SpawnInfo.Repeat.spawnCount.name());
        double interval = node.safeGetDouble(Keys.SpawnInfo.Repeat.interval.name());
        SafeJsonNode spawnsNode = node.safeGetArray(Keys.SpawnInfo.Repeat.spawn.name());
        List<SafeJsonNode> spawnNodes = spawnsNode.safeGetObjectListFromArray();
        List<EditionData> spawnList = new ArrayList<>(spawnNodes.size());
        for (var spawnNode : spawnNodes) {
            spawnList.add(jsonDataReader.spawnEditionDataFromJSON(spawnNode));
        }
        return SpawnInfo.RepeatSpawnInfo(startTime, spawnCount, interval, spawnList);
    }
}
