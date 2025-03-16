package engine.scene;

import engine.Game;
import engine.entity.trajectory.FixedTrajectory;
import engine.entity.Entity;
import engine.graphics.Animation;
import engine.graphics.AnimationInfo;
import engine.entity.PlayerShip;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class TestScene extends LevelScene {
    public TestScene(Game game){
        super(game);

        AnimationInfo testAnimInfo = new AnimationInfo("resources/textures/enemy-medium.png", 2, 32, 16, 0, 0, 32, 0);

        Entity testEntity = new Entity.Builder().setScene(this)
                .setStartingPosition(0.5f, 0.5f).setSize(0.5f,0.5f)
                .createSprite(3,testAnimInfo, 0.25f, true, false)
                .createFixedTrajectory(t-> 0.3f * (float) cos(t) + 0.5f, t-> 0.3f * (float) sin(t) + 0.5f, false)
                .build();
        this.playerShip = new PlayerShip(this);

        ScrollingBackGround background = new ScrollingBackGround("resources/textures/background.png", this, 1.0f, 1.0f, -0.1f, false);
        addVisual(background);
    }

    @Override
    public void update() {
        super.update();
        if(sceneTime > 5.00f){
            for(Entity entity: evilEntities){
                deleteEntity(entity);
            }
        }
    }
}
