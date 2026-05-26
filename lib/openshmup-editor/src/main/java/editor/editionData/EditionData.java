package editor.editionData;

import editor.attribute.Attribute;

import java.util.List;

public sealed interface EditionData permits AnimationEditionData.SpritesheetInfoData, EntityEditionData, ShotEditionData, HitboxEditionData, SpawnEditionData, TrajectoryEditionData, VisualEditionData {

    EditionData.Category getCategory();

    Type getType();

    List<Attribute> getAttributes();

    void setToDefault();

    enum Category {
        NONE, VISUAL, TRAJECTORY, ENTITY, SPAWN, HITBOX
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

        public enum Hitbox implements Type {
            rectangle, custom
        }
    }

    interface Key {

        String name();
    }

    //WARNING: changing any of these enum names will change the values of Strings used in serialization/deserialization
    final class Keys {

        private Keys() {}

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
}
