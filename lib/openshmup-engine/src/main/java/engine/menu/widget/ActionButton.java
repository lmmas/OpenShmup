package engine.menu.widget;

import engine.Engine;
import engine.hitbox.Hitbox;
import engine.hitbox.HitboxClickDetector;
import engine.input.InputStatesManager;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.style.VisualGroup;
import lombok.Getter;

import java.util.Map;

final public class ActionButton implements Widget {

    final private VisualGroup visualGroup;
    @Getter
    final private SceneVisual background;

    final private HitboxClickDetector hitboxClickDetector;

    private Runnable onClick;

    public ActionButton(Map<SceneVisual, Integer> visuals, SceneVisual background, Hitbox clickHitbox, Runnable onClick) {
        assert visuals.containsKey(background) : "background not found among widget visuals";
        this.visualGroup = new VisualGroup(visuals);
        this.background = background;
        this.onClick = onClick;
        this.hitboxClickDetector = new HitboxClickDetector(clickHitbox);
    }

    @Override
    public void handleInputs() {
        InputStatesManager inputStatesManager = Engine.getInputStatesManager();

        if (hitboxClickDetector.result(inputStatesManager.getLeftClickState(), inputStatesManager.getCursorPosition())) {
            onClick.run();
        }
    }
    @Override
    public VisualGroup getVisualGroup() {return this.visualGroup;}
}
