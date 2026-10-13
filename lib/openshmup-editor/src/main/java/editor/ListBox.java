package editor;

import engine.Engine;
import engine.hitbox.RectangleBox;
import engine.input.InputStatesManager;
import engine.menu.widget.ActionButton;
import engine.menu.widget.Widget;
import engine.menu.widget.Widgets;
import engine.visual.Visual;
import engine.visual.VisualGroup;
import layer.LayerMap;
import lombok.Getter;
import types.Vec2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ListBox implements Widget {
    final static private float elementHeight = 50f;

    private final VisualGroup visualGroup;

    private final ArrayList<ActionButton> elementList;

    private final RectangleBox rectangleBox;
    @Getter
    private Integer selectedIndex;

    public ListBox(Vec2D size, Vec2D topPosition, List<String> labelsList) {
        Vec2D centerPosition = new Vec2D(topPosition.x, topPosition.y - (float) (labelsList.size()) / 2 * elementHeight);
        this.rectangleBox = new RectangleBox(centerPosition, size);
        this.elementList = new ArrayList<>(labelsList.size());
        float topElementY = topPosition.y - (elementHeight / 2);
        LayerMap<Visual> visualLayers = new LayerMap<>();
        labelsList.forEach(label -> {
            int elementIndex = elementList.size();
            Vec2D elementPosition = new Vec2D(rectangleBox.getPosition().x, topElementY - elementIndex * elementHeight);
            Vec2D elementSize = new Vec2D(rectangleBox.getSize().x, elementHeight);
            Runnable onClick = () -> setSelectedIndex(elementIndex);
            ActionButton element = Widgets.TextButton(elementSize, elementPosition, Style.listElementUnselected, Style.Text.menuButtonLabelStyle, label, onClick);
            elementList.add(element);
            visualLayers.add(element.getVisualGroup(), 0);
        });
        this.visualGroup = new VisualGroup(visualLayers);
        this.selectedIndex = null;
    }

    @Override
    public VisualGroup getVisualGroup() {
        return visualGroup;
    }

    @Override
    public void handleInputs() {
        InputStatesManager inputStatesManager = Engine.getInputStatesManager();
        if (!rectangleBox.containsPoint(inputStatesManager.getCursorPosition())) {
            return;
        }
        elementList.forEach(ActionButton::handleInputs);
    }

    public void setSelectedIndex(Integer newIndex) {
        if (Objects.equals(selectedIndex, newIndex)) {
            return;
        }
        if (selectedIndex != null) {
            Widgets.setTextButtonStyle(elementList.get(selectedIndex), Style.listElementUnselected, Style.Text.menuButtonLabelStyle);
        }
        if (newIndex != null) {
            assert elementList.size() > newIndex : "list index out of bounds";
            Widgets.setTextButtonStyle(elementList.get(newIndex), Style.listElementSelected, Style.Text.menuButtonLabelStyle);
        }
        selectedIndex = newIndex;
    }

}
