package json.converters.entity;

import engine.gameData.GameDataManager;
import engine.level.entity.Entity;
import engine.level.entity.Projectile;
import engine.level.entity.extraComponent.ExtraComponent;
import engine.level.entity.hitbox.Hitbox;
import engine.level.entity.trajectory.Trajectory;
import engine.level.spawnable.Spawnable;
import engine.scene.visual.SceneVisual;
import engine.types.Vec2D;
import json.GameLoader;
import json.SafeJsonNode;

import java.util.ArrayList;
import java.util.List;

public class ProjectileFactory implements EntityFactory {

    @Override
    public Entity fromJson(SafeJsonNode node, GameLoader gameLoader, GameDataManager gameData) {
        int id = node.checkAndGetInt("id");
        boolean evil = node.checkAndGetBoolean("evil");
        Vec2D size = node.checkAndGetVec2D("size");
        SafeJsonNode hitboxNode = node.checkAndGetObject("hitbox");
        Hitbox hitbox = gameLoader.hitboxFromJson(hitboxNode, gameData.paths);
        SafeJsonNode deathSpawnNode = node.checkAndGetArray("deathSpawn");
        List<SafeJsonNode> spawnableNodes = deathSpawnNode.checkAndGetObjectListFromArray();
        List<Spawnable> deathSpawn = spawnableNodes.stream().map(gameLoader::spawnableFromJson).toList();
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
            extraComponents.add(gameLoader.extraComponentFromJson(shotNode, gameData, id == 0));
        }
        return new Projectile(0f, 0f, size.x, size.y, 0.0f, evil, id, sprite, trajectory, hitbox, deathSpawn, extraComponents);
    }
}
