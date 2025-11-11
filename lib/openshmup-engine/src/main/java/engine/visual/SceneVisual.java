package engine.visual;

import engine.graphics.Graphic;
import engine.graphics.RenderInfo;
import engine.assets.Texture;

import java.util.List;

abstract public class SceneVisual {
    private boolean visualShouldBeRemovedFlag = false;
    private boolean reloadGraphicsFlag = false;
    protected int sceneLayer;

    public SceneVisual(int layer){
        this.sceneLayer = layer;
    }

    abstract public SceneVisual copy();
    abstract public List<Graphic<?, ?>> getGraphics();
    abstract public List<Integer> getGraphicalSubLayers();
    abstract public List<Texture> getTextures();

    final public boolean shouldBeRemoved(){
        return visualShouldBeRemovedFlag;
    }

    final public void setShouldBeRemoved(){
        visualShouldBeRemovedFlag = true;
    }

    final public boolean getReloadGraphicsFlag(){
        return reloadGraphicsFlag;
    }

    final public void setReloadGraphicsFlag(boolean reloadGraphics){
        this.reloadGraphicsFlag = reloadGraphics;
    }

    public int getSceneLayer() {
        return sceneLayer;
    }

    public void setSceneLayer(int sceneLayer) {
        this.sceneLayer = sceneLayer;
    }

    abstract public void setPosition(float positionX, float positionY);
    abstract public void setScale(float scaleX, float scaleY);
    abstract public void initDisplay(float startingTimeSeconds);
    abstract public void update(float currentTimeSeconds);

    public static SceneVisual DEFAULT_EMPTY(){
        return EmptyVisual.getInstance();
    }
}
