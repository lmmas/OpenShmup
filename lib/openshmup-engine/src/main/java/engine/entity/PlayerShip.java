package engine.entity;

import engine.GlobalVars;
import engine.entity.hitbox.EntitySprite;
import engine.entity.hitbox.SimpleHitBox;
import engine.entity.trajectory.PlayerControlledTrajectory;
import engine.entity.trajectory.Trajectory;
import engine.graphics.MovingImage;
import engine.scene.GameControl;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;

import static org.lwjgl.glfw.GLFW.*;

final public class PlayerShip extends ShootingShip{

    public PlayerShip(LevelScene scene, float startingPosX, float startingPosY, float sizeX, float sizeY, float orientationRadians, EntitySprite sprite, SimpleHitBox hitBox, Spawnable deathSpawn, int hitPoints, Spawnable shot, float shotPeriodSeconds, float timeBeforeFirstShotSeconds) {
        super(scene, startingPosX, startingPosY, sizeX, sizeY, orientationRadians, false, sprite, new PlayerControlledTrajectory(scene, GlobalVars.playerSpeed), hitBox, deathSpawn, hitPoints, shot, shotPeriodSeconds, timeBeforeFirstShotSeconds);
    }

    @Override
    public void updateShot() {
        if(scene.getControlState(GameControl.FIRE)){
            if(lifetimeSeconds >= nextShotTimeSeconds) {
                shot.copyWithOffset(position.x, position.y).spawn(scene);
                nextShotTimeSeconds = lifetimeSeconds + shotPeriodSeconds;
            }
        }
    }
}
