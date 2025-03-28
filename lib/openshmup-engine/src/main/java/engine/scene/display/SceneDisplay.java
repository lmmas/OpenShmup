package engine.scene.display;

import engine.graphics.Graphic;
import engine.render.RenderInfo;
import engine.render.Texture;
import engine.scene.Scene;

import java.util.Optional;

public interface SceneDisplay {
    SceneDisplay copy();
    Optional<RenderInfo> getRenderInfo();
    Graphic<?,?>[] getGraphics();
    Optional<Texture> getTexture();
    void setScene(Scene scene);
    void setPosition(float positionX, float positionY);
    void update(float currentTimeSeconds);
    boolean shouldBeRemoved();
}
