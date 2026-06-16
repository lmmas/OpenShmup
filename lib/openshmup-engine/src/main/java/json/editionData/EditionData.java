package json.editionData;

import json.attribute.Attribute;

import java.util.List;

public sealed interface EditionData permits AnimationEditionData.SpritesheetInfoData, EntityEditionData, HitboxEditionData, ShotEditionData, SpawnEditionData, SpawnInfoEditionData, TrajectoryEditionData, VisualEditionData {

    Category getCategory();

    Type getType();

    List<Attribute> getAttributes();

    void setToDefault();

    static void checkForCategory(EditionData data, Category category) {
        assert data.getCategory() == category : "incorrect EditionData category: " + data.getCategory().name();
    }

    static void checkForType(EditionData data, Type type) {
        assert data.getType() == type : "incorrect EditionData type: " + data.getType().name();
    }

    static void checkForType(EditionData data, Category category, Type type) {
        assert data.getCategory() == category && data.getType() == type : "Incorrect EdiditonData type: " + data.getCategory().name() + ", " + data.getType().name();
    }

    static int getVisualId(EditionData data) {
        checkForCategory(data, Category.VISUAL);
        VisualEditionData visualData = (VisualEditionData) data;
        return switch (visualData) {
            case ScrollingImageEditionData scrollingImageData -> scrollingImageData.getIdAttribute().getValue();
            case AnimationEditionData animationData -> animationData.getIdAttribute().getValue();
        };
    }

    static int getTrajectoryId(EditionData data) {
        checkForCategory(data, Category.TRAJECTORY);
        TrajectoryEditionData trajectoryData = (TrajectoryEditionData) data;
        return switch (trajectoryData) {
            case FixedTrajectoryEditionData fixedTrajectoryEditionData ->
                fixedTrajectoryEditionData.getIdAttribute().getValue();
            case PlayerControlledTrajectoryEditionData playerControlledTrajectoryData ->
                playerControlledTrajectoryData.getIdAttribute().getValue();
        };
    }

    static int getEntityId(EditionData data) {
        checkForCategory(data, Category.ENTITY);
        EntityEditionData entityData = (EntityEditionData) data;
        return switch (entityData) {
            case ProjectileEditionData projectileData -> projectileData.getIdAttribute().getValue();
            case ShipEditionData shipData -> shipData.getIdAttribute().getValue();
        };
    }

    enum Category {
        NONE, VISUAL, TRAJECTORY, ENTITY, SPAWN, SPAWN_INFO, HITBOX
    }

    interface Type {
        String name();
    }

    //WARNING: changing any of these enum names will change the values of Strings used in serialization/deserialization
    enum Types implements Type {
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
    }

    interface Key {

        String name();
    }

    //WARNING: changing any of these enum names will change the values of Strings used in serialization/deserialization
    enum Keys implements Key {
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
                spawnTime,
                spawns
            }

            public enum Repeat implements Key {
                startTime,
                spawnCount,
                interval,
                spawns
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
    }

    static boolean hasTypeSelect(EditionData data) {
        return data.getCategory() != Category.NONE;
    }
}
