package engine.menu.item;

import engine.Engine;
import engine.InputStatesManager;
import engine.hitbox.Hitbox;
import engine.hitbox.HitboxClickDetector;
import engine.hitbox.SimpleRectangleHitbox;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.effects.ColorEffect;
import engine.types.Vec2D;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class BooleanField implements MenuItem {

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
        Hitbox hitbox = new SimpleRectangleHitbox(position.x, position.y, size.x, size.y);
        this.hitboxClickDetector = new HitboxClickDetector(hitbox);
        this.booleanVal = startingValue;
        if (!booleanVal) {
            toggleVisual.addColorEffect(invisibilityEffect);
        }
    }

    public boolean getBooleanVal() {
        return booleanVal;
    }

    @Override
    public void handleInputs() {
        InputStatesManager inputStatesManager = Engine.getInputStatesManager();
        if (hitboxClickDetector.result(inputStatesManager.getLeftClickState(), inputStatesManager.getCursorPosition())) {
            booleanVal = !booleanVal;
            if (booleanVal) {
                toggleVisual.clearColorEffects();
            }
            else {
                toggleVisual.addColorEffect(invisibilityEffect);
            }
        }
    }
}