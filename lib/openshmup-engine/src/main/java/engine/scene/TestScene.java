package engine.scene;

import engine.Game;

public class TestScene extends LevelScene {
    public TestScene(Game game){
        super(game, new LevelTimeline(game.getEditorDataManager(), 30.0f), false);
    }

    @Override
    public void update() {
        super.update();

    }
}
