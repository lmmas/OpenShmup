package json;

import engine.assets.Texture;
import engine.gameData.GameDataManager;
import engine.hitbox.CompositeHitbox;
import engine.hitbox.Hitbox;
import engine.hitbox.SimpleRectangleHitbox;
import engine.level.entity.Entity;
import engine.level.entity.Projectile;
import engine.level.entity.Ship;
import engine.level.entity.extraComponent.ExtraComponent;
import engine.level.entity.extraComponent.NonPlayerShot;
import engine.level.entity.extraComponent.PlayerShot;
import engine.level.entity.trajectory.FixedTrajectory;
import engine.level.entity.trajectory.PlayerControlledTrajectory;
import engine.level.entity.trajectory.Trajectory;
import engine.level.spawnable.DisplaySpawnInfo;
import engine.level.spawnable.EntitySpawnInfo;
import engine.level.spawnable.Spawnable;
import engine.scene.visual.Animation;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.ScrollingImage;
import engine.scene.visual.SpritesheetInfo;
import engine.scene.visual.style.TimeReference;
import engine.types.IVec2D;
import engine.types.Vec2D;
import json.editionData.*;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static engine.Engine.assetManager;
import static json.editionData.EditionData.*;

final public class GameDataLoader {

    final private Map<Types.Visual, BiFunction<EditionData, Path, SceneVisual>> visualFactories;

    final private Map<Types.Trajectory, Function<EditionData, Trajectory>> trajectoryFactories;

    final private Map<Types.Hitbox, BiFunction<EditionData, Path, Hitbox>> hitboxFactories;

    final private Map<Types.Spawn, Function<EditionData, Spawnable>> spawnFactories;

    final private Map<Types.Entity, BiFunction<EditionData, GameDataManager, Entity>> entityFactoryMap;

    public GameDataLoader() {

        this.visualFactories = new HashMap<>(2);
        visualFactories.put(Types.Visual.scrollingImage, VisualFactories::scrollingImageFactory);
        visualFactories.put(Types.Visual.animation, VisualFactories::animationFactory);

        this.trajectoryFactories = new HashMap<>(2);
        trajectoryFactories.put(Types.Trajectory.fixed, TrajectoryFactories::fixedTrajectoryFactory);
        trajectoryFactories.put(Types.Trajectory.player, TrajectoryFactories::playerControlledTrajectoryFactory);

        this.hitboxFactories = new HashMap<>(2);
        hitboxFactories.put(Types.Hitbox.rectangle, HitboxFactories::rectangleHitboxFactory);
        hitboxFactories.put(Types.Hitbox.custom, HitboxFactories::customHitboxFactory);

        this.spawnFactories = new HashMap<>(2);
        spawnFactories.put(Types.Spawn.display, SpawnFactories::displaySpawnFactory);
        spawnFactories.put(Types.Spawn.entity, SpawnFactories::entitySpawnFactory);

        this.entityFactoryMap = new HashMap<>(2);
        EntityFactories entityFactories = this.new EntityFactories();
        entityFactoryMap.put(Types.Entity.ship, entityFactories.shipFactory);
        entityFactoryMap.put(Types.Entity.projectile, entityFactories.projectileFactory);
    }

    public GameDataManager convertToGameObjects(GameEditionDataManager gameEditionData) {
        GameDataManager gameData = new GameDataManager(gameEditionData.getGameName());

        for (EditionData visualEditionData : gameEditionData.getVisualEditionDataList()) {
            var visualFactory = visualFactories.get((Types.Visual) visualEditionData.getType());
            assert visualFactory != null : "visual factory not found: " + visualEditionData.getType().name();
            SceneVisual visual = visualFactory.apply(visualEditionData, gameEditionData.paths.gameVisualsFile);
            gameData.addVisual(EditionData.getVisualId(visualEditionData), visual);
        }

        for (EditionData trajectoryEditionData : gameEditionData.getTrajectoryEditionDataList()) {
            var trajectoryFactory = trajectoryFactories.get((Types.Trajectory) trajectoryEditionData.getType());
            assert trajectoryFactory != null : "trajectory factory not found: " + trajectoryEditionData.getType().name();
            Trajectory trajectory = trajectoryFactory.apply(trajectoryEditionData);
            gameData.addTrajectory(EditionData.getTrajectoryId(trajectoryEditionData), trajectory);
        }

        for (EditionData entityEditionData : gameEditionData.getEntityEditionDataList()) {
            var entityFactory = entityFactoryMap.get((Types.Entity) entityEditionData.getType());
            assert entityFactory != null : "entity factory not found: " + entityEditionData.getType().name();
            Entity entity = entityFactory.apply(entityEditionData, gameData);
            gameData.addCustomEntity(EditionData.getEntityId(entityEditionData), entity);
        }

        return gameData;
    }

    private Hitbox convertToHitbox(HitboxEditionData hitboxEditionData, Path textureFolder) {
        var hitboxFactoy = hitboxFactories.get((Types.Hitbox) hitboxEditionData.getType());
        assert hitboxFactoy != null : "hitbox factory not found: " + hitboxEditionData.getType().name();
        return hitboxFactoy.apply(hitboxEditionData, textureFolder);
    }

    private Spawnable convertToSpawnable(EditionData spawnEditionData) {
        checkForCategory(spawnEditionData, EditionData.Category.SPAWN);
        var spawnableFactory = spawnFactories.get((Types.Spawn) spawnEditionData.getType());
        assert spawnableFactory != null : "spawn factory not found: " + spawnEditionData.getType().name();
        return spawnableFactory.apply(spawnEditionData);
    }

    private ExtraComponent convertShot(EditionData data, boolean isPlayer) {
        checkForType(data, Category.NONE, Types.shot);
        ShotEditionData shotEditionData = (ShotEditionData) data;
        float shotPeriod = shotEditionData.getShotPeriod().getValue();
        float firstShotTime = shotEditionData.getFirstShotTime().getValue();
        List<EditionData> spawnDataList = shotEditionData.getSpawnables().getDataList();
        List<Spawnable> shotList = spawnDataList.stream().map(this::convertToSpawnable).toList();
        if (isPlayer) {
            return new PlayerShot(shotList, shotPeriod, firstShotTime);
        }
        else {
            return new NonPlayerShot(shotList, shotPeriod, firstShotTime);
        }
    }

    static final private class VisualFactories {

        private VisualFactories() {}

        public static SceneVisual scrollingImageFactory(EditionData data, Path textureFolderPath) {
            ScrollingImageEditionData scrollingImageData = (ScrollingImageEditionData) data;
            int layer = scrollingImageData.getLayer().getValue();
            Vec2D size = scrollingImageData.getSize().getValue();
            String textureName = scrollingImageData.getFileName().getValue();
            Texture texture = assetManager.getTexture(textureFolderPath.resolve(textureName));
            boolean horizontalScrolling = scrollingImageData.getHorizontalScrolling().getValue();
            float speed = scrollingImageData.getSpeed().getValue();

            return new ScrollingImage(texture, layer, size, speed, horizontalScrolling, TimeReference.LEVEL);
        }

        public static SceneVisual animationFactory(EditionData data, Path textureFolderPath) {
            AnimationEditionData animationData = (AnimationEditionData) data;
            int layer = animationData.getLayer().getValue();
            Vec2D size = animationData.getSize().getValue();

            AnimationEditionData.SpritesheetInfoData spritesheetInfoData = (AnimationEditionData.SpritesheetInfoData) animationData.getSpritesheetInfo().getData();

            String fileName = spritesheetInfoData.getFileName().getValue();
            Path textureFilepath = textureFolderPath.resolve(fileName);
            int frameCount = spritesheetInfoData.getFrameCount().getValue();
            IVec2D frameSize = spritesheetInfoData.getFrameSize().getValue();
            IVec2D startingPosition = spritesheetInfoData.getStartPosition().getValue();
            IVec2D stride = spritesheetInfoData.getStride().getValue();

            double framePeriodSeconds = animationData.getFramePeriodSeconds().getValue();
            boolean looping = animationData.getLooping().getValue();

            Texture texture = assetManager.getTexture(textureFilepath);
            IVec2D animationResolution = texture.getResolution();

            SpritesheetInfo spritesheetInfo = new SpritesheetInfo(textureFilepath, frameCount,
                new Vec2D((float) frameSize.x / animationResolution.x, (float) frameSize.y / animationResolution.y),
                new Vec2D((float) startingPosition.x / animationResolution.x, (float) startingPosition.y / animationResolution.y),
                new Vec2D((float) stride.x / animationResolution.x, (float) stride.y / animationResolution.y));

            return new Animation(layer, texture, spritesheetInfo, framePeriodSeconds, looping, size, TimeReference.LEVEL);
        }
    }

    static final private class TrajectoryFactories {

        private TrajectoryFactories() {}

        public static Trajectory playerControlledTrajectoryFactory(EditionData data) {
            PlayerControlledTrajectoryEditionData playerControlledTrajectoryData = (PlayerControlledTrajectoryEditionData) data;
            float playerMovementSpeed = playerControlledTrajectoryData.getPlayerMovementSpeed().getValue();
            return new PlayerControlledTrajectory(playerMovementSpeed);
        }

        public static Trajectory fixedTrajectoryFactory(EditionData data) {
            FixedTrajectoryEditionData fixedTrajectoryData = (FixedTrajectoryEditionData) data;
            String functionXString = fixedTrajectoryData.getTrajectoryFunctionX().getValue();
            String functionYString = fixedTrajectoryData.getTrajectoryFunctionY().getValue();
            Function<Double, Float> trajectoryFunctionX = convertToFunction(functionXString);
            Function<Double, Float> trajectoryFunctionY = convertToFunction(functionYString);
            boolean relative = true;
            return new FixedTrajectory(trajectoryFunctionX, trajectoryFunctionY, relative);
        }

        private static Function<Double, Float> convertToFunction(String expressionString) {
            return t -> {
                Expression expr = new ExpressionBuilder(expressionString)
                    .variable("t")
                    .build()
                    .setVariable("t", t);
                return (float) expr.evaluate();
            };
        }
    }

    static final private class HitboxFactories {

        private HitboxFactories() {}

        public static Hitbox rectangleHitboxFactory(EditionData data, Path path) {
            RectangleHitboxEditionData rectangleHitboxData = (RectangleHitboxEditionData) data;
            Vec2D size = rectangleHitboxData.getSize().getValue();
            return new SimpleRectangleHitbox(Vec2D.ZERO, size);
        }

        public static Hitbox customHitboxFactory(EditionData data, Path path) {
            CustomHitboxEditionData customHitboxData = (CustomHitboxEditionData) data;
            String textureName = customHitboxData.getFileName().getValue();
            Texture texture = assetManager.getTexture(path.resolve(textureName));
            Vec2D size = customHitboxData.getSize().getValue();
            return new CompositeHitbox(texture, size);
        }
    }

    static final private class SpawnFactories {

        private SpawnFactories() {}

        public static Spawnable displaySpawnFactory(EditionData data) {
            DisplaySpawnEditionData displaySpawnData = (DisplaySpawnEditionData) data;
            int id = displaySpawnData.getVisualID().getValue();
            Vec2D positionVec = displaySpawnData.getPosition().getValue();
            return new DisplaySpawnInfo(id, positionVec);
        }

        public static Spawnable entitySpawnFactory(EditionData data) {
            EntitySpawnEditionData entitySpawnData = (EntitySpawnEditionData) data;
            int id = entitySpawnData.getEntityID().getValue();
            Vec2D startingPosition = entitySpawnData.getPosition().getValue();
            int trajectoryId = entitySpawnData.getTrajectoryID().getValue();
            return new EntitySpawnInfo(id, startingPosition, trajectoryId);
        }
    }

    final public class EntityFactories {

        final public BiFunction<EditionData, GameDataManager, Entity> shipFactory = (data, gameData) -> {
            ShipEditionData shipData = (ShipEditionData) data;

            int id = shipData.getId();
            boolean evil = shipData.getEvil().getValue();
            Vec2D size = shipData.getSize().getValue();
            HitboxEditionData hitboxData = (HitboxEditionData) shipData.getHitbox().getData();
            Hitbox hitbox = convertToHitbox(hitboxData, gameData.paths.gameTextureFolder);
            List<EditionData> deathSpawnList = shipData.getDeathspawn().getDataList();
            List<Spawnable> deathSpawn = deathSpawnList.stream().map(GameDataLoader.this::convertToSpawnable).toList();
            int spriteVisualId = shipData.getSpriteId().getValue();
            SceneVisual sprite = gameData.getGameVisual(spriteVisualId);
            Trajectory trajectory;
            int defaultTrajectoryId = shipData.getTrajectoryId().getValue();
            trajectory = gameData.getTrajectory(defaultTrajectoryId);

            ArrayList<ExtraComponent> extraComponents = new ArrayList<>();
            List<EditionData> shotDataList = shipData.getShots().getDataList();
            List<ExtraComponent> shotList = shotDataList.stream().map(shotData -> convertShot(shotData, (id == 0))).toList();
            extraComponents.addAll(shotList);
            int hp = shipData.getHp().getValue();
            return new Ship(Vec2D.ZERO, size, 0.0f, evil, id, sprite, trajectory, hitbox, deathSpawn, extraComponents, hp);
        };

        final public BiFunction<EditionData, GameDataManager, Entity> projectileFactory = (data, gameData) -> {
            ProjectileEditionData shipData = (ProjectileEditionData) data;

            int id = shipData.getId();
            boolean evil = shipData.getEvil().getValue();
            Vec2D size = shipData.getSize().getValue();
            HitboxEditionData hitboxData = (HitboxEditionData) shipData.getHitbox().getData();
            Hitbox hitbox = convertToHitbox(hitboxData, gameData.paths.gameTextureFolder);
            List<EditionData> deathSpawnList = shipData.getDeathspawn().getDataList();
            List<Spawnable> deathSpawn = deathSpawnList.stream().map(GameDataLoader.this::convertToSpawnable).toList();
            int spriteVisualId = shipData.getSpriteId().getValue();
            SceneVisual sprite = gameData.getGameVisual(spriteVisualId);
            Trajectory trajectory;
            int defaultTrajectoryId = shipData.getTrajectoryId().getValue();
            trajectory = gameData.getTrajectory(defaultTrajectoryId);

            ArrayList<ExtraComponent> extraComponents = new ArrayList<>();
            List<EditionData> shotDataList = shipData.getShots().getDataList();
            List<ExtraComponent> shotList = shotDataList.stream().map(shotData -> convertShot(shotData, (id == 0))).toList();
            extraComponents.addAll(shotList);
            return new Projectile(Vec2D.ZERO, size, 0.0f, evil, id, sprite, trajectory, hitbox, deathSpawn, extraComponents);
        };
    }
}
