package json.converters.extraComponent;

import com.fasterxml.jackson.databind.node.ObjectNode;
import json.JsonFieldNames;
import json.SafeJsonNode;
import json.converters.JsonDataConverter;
import json.editionData.ShotEditionData;
import json.editionData.SpawnEditionData;

import java.nio.file.Path;
import java.util.List;

final public class ShotConverter {

    public ShotEditionData fromJson(SafeJsonNode node, JsonDataConverter jsonDataConverter, Path textureFolderPath) {
        float shotPeriod = node.safeGetFloat(JsonFieldNames.Shot.shotPeriod);
        float firstShotTime = node.safeGetFloat(JsonFieldNames.Shot.firstShotTime);
        SafeJsonNode spawnsNode = node.safeGetArray(JsonFieldNames.Shot.spawn);
        List<SafeJsonNode> spawnableNodes = spawnsNode.safeGetObjectListFromArray();
        List<SpawnEditionData> spawnableList = spawnableNodes.stream().map(jsonDataConverter::spawnEditionDataFromJSON).toList();
        return new ShotEditionData(shotPeriod, firstShotTime, spawnableList);
    }

    public ObjectNode toJson(ObjectNode node, ShotEditionData shotData, JsonDataConverter jsonDataConverter) {
        shotData.getShotPeriod().addToNode(node);
        shotData.getFirstShotTime().addToNode(node);
        var spawnablesNode = node.putArray(JsonFieldNames.Shot.spawn);
        for (var spawnableData : shotData.getSpawnables().getDataList()) {
            ObjectNode spawnableNode = spawnablesNode.addObject();
            spawnableNode = jsonDataConverter.spawnEditionDataToJSON(spawnableData, spawnableNode);
        }
        return node;
    }
}
