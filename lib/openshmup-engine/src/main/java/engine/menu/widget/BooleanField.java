package engine.menu.widget;

import engine.Engine;
import engine.hitbox.Hitbox;
import engine.hitbox.HitboxClickDetector;
import engine.hitbox.SimpleRectangleHitbox;
import engine.input.InputStatesManager;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.effects.ColorEffect;
import types.LayerMap;
import types.Vec2D;

import java.util.Map;

final public class BooleanField implements Widget {

    private LayerMap<SceneVisual> visualLayers;

    private SceneVisual toggleVisual;

    final private ColorEffect invisibilityEffect;

    private boolean booleanVal;

    final private HitboxClickDetector hitboxClickDetector;

    public BooleanField(Vec2D size, Vec2D position, SceneVisual toggleVisual, Map<SceneVisual, Integer> otherVisuals, boolean startingValue) {
        this.toggleVisual = toggleVisual;
        this.visualLayers = new LayerMap<>(otherVisuals);
        this.visualLayers.add(toggleVisual, 1);
        this.invisibilityEffect = ColorEffect.Invisibility();
        Hitbox hitbox = new SimpleRectangleHitbox(position, size);
        this.hitboxClickDetector = new HitboxClickDetector(hitbox);
        this.booleanVal = startingValue;
        if (!booleanVal) {
            toggleVisual.addColorEffect(invisibilityEffect);
        }
    }

    public boolean getBooleanValue() {
        return booleanVal;
    }

    public void setValue(boolean value) {
        if (this.booleanVal != value) {
            this.booleanVal = value;
            if (booleanVal) {
                toggleVisual.clearColorEffects();
            }
            else {
                toggleVisual.addColorEffect(invisibilityEffect);
            }
        }
    }

    @Override
    public LayerMap<SceneVisual> getVisualLayers() {
        return visualLayers;
    }

    @Override
    public void handleInputs() {
        InputStatesManager inputStatesManager = Engine.getInputStatesManager();
        if (hitboxClickDetector.result(inputStatesManager.getLeftClickState(), inputStatesManager.getCursorPosition())) {
            setValue(!booleanVal);
        }
    }
}