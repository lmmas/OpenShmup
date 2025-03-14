package engine.scene;

import engine.entity.FixedTrajectory;
import engine.entity.NonPlayerEntity;
import engine.graphics.Animation;
import engine.graphics.AnimationInfo;
import engine.entity.PlayerShip;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class TestScene extends LevelScene {
    NonPlayerEntity testEntity;
    public TestScene(long window){
        super(window);
        AnimationInfo testAnimInfo = new AnimationInfo("resources/textures/enemy-medium.png", 2, 32, 16, 0, 0, 32, 0);
        Animation testSprite = new Animation(this, 3, testAnimInfo, 0.25f, true);
        ScrollingBackGround background = new ScrollingBackGround("resources/textures/background.png", this, 1.0f, 1.0f, -0.1f, false);
        FixedTrajectory testTraj = new FixedTrajectory(t-> 0.3f * (float) cos(t) + 0.5f, t-> 0.3f * (float) sin(t) + 0.5f, false);
        this.testEntity = new NonPlayerEntity(this, testSprite, 0.5f,0.5f, testTraj, 0.5f, 0.5f,true);
        this.playerShip = new PlayerShip(this);
    }

    @Override
    public void update() {
        super.update();
        if(sceneTime > 5.00f){
            deleteEntity(testEntity);
        }
    }
}
