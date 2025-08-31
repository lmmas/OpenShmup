package engine.scene.display;

import engine.graphics.Graphic;
import engine.render.RenderInfo;
import engine.assets.Texture;

import java.util.List;

public interface SceneVisual {
    SceneVisual copy();
    List<RenderInfo> getRenderInfos();
    List<Graphic<?, ?>> getGraphics();
    List<Texture> getTextures();
    boolean shouldBeRemoved();
    void setPosition(float positionX, float positionY);
    void setScale(float scaleX, float scaleY);
    void initDisplay(float startingTimeSeconds);
    void update(float currentTimeSeconds);

    static SceneVisual DEFAULT_EMPTY(){
        return EmptyVisual.getInstance();
    }
}
