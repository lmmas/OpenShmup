package engine.entity;

import engine.entity.hitbox.SimpleHitBox;
import engine.entity.trajectory.Trajectory;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;

public class Ship extends Entity{
    private int hitPoints;

    public Ship(LevelScene scene, float startingPosX, float startingPosY, float sizeX, float sizeY, float orientationRadians, boolean evil, EntitySprite sprite, Trajectory trajectory, SimpleHitBox hitbox, Spawnable deathSpawn, int hitPoints) {
        super(scene, EntityType.SHIP, startingPosX, startingPosY, sizeX, sizeY, orientationRadians, evil, sprite, trajectory, hitbox, deathSpawn);
        this.hitPoints = hitPoints;
    }


    public void takeDamage(int damageValue){
        hitPoints -= damageValue;
    }

    public boolean isDead(){
        return hitPoints <= 0;
    }

    public int getHP() {
        return hitPoints;
    }
}
