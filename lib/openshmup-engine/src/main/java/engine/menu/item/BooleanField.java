package engine.menu.item;

import engine.Engine;
import engine.InputStatesManager;
import engine.hitbox.Hitbox;
import engine.hitbox.HitboxClickDetector;
import engine.hitbox.SimpleRectangleHitbox;
import engine.scene.visual.SceneVisual;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class BooleanField {

    @Getter
    private ArrayList<SceneVisual> visuals;

    private SceneVisual toggleVisual;

    private boolean booleanVal;

    final private HitboxClickDetector hitboxClickDetector;

    public BooleanField(float sizeX, float sizeY, float positionX, float positionY, SceneVisual toggleVisual, List<SceneVisual> otherVisuals) {
        this.toggleVisual = toggleVisual;
        this.visuals = new ArrayList<>();
        this.visuals.addAll(otherVisuals);
        Hitbox hitbox = new SimpleRectangleHitbox(positionX, positionY, sizeX, sizeY);
        this.hitboxClickDetector = new HitboxClickDetector(hitbox);
    }

    public boolean getBooleanVal() {
        return booleanVal;
    }

    public void handleInputs() {
        InputStatesManager inputStatesManager = Engine.getInputStatesManager();
        if (hitboxClickDetector.result(inputStatesManager.getLeftClickState(), inputStatesManager.getCursorPosition())) {
            booleanVal = !booleanVal;
            if (booleanVal) {
                visuals.add(toggleVisual);
            }
            else {
                visuals.remove(toggleVisual);
            }
        }
    }
}