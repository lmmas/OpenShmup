package engine.menu.widget;

import engine.Engine;
import engine.hitbox.Hitbox;
import engine.hitbox.HitboxClickDetector;
import engine.input.InputStatesManager;
import engine.scene.visual.SceneVisual;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

final public class ActionButton implements Widget {
    @Getter
    final private SceneVisual background;
    @Getter
    final private List<SceneVisual> otherVisuals;

    final private HitboxClickDetector hitboxClickDetector;

    private Runnable onClick;

    public ActionButton(SceneVisual background, List<SceneVisual> otherVisuals, Hitbox clickHitbox, Runnable onClick) {
        this.background = background;
        this.onClick = onClick;
        this.otherVisuals = otherVisuals;
        this.hitboxClickDetector = new HitboxClickDetector(clickHitbox);
    }

    @Override
    public List<SceneVisual> getVisuals() {
        ArrayList<SceneVisual> visuals = new ArrayList<>(otherVisuals.size() + 1);
        visuals.add(background);
        visuals.addAll(otherVisuals);
        return visuals;
    }

    @Override
    public void handleInputs() {
        InputStatesManager inputStatesManager = Engine.getInputStatesManager();

        if (hitboxClickDetector.result(inputStatesManager.getLeftClickState(), inputStatesManager.getCursorPosition())) {
            onClick.run();
        }
    }
}
