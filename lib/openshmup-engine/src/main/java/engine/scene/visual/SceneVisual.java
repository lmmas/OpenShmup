package engine.scene.visual;

import engine.graphics.Graphic;
import engine.render.RenderInfo;
import engine.assets.Texture;

import java.util.List;

abstract public class SceneVisual {
    private boolean visualShouldBeRemovedFlag = false;
    private boolean reloadGraphicsFlag = false;

    abstract public SceneVisual copy();
    abstract public List<RenderInfo> getRenderInfos();
    abstract public List<Graphic<?, ?>> getGraphics();
    abstract public List<Texture> getTextures();
    public boolean shouldBeRemoved(){
        return visualShouldBeRemovedFlag;
    }
    public void setShouldBeRemoved(){
        visualShouldBeRemovedFlag = true;
    }

    public boolean getReloadGraphicsFlag(){
        return reloadGraphicsFlag;
    }

    public void setReloadGraphicsFlag(boolean reloadGraphics){
        this.reloadGraphicsFlag = reloadGraphics;
    }

    abstract public void setPosition(float positionX, float positionY);
    abstract public void setScale(float scaleX, float scaleY);
    abstract public void initDisplay(float startingTimeSeconds);
    abstract public void update(float currentTimeSeconds);

    public static SceneVisual DEFAULT_EMPTY(){
        return EmptyVisual.getInstance();
    }
}
