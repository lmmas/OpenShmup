package json.factories;

import engine.gameData.GameDataManager;
import engine.hitbox.Hitbox;
import engine.level.entity.Entity;
import engine.level.entity.Projectile;
import engine.level.entity.Ship;
import engine.level.entity.extraComponent.ExtraComponent;
import engine.level.entity.trajectory.Trajectory;
import engine.level.spawnable.Spawnable;
import engine.scene.visual.SceneVisual;
import engine.types.Vec2D;
import json.GameLoader;
import json.JsonFieldNames;
import json.SafeJsonNode;
import json.TriFunction;

import java.util.ArrayList;
import java.util.List;

public class EntityFactories {

    final public static TriFunction<SafeJsonNode, GameLoader, GameDataManager, Entity> shipFactory = (node, gameFactory, gameData) -> {
        int id = node.safeGetInt(JsonFieldNames.Ship.id);
        boolean evil = node.safeGetBoolean(JsonFieldNames.Ship.evil);
        Vec2D size = node.safeGetVec2D(JsonFieldNames.Ship.size);
        SafeJsonNode hitboxNode = node.safeGetObject(JsonFieldNames.Ship.hitbox);
        Hitbox hitbox = gameFactory.hitboxFromJson(hitboxNode, gameData.paths);
        SafeJsonNode deathSpawnNode = node.safeGetArray(JsonFieldNames.Ship.deathSpawn);
        List<SafeJsonNode> spawnableNodes = deathSpawnNode.safeGetObjectListFromArray();
        List<Spawnable> deathSpawn = spawnableNodes.stream().map(gameFactory::spawnableFromJson).toList();
        int spriteVisualId = node.safeGetInt(JsonFieldNames.Ship.spriteVisualId);
        SceneVisual sprite = gameData.getGameVisual(spriteVisualId);
        Trajectory trajectory;
        if (node.hasField(JsonFieldNames.Ship.defaultTrajectoryId)) {
            int defaultTrajectoryId = node.safeGetInt(JsonFieldNames.Ship.defaultTrajectoryId);
            trajectory = gameData.getTrajectory(defaultTrajectoryId);
        }
        else {
            trajectory = Trajectory.DEFAULT_EMPTY();
        }
        ArrayList<ExtraComponent> extraComponents = new ArrayList<>();
        if (node.hasField(JsonFieldNames.Ship.shot)) {
            SafeJsonNode shotNode = node.safeGetObject(JsonFieldNames.Ship.shot);
            extraComponents.add(gameFactory.extraComponentFromJson(shotNode, gameData, id == 0));
        }
        int hp = node.safeGetInt(JsonFieldNames.Ship.hp);
        return new Ship(0f, 0f, size.x, size.y, 0.0f, evil, id, sprite, trajectory, hitbox, deathSpawn, extraComponents, hp);
    };

    final public static TriFunction<SafeJsonNode, GameLoader, GameDataManager, Entity> projectileFactory = (node, gameFactory, gameData) -> {
        int id = node.safeGetInt(JsonFieldNames.Projectile.id);
        boolean evil = node.safeGetBoolean(JsonFieldNames.Projectile.evil);
        Vec2D size = node.safeGetVec2D(JsonFieldNames.Projectile.size);
        SafeJsonNode hitboxNode = node.safeGetObject(JsonFieldNames.Projectile.hitbox);
        Hitbox hitbox = gameFactory.hitboxFromJson(hitboxNode, gameData.paths);
        SafeJsonNode deathSpawnNode = node.safeGetArray(JsonFieldNames.Projectile.deathSpawn);
        List<SafeJsonNode> spawnableNodes = deathSpawnNode.safeGetObjectListFromArray();
        List<Spawnable> deathSpawn = spawnableNodes.stream().map(gameFactory::spawnableFromJson).toList();
        int spriteVisualId = node.safeGetInt(JsonFieldNames.Projectile.spriteVisualId);
        SceneVisual sprite = gameData.getGameVisual(spriteVisualId);
        Trajectory trajectory;
        if (node.hasField(JsonFieldNames.Projectile.defaultTrajectoryId)) {
            int defaultTrajectoryId = node.safeGetInt(JsonFieldNames.Projectile.defaultTrajectoryId);
            trajectory = gameData.getTrajectory(defaultTrajectoryId);
        }
        else {
            trajectory = Trajectory.DEFAULT_EMPTY();
        }
        ArrayList<ExtraComponent> extraComponents = new ArrayList<>();
        if (node.hasField(JsonFieldNames.Projectile.shot)) {
            SafeJsonNode shotNode = node.safeGetObject(JsonFieldNames.Projectile.shot);
            extraComponents.add(gameFactory.extraComponentFromJson(shotNode, gameData, id == 0));
        }
        return new Projectile(0f, 0f, size.x, size.y, 0.0f, evil, id, sprite, trajectory, hitbox, deathSpawn, extraComponents);
    };
}
