package engine.scene.visual;

import engine.assets.Texture;
import engine.graphics.Graphic;
import engine.graphics.image.ImageGraphic;
import engine.scene.visual.effects.ColorEffect;
import engine.types.RGBAValue;
import engine.types.Vec2D;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

abstract public class SceneVisual {

    private boolean visualShouldBeRemovedFlag;

    private boolean reloadGraphicsFlag;
    @Getter @Setter
    protected int sceneLayerIndex;
    @Getter final
    protected List<Graphic<?>> graphicsList;

    final protected List<Integer> graphicalSubLayers;

    final private int maxGraphicalSubLayer;

    final private List<ColorEffect> colorEffectList;

    protected RGBAValue colorCoefs;

    protected RGBAValue addedColor;

    public SceneVisual(int layer, List<Graphic<?>> graphicsList, List<Integer> graphicalSubLayers) {
        this.visualShouldBeRemovedFlag = false;
        this.reloadGraphicsFlag = false;
        this.sceneLayerIndex = layer;
        this.graphicsList = graphicsList;
        this.graphicalSubLayers = graphicalSubLayers;
        this.maxGraphicalSubLayer = graphicalSubLayers.stream().mapToInt(n -> n).max().orElse(0);
        this.colorEffectList = new ArrayList<>();
        this.colorCoefs = RGBAValue.ONE;
        this.addedColor = RGBAValue.ZERO;
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

    public void setScale(Vec2D scale) {
        this.graphicsList.forEach(g -> g.setScale(scale));
    }

    public void setPosition(Vec2D position) {
        this.graphicsList.forEach(g -> g.setPosition(position));
    }

    public void init() {

    }

    public void update() {

    }

    abstract public void updateGraphicsColor();

    public void addColorEffect(ColorEffect colorEffect) {
        colorEffectList.add(colorEffect);
        updateColorEffects();
    }

    public void clearColorEffects() {
        colorEffectList.clear();
        updateColorEffects();
    }

    private void updateColorEffects() {
        this.colorCoefs = RGBAValue.ONE;
        this.addedColor = RGBAValue.ZERO;
        for (var colorEffect : colorEffectList) {
            RGBAValue colorCoefsResult = this.colorCoefs.multiply(colorEffect.colorCoefs());
            RGBAValue addedColorResult = this.addedColor.multiply(colorEffect.colorCoefs()).add(colorEffect.addedColor());
            this.colorCoefs = colorCoefsResult;
            this.addedColor = addedColorResult;
        }
        updateGraphicsColor();
    }

}
