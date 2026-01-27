package json.factories;

import engine.gameData.GameDataManager;
import engine.level.entity.extraComponent.ExtraComponent;
import engine.level.entity.extraComponent.NonPlayerShot;
import engine.level.entity.extraComponent.PlayerShot;
import engine.level.spawnable.Spawnable;
import json.GameLoader;
import json.JsonFieldNames;
import json.SafeJsonNode;
import json.TetraFunction;

import java.util.List;

public class ExtraComponentFactories {

    final public static TetraFunction<SafeJsonNode, GameDataManager, GameLoader, Boolean, ExtraComponent> shotFactory = (node, gameDataManager, gameFactory, isPlayer) -> {
        float shotPeriod = node.safeGetFloat(JsonFieldNames.Shot.shotPeriod);
        float firstShotTime = node.safeGetFloat(JsonFieldNames.Shot.firstShotTime);
        SafeJsonNode spawnsNode = node.safeGetArray(JsonFieldNames.Shot.spawn);
        List<SafeJsonNode> spawnableNodes = spawnsNode.safeGetObjectListFromArray();
        List<Spawnable> shotList = spawnableNodes.stream().map(gameFactory::spawnableFromJson).toList();
        if (isPlayer) {
            return new PlayerShot(shotList, shotPeriod, firstShotTime);
        }
        else {
            return new NonPlayerShot(shotList, shotPeriod, firstShotTime);
        }
    };
}
