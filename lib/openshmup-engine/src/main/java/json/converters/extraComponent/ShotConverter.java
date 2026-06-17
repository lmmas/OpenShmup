package json.converters.extraComponent;

import json.JsonFieldNames;
import json.SafeJsonNode;
import json.converters.JsonDataConverter;
import json.editionData.EditionData;

import java.nio.file.Path;
import java.util.List;

import static json.editionData.EditionData.Shot;

final public class ShotConverter {

    public EditionData fromJson(SafeJsonNode node, JsonDataConverter jsonDataConverter, Path textureFolderPath) {
        float shotPeriod = node.safeGetFloat(JsonFieldNames.Shot.shotPeriod);
        float firstShotTime = node.safeGetFloat(JsonFieldNames.Shot.firstShotTime);
        SafeJsonNode spawnsNode = node.safeGetArray(JsonFieldNames.Shot.spawn);
        List<SafeJsonNode> spawnableNodes = spawnsNode.safeGetObjectListFromArray();
        List<EditionData> spawnableList = spawnableNodes.stream().map(jsonDataConverter::spawnEditionDataFromJSON).toList();
        return Shot(shotPeriod, firstShotTime, spawnableList);
    }

}
