package engine.menu.widget;

import engine.Engine;
import engine.hitbox.HitboxClickDetector;
import engine.hitbox.SimpleRectangleHitbox;
import engine.input.InputStatesManager;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.effects.ColorEffect;
import engine.scene.visual.style.VisualGroup;
import types.Vec2D;

import java.util.Map;

final public class BooleanField implements Widget {

    private final VisualGroup visualGroup;

    private SceneVisual toggleVisual;

    final private ColorEffect invisibilityEffect;

    private boolean booleanVal;

    final private HitboxClickDetector hitboxClickDetector;

    public BooleanField(Vec2D size, Vec2D position, Map<SceneVisual, Integer> visuals, SceneVisual toggleVisual, boolean startingValue) {
        assert visuals.containsKey(toggleVisual) : "toggle visual not found among widget visuals";
        this.toggleVisual = toggleVisual;
        this.visualGroup = new VisualGroup(visuals);
        this.invisibilityEffect = ColorEffect.Invisibility();
        this.hitboxClickDetector = new HitboxClickDetector(new SimpleRectangleHitbox(position, size));
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
    public VisualGroup getVisualGroup() {
        return visualGroup;
    }

    @Override
    public void handleInputs() {
        InputStatesManager inputStatesManager = Engine.getInputStatesManager();
        if (hitboxClickDetector.result(inputStatesManager.getLeftClickState(), inputStatesManager.getCursorPosition())) {
            setValue(!booleanVal);
        }
    }
}