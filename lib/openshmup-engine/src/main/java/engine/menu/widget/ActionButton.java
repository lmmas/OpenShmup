package engine.menu.widget;

import engine.Engine;
import engine.hitbox.Hitbox;
import engine.hitbox.HitboxClickDetector;
import engine.input.InputStatesManager;
import engine.scene.visual.SceneVisual;
import layer.LayerMap;
import lombok.Getter;

import java.util.Map;

final public class ActionButton implements Widget {
    @Getter
    final private SceneVisual background;

    final private LayerMap<SceneVisual> visualLayers;

    final private HitboxClickDetector hitboxClickDetector;

    private Runnable onClick;

    public ActionButton(SceneVisual background, Map<SceneVisual, Integer> otherVisuals, Hitbox clickHitbox, Runnable onClick) {
        this.background = background;
        this.onClick = onClick;
        this.visualLayers = new LayerMap<>(otherVisuals);
        this.visualLayers.add(background, 0);
        this.hitboxClickDetector = new HitboxClickDetector(clickHitbox);
    }

    @Override
    public LayerMap<SceneVisual> getVisualLayers() {
        return visualLayers;
    }

    @Override
    public void handleInputs() {
        InputStatesManager inputStatesManager = Engine.getInputStatesManager();

        if (hitboxClickDetector.result(inputStatesManager.getLeftClickState(), inputStatesManager.getCursorPosition())) {
            onClick.run();
        }
    }
}
