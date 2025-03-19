package engine.scene;

import engine.graphics.Graphic;

public interface SceneVisual {
    Graphic<?,?>[] getGraphics();
    void update();
    void delete();
}
