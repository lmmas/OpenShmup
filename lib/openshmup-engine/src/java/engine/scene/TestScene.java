package engine.scene;

import engine.Game;
import engine.entity.Entity;
import engine.graphics.AnimationInfo;
import engine.entity.PlayerShip;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class TestScene extends LevelScene {
    public TestScene(Game game){
        super(game);

        AnimationInfo testAnimInfo = new AnimationInfo("Games/game1/textures/enemy-medium.png", 2, 32, 16, 0, 0, 32, 0);

        Entity testEntity = new Entity.Builder().setScene(this)
                .setStartingPosition(0.5f, 0.5f).setSize(0.5f,0.5f)
                .createSprite(3,"Games/game1/textures/troll-face-poster.png", false)
                .createFixedTrajectory(t-> 0.2f * (float) cos(2 * t) + 0.5f, t-> 0.2f * (float) sin(2 * t) + 0.5f, false)
                .build();
        this.playerShip = new PlayerShip(this);

        ScrollingBackGround background = new ScrollingBackGround("Games/game1/textures/background.png", this, 1.0f, 1.0f, -0.1f, false);
        addVisual(background);
    }

    @Override
    public void update() {
        super.update();

    }
}
