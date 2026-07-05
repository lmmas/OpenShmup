package editor.fieldNode;

import editor.EditionMenu;
import editor.Widgets;
import engine.menu.Menu;
import engine.menu.MenuElementGroup;
import engine.menu.MenuScreen;
import engine.menu.widget.SelectorButtons;
import engine.types.Vec2D;
import json.editionData.EditionData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

final public class ListFields implements FieldNode {

    final private List<EditionData> dataList;

    final private List<EditionData> tempDataList;

    final private List<EditionDataFieldNode> dataFieldsList;

    final private List<MenuScreen> menuScreenList;

    private FieldNode selectedDataFields;

    final private MenuElementGroup buttonsGroup;

    private boolean isActive;

    private Menu menu;

    final private boolean openInNewScreen;

    public ListFields(List<EditionData> dataList, Vec2D startPosition, boolean openInNewScreen) {
        this.dataList = dataList;
        this.tempDataList = new ArrayList<>(dataList);
        this.openInNewScreen = openInNewScreen;

        this.dataFieldsList = new ArrayList<>(dataList.size());
        for (int i = 0; i < dataList.size(); i++) {
            dataFieldsList.add(null);
        }

        if (openInNewScreen) {
            menuScreenList = new ArrayList<>(dataList.size());
            for (int i = 0; i < dataList.size(); i++) {
                menuScreenList.add(null);
            }
        }
        else {
            menuScreenList = null;
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
            Integer oldValue = selectorButtons.getSelectedValue();
            if (oldValue != null) {
                selectedDataFields.setActive(false);
            }
            if (newValue != null) {
                Vec2D fieldsStartPosition;
                if (openInNewScreen) {
                    fieldsStartPosition = new Vec2D(120f, 830f);
                    if (dataFieldsList.get(newValue) == null) {
                        EditionDataFieldNode node = EditionDataFieldNode.createFromEtitionData(tempDataList.get(newValue), fieldsStartPosition);
                        node.setMenu(this.menu);
                        dataFieldsList.set(newValue, node);
                        Runnable onApply = () -> {
                            node.applyChanges();
                            tempDataList.set(newValue, node.getEditionData());
                        };
                        Runnable onClose = () -> {
                            selectorButtons.setSelectedValue(null);
                            selectedDataFields = null;
                        };
                        MenuScreen screen = EditionMenu.openEditPanel(this.menu, 8, node, onApply, onClose);
                        menuScreenList.set(newValue, screen);
                    }
                    else {
                        menu.addMenuScreen(menuScreenList.get(newValue));
                    }
                }
                else {
                    fieldsStartPosition = startPosition.add(200f, 0f);
                    if (dataFieldsList.get(newValue) == null) {
                        EditionDataFieldNode node = EditionDataFieldNode.createFromEtitionData(tempDataList.get(newValue), fieldsStartPosition);
                        node.setMenu(this.menu);
                        dataFieldsList.set(newValue, node);
                    }
                }
                selectedDataFields = dataFieldsList.get(newValue);
                selectedDataFields.setActive(isActive);
            }
        };

        SelectorButtons dataSelector = Widgets.EditorSelector(2, dataList.size(), selectorButtonSize, startPosition, stride, selectorLabels, selectorOnChange, null);
        this.buttonsGroup = new MenuElementGroup(List.of(dataSelector), List.of());
    }
    @Override
    public void setMenu(Menu menu) {
        this.menu = menu;
        for (EditionDataFieldNode fields : dataFieldsList) {
            if (fields != null) {
                fields.setMenu(menu);
            }
        }
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
        for (EditionDataFieldNode fields : dataFieldsList) {
            if (fields != null) {
                fields.applyChanges();
            }
        }
        for (int i = 0; i < tempDataList.size(); i++) {
            if (dataFieldsList.get(i) != null) {
                tempDataList.set(i, dataFieldsList.get(i).getEditionData());
            }
        }
        dataList.clear();
        dataList.addAll(tempDataList);
    }
}
