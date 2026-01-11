package json.factories;

import engine.entity.extraComponent.ExtraComponent;
import engine.entity.hitbox.Hitbox;
import engine.entity.trajectory.Trajectory;
import engine.gameData.GameDataManager;
import engine.scene.spawnable.Spawnable;
import engine.visual.SceneVisual;
import json.SafeJsonNode;
import json.factories.extraComponent.ExtraComponentFactory;
import json.factories.extraComponent.ShotFactory;
import json.factories.hitbox.CompositeHitboxFactory;
import json.factories.hitbox.HitboxFactory;
import json.factories.hitbox.SimpleRectangleHitboxFactory;
import json.factories.spawnable.EntitySpawnInfoFactory;
import json.factories.spawnable.SpawnableFactory;
import json.factories.spawnable.VisualSpawnInfoFactory;
import json.factories.trajectory.FixedTrajectoryFactory;
import json.factories.trajectory.PlayerTrajectoryFactory;
import json.factories.trajectory.TrajectoryFactory;
import json.factories.visual.AnimationFactory;
import json.factories.visual.ScrollingImageFactory;
import json.factories.visual.VisualFactory;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class GameFactory {

    final private Map<String, VisualFactory> visualFactories;

    final private Map<String, TrajectoryFactory> trajectoryFactories;

    final private Map<String, HitboxFactory> hitboxFactories;

    final private Map<String, SpawnableFactory> spawnableFactories;

    final private Map<String, ExtraComponentFactory> extraComponentFactories;

    public GameFactory() {
        this.visualFactories = new HashMap<>(2);
        visualFactories.put("scrollingImage", new ScrollingImageFactory());
        visualFactories.put("animation", new AnimationFactory());

        this.trajectoryFactories = new HashMap<>(2);
        trajectoryFactories.put("fixed", new FixedTrajectoryFactory());
        trajectoryFactories.put("player", new PlayerTrajectoryFactory());

        this.hitboxFactories = new HashMap<>(2);
        hitboxFactories.put("simpleRectangle", new SimpleRectangleHitboxFactory());
        hitboxFactories.put("composite", new CompositeHitboxFactory());

        this.spawnableFactories = new HashMap<>(2);
        spawnableFactories.put("visual", new VisualSpawnInfoFactory());
        spawnableFactories.put("entity", new EntitySpawnInfoFactory());

        this.extraComponentFactories = new HashMap<>(1);
        extraComponentFactories.put("shot", new ShotFactory());
    }

    public SceneVisual visualFromJson(SafeJsonNode node, Path textureFilepath) {
        String type = node.checkAndGetString("type");
        VisualFactory factory = visualFactories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": visual type is not supported");
        }
        return factory.fromJson(node, textureFilepath);
    }

    public Trajectory trajectoryFromJSon(SafeJsonNode node) {
        String type = node.checkAndGetString("type");
        TrajectoryFactory factory = trajectoryFactories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": trajectory type is not supported");
        }
        return factory.fromJson(node);
    }

    public Hitbox hitboxFromJson(SafeJsonNode node) {
        String type = node.checkAndGetString("type");
        HitboxFactory factory = hitboxFactories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": hitbox type is not supported");
        }
        return factory.fromJson(node);
    }

    public Spawnable spawnableFromJson(SafeJsonNode node) {
        String type = node.checkAndGetString("type");
        SpawnableFactory factory = spawnableFactories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": spawnable type is not supported");
        }
        return factory.fromJson(node);
    }

    public ExtraComponent extraComponentFromJson(SafeJsonNode node, GameDataManager gameData, boolean isPlayer) {
        String type = "shot";
        ExtraComponentFactory factory = extraComponentFactories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": extra component type is not supported");
        }
        return factory.fromJson(node, gameData, this, isPlayer);
    }
}
