package engine.menu.item;

import engine.hitbox.Hitbox;
import engine.scene.visual.SceneVisual;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

final public class SelectorButtons implements MenuItem {

    final private List<ActionButton> actionButtons;

    final private List<SceneVisual> visuals;
    @Getter
    private int selectedValue;

    final private Consumer<Integer> onChange;

    public SelectorButtons(List<List<SceneVisual>> buttonVisuals, List<Hitbox> hitboxes, Consumer<Integer> onChange) {
        this.onChange = onChange;
        this.selectedValue = 0;
        assert buttonVisuals.size() == hitboxes.size() : "list size mismatch";
        int buttonCount = buttonVisuals.size();
        ArrayList<Runnable> onClicks = new ArrayList<>(buttonCount);
        for (int i = 0; i < buttonCount; i++) {
            final int buttonValue = i;
            onClicks.add(() -> {
                boolean valueChanged = selectedValue != buttonValue;
                selectedValue = buttonValue;
                if (valueChanged) {
                    this.onChange.accept(selectedValue);
                }
            });
        }
        this.actionButtons = new ArrayList<>(buttonCount);
        this.visuals = new ArrayList<>();
        for (int i = 0; i < buttonCount; i++) {
            this.actionButtons.add(new ActionButton(buttonVisuals.get(i), hitboxes.get(i), onClicks.get(i)));
            this.visuals.addAll(buttonVisuals.get(i));
        }
    }

    @Override public List<SceneVisual> getVisuals() {
        return visuals;
    }

    @Override public void handleInputs() {
        actionButtons.forEach(ActionButton::handleInputs);
    }
}
