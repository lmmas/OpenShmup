package editor.fieldNode;

import edition.EditionData;
import editor.EditionMenu;
import editor.Style;
import engine.menu.Menu;
import engine.menu.MenuScreen;
import engine.menu.widget.ActionButton;
import lombok.Getter;
import types.Vec2D;

import java.util.ArrayList;
import java.util.Objects;

import static engine.menu.widget.Widgets.TextButton;

final public class ListFields implements FieldNode {

    @Getter
    final private EditionData.Category category;
    @Getter
    final private ArrayList<EditionData> dataList;

    final private ArrayList<EditionDataFieldNode> dataFieldsList;

    final private boolean openInNewScreen;

    final private ArrayList<MenuScreen> menuScreenList;

    private Integer selectedIndex;

    final private Vec2D listStartPosition;

    final private Vec2D fieldsStartPosition;

    final private ArrayList<ActionButton> selectButtons;

    final private ArrayList<ActionButton> deleteButtons;

    private ActionButton addButton;

    private boolean isActive;

    private Menu menu;

    public ListFields(EditionData.Category category, ArrayList<EditionData> dataList, Vec2D startPosition, boolean openInNewScreen) {
        this.category = category;
        this.dataList = dataList;
        for (EditionData data : dataList) {
            assert data.getCategory() == category : "incorrect data category";
        }
        this.dataFieldsList = new ArrayList<>(dataList.size());
        for (int i = 0; i < dataList.size(); i++) {
            dataFieldsList.add(null);
        }
        this.openInNewScreen = openInNewScreen;
        if (openInNewScreen) {
            menuScreenList = new ArrayList<>(dataList.size());
            for (int i = 0; i < dataList.size(); i++) {
                menuScreenList.add(null);
            }
        }
        else {
            menuScreenList = null;
        }
        this.selectedIndex = null;
        this.listStartPosition = startPosition;
        if (openInNewScreen) {
            this.fieldsStartPosition = new Vec2D(120f, 830f);
        }
        else {
            this.fieldsStartPosition = startPosition.add(200f, 0f);
        }
        this.isActive = false;
        this.selectButtons = new ArrayList<>(dataList.size());
        this.deleteButtons = new ArrayList<>(dataList.size());
        for (EditionData data : dataList) {
            addItemButtons(data);
        }
        this.buildAddButton();
    }

    private void selectListItem(Integer newIndex) {
        if (!Objects.equals(selectedIndex, newIndex)) {
            if (selectedIndex != null) {
                dataFieldsList.get(selectedIndex).setActive(false);
            }
            if (newIndex != null) {
                if (openInNewScreen) {
                    this.openInNewScreen(newIndex);
                }
                else {
                    if (dataFieldsList.get(newIndex) == null) {
                        EditionDataFieldNode node = EditionDataFieldNode.createFromEtitionData(dataList.get(newIndex), fieldsStartPosition);
                        node.setMenu(this.menu);
                        dataFieldsList.set(newIndex, node);
                    }
                    dataFieldsList.get(newIndex).setActive(isActive);
                }
                selectedIndex = newIndex;
            }
        }
    }

    private void openInNewScreen(int index) {
        if (dataFieldsList.get(index) == null) {
            EditionDataFieldNode node = EditionDataFieldNode.createFromEtitionData(dataList.get(index), fieldsStartPosition);
            node.setMenu(this.menu);
            dataFieldsList.set(index, node);
            Runnable onApply = () -> {
                node.applyChanges();
                dataList.set(index, node.getEditionData());
            };
            Runnable onClose = () -> selectedIndex = null;
            MenuScreen screen = EditionMenu.openEditPanel(this.menu, 8, node, onApply, onClose);
            menuScreenList.set(index, screen);
        }
        else {
            menu.addMenuScreen(menuScreenList.get(index));
        }
        dataFieldsList.get(index).setActive(isActive);
    }

    public void deleteItem(int indexToRemove) {
        assert indexToRemove < dataList.size() : "incorrect index";
        int oldListSize = dataList.size();
        if (Objects.equals(selectedIndex, indexToRemove)) {
            selectListItem(null);
        }
        else if (selectedIndex != null && selectedIndex > indexToRemove) {
            selectedIndex -= 1; // adjusts the selected index without retriggering the selectListItem method
        }
        dataList.remove(indexToRemove);
        dataFieldsList.remove(indexToRemove);
        if (menuScreenList != null) {
            menuScreenList.remove(indexToRemove);
        }
        for (int i = indexToRemove; i < oldListSize; i++) {
            if (isActive) {
                assert menu != null : "menu not found";
                menu.removeFromCurrentScreen(selectButtons.get(i));
                menu.removeFromCurrentScreen(deleteButtons.get(i));
            }
        }
        if (isActive) {
            menu.removeFromCurrentScreen(addButton);
        }
        selectButtons.removeAll(selectButtons.subList(indexToRemove, oldListSize));
        deleteButtons.removeAll(deleteButtons.subList(indexToRemove, oldListSize));

        for (int i = indexToRemove; i < dataList.size(); i++) {
            addItemButtons(dataList.get(i));
        }
        buildAddButton();
        if (isActive) {
            menu.addToCurrentScreen(addButton);
        }
    }

    private void addNewItem() {
        EditionData newData = getDefaultofCategory(this.category);
        this.dataList.add(newData);
        this.dataFieldsList.add(null);
        if (menuScreenList != null) {
            menuScreenList.add(null);
        }
        addItemButtons(newData);
        if (isActive) {
            menu.removeFromCurrentScreen(this.addButton);
        }
        buildAddButton();
        if (isActive) {
            menu.addToCurrentScreen(this.addButton);
        }
        selectListItem(selectButtons.size() - 1);// selects the last element of the list, the one just added
    }

    public static EditionData getDefaultofCategory(EditionData.Category category) {
        return switch (category) {
            case VISUAL -> EditionData.Defaults.Animation();
            case TRAJECTORY -> EditionData.Defaults.FixedTrajectory();
            case ENTITY -> EditionData.Defaults.Ship();
            case HITBOX -> EditionData.Defaults.RectangleHitbox();
            case SPAWN -> EditionData.Defaults.DisplaySpawn();
            case SPAWN_INFO -> EditionData.Defaults.SingleSpawnInfo();
            case NONE -> EditionData.Defaults.Shot();
            case CONFIG -> EditionData.Defaults.Config(EditionData.Types.Config.general);
        };
    }

    private void addItemButtons(EditionData newItem) {
        assert selectButtons.size() == deleteButtons.size();
        int itemIndex = selectButtons.size();
        Vec2D selectButtonSize = new Vec2D(200f, 50f);
        Vec2D selectButtonPosition = listStartPosition.add(0f, -itemIndex * selectButtonSize.y);
        String buttonLabel = Integer.toString(switch (newItem.getCategory()) {
            case VISUAL -> EditionData.getVisualId(newItem);
            case TRAJECTORY -> EditionData.getTrajectoryId(newItem);
            case ENTITY -> EditionData.getEntityId(newItem);
            default -> itemIndex + 1;
        });
        ActionButton newSelect = TextButton(2, selectButtonSize, selectButtonPosition, Style.menuButtonStyle1, Style.Text.menuButtonLabelStyle, buttonLabel, () -> selectListItem(itemIndex));
        Vec2D deleteButtonSize = new Vec2D(60f, 50f);
        Vec2D deleteButtonPosition = selectButtonPosition.add(130f, 0f);
        ActionButton newDelete = TextButton(2, deleteButtonSize, deleteButtonPosition, Style.menuButtonStyle1, Style.Text.menuButtonLabelStyle, "X", () -> deleteItem(itemIndex));
        selectButtons.add(newSelect);
        deleteButtons.add(newDelete);
        if (isActive) {
            assert menu != null : "menu not found";
            menu.addToCurrentScreen(newSelect);
            menu.addToCurrentScreen(newDelete);
        }
    }

    private void buildAddButton() {
        Vec2D buttonSize = new Vec2D(100f, 50f);
        Vec2D buttonPosition = listStartPosition.add(0f, -selectButtons.size() * buttonSize.y);
        this.addButton = TextButton(2, buttonSize, buttonPosition, Style.menuButtonStyle1, Style.Text.menuButtonLabelStyle, "Add", this::addNewItem);
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
        assert addButton != null : "add button not found";
        if (active != this.isActive) {
            this.isActive = active;
            if (menu != null) {
                if (active) {
                    selectButtons.forEach(menu::addToCurrentScreen);
                    deleteButtons.forEach(menu::addToCurrentScreen);
                    menu.addToCurrentScreen(addButton);
                }
                else {
                    selectButtons.forEach(menu::removeFromCurrentScreen);
                    deleteButtons.forEach(menu::removeFromCurrentScreen);
                    menu.removeFromCurrentScreen(addButton);
                }
            }
            if (selectedIndex != null) {
                dataFieldsList.get(selectedIndex).setActive(active);
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
        for (int i = 0; i < dataList.size(); i++) {
            if (dataFieldsList.get(i) != null) {
                dataList.set(i, dataFieldsList.get(i).getEditionData());
            }
        }
    }
}
