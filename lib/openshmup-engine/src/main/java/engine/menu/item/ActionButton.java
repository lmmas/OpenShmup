package engine.menu.item;

import engine.Engine;
import engine.InputStatesManager;
import engine.hitbox.Hitbox;
import engine.hitbox.HitboxClickDetector;
import engine.scene.visual.SceneVisual;
import lombok.Getter;

import java.util.List;

public class ActionButton implements MenuItem {

    @Getter final private List<SceneVisual> visuals;

    final private HitboxClickDetector hitboxClickDetector;

    private Runnable onClick;

    public ActionButton(List<SceneVisual> visuals, Hitbox clickHitbox, Runnable onClick) {
        this.onClick = onClick;
        this.visuals = visuals;
        this.hitboxClickDetector = new HitboxClickDetector(clickHitbox);
    }

    @Override
    public void handleInputs() {
        InputStatesManager inputStatesManager = Engine.getInputStatesManager();

        if (hitboxClickDetector.result(inputStatesManager.getLeftClickState(), inputStatesManager.getCursorPosition())) {
            onClick.run();
        }
    }
}
