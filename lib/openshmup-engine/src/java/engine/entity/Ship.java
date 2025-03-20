package engine.entity;

import engine.entity.hitbox.EntitySprite;
import engine.entity.hitbox.SimpleHitBox;
import engine.entity.trajectory.Trajectory;
import engine.scene.LevelScene;

public class Ship extends Entity{
    private int hitPoints;

    public Ship(LevelScene scene, float startingPosX, float startingPosY, float sizeX, float sizeY, float orientationRadians, boolean evil, EntitySprite sprite, Trajectory trajectory, SimpleHitBox hitbox, int hitPoints) {
        super(scene, EntityType.SHIP, startingPosX, startingPosY, sizeX, sizeY, orientationRadians, evil, sprite, trajectory, hitbox);
        this.hitPoints = hitPoints;
    }


    public void takeDamage(int damageValue){
        hitPoints -= damageValue;
    }

    public boolean isDead(){
        return hitPoints <= 0;
    }

    public void deathEvent(){
    }
}
