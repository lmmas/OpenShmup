package json.converters.extraComponent;

import com.fasterxml.jackson.databind.node.ObjectNode;
import editor.editionData.ExtraComponentEditionData;
import editor.editionData.ShotEditionData;
import editor.editionData.SpawnableEditionData;
import json.JsonFieldNames;
import json.SafeJsonNode;
import json.converters.JsonDataConverter;

import java.nio.file.Path;
import java.util.List;

public class ShotConverter implements ExtraComponentConverter {

    @Override
    public ExtraComponentEditionData fromJson(SafeJsonNode node, JsonDataConverter jsonDataConverter, Path textureFolderPath) {
        float shotPeriod = node.safeGetFloat(JsonFieldNames.Shot.shotPeriod);
        float firstShotTime = node.safeGetFloat(JsonFieldNames.Shot.firstShotTime);
        SafeJsonNode spawnsNode = node.safeGetArray(JsonFieldNames.Shot.spawn);
        List<SafeJsonNode> spawnableNodes = spawnsNode.safeGetObjectListFromArray();
        List<SpawnableEditionData> spawnableList = spawnableNodes.stream().map(jsonDataConverter::spawnableEditionDataFromJSON).toList();
        return new ShotEditionData(shotPeriod, firstShotTime, spawnableList);
    }

    @Override
    public ObjectNode toJson(ObjectNode node, ExtraComponentEditionData extraComponentData, JsonDataConverter jsonDataConverter) {
        ShotEditionData shotData = (ShotEditionData) extraComponentData;
        shotData.getShotPeriod().addToNode(node);
        shotData.getFirstShotTime().addToNode(node);
        var spawnablesNode = node.putArray(JsonFieldNames.Shot.spawn);
        for (var spawnableData : shotData.getSpawnables()) {
            ObjectNode spawnableNode = spawnablesNode.addObject();
            spawnableNode = jsonDataConverter.spawnableEditionDataToJSON(spawnableData, spawnableNode);
        }
        return node;
    }
}
