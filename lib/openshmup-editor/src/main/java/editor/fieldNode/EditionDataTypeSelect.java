package editor.fieldNode;

import editor.Style;
import editor.Widgets;
import engine.menu.Menu;
import engine.menu.MenuElementGroup;
import engine.menu.widget.SelectorButtons;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.TextAlignment;
import engine.types.Vec2D;
import json.editionData.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

final public class EditionDataTypeSelect<D extends EditionData> implements FieldNode {

    final private static Map<EditionData.Category, List<EditionData.Type>> typesMap = Map.of(
        EditionData.Category.VISUAL, List.of(EditionData.Types.Visual.scrollingImage, EditionData.Types.Visual.animation),
        EditionData.Category.TRAJECTORY, List.of(EditionData.Types.Trajectory.fixed, EditionData.Types.Trajectory.player),
        EditionData.Category.ENTITY, List.of(EditionData.Types.Entity.projectile, EditionData.Types.Entity.ship),
        EditionData.Category.SPAWN, List.of(EditionData.Types.Spawn.display, EditionData.Types.Spawn.entity),
        EditionData.Category.HITBOX, List.of(EditionData.Types.Hitbox.rectangle, EditionData.Types.Hitbox.custom)
    );

    final private static Map<EditionData.Category, List<String>> labelsMap = Map.of(
        EditionData.Category.VISUAL, List.of("Scrolling Image", "Animation"),
        EditionData.Category.TRAJECTORY, List.of("Fixed", "Player Controlled"),
        EditionData.Category.ENTITY, List.of("Projectile", "Ship"),
        EditionData.Category.SPAWN, List.of("Display", "Entity"),
        EditionData.Category.HITBOX, List.of("Rectangle", "Custom")
    );

    final private static Map<EditionData.Category, List<Supplier<EditionData>>> constructorsMap = Map.of(
        EditionData.Category.VISUAL, List.of(ScrollingImageEditionData::DEFAULT, AnimationEditionData::DEFAULT),
        EditionData.Category.TRAJECTORY, List.of(FixedTrajectoryEditionData::DEFAULT, PlayerControlledTrajectoryEditionData::DEFAULT),
        EditionData.Category.ENTITY, List.of(ProjectileEditionData::DEFAULT, ShipEditionData::DEFAULT),
        EditionData.Category.SPAWN, List.of(DisplaySpawnEditionData::DEFAULT, EntitySpawnEditionData::DEFAULT),
        EditionData.Category.HITBOX, List.of(RectangleHitboxEditionData::DEFAULT, CustomHitboxEditionData::DEFAULT)
    );

    final public static float selectorSpacing = 70f;

    final public static Vec2D buttonSize = new Vec2D(250f, 60f);

    final public static Vec2D buttonStride = new Vec2D(280f, 0f);

    final private EditionData.Category category;

    final private List<EditionDataFields<D>> editionDataFieldsList;

    private final List<EditionData.Type> types;

    private boolean isActive;

    private Menu menu;

    final private SelectorButtons selectorButtons;

    final private TextDisplay typeSelectText;

    public EditionDataTypeSelect(D editionData, Vec2D startPosition) {
        this.category = editionData.getCategory();
        assert category != EditionData.Category.NONE : "Invalid editionData type";
        EditionData.Type editionDataType = editionData.getType();
        int selectedValue = 0;
        this.types = typesMap.get(this.category);
        List<String> typeLabels = labelsMap.get(this.category);
        List<Supplier<EditionData>> defaultConstructors = constructorsMap.get(this.category);

        List<D> dataList = new ArrayList<>(types.size());
        for (int i = 0; i < types.size(); i++) {
            D data;
            if (editionDataType == types.get(i)) {
                data = editionData;
                selectedValue = i;
            }
            else {
                data = (D) defaultConstructors.get(i).get();
            }
            dataList.add(data);
        }
        this.editionDataFieldsList = new ArrayList<>(dataList.size());
        for (D data : dataList) {
            editionDataFieldsList.add(new EditionDataFields<D>(data, startPosition.add(0f, -60f)));
        }
        this.isActive = false;
        BiConsumer<SelectorButtons, Integer> onChange = (selector, newValue) -> {
            if (this.isActive) {
                editionDataFieldsList.get(selector.getSelectedValue()).setActive(false);
                editionDataFieldsList.get(newValue).setActive(true);
            }
        };
        this.menu = null;
        this.typeSelectText = new TextDisplay(1, false, startPosition, "Type:", Style.Text.menuTextStyle, TextAlignment.LEFT);
        Vec2D fieldPosition = startPosition.add(200f, 0f);
        Vec2D selectorButtonsSize = new Vec2D(200f, buttonSize.y);
        Vec2D selectorButtonsStride = new Vec2D(220f, 0.0f);
        this.selectorButtons = Widgets.TypeSelectorButtons(2, dataList.size(), selectorButtonsSize, fieldPosition, selectorButtonsStride, typeLabels, onChange, selectedValue);
        this.selectorButtons.setSelectedValue(selectedValue);
    }

    public D getSelectedEditionData() {
        return editionDataFieldsList.get(selectorButtons.getSelectedValue()).getEditionData();
    }

    public void setData(EditionData editionData) {
        assert editionData.getCategory() == this.category : "Invalid editionData type";
        EditionData.Type type = editionData.getType();
        for (int i = 0; i < this.types.size(); i++) {
            if (type == this.types.get(i)) {
                editionDataFieldsList.get(i).setEditionData((D) editionData);
            }
        }
    }

    @Override
    public void setMenu(Menu menu) {
        this.menu = menu;
        for (EditionDataFields<D> fields : editionDataFieldsList) {
            fields.setMenu(menu);
        }
    }

    @Override
    public MenuElementGroup getAllActiveElements() {
        MenuElementGroup allActiveElements = new MenuElementGroup();
        if (this.isActive) {
            allActiveElements.widgets().add(selectorButtons);
            allActiveElements.visuals().add(typeSelectText);
            for (EditionDataFields<D> fields : editionDataFieldsList) {
                MenuElementGroup editionDataFieldsElements = fields.getAllActiveElements();
                allActiveElements.widgets().addAll(editionDataFieldsElements.widgets());
                allActiveElements.visuals().addAll(editionDataFieldsElements.visuals());
            }
        }
        return allActiveElements;
    }

    @Override
    public void setActive(boolean active) {
        if (this.isActive != active) {
            this.isActive = active;
            if (this.menu != null) {
                if (active) {
                    menu.addToCurrentScreen(selectorButtons);
                }
                else {
                    menu.removeFromCurrentScreen(selectorButtons);
                }
            }
            editionDataFieldsList.get(selectorButtons.getSelectedValue()).setActive(active);
        }
    }

    @Override
    public void applyChanges() {
        for (int i = 0; i < editionDataFieldsList.size(); i++) {
            if (i == selectorButtons.getSelectedValue()) {
                editionDataFieldsList.get(i).applyChanges();
            }
            else {
                editionDataFieldsList.get(i).resetToDefault();
            }
        }
    }
}
