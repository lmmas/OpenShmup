package edition;

import edition.attribute.*;
import lombok.Getter;
import lombok.NonNull;
import types.IVec2D;
import types.Vec2D;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final public class EditionData implements Serializable {

    @Getter
    final private Category category;
    @Getter
    final private Type type;
    @Getter
    final private ArrayList<Attribute> attributesList;

    final private HashMap<Key, Object> defaultValues;

    private EditionData(Category category, Type type, List<? extends Attribute> attributesList, Map<Key, Object> defaultValues) {
        assert defaultValues.size() == attributesList.size() : "incorrect default values";
        this.category = category;
        this.type = type;
        this.attributesList = new ArrayList<>(attributesList);
        this.defaultValues = new HashMap<>(defaultValues);
    }

    public boolean hasTypeSelect() {
        return this.category != Category.NONE;
    }

    @NonNull
    public Attribute get(Key key) {
        for (Attribute attribute : attributesList) {
            if (attribute.getKey() == key) {
                return attribute;
            }
        }
        assert false : "attribute not found: " + key.name() + "in Edition data of type" + this.category + ", " + this.type;
        return null;
    }

    private void setToMap(Map<Key, Object> objectMap) {
        for (Map.Entry<Key, Object> entry : objectMap.entrySet()) {
            Attribute attribute = this.get(entry.getKey());
            Object value = entry.getValue();
            switch (attribute) {
                case BooleanAttribute booleanAttribute -> {
                    assert value instanceof Boolean : "incorrect value type";
                    booleanAttribute.setValue((Boolean) value);
                }
                case DoubleAttribute doubleAttribute -> {
                    assert value instanceof Double : "incorrect value type";
                    doubleAttribute.setValue((Double) value);
                }
                case EditionDataAttribute editionDataAttribute -> {
                    assert value instanceof EditionData : "incorrect value type";
                    editionDataAttribute.setData((EditionData) value);
                }
                case FloatAttribute floatAttribute -> {
                    assert value instanceof Float : "incorrect value type";
                    floatAttribute.setValue((Float) value);
                }
                case IntegerAttribute integerAttribute -> {
                    assert value instanceof Integer : "incorrect value type";
                    integerAttribute.setValue((Integer) value);
                }
                case IVec2DAttribute iVec2DAttribute -> {
                    assert value instanceof IVec2D : "incorrect value type";
                    iVec2DAttribute.setValue((IVec2D) value);
                }
                case ListAttribute listAttribute -> {
                    assert value instanceof List : "incorrect value type";
                    listAttribute.setDataList((List<EditionData>) value);
                }
                case StringAttribute stringAttribute -> {
                    assert value instanceof String : "incorrect value type";
                    stringAttribute.setValue((String) value);
                }
                case Vec2DAttribute vec2DAttribute -> {
                    assert value instanceof Vec2D : "incorrect value type";
                    vec2DAttribute.setValue((Vec2D) value);
                }
            }
        }
    }

    public void setToDefault() {
        this.setToMap(defaultValues);
    }

    public void checkForCategory(Category category) {
        assert this.getCategory() == category : "incorrect EditionData category: " + this.getCategory().name();
    }

    public void checkForType(Type type) {
        assert this.getType() == type : "incorrect EditionData type: " + this.getType().name();
    }

    public void checkForType(Category category, Type type) {
        assert this.getCategory() == category && this.getType() == type : "Incorrect EdiditonData type: " + this.getCategory().name() + ", " + this.getType().name();
    }

    public static int getVisualId(EditionData data) {
        data.checkForCategory(Category.VISUAL);
        IntegerAttribute idAttribute = (IntegerAttribute) switch ((Types.Visual) data.getType()) {
            case scrollingImage -> data.get(Keys.Visual.ScrollingImage.id);
            case animation -> data.get(Keys.Visual.Animation.id);
        };
        return idAttribute.getValue();
    }

    public static int getTrajectoryId(EditionData data) {
        data.checkForCategory(Category.TRAJECTORY);
        IntegerAttribute idAttribute = (IntegerAttribute) switch ((Types.Trajectory) data.getType()) {
            case fixed -> data.get(Keys.Trajectory.FixedTrajectory.id);
            case player -> data.get(Keys.Trajectory.PlayerControlledTrajectory.id);
        };
        return idAttribute.getValue();
    }

    public static int getEntityId(EditionData data) {
        data.checkForCategory(Category.ENTITY);
        IntegerAttribute idAttribute = (IntegerAttribute) switch ((Types.Entity) data.getType()) {
            case projectile -> data.get(Keys.Entity.Projectile.id);
            case ship -> data.get(Keys.Entity.Ship.id);
        };
        return idAttribute.getValue();
    }

    public enum Category {
        NONE, VISUAL, TRAJECTORY, ENTITY, SPAWN, SPAWN_INFO, HITBOX, CONFIG
    }

    public interface Type extends Serializable {

        String name();
    }

    //WARNING: changing any of these enum names will change the values of Strings used in serialization/deserialization
    public enum Types implements Type {
        shot, spritesheetInfo;

        public enum Visual implements Type {
            scrollingImage, animation
        }

        public enum Trajectory implements Type {
            fixed, player
        }

        public enum Entity implements Type {
            projectile, ship
        }

        public enum Spawn implements Type {
            display, entity
        }

        public enum SpawnInfo implements Type {
            single, repeat
        }

        public enum Hitbox implements Type {
            rectangle, custom
        }

        public enum Config implements Type {
            general, levelUI, lives
        }
    }

    public interface Key extends Serializable {

        String name();
    }

    //WARNING: changing any of these enum names will change the values of Strings used in serialization/deserialization
    public enum Keys implements Key {
        type;

        final public static class Visual {

            private Visual() {}

            public enum ScrollingImage implements Key {
                id,
                layer,
                size,
                fileName,
                horizontalScrolling,
                speed
            }

            public enum Animation implements Key {
                id,
                layer,
                size,
                spritesheetInfo,
                framePeriodSeconds,
                looping
            }
        }

        final public static class Trajectory {

            private Trajectory() {}

            public enum FixedTrajectory implements Key {
                id,
                functionX,
                functionY
            }

            public enum PlayerControlledTrajectory implements Key {
                id,
                playerMovementSpeed
            }
        }

        final public static class Entity {

            private Entity() {}

            public enum Projectile implements Key {
                id,
                evil,
                size,
                hitbox,
                deathSpawn,
                spriteVisualId,
                defaultTrajectoryId,
                shots,
            }

            public enum Ship implements Key {
                id,
                evil,
                size,
                hitbox,
                deathSpawn,
                spriteVisualId,
                defaultTrajectoryId,
                shots,
                hp
            }
        }

        final public static class SpawnInfo {

            private SpawnInfo() {}

            public enum Single implements Key {
                time,
                spawn
            }

            public enum Repeat implements Key {
                startTime,
                spawnCount,
                interval,
                spawn
            }
        }

        final public static class Spawn {

            private Spawn() {}

            public enum DisplaySpawn implements Key {
                id,
                position
            }

            public enum EntitySpawn implements Key {
                id,
                startingPosition,
                trajectory
            }
        }

        final public static class Hitbox {

            private Hitbox() {}

            public enum RectangleHitbox implements Key {
                size
            }

            public enum CustomHitbox implements Key {
                fileName,
                size
            }
        }

        public enum SpritesheetInfo implements Key {
            fileName,
            frameCount,
            frameSize,
            startingPosition,
            stride
        }

        public enum Shot implements Key {
            shotPeriod,
            firstShotTime,
            spawn
        }

        final public static class Config {

            public enum General implements Key {
                resolution
            }

            public enum LevelUI implements Key {
                lives;

                public enum Lives implements Key {
                    fileName, size, position, stride
                }
            }
        }
    }

    final public static class Visual {

        private Visual() {}

        public static EditionData ScrollingImage() {
            List<Attribute> attributeList = List.of(
                new IntegerAttribute(Keys.Visual.ScrollingImage.id),
                new IntegerAttribute(Keys.Visual.ScrollingImage.layer),
                new Vec2DAttribute(Keys.Visual.ScrollingImage.size),
                new StringAttribute(Keys.Visual.ScrollingImage.fileName),
                new FloatAttribute(Keys.Visual.ScrollingImage.speed),
                new BooleanAttribute(Keys.Visual.ScrollingImage.horizontalScrolling)
            );
            return new EditionData(Category.VISUAL, Types.Visual.scrollingImage, attributeList, scrollingImageDefaultValues);
        }
        final private static Map<Key, Object> scrollingImageDefaultValues = Map.of(
            Keys.Visual.ScrollingImage.id, 0,
            Keys.Visual.ScrollingImage.layer, 0,
            Keys.Visual.ScrollingImage.size, Vec2D.ZERO,
            Keys.Visual.ScrollingImage.fileName, "",
            Keys.Visual.ScrollingImage.speed, 100.0f,
            Keys.Visual.ScrollingImage.horizontalScrolling, false
        );
        public static EditionData ScrollingImage(int id, int layer, Vec2D size, String imageFileName, float speed, boolean horizontalScrolling) {
            EditionData scrollingImageData = ScrollingImage();
            scrollingImageData.setToMap(Map.of(
                    Keys.Visual.ScrollingImage.id, id,
                    Keys.Visual.ScrollingImage.layer, layer,
                    Keys.Visual.ScrollingImage.size, size,
                    Keys.Visual.ScrollingImage.fileName, imageFileName,
                    Keys.Visual.ScrollingImage.speed, speed,
                    Keys.Visual.ScrollingImage.horizontalScrolling, horizontalScrolling
                )
            );
            return scrollingImageData;
        }

        private static EditionData Animation() {
            List<Attribute> attributeList = List.of(
                new IntegerAttribute(Keys.Visual.Animation.id),
                new IntegerAttribute(Keys.Visual.Animation.layer),
                new Vec2DAttribute(Keys.Visual.Animation.size),
                new EditionDataAttribute(Keys.Visual.Animation.spritesheetInfo),
                new DoubleAttribute(Keys.Visual.Animation.framePeriodSeconds),
                new BooleanAttribute(Keys.Visual.Animation.looping)
            );
            return new EditionData(Category.VISUAL, Types.Visual.animation, attributeList, defaultAnimationValues);
        }
        final private static Map<Key, Object> defaultAnimationValues = Map.of(
            Keys.Visual.Animation.id, 0,
            Keys.Visual.Animation.layer, 0,
            Keys.Visual.Animation.size, Vec2D.ZERO,
            Keys.Visual.Animation.spritesheetInfo, Defaults.SpritesheetInfo(),
            Keys.Visual.Animation.framePeriodSeconds, 1.0d,
            Keys.Visual.Animation.looping, false
        );
        public static EditionData Animation(int id, int layer, Vec2D size, double framePeriodSeconds, boolean looping, EditionData spritesheetInfo) {
            EditionData animationData = Animation();
            animationData.setToMap(Map.of(
                Keys.Visual.Animation.id, id,
                Keys.Visual.Animation.layer, layer,
                Keys.Visual.Animation.size, size,
                Keys.Visual.Animation.framePeriodSeconds, framePeriodSeconds,
                Keys.Visual.Animation.looping, looping,
                Keys.Visual.Animation.spritesheetInfo, spritesheetInfo)
            );
            return animationData;
        }
    }

    final public static class Trajectory {

        private Trajectory() {}

        private static EditionData FixedTrajectory() {
            List<Attribute> attributeList = List.of(
                new IntegerAttribute(Keys.Trajectory.FixedTrajectory.id),
                new StringAttribute(Keys.Trajectory.FixedTrajectory.functionX),
                new StringAttribute(Keys.Trajectory.FixedTrajectory.functionY)
            );
            return new EditionData(Category.TRAJECTORY, Types.Trajectory.fixed, attributeList, fixedTrajectoryDefaultValues);
        }
        final private static Map<Key, Object> fixedTrajectoryDefaultValues = Map.of(
            Keys.Trajectory.FixedTrajectory.id, 0,
            Keys.Trajectory.FixedTrajectory.functionX, "",
            Keys.Trajectory.FixedTrajectory.functionY, ""
        );
        public static EditionData FixedTrajectory(int id, String functionX, String functionY) {
            EditionData fixedTrajectoryData = FixedTrajectory();
            fixedTrajectoryData.setToMap(Map.of(
                Keys.Trajectory.FixedTrajectory.id, id,
                Keys.Trajectory.FixedTrajectory.functionX, functionX,
                Keys.Trajectory.FixedTrajectory.functionY, functionY
            ));
            return fixedTrajectoryData;
        }

        private static EditionData PlayerControlledTrajectory() {
            List<Attribute> attributeList = List.of(
                new IntegerAttribute(Keys.Trajectory.PlayerControlledTrajectory.id),
                new FloatAttribute(Keys.Trajectory.PlayerControlledTrajectory.playerMovementSpeed)
            );
            return new EditionData(Category.TRAJECTORY, Types.Trajectory.player, attributeList, playerControlledTrajectoryDefaultValues);
        }
        final private static Map<Key, Object> playerControlledTrajectoryDefaultValues = Map.of(
            Keys.Trajectory.PlayerControlledTrajectory.id, 0,
            Keys.Trajectory.PlayerControlledTrajectory.playerMovementSpeed, 100.0f
        );
        public static EditionData PlayerControlledTrajectory(int id, float playerMovementSpeed) {
            EditionData playerControlledTrajectoryData = PlayerControlledTrajectory();
            playerControlledTrajectoryData.setToMap(Map.of(
                Keys.Trajectory.PlayerControlledTrajectory.id, id,
                Keys.Trajectory.PlayerControlledTrajectory.playerMovementSpeed, playerMovementSpeed
            ));
            return playerControlledTrajectoryData;
        }
    }

    final public static class Entity {

        private Entity() {}

        private static EditionData Projectile() {
            List<Attribute> attributeList = List.of(
                new IntegerAttribute(Keys.Entity.Projectile.id),
                new BooleanAttribute(Keys.Entity.Projectile.evil),
                new Vec2DAttribute(Keys.Entity.Projectile.size),
                new IntegerAttribute(Keys.Entity.Projectile.spriteVisualId),
                new EditionDataAttribute(Keys.Entity.Projectile.hitbox),
                new IntegerAttribute(Keys.Entity.Projectile.defaultTrajectoryId),
                new ListAttribute(Category.NONE, Keys.Entity.Projectile.shots),
                new ListAttribute(Category.SPAWN, Keys.Entity.Projectile.deathSpawn)
            );
            return new EditionData(Category.ENTITY, Types.Entity.projectile, attributeList, projectileDefaultValues);
        }
        private final static Map<Key, Object> projectileDefaultValues = Map.of(
            Keys.Entity.Projectile.id, 0,
            Keys.Entity.Projectile.evil, true,
            Keys.Entity.Projectile.size, Vec2D.ZERO,
            Keys.Entity.Projectile.spriteVisualId, 0,
            Keys.Entity.Projectile.hitbox, Defaults.RectangleHitbox(),
            Keys.Entity.Projectile.defaultTrajectoryId, 0,
            Keys.Entity.Projectile.shots, List.of(),
            Keys.Entity.Projectile.deathSpawn, List.of()
        );
        public static EditionData Projectile(int id, boolean evil, Vec2D size, int spriteVisualId, EditionData hitbox, Integer defaultTrajectoryId, List<EditionData> deathspawn, List<EditionData> shots) {
            EditionData projectileData = Projectile();
            projectileData.setToMap(Map.of(
                Keys.Entity.Projectile.id, id,
                Keys.Entity.Projectile.evil, evil,
                Keys.Entity.Projectile.size, size,
                Keys.Entity.Projectile.spriteVisualId, spriteVisualId,
                Keys.Entity.Projectile.hitbox, hitbox,
                Keys.Entity.Projectile.defaultTrajectoryId, defaultTrajectoryId,
                Keys.Entity.Projectile.shots, shots,
                Keys.Entity.Projectile.deathSpawn, deathspawn
            ));
            return projectileData;
        }

        public static EditionData Ship() {
            List<Attribute> attributeList = List.of(
                new IntegerAttribute(Keys.Entity.Ship.id),
                new BooleanAttribute(Keys.Entity.Ship.evil),
                new IntegerAttribute(Keys.Entity.Ship.hp),
                new Vec2DAttribute(Keys.Entity.Ship.size),
                new IntegerAttribute(Keys.Entity.Ship.spriteVisualId),
                new EditionDataAttribute(Keys.Entity.Ship.hitbox),
                new IntegerAttribute(Keys.Entity.Ship.defaultTrajectoryId),
                new ListAttribute(Category.NONE, Keys.Entity.Ship.shots),
                new ListAttribute(Category.SPAWN, Keys.Entity.Ship.deathSpawn)
            );
            return new EditionData(Category.ENTITY, Types.Entity.ship, attributeList, shipDefaultValues);
        }
        private final static Map<Key, Object> shipDefaultValues = Map.of(
            Keys.Entity.Ship.id, 0,
            Keys.Entity.Ship.evil, true,
            Keys.Entity.Ship.hp, 1,
            Keys.Entity.Ship.size, Vec2D.ZERO,
            Keys.Entity.Ship.spriteVisualId, 0,
            Keys.Entity.Ship.hitbox, Defaults.RectangleHitbox(),
            Keys.Entity.Ship.defaultTrajectoryId, 0,
            Keys.Entity.Ship.shots, List.of(),
            Keys.Entity.Ship.deathSpawn, List.of()
        );
        public static EditionData Ship(int id, boolean evil, int hp, Vec2D size, int spriteVisualId, EditionData hitbox, Integer defaultTrajectoryId, List<EditionData> deathspawn, List<EditionData> shots) {
            EditionData shipData = Ship();
            shipData.setToMap(Map.of(
                Keys.Entity.Ship.id, id,
                Keys.Entity.Ship.evil, evil,
                Keys.Entity.Ship.hp, hp,
                Keys.Entity.Ship.size, size,
                Keys.Entity.Ship.spriteVisualId, spriteVisualId,
                Keys.Entity.Ship.hitbox, hitbox,
                Keys.Entity.Ship.defaultTrajectoryId, defaultTrajectoryId,
                Keys.Entity.Ship.shots, shots,
                Keys.Entity.Ship.deathSpawn, deathspawn
            ));
            return shipData;
        }
    }

    final public static class Spawn {

        private Spawn() {}

        public static EditionData DisplaySpawn() {
            List<Attribute> attributeList = List.of(
                new IntegerAttribute(Keys.Spawn.DisplaySpawn.id),
                new Vec2DAttribute(Keys.Spawn.DisplaySpawn.position)
            );
            return new EditionData(Category.SPAWN, Types.Spawn.display, attributeList, displaySpawnDefaultValues);
        }
        final private static Map<Key, Object> displaySpawnDefaultValues = Map.of(
            Keys.Spawn.DisplaySpawn.id, 0,
            Keys.Spawn.DisplaySpawn.position, Vec2D.ZERO
        );
        public static EditionData DisplaySpawn(int id, Vec2D position) {
            EditionData displaySpawnData = DisplaySpawn();
            displaySpawnData.setToMap(Map.of(
                Keys.Spawn.DisplaySpawn.id, id,
                Keys.Spawn.DisplaySpawn.position, position
            ));
            return displaySpawnData;
        }

        private static EditionData EntitySpawn() {
            List<Attribute> attributeList = List.of(
                new IntegerAttribute(Keys.Spawn.EntitySpawn.id),
                new IntegerAttribute(Keys.Spawn.EntitySpawn.trajectory),
                new Vec2DAttribute(Keys.Spawn.EntitySpawn.startingPosition)
            );
            return new EditionData(Category.SPAWN, Types.Spawn.entity, attributeList, entitySpawnDefaultValues);
        }
        final private static Map<Key, Object> entitySpawnDefaultValues = Map.of(
            Keys.Spawn.EntitySpawn.id, 0,
            Keys.Spawn.EntitySpawn.trajectory, 0,
            Keys.Spawn.EntitySpawn.startingPosition, Vec2D.ZERO
        );
        public static EditionData EntitySpawn(int id, int trajectoryId, Vec2D startingPosition) {
            EditionData entitySpawnData = EntitySpawn();
            entitySpawnData.setToMap(Map.of(
                Keys.Spawn.EntitySpawn.id, id,
                Keys.Spawn.EntitySpawn.trajectory, trajectoryId,
                Keys.Spawn.EntitySpawn.startingPosition, startingPosition
            ));
            return entitySpawnData;
        }
    }

    final public static class SpawnInfo {

        private SpawnInfo() {}

        public static EditionData SingleSpawnInfo() {
            List<Attribute> attributeList = List.of(
                new DoubleAttribute(Keys.SpawnInfo.Single.time),
                new ListAttribute(Category.SPAWN, Keys.SpawnInfo.Single.spawn)
            );
            return new EditionData(Category.SPAWN_INFO, Types.SpawnInfo.single, attributeList, singleSpawnInfoDefaultValues);
        }
        final private static Map<Key, Object> singleSpawnInfoDefaultValues = Map.of(
            Keys.SpawnInfo.Single.time, 0d,
            Keys.SpawnInfo.Single.spawn, List.of()
        );
        public static EditionData SingleSpawnInfo(double spawnTime, List<EditionData> spawns) {
            EditionData singleSpawnInfoData = SingleSpawnInfo();
            singleSpawnInfoData.setToMap(Map.of(
                Keys.SpawnInfo.Single.time, spawnTime,
                Keys.SpawnInfo.Single.spawn, spawns
            ));
            return singleSpawnInfoData;
        }

        private static EditionData RepeatSpawnInfo() {
            List<Attribute> attributeList = List.of(
                new DoubleAttribute(Keys.SpawnInfo.Repeat.startTime),
                new IntegerAttribute(Keys.SpawnInfo.Repeat.spawnCount),
                new DoubleAttribute(Keys.SpawnInfo.Repeat.interval),
                new ListAttribute(Category.SPAWN, Keys.SpawnInfo.Repeat.spawn)
            );
            return new EditionData(Category.SPAWN_INFO, Types.SpawnInfo.repeat, attributeList, repeatSpawnInfoDefaultValues);
        }
        final private static Map<Key, Object> repeatSpawnInfoDefaultValues = Map.of(
            Keys.SpawnInfo.Repeat.startTime, 0.0d,
            Keys.SpawnInfo.Repeat.spawnCount, 1,
            Keys.SpawnInfo.Repeat.interval, 1.0d,
            Keys.SpawnInfo.Repeat.spawn, List.of()
        );
        public static EditionData RepeatSpawnInfo(double startTime, int spawnCount, double interval, List<EditionData> spawns) {
            EditionData repeatSpawnInfoData = RepeatSpawnInfo();
            repeatSpawnInfoData.setToMap(Map.of(
                Keys.SpawnInfo.Repeat.startTime, startTime,
                Keys.SpawnInfo.Repeat.spawnCount, spawnCount,
                Keys.SpawnInfo.Repeat.interval, interval,
                Keys.SpawnInfo.Repeat.spawn, spawns
            ));
            return repeatSpawnInfoData;
        }
    }

    final public static class Hitbox {

        private Hitbox() {}

        public static EditionData RectangleHitbox() {
            List<Attribute> attributeList = List.of(
                new Vec2DAttribute(Keys.Hitbox.RectangleHitbox.size)
            );
            return new EditionData(Category.HITBOX, Types.Hitbox.rectangle, attributeList, rectangleHitboxDefaultValues);
        }
        final private static Map<Key, Object> rectangleHitboxDefaultValues = Map.of(
            Keys.Hitbox.RectangleHitbox.size, Vec2D.ZERO
        );
        public static EditionData RectangleHitbox(Vec2D size) {
            EditionData rectangleHitboxData = RectangleHitbox();
            rectangleHitboxData.setToMap(Map.of(
                Keys.Hitbox.RectangleHitbox.size, size
            ));
            return rectangleHitboxData;
        }

        private static EditionData CustomHitbox() {
            List<Attribute> attributeList = List.of(
                new Vec2DAttribute(Keys.Hitbox.CustomHitbox.size),
                new StringAttribute(Keys.Hitbox.CustomHitbox.fileName)
            );
            return new EditionData(Category.HITBOX, Types.Hitbox.custom, attributeList, customHitboxDefaultValues);
        }
        final private static Map<Key, Object> customHitboxDefaultValues = Map.of(
            Keys.Hitbox.CustomHitbox.size, Vec2D.ZERO,
            Keys.Hitbox.CustomHitbox.fileName, ""
        );
        public static EditionData CustomHitbox(Vec2D size, String fileName) {
            EditionData customHitboxData = CustomHitbox();
            customHitboxData.setToMap(Map.of(
                Keys.Hitbox.CustomHitbox.size, size,
                Keys.Hitbox.CustomHitbox.fileName, fileName
            ));
            return customHitboxData;
        }
    }

    final public static class Config {

        private Config() {}

        private static EditionData General() {
            List<Attribute> attributeList = List.of(
                new IVec2DAttribute(Keys.Config.General.resolution)
            );
            return new EditionData(Category.CONFIG, Types.Config.general, attributeList, generalConfigDefaultValues);
        }
        final private static Map<Key, Object> generalConfigDefaultValues = Map.of(
            Keys.Config.General.resolution, new IVec2D(1080, 1080)
        );
        public static EditionData General(IVec2D resolution) {
            EditionData data = General();
            data.setToMap(Map.of(Keys.Config.General.resolution, resolution));
            return data;
        }

        private static EditionData LevelUI() {
            var attributeList = List.of(
                new EditionDataAttribute(Keys.Config.LevelUI.lives)
            );
            return new EditionData(Category.CONFIG, Types.Config.levelUI, attributeList, levelUIConfigDefaultValues);
        }
        final private static Map<Key, Object> levelUIConfigDefaultValues = Map.of(
            Keys.Config.LevelUI.lives, Defaults.Config(Types.Config.lives)
        );
        public static EditionData LevelUI(EditionData livesData) {
            EditionData data = LevelUI();
            data.setToMap(Map.of(
                Keys.Config.LevelUI.lives, livesData
            ));
            return data;
        }

        final public static class LevelUI {

            private static EditionData Lives() {
                var attributeList = List.of(
                    new StringAttribute(Keys.Config.LevelUI.Lives.fileName),
                    new Vec2DAttribute(Keys.Config.LevelUI.Lives.size),
                    new Vec2DAttribute(Keys.Config.LevelUI.Lives.position),
                    new Vec2DAttribute(Keys.Config.LevelUI.Lives.stride)
                );
                return new EditionData(Category.CONFIG, Types.Config.lives, attributeList, livesConfigDefaultValues);
            }
            final private static Map<Key, Object> livesConfigDefaultValues = Map.of(
                Keys.Config.LevelUI.Lives.fileName, "",
                Keys.Config.LevelUI.Lives.size, new Vec2D(20f, 20f),
                Keys.Config.LevelUI.Lives.position, new Vec2D(80f, 1000f),
                Keys.Config.LevelUI.Lives.stride, new Vec2D(30f, 0f)
            );
            public static EditionData Lives(String fileName, Vec2D size, Vec2D startPosition, Vec2D stride) {
                EditionData data = Lives();
                data.setToMap(Map.of(
                    Keys.Config.LevelUI.Lives.fileName, fileName,
                    Keys.Config.LevelUI.Lives.size, size,
                    Keys.Config.LevelUI.Lives.position, startPosition,
                    Keys.Config.LevelUI.Lives.stride, stride
                ));
                return data;
            }
        }
    }

    private static EditionData Shot() {
        List<Attribute> attributeList = List.of(
            new DoubleAttribute(Keys.Shot.shotPeriod),
            new DoubleAttribute(Keys.Shot.firstShotTime),
            new ListAttribute(Category.SPAWN, Keys.Shot.spawn)
        );
        return new EditionData(Category.NONE, Types.shot, attributeList, shotDefaultValues);
    }
    final private static Map<Key, Object> shotDefaultValues = Map.of(
        Keys.Shot.shotPeriod, 1.0f,
        Keys.Shot.firstShotTime, 0.0f,
        Keys.Shot.spawn, List.of()
    );
    public static EditionData Shot(double shotPeriod, double firstShotTime, List<EditionData> spawnables) {
        EditionData shotData = Shot();
        shotData.setToMap(Map.of(
            Keys.Shot.shotPeriod, shotPeriod,
            Keys.Shot.firstShotTime, firstShotTime,
            Keys.Shot.spawn, spawnables
        ));
        return shotData;
    }

    public static EditionData SpritesheetInfo() {
        List<Attribute> attributeList = List.of(
            new StringAttribute(Keys.SpritesheetInfo.fileName),
            new IntegerAttribute(Keys.SpritesheetInfo.frameCount),
            new IVec2DAttribute(Keys.SpritesheetInfo.frameSize),
            new IVec2DAttribute(Keys.SpritesheetInfo.startingPosition),
            new IVec2DAttribute(Keys.SpritesheetInfo.stride)
        );
        return new EditionData(Category.NONE, Types.spritesheetInfo, attributeList, spritesheetInfoDefaultValues);
    }
    final private static Map<Key, Object> spritesheetInfoDefaultValues = Map.of(
        Keys.SpritesheetInfo.fileName, "",
        Keys.SpritesheetInfo.frameCount, 0,
        Keys.SpritesheetInfo.frameSize, IVec2D.ZERO,
        Keys.SpritesheetInfo.startingPosition, IVec2D.ZERO,
        Keys.SpritesheetInfo.stride, IVec2D.ZERO
    );
    public static EditionData SpritesheetInfo(String fileName, int frameCount, IVec2D framesize, IVec2D startPosition, IVec2D stride) {
        EditionData spritesheetInfoData = SpritesheetInfo();
        spritesheetInfoData.setToMap(Map.of(
            Keys.SpritesheetInfo.fileName, fileName,
            Keys.SpritesheetInfo.frameCount, frameCount,
            Keys.SpritesheetInfo.frameSize, framesize,
            Keys.SpritesheetInfo.startingPosition, startPosition,
            Keys.SpritesheetInfo.stride, stride
        ));
        return spritesheetInfoData;
    }

    final public static class Defaults {

        private Defaults() {}

        public static EditionData Animation() {
            EditionData animationData = Visual.Animation();
            animationData.setToDefault();
            return animationData;
        }

        public static EditionData ScrollingImage() {
            EditionData scrollingImageData = Visual.ScrollingImage();
            scrollingImageData.setToDefault();
            return scrollingImageData;
        }

        public static EditionData FixedTrajectory() {
            EditionData fixedTrajectoryData = Trajectory.FixedTrajectory();
            fixedTrajectoryData.setToDefault();
            return fixedTrajectoryData;
        }

        public static EditionData PlayerControlledTrajectory() {
            EditionData playerControlledTrajectoryData = Trajectory.PlayerControlledTrajectory();
            playerControlledTrajectoryData.setToDefault();
            return playerControlledTrajectoryData;
        }

        public static EditionData Projectile() {
            EditionData projectileData = Entity.Projectile();
            projectileData.setToDefault();
            return projectileData;
        }

        public static EditionData Ship() {
            EditionData shipData = Entity.Ship();
            shipData.setToDefault();
            return shipData;
        }

        public static EditionData DisplaySpawn() {
            EditionData displaySpawnData = Spawn.DisplaySpawn();
            displaySpawnData.setToDefault();
            return displaySpawnData;
        }

        public static EditionData EntitySpawn() {
            EditionData entitySpawnData = Spawn.EntitySpawn();
            entitySpawnData.setToDefault();
            return entitySpawnData;
        }

        public static EditionData SingleSpawnInfo() {
            EditionData singleSpawnInfoData = SpawnInfo.SingleSpawnInfo();
            singleSpawnInfoData.setToDefault();
            return singleSpawnInfoData;
        }

        public static EditionData RepeatSpawnInfo() {
            EditionData repeatSpawnInfoData = SpawnInfo.RepeatSpawnInfo();
            repeatSpawnInfoData.setToDefault();
            return repeatSpawnInfoData;
        }

        public static EditionData RectangleHitbox() {
            EditionData rectangleHitboxData = Hitbox.RectangleHitbox();
            rectangleHitboxData.setToDefault();
            return rectangleHitboxData;
        }

        public static EditionData CustomHitbox() {
            EditionData customHitboxData = Hitbox.CustomHitbox();
            customHitboxData.setToDefault();
            return customHitboxData;
        }

        public static EditionData Shot() {
            EditionData shotData = EditionData.Shot();
            shotData.setToDefault();
            return shotData;
        }

        public static EditionData SpritesheetInfo() {
            EditionData spritesheetInfoData = EditionData.SpritesheetInfo();
            spritesheetInfoData.setToDefault();
            return spritesheetInfoData;
        }

        public static EditionData Config(Types.Config type) {
            EditionData configData = switch (type) {
                case general -> Config.General();
                case levelUI -> Config.LevelUI();
                case lives -> Config.LevelUI.Lives();
            };
            configData.setToDefault();
            return configData;
        }
    }
}
