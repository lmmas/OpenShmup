package json.readers.spawnInfo;

import json.SafeJsonNode;
import json.editionData.EditionData;
import json.readers.JsonDataReader;

import java.util.ArrayList;
import java.util.List;

import static json.editionData.EditionData.Keys;
import static json.editionData.EditionData.SpawnInfo;

public class SingleSpawnInfoReader implements SpawnInfoReader {

    @Override
    public EditionData fromJSON(SafeJsonNode node, JsonDataReader jsonDataReader) {
        double spawnTime = node.safeGetDouble(Keys.SpawnInfo.Single.time.name());
        SafeJsonNode spawnsNode = node.safeGetArray(Keys.SpawnInfo.Single.spawn.name());
        List<SafeJsonNode> spawnNodes = spawnsNode.safeGetObjectListFromArray();
        List<EditionData> spawnList = new ArrayList<>(spawnNodes.size());
        for (var spawnNode : spawnNodes) {
            spawnList.add(jsonDataReader.spawnEditionDataFromJSON(spawnNode));
        }
        return SpawnInfo.SingleSpawnInfo(spawnTime, spawnList);
    }
}
