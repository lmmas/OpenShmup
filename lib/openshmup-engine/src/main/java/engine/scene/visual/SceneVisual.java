package engine.scene.visual;

import engine.assets.Texture;
import engine.graphics.Graphic;
import engine.graphics.image.ImageGraphic;
import engine.scene.visual.effects.ColorEffect;
import engine.types.RGBAValue;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

abstract public class SceneVisual {

    private boolean visualShouldBeRemovedFlag;

    private boolean reloadGraphicsFlag;
    @Getter @Setter
    protected int sceneLayerIndex;
    @Getter final protected List<Graphic<?>> graphicsList;

    final protected List<Integer> graphicalSubLayers;

    final private int maxGraphicalSubLayer;

    final private List<ColorEffect> colorEffectList;

    public SceneVisual(int layer, List<Graphic<?>> graphicsList, List<Integer> graphicalSubLayers) {
        this.visualShouldBeRemovedFlag = false;
        this.reloadGraphicsFlag = false;
        this.sceneLayerIndex = layer;
        this.graphicsList = graphicsList;
        this.graphicalSubLayers = graphicalSubLayers;
        this.maxGraphicalSubLayer = graphicalSubLayers.stream().mapToInt(n -> n).max().orElse(0);
        this.colorEffectList = new ArrayList<>();
    }

    abstract public SceneVisual copy();

    final public List<Integer> getGraphicalSubLayers() {
        return graphicalSubLayers;
    }

    final public int getMaxGraphicalSubLayer() {
        return maxGraphicalSubLayer;
    }

    final public List<Texture> getTextures() {
        List<Texture> textures = new ArrayList<>();
        for (var graphic : graphicsList) {
            if (graphic instanceof ImageGraphic imageGraphic) {
                textures.add(imageGraphic.getTexture());
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
        this.graphicsList.forEach(g -> g.setScale(scaleX, scaleY));
    }

    public void setPosition(float positionX, float positionY) {
        this.graphicsList.forEach(g -> g.setPosition(positionX, positionY));
    }

    public void initDisplay() {

    }

    public void update() {

    }

    abstract public void updateGraphicColor(RGBAValue colorCoefs, RGBAValue addedColor);

    public void addColorEffect(ColorEffect colorEffect) {
        colorEffectList.add(colorEffect);
        updateColorEffects();
    }

    public void clearColorEffects() {
        colorEffectList.clear();
        updateColorEffects();
    }

    private void updateColorEffects() {
        RGBAValue colorCoefs = new RGBAValue(1.0f, 1.0f, 1.0f, 1.0f);
        RGBAValue addedColor = new RGBAValue(0.0f, 0.0f, 0.0f, 0.0f);
        for (var colorEffect : colorEffectList) {
            RGBAValue colorCoefsResult = colorCoefs.multiply(colorEffect.colorCoefs());
            RGBAValue addedColorResult = addedColor.multiply(colorEffect.colorCoefs()).add(colorEffect.addedColor());
            colorCoefs = colorCoefsResult;
            addedColor = addedColorResult;
        }
        updateGraphicColor(colorCoefs, addedColor);
    }

    public static SceneVisual DEFAULT_EMPTY() {
        return EmptyVisual.getInstance();
    }
}
