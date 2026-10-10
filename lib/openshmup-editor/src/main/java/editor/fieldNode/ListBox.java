package editor.fieldNode;

import editor.Style;
import engine.Engine;
import engine.hitbox.RectangleBox;
import engine.input.InputStatesManager;
import engine.menu.widget.ActionButton;
import engine.menu.widget.Widget;
import engine.menu.widget.Widgets;
import engine.visual.VisualGroup;
import lombok.Getter;
import types.Vec2D;

import java.util.ArrayList;
import java.util.Objects;

public final class ListBox implements Widget {
    final static private float elementHeight = 50f;

    private VisualGroup visualGroup;

    private final ArrayList<ActionButton> elementList;

    private final RectangleBox rectangleBox;
    @Getter
    private Integer selectedIndex;

    private float topElementY;

    public ListBox(Vec2D size, Vec2D position, ArrayList<String> labelsList) {
        this.rectangleBox = new RectangleBox(position, size);
        this.elementList = new ArrayList<>(labelsList.size());
        this.topElementY = rectangleBox.getUpBound() - (elementHeight / 2);
        labelsList.forEach(this::addElement);
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

    public void addElement(String label) {
        int elementIndex = elementList.size();
        Vec2D elementPosition = new Vec2D(rectangleBox.getPosition().x, topElementY + elementIndex * elementHeight);
        Vec2D elementSize = new Vec2D(rectangleBox.getSize().x, elementHeight);
        Runnable onClick = () -> setSelectedIndex(elementIndex);
        elementList.add(Widgets.TextButton(elementSize, elementPosition, Style.listElementUnselected, Style.Text.menuButtonLabelStyle, label, onClick));
    }
}
