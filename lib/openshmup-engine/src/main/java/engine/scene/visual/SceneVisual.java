package engine.scene.visual;

import engine.graphics.Graphic;
import engine.scene.Scene;

public interface SceneVisual{
    SceneVisual copy();
    Graphic<?,?>[] getGraphics();
    void setScene(Scene scene);
    void setPosition(float positionX, float positionY);
    void update(float currentTimeSeconds);
}
