package json.converters.spawnInfo;

import json.SafeJsonNode;
import json.converters.JsonDataConverter;
import json.editionData.EditionData;

import java.util.ArrayList;
import java.util.List;

import static json.editionData.EditionData.Keys;
import static json.editionData.EditionData.SpawnInfo;

public class SingleSpawnInfoConverter implements SpawnInfoConverter {

    @Override
    public EditionData fromJSON(SafeJsonNode node, JsonDataConverter jsonDataConverter) {
        double spawnTime = node.safeGetFloat(Keys.SpawnInfo.Single.time.name());
        SafeJsonNode spawnsNode = node.safeGetArray(Keys.SpawnInfo.Single.spawn.name());
        List<SafeJsonNode> spawnNodes = spawnsNode.safeGetObjectListFromArray();
        List<EditionData> spawnList = new ArrayList<>(spawnNodes.size());
        for (var spawnNode : spawnNodes) {
            spawnList.add(jsonDataConverter.spawnEditionDataFromJSON(spawnNode));
        }
        return SpawnInfo.SingleSpawnInfo(spawnTime, spawnList);
    }
}
