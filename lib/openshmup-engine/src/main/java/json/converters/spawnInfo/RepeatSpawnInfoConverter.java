package json.converters.spawnInfo;

import json.SafeJsonNode;
import json.converters.JsonDataConverter;
import json.editionData.EditionData;

import java.util.ArrayList;
import java.util.List;

import static json.editionData.EditionData.Keys;
import static json.editionData.EditionData.SpawnInfo;

public class RepeatSpawnInfoConverter implements SpawnInfoConverter {

    @Override
    public EditionData fromJSON(SafeJsonNode node, JsonDataConverter jsonDataConverter) {
        double startTime = node.safeGetFloat(Keys.SpawnInfo.Repeat.startTime.name());
        int spawnCount = node.safeGetInt(Keys.SpawnInfo.Repeat.spawnCount.name());
        double interval = node.safeGetFloat(Keys.SpawnInfo.Repeat.interval.name());
        SafeJsonNode spawnsNode = node.safeGetArray(Keys.SpawnInfo.Repeat.spawn.name());
        List<SafeJsonNode> spawnNodes = spawnsNode.safeGetObjectListFromArray();
        List<EditionData> spawnList = new ArrayList<>(spawnNodes.size());
        for (var spawnNode : spawnNodes) {
            spawnList.add(jsonDataConverter.spawnEditionDataFromJSON(spawnNode));
        }
        return SpawnInfo.RepeatSpawnInfo(startTime, spawnCount, interval, spawnList);
    }
}
