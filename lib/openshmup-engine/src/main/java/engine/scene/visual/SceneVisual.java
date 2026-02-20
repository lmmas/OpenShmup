package engine.scene.visual;

import engine.assets.Texture;
import engine.graphics.Graphic;
import engine.graphics.image.Image;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

abstract public class SceneVisual {

    private boolean visualShouldBeRemovedFlag = false;

    private boolean reloadGraphicsFlag = false;
    @Getter @Setter
    protected int sceneLayerIndex;

    final protected List<Integer> graphicalSubLayers;

    final private int maxGraphicalSubLayer;

    public SceneVisual(int layer, List<Integer> graphicalSubLayers) {
        this.sceneLayerIndex = layer;
        this.graphicalSubLayers = graphicalSubLayers;
        this.maxGraphicalSubLayer = graphicalSubLayers.stream().mapToInt(n -> n).max().orElse(0);
    }

    abstract public SceneVisual copy();

    abstract public List<Graphic<?>> getGraphics();

    final public List<Integer> getGraphicalSubLayers() {
        return graphicalSubLayers;
    }

    final public int getMaxGraphicalSubLayer() {
        return maxGraphicalSubLayer;
    }

    final public List<Texture> getTextures() {
        var graphics = this.getGraphics();
        List<Texture> textures = new ArrayList<>();
        for (var graphic : graphics) {
            if (graphic instanceof Image image) {
                textures.add(image.getTexture());
            }
        }
        return textures;
    }

    final public boolean getShouldBeRemoved() {
        return visualShouldBeRemovedFlag;
    }

    final public void setShouldBeRemoved() {
        visualShouldBeRemovedFlag = true;
    }

    final public boolean getReloadGraphicsFlag() {
        return reloadGraphicsFlag;
    }

    final public void setReloadGraphicsFlag(boolean reloadGraphics) {
        this.reloadGraphicsFlag = reloadGraphics;
    }

    public void setScale(float scaleX, float scaleY) {
        this.getGraphics().forEach(g -> g.setScale(scaleX, scaleY));
    }

    public void setPosition(float positionX, float positionY) {
        this.getGraphics().forEach(g -> g.setPosition(positionX, positionY));
    }

    public void initDisplay() {

    }

    public void update() {

    }

    public static SceneVisual DEFAULT_EMPTY() {
        return EmptyVisual.getInstance();
    }
}
