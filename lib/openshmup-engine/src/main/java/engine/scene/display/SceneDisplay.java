package engine.scene.display;

import engine.graphics.Graphic;
import engine.render.RenderInfo;
import engine.assets.Texture;

import java.util.List;
import java.util.Optional;

public interface SceneDisplay {
    SceneDisplay copy();
    RenderInfo getRenderInfo();
    List<Graphic<?, ?>> getGraphics();
    Optional<Texture> getTexture();
    void initDisplay(float startingTimeSeconds);
    void setPosition(float positionX, float positionY);
    void update(float currentTimeSeconds);
    boolean shouldBeRemoved();
}
