package engine.scene.visual;

import engine.graphics.Graphic;

public interface SceneVisual{
    Graphic<?,?>[] getGraphics();
    void setPosition(float positionX, float positionY);
    void update();
    void delete();
}
