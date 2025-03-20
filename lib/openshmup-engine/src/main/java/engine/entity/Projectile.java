package engine.entity;

import engine.entity.hitbox.EntitySprite;
import engine.entity.hitbox.SimpleHitBox;
import engine.entity.trajectory.Trajectory;
import engine.scene.LevelScene;

public class Projectile extends Entity {

    public Projectile(LevelScene scene, float startingPosX, float startingPosY, float sizeX, float sizeY, float orientationRadians, boolean evil, EntitySprite sprite, Trajectory trajectory, SimpleHitBox hitbox) {
        super(scene, EntityType.PROJECTILE, startingPosX, startingPosY, sizeX, sizeY, orientationRadians, evil, sprite, trajectory, hitbox);
    }
}
