package engine.scene;

import engine.graphics.Graphic;

import java.util.ArrayList;

public interface SceneVisual {
    Graphic<?,?>[] getGraphics();
    void update();
    void delete();
}
