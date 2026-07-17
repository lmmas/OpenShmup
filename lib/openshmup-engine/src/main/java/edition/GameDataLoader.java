package edition;

import edition.attribute.*;
import engine.assets.Texture;
import engine.gameData.GameConfig;
import engine.gameData.GameDataManager;
import engine.hitbox.CompositeHitbox;
import engine.hitbox.Hitbox;
import engine.hitbox.SimpleRectangleHitbox;
import engine.level.LevelTimeline;
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
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static edition.EditionData.*;
import static engine.Engine.assetManager;

final public class GameDataLoader {

    @FunctionalInterface
    private interface VisualFactory {

        SceneVisual build(EditionData data, Path textureFolderPath);
    }

    final private Map<Types.Visual, VisualFactory> visualFactories;

    @FunctionalInterface
    private interface TrajectoryFactory {

        Trajectory build(EditionData data);
    }

    final private Map<Types.Trajectory, TrajectoryFactory> trajectoryFactories;

    @FunctionalInterface
    private interface HitboxFactory {

        Hitbox build(EditionData data, Path textureFolderPath);
    }

    final private Map<Types.Hitbox, HitboxFactory> hitboxFactories;

    @FunctionalInterface
    private interface SpawnableFactory {

        Spawnable build(EditionData data);
    }

    final private Map<Types.Spawn, SpawnableFactory> spawnFactories;

    @FunctionalInterface
    private interface EntityFactory {

        Entity build(EditionData data, GameDataManager gameDataManager);
    }

    final private Map<Types.Entity, EntityFactory> entityFactoryMap;

    @FunctionalInterface
    private interface TimelineBuilder {

        void buildTimeline(LevelTimeline timeline, EditionData data);
    }

    final private Map<Types.SpawnInfo, TimelineBuilder> timelineBuilderMap;

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
        entityFactoryMap.put(Types.Entity.ship, entityFactories::shipFactory);
        entityFactoryMap.put(Types.Entity.projectile, entityFactories::projectileFactory);

        TimelineBuilders timelineBuilders = this.new TimelineBuilders();
        this.timelineBuilderMap = new HashMap<>(2);
        timelineBuilderMap.put(Types.SpawnInfo.single, timelineBuilders::singleSpawnInfoBuilder);
        timelineBuilderMap.put(Types.SpawnInfo.repeat, timelineBuilders::repeatSpawnInfoBuilder);

    }

    public GameDataManager convertToGameObjects(GameEditionData gameEditionData) {
        GameDataManager gameData = new GameDataManager(gameEditionData.paths.gameFolder, "");

        GameConfig config = gameData.config;

        EditionData generalConfigData = gameEditionData.configs.get(Types.Config.general);
        IVec2D nativeResolution = ((IVec2DAttribute) generalConfigData.get(Keys.Config.General.resolution)).getValue();
        config.setNativeResolution(nativeResolution);

        EditionData levelUIConfigData = gameEditionData.configs.get(Types.Config.levelUI);
        EditionData livesDisplayConfigData = ((EditionDataAttribute) levelUIConfigData.get(Keys.Config.LevelUI.lives)).getData();
        String livesDisplayFileName = ((StringAttribute) livesDisplayConfigData.get(Keys.Config.LevelUI.Lives.fileName)).getValue();
        config.levelUI.lives.textureFilepath = gameData.paths.gameTextureFolder.resolve(livesDisplayFileName);
        config.levelUI.lives.size = ((Vec2DAttribute) livesDisplayConfigData.get(Keys.Config.LevelUI.Lives.size)).getValue();
        config.levelUI.lives.position = ((Vec2DAttribute) livesDisplayConfigData.get(Keys.Config.LevelUI.Lives.position)).getValue();
        config.levelUI.lives.stride = ((Vec2DAttribute) livesDisplayConfigData.get(Keys.Config.LevelUI.Lives.stride)).getValue();

        for (EditionData visualEditionData : gameEditionData.getVisualEditionDataList()) {
            visualEditionData.checkForCategory(Category.VISUAL);
            var visualFactory = visualFactories.get((Types.Visual) visualEditionData.getType());
            assert visualFactory != null : "visual factory not found: " + visualEditionData.getType().name();
            SceneVisual visual = visualFactory.build(visualEditionData, gameEditionData.paths.gameTextureFolder);
            gameData.addVisual(EditionData.getVisualId(visualEditionData), visual);
        }

        for (EditionData trajectoryEditionData : gameEditionData.getTrajectoryEditionDataList()) {
            trajectoryEditionData.checkForCategory(Category.TRAJECTORY);
            var trajectoryFactory = trajectoryFactories.get((Types.Trajectory) trajectoryEditionData.getType());
            assert trajectoryFactory != null : "trajectory factory not found: " + trajectoryEditionData.getType().name();
            Trajectory trajectory = trajectoryFactory.build(trajectoryEditionData);
            gameData.addTrajectory(EditionData.getTrajectoryId(trajectoryEditionData), trajectory);
        }

        for (EditionData entityEditionData : gameEditionData.getEntityEditionDataList()) {
            entityEditionData.checkForCategory(Category.ENTITY);
            var entityFactory = entityFactoryMap.get((Types.Entity) entityEditionData.getType());
            assert entityFactory != null : "entity factory not found: " + entityEditionData.getType().name();
            Entity entity = entityFactory.build(entityEditionData, gameData);
            gameData.addCustomEntity(EditionData.getEntityId(entityEditionData), entity);
        }

        LevelTimeline timeline = new LevelTimeline(gameData, 3000);
        for (EditionData spawnInfoEditionData : gameEditionData.getTimelineDataList()) {
            spawnInfoEditionData.checkForCategory(Category.SPAWN_INFO);
            var timelineBuilder = timelineBuilderMap.get((Types.SpawnInfo) spawnInfoEditionData.getType());
            assert timelineBuilder != null : "timeline builder not found: " + spawnInfoEditionData.getType().name();
            timelineBuilder.buildTimeline(timeline, spawnInfoEditionData);
        }
        gameData.addTimeline(timeline);

        return gameData;
    }

    private Hitbox convertToHitbox(EditionData hitboxEditionData, Path textureFolder) {
        hitboxEditionData.checkForCategory(Category.HITBOX);
        var hitboxFactoy = hitboxFactories.get((Types.Hitbox) hitboxEditionData.getType());
        assert hitboxFactoy != null : "hitbox factory not found: " + hitboxEditionData.getType().name();
        return hitboxFactoy.build(hitboxEditionData, textureFolder);
    }

    private Spawnable convertToSpawnable(EditionData spawnEditionData) {
        spawnEditionData.checkForCategory(EditionData.Category.SPAWN);
        var spawnableFactory = spawnFactories.get((Types.Spawn) spawnEditionData.getType());
        assert spawnableFactory != null : "spawn factory not found: " + spawnEditionData.getType().name();
        return spawnableFactory.build(spawnEditionData);
    }

    private ExtraComponent convertShot(EditionData data, boolean isPlayer) {
        data.checkForType(Category.NONE, Types.shot);
        double shotPeriod = ((DoubleAttribute) data.get(Keys.Shot.shotPeriod)).getValue();
        double firstShotTime = ((DoubleAttribute) data.get(Keys.Shot.firstShotTime)).getValue();
        List<EditionData> spawnDataList = ((ListAttribute) data.get(Keys.Shot.spawn)).getDataList();
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
            data.checkForType(Types.Visual.scrollingImage);
            int layer = ((IntegerAttribute) data.get(Keys.Visual.ScrollingImage.layer)).getValue();
            Vec2D size = ((Vec2DAttribute) data.get(Keys.Visual.ScrollingImage.size)).getValue();
            String textureName = ((StringAttribute) data.get(Keys.Visual.ScrollingImage.fileName)).getValue();
            Texture texture = assetManager.getTexture(textureFolderPath.resolve(textureName));
            boolean horizontalScrolling = ((BooleanAttribute) data.get(Keys.Visual.ScrollingImage.horizontalScrolling)).getValue();
            float speed = ((FloatAttribute) data.get(Keys.Visual.ScrollingImage.speed)).getValue();

            return new ScrollingImage(texture, layer, size, speed, horizontalScrolling, TimeReference.LEVEL);
        }

        public static SceneVisual animationFactory(EditionData data, Path textureFolderPath) {
            data.checkForType(Types.Visual.animation);
            int layer = ((IntegerAttribute) data.get(Keys.Visual.Animation.layer)).getValue();
            Vec2D size = ((Vec2DAttribute) data.get(Keys.Visual.Animation.size)).getValue();

            EditionData spritesheetInfoData = ((EditionDataAttribute) data.get(Keys.Visual.Animation.spritesheetInfo)).getData();

            String fileName = ((StringAttribute) spritesheetInfoData.get(Keys.SpritesheetInfo.fileName)).getValue();
            Path textureFilepath = textureFolderPath.resolve(fileName);
            int frameCount = ((IntegerAttribute) spritesheetInfoData.get(Keys.SpritesheetInfo.frameCount)).getValue();
            IVec2D frameSize = ((IVec2DAttribute) spritesheetInfoData.get(Keys.SpritesheetInfo.frameSize)).getValue();
            IVec2D startingPosition = ((IVec2DAttribute) spritesheetInfoData.get(Keys.SpritesheetInfo.startingPosition)).getValue();
            IVec2D stride = ((IVec2DAttribute) spritesheetInfoData.get(Keys.SpritesheetInfo.stride)).getValue();

            double framePeriodSeconds = ((DoubleAttribute) data.get(Keys.Visual.Animation.framePeriodSeconds)).getValue();
            boolean looping = ((BooleanAttribute) data.get(Keys.Visual.Animation.looping)).getValue();

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
            data.checkForType(Types.Trajectory.player);
            float playerMovementSpeed = ((FloatAttribute) data.get(Keys.Trajectory.PlayerControlledTrajectory.playerMovementSpeed)).getValue();
            return new PlayerControlledTrajectory(playerMovementSpeed);
        }

        public static Trajectory fixedTrajectoryFactory(EditionData data) {
            data.checkForType(Types.Trajectory.fixed);
            String functionXString = ((StringAttribute) data.get(Keys.Trajectory.FixedTrajectory.functionX)).getValue();
            String functionYString = ((StringAttribute) data.get(Keys.Trajectory.FixedTrajectory.functionY)).getValue();
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
            data.checkForType(Types.Hitbox.rectangle);
            Vec2D size = ((Vec2DAttribute) data.get(Keys.Hitbox.RectangleHitbox.size)).getValue();
            return new SimpleRectangleHitbox(Vec2D.ZERO, size);
        }

        public static Hitbox customHitboxFactory(EditionData data, Path path) {
            data.checkForType(Types.Hitbox.custom);
            String textureName = ((StringAttribute) data.get(Keys.Hitbox.CustomHitbox.fileName)).getValue();
            Texture texture = assetManager.getTexture(path.resolve(textureName));
            Vec2D size = ((Vec2DAttribute) data.get(Keys.Hitbox.CustomHitbox.size)).getValue();
            return new CompositeHitbox(texture, size);
        }
    }

    static final private class SpawnFactories {

        private SpawnFactories() {}

        public static Spawnable displaySpawnFactory(EditionData data) {
            data.checkForType(Types.Spawn.display);
            int id = ((IntegerAttribute) data.get(Keys.Spawn.DisplaySpawn.id)).getValue();
            Vec2D positionVec = ((Vec2DAttribute) data.get(Keys.Spawn.DisplaySpawn.position)).getValue();
            return new DisplaySpawnInfo(id, positionVec);
        }

        public static Spawnable entitySpawnFactory(EditionData data) {
            data.checkForType(Types.Spawn.entity);
            int id = ((IntegerAttribute) data.get(Keys.Spawn.EntitySpawn.id)).getValue();
            Vec2D startingPosition = ((Vec2DAttribute) data.get(Keys.Spawn.EntitySpawn.startingPosition)).getValue();
            int trajectoryId = ((IntegerAttribute) data.get(Keys.Spawn.EntitySpawn.trajectory)).getValue();
            return new EntitySpawnInfo(id, startingPosition, trajectoryId);
        }
    }

    final public class EntityFactories {

        public Entity shipFactory(EditionData data, GameDataManager gameData) {
            data.checkForType(Types.Entity.ship);
            int id = ((IntegerAttribute) data.get(Keys.Entity.Ship.id)).getValue();
            boolean evil = ((BooleanAttribute) data.get(Keys.Entity.Ship.evil)).getValue();
            Vec2D size = ((Vec2DAttribute) data.get(Keys.Entity.Ship.size)).getValue();
            EditionData hitboxData = ((EditionDataAttribute) data.get(Keys.Entity.Ship.hitbox)).getData();
            Hitbox hitbox = convertToHitbox(hitboxData, gameData.paths.gameTextureFolder);
            List<EditionData> deathSpawnList = ((ListAttribute) data.get(Keys.Entity.Ship.deathSpawn)).getDataList();
            List<Spawnable> deathSpawn = deathSpawnList.stream().map(GameDataLoader.this::convertToSpawnable).toList();
            int spriteVisualId = ((IntegerAttribute) data.get(Keys.Entity.Ship.spriteVisualId)).getValue();
            SceneVisual sprite = gameData.getGameVisual(spriteVisualId);
            Trajectory trajectory;
            int defaultTrajectoryId = ((IntegerAttribute) data.get(Keys.Entity.Ship.defaultTrajectoryId)).getValue();
            trajectory = gameData.getTrajectory(defaultTrajectoryId);

            ArrayList<ExtraComponent> extraComponents = new ArrayList<>();
            List<EditionData> shotDataList = ((ListAttribute) data.get(Keys.Entity.Ship.shots)).getDataList();
            List<ExtraComponent> shotList = shotDataList.stream().map(shotData -> convertShot(shotData, (id == 0))).toList();
            extraComponents.addAll(shotList);
            int hp = ((IntegerAttribute) data.get(Keys.Entity.Ship.hp)).getValue();
            return new Ship(Vec2D.ZERO, size, 0.0f, evil, id, sprite, trajectory, hitbox, deathSpawn, extraComponents, hp);
        }

        public Entity projectileFactory(EditionData data, GameDataManager gameData) {
            data.checkForType(Types.Entity.projectile);
            int id = ((IntegerAttribute) data.get(Keys.Entity.Projectile.id)).getValue();
            boolean evil = ((BooleanAttribute) data.get(Keys.Entity.Projectile.evil)).getValue();
            Vec2D size = ((Vec2DAttribute) data.get(Keys.Entity.Projectile.size)).getValue();
            EditionData hitboxData = ((EditionDataAttribute) data.get(Keys.Entity.Projectile.hitbox)).getData();
            Hitbox hitbox = convertToHitbox(hitboxData, gameData.paths.gameTextureFolder);
            List<EditionData> deathSpawnList = ((ListAttribute) data.get(Keys.Entity.Projectile.deathSpawn)).getDataList();
            List<Spawnable> deathSpawn = deathSpawnList.stream().map(GameDataLoader.this::convertToSpawnable).toList();
            int spriteVisualId = ((IntegerAttribute) data.get(Keys.Entity.Projectile.spriteVisualId)).getValue();
            SceneVisual sprite = gameData.getGameVisual(spriteVisualId);
            Trajectory trajectory;
            int defaultTrajectoryId = ((IntegerAttribute) data.get(Keys.Entity.Projectile.defaultTrajectoryId)).getValue();
            trajectory = gameData.getTrajectory(defaultTrajectoryId);

            ArrayList<ExtraComponent> extraComponents = new ArrayList<>();
            List<EditionData> shotDataList = ((ListAttribute) data.get(Keys.Entity.Projectile.shots)).getDataList();
            List<ExtraComponent> shotList = shotDataList.stream().map(shotData -> convertShot(shotData, (id == 0))).toList();
            extraComponents.addAll(shotList);
            return new Projectile(Vec2D.ZERO, size, 0.0f, evil, id, sprite, trajectory, hitbox, deathSpawn, extraComponents);
        }
    }

    final public class TimelineBuilders {

        private TimelineBuilders() {}

        public void singleSpawnInfoBuilder(LevelTimeline timeline, EditionData spawnInfoData) {
            double spawnTime = ((DoubleAttribute) spawnInfoData.get(Keys.SpawnInfo.Single.time)).getValue();
            ListAttribute spawnList = (ListAttribute) spawnInfoData.get(Keys.SpawnInfo.Single.spawn);
            assert spawnList.getCategory() == Category.SPAWN : "incorrect ListAttribrute category";
            List<EditionData> spawnDataList = spawnList.getDataList();
            List<Spawnable> spawnableList = spawnDataList.stream().map(GameDataLoader.this::convertToSpawnable).toList();
            for (Spawnable spawnable : spawnableList) {
                timeline.addSpawnable(spawnTime, spawnable);
            }
        }

        public void repeatSpawnInfoBuilder(LevelTimeline timeline, EditionData spawnInfoData) {
            double startTime = ((DoubleAttribute) spawnInfoData.get(Keys.SpawnInfo.Repeat.startTime)).getValue();
            int spawnCount = ((IntegerAttribute) spawnInfoData.get(Keys.SpawnInfo.Repeat.spawnCount)).getValue();
            double interval = ((DoubleAttribute) spawnInfoData.get(Keys.SpawnInfo.Repeat.interval)).getValue();
            ListAttribute spawnList = (ListAttribute) spawnInfoData.get(Keys.SpawnInfo.Repeat.spawn);
            assert spawnList.getCategory() == Category.SPAWN : "incorrect ListAttribrute category";
            List<EditionData> spawnDataList = spawnList.getDataList();
            List<Spawnable> spawnableList = spawnDataList.stream().map(GameDataLoader.this::convertToSpawnable).toList();
            for (int i = 0; i < spawnCount; i++) {
                double spawnTime = startTime + i * interval;
                for (Spawnable spawnable : spawnableList) {
                    timeline.addSpawnable(spawnTime, spawnable);
                }
            }
        }
    }
}
