package engine.scene;

import engine.entity.FixedTrajectory;
import engine.entity.NonPlayerEntity;
import engine.graphics.SimpleSprite;
import engine.entity.PlayerShip;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class TestScene extends LevelScene {
    public TestScene(long window){
        super(window);

        SimpleSprite testSprite = new SimpleSprite("resources/textures/trollface_PNG15.png", this, 3);
        ScrollingBackGround background = new ScrollingBackGround("resources/textures/background.png", this, 1.0f, 1.0f, -0.1f, false);
        FixedTrajectory testTraj = new FixedTrajectory(t-> 0.3f * (float) cos(t) + 0.5f, t-> 0.3f * (float) sin(t) + 0.5f, false);
        float currentTimeSeconds = (float)(System.currentTimeMillis() - startingTimeMilis)/ 1000.0f;
        NonPlayerEntity trollface = new NonPlayerEntity(this, testSprite, 0.5f,0.5f, testTraj, 0.5f, 0.5f,true, currentTimeSeconds);

        this.playerShip = new PlayerShip(this);
    }
}
