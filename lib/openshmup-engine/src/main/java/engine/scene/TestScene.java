package engine.scene;

import engine.Game;
import engine.entity.Entity;
import engine.graphics.AnimationInfo;
import engine.entity.PlayerShip;
import engine.scene.visual.ScrollingBackGround;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class TestScene extends LevelScene {
    public TestScene(Game game){
        super(game, new LevelTimeline(game.getEditorDataManager(), 30.0f));
    }

    @Override
    public void update() {
        super.update();

    }
}
