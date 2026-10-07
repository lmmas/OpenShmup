package engine.menu.widget;

import engine.hitbox.Hitbox;
import engine.visual.Visual;
import engine.visual.VisualGroup;
import layer.LayerMap;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

final public class SelectorButtons implements Widget {

    @Getter
    final private List<ActionButton> actionButtons;

    final private VisualGroup visualGroup;

    @Getter
    @Setter
    private Integer selectedValue;

    final private BiConsumer<SelectorButtons, Integer> onChange;

    public SelectorButtons(List<Map<Visual, Integer>> buttonVisuals, List<Visual> buttonBackgrounds, List<Hitbox> hitboxes, BiConsumer<SelectorButtons, Integer> onChange, Integer startingValue) {
        this.onChange = onChange;
        this.selectedValue = startingValue;
        assert buttonVisuals.size() == hitboxes.size() : "list size mismatch";
        int buttonCount = buttonVisuals.size();
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
        LayerMap<Visual> visuals = new LayerMap<>();
        for (int i = 0; i < buttonCount; i++) {
            this.actionButtons.add(new ActionButton(buttonVisuals.get(i), buttonBackgrounds.get(i), hitboxes.get(i), onClicks.get(i)));
            buttonVisuals.get(i).forEach(visuals::add);
        }
        this.visualGroup = new VisualGroup(visuals);
    }

    @Override
    public VisualGroup getVisualGroup() {
        return visualGroup;
    }

    @Override
    public void handleInputs() {
        actionButtons.forEach(ActionButton::handleInputs);
    }
}
