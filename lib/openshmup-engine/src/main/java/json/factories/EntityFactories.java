package json.factories;

import engine.gameData.GameDataManager;
import engine.level.entity.Entity;
import engine.level.entity.Projectile;
import engine.level.entity.Ship;
import engine.level.entity.extraComponent.ExtraComponent;
import engine.level.entity.hitbox.Hitbox;
import engine.level.entity.trajectory.Trajectory;
import engine.level.spawnable.Spawnable;
import engine.scene.visual.SceneVisual;
import engine.types.Vec2D;
import json.GameLoader;
import json.SafeJsonNode;
import json.TriFunction;

import java.util.ArrayList;
import java.util.List;

public class EntityFactories {

    final public static TriFunction<SafeJsonNode, GameLoader, GameDataManager, Entity> shipFactory = (node, gameFactory, gameData) -> {
        int id = node.checkAndGetInt("id");
        boolean evil = node.checkAndGetBoolean("evil");
        Vec2D size = node.checkAndGetVec2D("size");
        SafeJsonNode hitboxNode = node.checkAndGetObject("hitbox");
        Hitbox hitbox = gameFactory.hitboxFromJson(hitboxNode, gameData.paths);
        SafeJsonNode deathSpawnNode = node.checkAndGetArray("deathSpawn");
        List<SafeJsonNode> spawnableNodes = deathSpawnNode.checkAndGetObjectListFromArray();
        List<Spawnable> deathSpawn = spawnableNodes.stream().map(gameFactory::spawnableFromJson).toList();
        int spriteVisualId = node.checkAndGetInt("spriteVisualId");
        SceneVisual sprite = gameData.getGameVisual(spriteVisualId);
        Trajectory trajectory;
        if (node.hasField("defaultTrajectoryId")) {
            int defaultTrajectoryId = node.checkAndGetInt("defaultTrajectoryId");
            trajectory = gameData.getTrajectory(defaultTrajectoryId);
        }
        else {
            trajectory = Trajectory.DEFAULT_EMPTY();
        }
        ArrayList<ExtraComponent> extraComponents = new ArrayList<>();
        if (node.hasField("shot")) {
            SafeJsonNode shotNode = node.checkAndGetObject("shot");
            extraComponents.add(gameFactory.extraComponentFromJson(shotNode, gameData, id == 0));
        }
        int hp = node.checkAndGetInt("hp");
        return new Ship(0f, 0f, size.x, size.y, 0.0f, evil, id, sprite, trajectory, hitbox, deathSpawn, extraComponents, hp);
    };

    final public static TriFunction<SafeJsonNode, GameLoader, GameDataManager, Entity> projectileFactory = (node, gameFactory, gameData) -> {
        int id = node.checkAndGetInt("id");
        boolean evil = node.checkAndGetBoolean("evil");
        Vec2D size = node.checkAndGetVec2D("size");
        SafeJsonNode hitboxNode = node.checkAndGetObject("hitbox");
        Hitbox hitbox = gameFactory.hitboxFromJson(hitboxNode, gameData.paths);
        SafeJsonNode deathSpawnNode = node.checkAndGetArray("deathSpawn");
        List<SafeJsonNode> spawnableNodes = deathSpawnNode.checkAndGetObjectListFromArray();
        List<Spawnable> deathSpawn = spawnableNodes.stream().map(gameFactory::spawnableFromJson).toList();
        int spriteVisualId = node.checkAndGetInt("spriteVisualId");
        SceneVisual sprite = gameData.getGameVisual(spriteVisualId);
        Trajectory trajectory;
        if (node.hasField("defaultTrajectoryId")) {
            int defaultTrajectoryId = node.checkAndGetInt("defaultTrajectoryId");
            trajectory = gameData.getTrajectory(defaultTrajectoryId);
        }
        else {
            trajectory = Trajectory.DEFAULT_EMPTY();
        }
        ArrayList<ExtraComponent> extraComponents = new ArrayList<>();
        if (node.hasField("shot")) {
            SafeJsonNode shotNode = node.checkAndGetObject("shot");
            extraComponents.add(gameFactory.extraComponentFromJson(shotNode, gameData, id == 0));
        }
        return new Projectile(0f, 0f, size.x, size.y, 0.0f, evil, id, sprite, trajectory, hitbox, deathSpawn, extraComponents);
    };
}
