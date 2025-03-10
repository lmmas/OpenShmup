package engine;

import engine.render.Shader;
import engine.visual.SimpleSprite;
import engine.visual.StaticImage;
import engine.visual.Visual;
import entity.FixedTrajectory;
import entity.NonPlayerEntity;
import entity.PlayerShip;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class TestScene extends LevelScene {
    public TestScene(long window){
        super(window);
        SimpleSprite testSprite = new SimpleSprite("resources/textures/gunther-2.png",3);
        StaticImage testBackground = new StaticImage("resources/textures/gunther-2.png",0);
        testBackground.setPosition(0.5f, 0.5f);
        testBackground.setSize(1.0f,1.0f);
        addVisual(testBackground);
        FixedTrajectory testTraj = new FixedTrajectory(t-> 0.3f * (float) cos(t) + 0.5f, t-> 0.3f * (float) sin(t) + 0.5f, false);
        float currentTimeSeconds = (float)(System.currentTimeMillis() - startingTimeMillis)/ 1000.0f;
        NonPlayerEntity gunther = new NonPlayerEntity(testSprite, 0.5f,0.5f, testTraj, 0.5f, 0.5f,true, currentTimeSeconds);
        addEntity(gunther);
        this.playerShip = new PlayerShip();
        addVisual((Visual<?,?>)playerShip.getSprite());
    }
}
