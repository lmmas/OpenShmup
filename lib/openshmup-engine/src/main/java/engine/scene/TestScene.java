package engine.scene;

import engine.Engine;

public class TestScene extends LevelScene {
    public TestScene(Engine engine){
        super(engine, new LevelTimeline(engine.getEditorDataManager(), 30.0f), false);
    }

    @Override
    public void update() {
        super.update();

    }
}
