package json.converters.extraComponent;

import engine.entity.extraComponent.ExtraComponent;
import engine.entity.extraComponent.NonPlayerShot;
import engine.entity.extraComponent.PlayerShot;
import engine.gameData.GameDataManager;
import engine.scene.spawnable.Spawnable;
import json.SafeJsonNode;
import json.factories.GameFactory;

import java.util.List;

public class ShotFactory implements ExtraComponentFactory {

    @Override
    public ExtraComponent fromJson(SafeJsonNode node, GameDataManager gameData, GameFactory gameFactory, boolean isPlayer) {
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
    }
}
