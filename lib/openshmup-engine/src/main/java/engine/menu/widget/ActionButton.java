package engine.menu.widget;

import engine.Engine;
import engine.hitbox.Hitbox;
import engine.hitbox.HitboxClickListener;
import engine.input.InputStatesManager;
import engine.visual.Visual;
import engine.visual.VisualGroup;
import lombok.Getter;

import java.util.Map;

final public class ActionButton implements Widget {

    final private VisualGroup visualGroup;
    @Getter
    final private Visual background;

    final private HitboxClickListener hitboxClickListener;

    private Runnable onClick;

    public ActionButton(Map<Visual, Integer> visuals, Visual background, Hitbox clickHitbox, Runnable onClick) {
        assert visuals.containsKey(background) : "background not found among widget visuals";
        this.visualGroup = new VisualGroup(visuals);
        this.background = background;
        this.onClick = onClick;
        this.hitboxClickListener = new HitboxClickListener(clickHitbox);
    }

    @Override
    public void handleInputs() {
        InputStatesManager inputStatesManager = Engine.getInputStatesManager();

        if (hitboxClickListener.result(inputStatesManager.getLeftClickState(), inputStatesManager.getCursorPosition())) {
            onClick.run();
        }
    }
    @Override
    public VisualGroup getVisualGroup() {return this.visualGroup;}
}
