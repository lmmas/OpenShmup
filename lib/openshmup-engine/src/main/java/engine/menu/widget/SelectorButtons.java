package engine.menu.widget;

import engine.hitbox.Hitbox;
import engine.scene.visual.SceneVisual;
import lombok.Getter;
import lombok.Setter;
import types.LayerMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

final public class SelectorButtons implements Widget {

    @Getter
    final private List<ActionButton> actionButtons;

    final private LayerMap<SceneVisual> visualLayers;
    @Getter
    @Setter
    private Integer selectedValue;

    final private BiConsumer<SelectorButtons, Integer> onChange;

    public SelectorButtons(List<SceneVisual> buttonBackgrounds, List<Map<SceneVisual, Integer>> otherButtonVisuals, List<Hitbox> hitboxes, BiConsumer<SelectorButtons, Integer> onChange, Integer startingValue) {
        this.onChange = onChange;
        this.selectedValue = startingValue;
        assert otherButtonVisuals.size() == hitboxes.size() : "list size mismatch";
        int buttonCount = otherButtonVisuals.size();
        ArrayList<Runnable> onClicks = new ArrayList<>(buttonCount);
        for (int i = 0; i < buttonCount; i++) {
            final int buttonValue = i;
            onClicks.add(() -> {
                if (selectedValue == null || buttonValue != selectedValue) {
                    this.onChange.accept(this, buttonValue);
                }
                selectedValue = buttonValue;
            });
        }
        this.actionButtons = new ArrayList<>(buttonCount);
        this.visualLayers = new LayerMap<>();
        for (int i = 0; i < buttonCount; i++) {
            this.actionButtons.add(new ActionButton(buttonBackgrounds.get(i), otherButtonVisuals.get(i), hitboxes.get(i), onClicks.get(i)));
            this.visualLayers.add(buttonBackgrounds.get(i), 0);
            otherButtonVisuals.get(i).forEach(visualLayers::add);
        }
    }

    @Override
    public LayerMap<SceneVisual> getVisualLayers() {
        return visualLayers;
    }

    @Override
    public void handleInputs() {
        actionButtons.forEach(ActionButton::handleInputs);
    }
}
