package engine.entity;

import engine.entity.hitbox.Hitbox;
import engine.entity.shot.EntityShot;
import engine.entity.sprite.EntitySprite;
import engine.entity.trajectory.Trajectory;
import engine.scene.spawnable.Spawnable;

public class NonShipEntity extends Entity {

    public NonShipEntity(float startingPosX, float startingPosY, float sizeX, float sizeY, float orientationRadians, boolean evil, int entityId, EntitySprite sprite, Trajectory trajectory, Hitbox hitbox, EntityShot shot, Spawnable deathSpawn) {
        super(startingPosX, startingPosY, sizeX, sizeY, orientationRadians, evil, entityId, sprite, trajectory, hitbox, shot, deathSpawn);
    }

    @Override
    public Entity copy() {
        return new NonShipEntity(trajectoryStartingPosition.x, trajectoryStartingPosition.y, size.x , size.y, orientationRadians, evil, entityId, sprite.copy(), trajectory.copyIfNotReusable(), hitbox.copy(), shot.copyIfNotReusable(), deathSpawn.copy());
    }

    @Override
    public EntityType getType() {
        return EntityType.PROJECTILE;
    }
}
