package json.readers.extraComponent;

import json.JsonFieldNames;
import json.SafeJsonNode;
import json.editionData.EditionData;
import json.readers.JsonDataReader;

import java.nio.file.Path;
import java.util.List;

import static json.editionData.EditionData.Shot;

final public class ShotReader {

    public EditionData fromJson(SafeJsonNode node, JsonDataReader jsonDataReader, Path textureFolderPath) {
        double shotPeriod = node.safeGetDouble(JsonFieldNames.Shot.shotPeriod);
        double firstShotTime = node.safeGetDouble(JsonFieldNames.Shot.firstShotTime);
        SafeJsonNode spawnsNode = node.safeGetArray(JsonFieldNames.Shot.spawn);
        List<SafeJsonNode> spawnableNodes = spawnsNode.safeGetObjectListFromArray();
        List<EditionData> spawnableList = spawnableNodes.stream().map(jsonDataReader::spawnEditionDataFromJSON).toList();
        return Shot(shotPeriod, firstShotTime, spawnableList);
    }

}
