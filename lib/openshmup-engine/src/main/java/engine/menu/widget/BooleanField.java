package engine.menu.widget;

import engine.Engine;
import engine.hitbox.Hitbox;
import engine.hitbox.HitboxClickDetector;
import engine.hitbox.SimpleRectangleHitbox;
import engine.input.InputStatesManager;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.effects.ColorEffect;
import lombok.Getter;
import types.Vec2D;

import java.util.ArrayList;
import java.util.List;

final public class BooleanField implements Widget {

    @Getter
    private ArrayList<SceneVisual> visuals;

    private SceneVisual toggleVisual;

    final private ColorEffect invisibilityEffect;

    private boolean booleanVal;

    final private HitboxClickDetector hitboxClickDetector;

    public BooleanField(Vec2D size, Vec2D position, SceneVisual toggleVisual, List<SceneVisual> otherVisuals, boolean startingValue) {
        this.toggleVisual = toggleVisual;
        this.visuals = new ArrayList<>();
        this.visuals.addAll(otherVisuals);
        this.visuals.add(toggleVisual);
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
    public void handleInputs() {
        InputStatesManager inputStatesManager = Engine.getInputStatesManager();
        if (hitboxClickDetector.result(inputStatesManager.getLeftClickState(), inputStatesManager.getCursorPosition())) {
            setValue(!booleanVal);
        }
    }
}