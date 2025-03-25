package engine.scene.display;

import engine.graphics.Graphic;
import engine.scene.Scene;

public interface SceneDisplay {
    SceneDisplay copy();
    Graphic<?,?>[] getGraphics();
    void setScene(Scene scene);
    void setPosition(float positionX, float positionY);
    void update(float currentTimeSeconds);
    boolean shouldBeRemoved();
}
