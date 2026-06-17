package editor.fieldNode;

import editor.Widgets;
import engine.menu.Menu;
import engine.menu.MenuElementGroup;
import engine.menu.widget.SelectorButtons;
import engine.types.Vec2D;
import json.editionData.EditionData;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

final public class ListFields implements FieldNode {

    @Getter
    final private List<EditionData> dataList;

    final private List<FieldNode> dataFieldsList;

    private FieldNode selectedDataFields;

    final private MenuElementGroup buttonsGroup;

    private boolean isActive;

    private Menu menu;

    public ListFields(int layer, List<EditionData> dataList, Vec2D startPosition) {
        this.dataList = dataList;
        Vec2D fieldsStartPosition = startPosition.add(200f, 0f);
        this.dataFieldsList = new ArrayList<>(dataList.size());
        for (EditionData data : dataList) {
            if (data.hasTypeSelect()) {
                dataFieldsList.add(new EditionDataTypeSelect(data, fieldsStartPosition));
            }
            else {
                dataFieldsList.add(new EditionDataFields(data, fieldsStartPosition));
            }
        }
        this.selectedDataFields = null;
        this.isActive = false;
        Vec2D selectorButtonSize = new Vec2D(200f, 50f);
        Vec2D stride = new Vec2D(0f, -60f);
        List<String> selectorLabels = new ArrayList<>(dataList.size());
        for (int i = 0; i < dataList.size(); i++) {
            selectorLabels.add((i + 1) + "");
        }
        BiConsumer<SelectorButtons, Integer> selectorOnChange = (selectorButtons, newValue) -> {
            if (selectorButtons.getSelectedValue() != null) {
                selectedDataFields.setActive(false);
            }
            if (newValue != null) {
                selectedDataFields = dataFieldsList.get(newValue);
                if (isActive) {
                    selectedDataFields.setActive(true);
                }
            }
        };
        SelectorButtons dataSelector = Widgets.EditorSelector(layer, dataList.size(), selectorButtonSize, startPosition, stride, selectorLabels, selectorOnChange, null);
        this.buttonsGroup = new MenuElementGroup(List.of(dataSelector), List.of());
    }
    @Override
    public void setMenu(Menu menu) {
        this.menu = menu;
        dataFieldsList.forEach(fields -> fields.setMenu(menu));
    }
    @Override
    public MenuElementGroup getAllActiveElements() {
        MenuElementGroup allActiveElements = new MenuElementGroup();
        if (isActive) {
            allActiveElements.widgets().addAll(buttonsGroup.widgets());
            allActiveElements.visuals().addAll(buttonsGroup.visuals());
            if (selectedDataFields != null) {
                MenuElementGroup selectedDataElements = selectedDataFields.getAllActiveElements();
                allActiveElements.widgets().addAll(selectedDataElements.widgets());
                allActiveElements.visuals().addAll(selectedDataElements.visuals());
            }
        }
        return allActiveElements;
    }
    @Override
    public void setActive(boolean active) {
        if (active != this.isActive) {
            this.isActive = active;
            if (menu != null) {
                if (active) {
                    menu.addToCurrentScreen(buttonsGroup);
                }
                else {
                    menu.removeFromCurrentScreen(buttonsGroup);
                }
            }
            if (selectedDataFields != null) {
                selectedDataFields.setActive(active);
            }
        }
    }
    @Override
    public void applyChanges() {
        dataFieldsList.forEach(FieldNode::applyChanges);
    }
}
