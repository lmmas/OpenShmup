package engine.visual;

import engine.assets.Texture;
import engine.graphics.Graphic;
import engine.graphics.ScissorBox;
import engine.graphics.image.ImageGraphic;
import engine.visual.effects.ColorEffect;
import layer.LayerEntry;
import layer.LayerMap;
import lombok.Getter;
import lombok.Setter;
import types.RGBAValue;
import types.Vec2D;

import java.util.ArrayList;
import java.util.List;

abstract public class Visual {
    @Setter
    @Getter
    private Vec2D scale;
    @Setter
    @Getter
    private Vec2D position;
    @Getter
    final protected LayerMap<Graphic<?>> graphicalLayers;

    private boolean visualShouldBeRemovedFlag;
    @Getter
    final protected ArrayList<LayerEntry<Graphic<?>>> graphicsToAdd;
    @Getter
    final protected ArrayList<Graphic<?>> graphicsToRemove;

    final private List<ColorEffect> colorEffectList;

    protected RGBAValue colorCoefs;

    protected RGBAValue addedColor;

    public Visual(Vec2D scale, Vec2D position) {
        this.scale = scale;
        this.position = position;
        this.visualShouldBeRemovedFlag = false;
        this.graphicalLayers = new LayerMap<>();
        this.graphicsToAdd = new ArrayList<>();
        this.graphicsToRemove = new ArrayList<>();
        this.colorEffectList = new ArrayList<>();
        this.colorCoefs = RGBAValue.ONE;
        this.addedColor = RGBAValue.ZERO;
    }

    abstract public Visual copy();

    final public List<Texture> getTextures() {
        List<Texture> textures = new ArrayList<>();
        graphicalLayers.forEachObject(graphic -> {
            if (graphic instanceof ImageGraphic imageGraphic) {
                textures.add(imageGraphic.getTexture());
            }
        });
        return textures;
    }

    final public boolean getShouldBeRemoved() {
        return visualShouldBeRemovedFlag;
    }

    final public void setToRemove(boolean value) {
        visualShouldBeRemovedFlag = value;
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

    public void setScissorBox(ScissorBox scissorBox) {
        graphicalLayers.forEachObject((graphic) -> graphic.setScissorBox(scissorBox));
    }

}
