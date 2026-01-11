package json.factories;

import engine.entity.extraComponent.ExtraComponent;
import engine.entity.extraComponent.NonPlayerShot;
import engine.entity.extraComponent.PlayerShot;
import engine.gameData.GameDataManager;
import engine.scene.spawnable.Spawnable;
import json.SafeJsonNode;
import json.TetraFunction;

import java.util.List;

public class ExtraComponentFactories {

    final public static TetraFunction<SafeJsonNode, GameDataManager, GameFactory, Boolean, ExtraComponent> shotFactory = (node, gameDataManager, gameFactory, isPlayer) -> {
        float shotPeriod = node.checkAndGetFloat("shotPeriod");
        float firstShotTime = node.checkAndGetFloat("firstShotTime");
        SafeJsonNode spawnsNode = node.checkAndGetArray("spawn");
        List<SafeJsonNode> spawnableNodes = spawnsNode.checkAndGetObjectListFromArray();
        List<Spawnable> shotList = spawnableNodes.stream().map(gameFactory::spawnableFromJson).toList();
        if (isPlayer) {
            return new PlayerShot(shotList, shotPeriod, firstShotTime);
        }
        else {
            return new NonPlayerShot(shotList, shotPeriod, firstShotTime);
        }
    };
}
