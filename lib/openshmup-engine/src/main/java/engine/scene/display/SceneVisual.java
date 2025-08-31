package engine.scene.display;

import engine.graphics.Graphic;
import engine.render.RenderInfo;
import engine.assets.Texture;

import java.util.List;

abstract public class SceneVisual {
    private boolean visualShouldBeRemoved = false;

    abstract public SceneVisual copy();
    abstract public List<RenderInfo> getRenderInfos();
    abstract public List<Graphic<?, ?>> getGraphics();
    abstract public List<Texture> getTextures();
    public boolean shouldBeRemoved(){
        return visualShouldBeRemoved;
    }
    public void setShouldBeRemoved(){
        visualShouldBeRemoved = true;
    }
    abstract public void setPosition(float positionX, float positionY);
    abstract public void setScale(float scaleX, float scaleY);
    abstract public void initDisplay(float startingTimeSeconds);
    abstract public void update(float currentTimeSeconds);

    public static SceneVisual DEFAULT_EMPTY(){
        return EmptyVisual.getInstance();
    }
}
