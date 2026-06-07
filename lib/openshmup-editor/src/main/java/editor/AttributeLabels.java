package editor;

import json.editionData.EditionData;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static json.editionData.EditionData.Keys.*;

final public class AttributeLabels {

    private AttributeLabels() {}

    final private static Map<EditionData.Key, String> labelMap = Stream.of(new Object[][]{
        {Visual.ScrollingImage.id, "Visual ID yay"},
        {Visual.ScrollingImage.layer, "Scene layer"},
        {Visual.ScrollingImage.size, "Size"},
        {Visual.ScrollingImage.fileName, "Image file name"},
        {Visual.ScrollingImage.horizontalScrolling, "Scrolling speed"},
        {Visual.ScrollingImage.speed, "Horizontal scrolling"},
        {Visual.Animation.id, "Visual ID"},
        {Visual.Animation.layer, "Scene layer"},
        {Visual.Animation.size, "Size"},
        {Visual.Animation.spritesheetInfo, "Frame period (seconds)"},
        {Visual.Animation.framePeriodSeconds, "Looping"},
        {Visual.Animation.looping, "Spritesheet info"},
        {Trajectory.FixedTrajectory.id, "Trajectory ID"},
        {Trajectory.FixedTrajectory.functionX, "Trajectory function X"},
        {Trajectory.FixedTrajectory.functionY, "Trajectory function Y"},
        {Trajectory.PlayerControlledTrajectory.id, "Trajectory ID"},
        {Trajectory.PlayerControlledTrajectory.playerMovementSpeed, "Player movement speed (pix/s)"},
        {Entity.Projectile.id, "Entity ID"},
        {Entity.Projectile.evil, "Evil"},
        {Entity.Projectile.size, "Size"},
        {Entity.Projectile.spriteVisualId, "Sprite visual ID"},
        {Entity.Projectile.hitbox, "Hitbox"},
        {Entity.Projectile.defaultTrajectoryId, "Default trajectory ID"},
        {Entity.Projectile.deathSpawn, "Death spawn"},
        {Entity.Projectile.shots, "Shot"},
        {Entity.Ship.id, "Entity ID"},
        {Entity.Ship.evil, "Evil"},
        {Entity.Ship.size, "Size"},
        {Entity.Ship.spriteVisualId, "Sprite visual ID"},
        {Entity.Ship.hitbox, "Hitbox"},
        {Entity.Ship.defaultTrajectoryId, "Default trajectory ID"},
        {Entity.Ship.deathSpawn, "Death spawn"},
        {Entity.Ship.shots, "Shot"},
        {Entity.Ship.hp, "HP"},
        {Spawn.DisplaySpawn.id, "Visual ID"},
        {Spawn.DisplaySpawn.position, "Spawning position"},
        {Spawn.EntitySpawn.id, "Visual ID"},
        {Spawn.EntitySpawn.startingPosition, "Spawning position"},
        {Spawn.EntitySpawn.trajectory, "Trajectory ID"},
        {Hitbox.RectangleHitbox.size, "Size"},
        {Hitbox.CustomHitbox.fileName, "Texture file name"},
        {Hitbox.CustomHitbox.size, "Size"},
        {SpritesheetInfo.fileName, "File name"},
        {SpritesheetInfo.frameCount, "Frame count"},
        {SpritesheetInfo.frameSize, "Frame size"},
        {SpritesheetInfo.startingPosition, "Start position"},
        {SpritesheetInfo.stride, "Stride"},
        {Shot.shotPeriod, "Shot period"},
        {Shot.firstShotTime, "First shot time"},
        {Shot.spawn, "Spawns"},
    }).collect(Collectors.toMap(data -> (EditionData.Key) data[0], data -> (String) data[1]));

    public static String get(EditionData.Key key) {
        assert labelMap.containsKey(key) : "label not found: " + key.toString();
        return labelMap.get(key);
    }
}
