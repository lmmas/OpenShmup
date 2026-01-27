package json;

final public class JsonFieldNames {

    private JsonFieldNames() {}

    public static class ScrollingImage {

        final public static String id = "id";

        final public static String layer = "layer";

        final public static String size = "size";

        final public static String fileName = "fileName";

        final public static String horizontalScrolling = "horizontalScrolling";

        final public static String speed = "speed";
    }

    public static class Animation {

        final public static String id = "id";

        final public static String layer = "layer";

        final public static String size = "size";

        final public static String spritesheetInfo = "spritesheetInfo";

        final public static String framePeriodSeconds = "framePeriodSeconds";

        final public static String looping = "looping";

        public static class SpritesheetInfo {

            final public static String fileName = "fileName";

            final public static String frameCount = "frameCount";

            final public static String frameSize = "frameSize";

            final public static String startingPosition = "startingPosition";

            final public static String stride = "stride";
        }
    }

    public static class FixedTrajectory {

        final public static String id = "id";

        final public static String functionX = "functionX";

        final public static String functionY = "functionY";
    }

    public static class PlayerControlledTrajectory {

        final public static String id = "id";

        final public static String playerMovementSpeed = "playerMovementSpeed";
    }

    public static class SimpleRectangleHitbox {

        final public static String size = "size";
    }

    public static class CompositeHitbox {

        final public static String fileName = "fileName";

        final public static String size = "size";
    }

    public static class Shot {

        final public static String shotPeriod = "shotPeriod";

        final public static String firstShotTime = "firstShotTime";

        final public static String spawn = "spawn";
    }

    public static class Ship {

        final public static String id = "id";

        final public static String evil = "evil";

        final public static String size = "size";

        final public static String hitbox = "hitbox";

        final public static String deathSpawn = "deathSpawn";

        final public static String spriteVisualId = "spriteVisualId";

        final public static String defaultTrajectoryId = "defaultTrajectoryId";

        final public static String shot = "shot";

        final public static String hp = "hp";
    }

    public static class Projectile {

        final public static String id = "id";

        final public static String evil = "evil";

        final public static String size = "size";

        final public static String hitbox = "hitbox";

        final public static String deathSpawn = "deathSpawn";

        final public static String spriteVisualId = "spriteVisualId";

        final public static String defaultTrajectoryId = "defaultTrajectoryId";

        final public static String shot = "shot";
    }

    public static class DisplaySpawnInfo {

        final public static String id = "id";

        final public static String position = "position";
    }

    public static class EntitySpawnInfo {

        final public static String id = "id";

        final public static String startingPosition = "startingPosition";

        final public static String trajectory = "trajectory";
    }
}
