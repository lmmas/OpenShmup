package editor.fieldNode;

import editor.Style;
import editor.Widgets;
import engine.menu.Menu;
import engine.menu.widget.SelectorButtons;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.TextAlignment;
import engine.types.Vec2D;
import json.editionData.EditionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static json.editionData.EditionData.*;

final public class EditionDataTypeSelect implements EditionDataFieldNode {

    final private static Map<Category, List<Type>> typesMap = Map.of(
        Category.VISUAL, List.of(Types.Visual.scrollingImage, Types.Visual.animation),
        Category.TRAJECTORY, List.of(Types.Trajectory.fixed, Types.Trajectory.player),
        Category.ENTITY, List.of(Types.Entity.projectile, Types.Entity.ship),
        Category.SPAWN, List.of(Types.Spawn.display, Types.Spawn.entity),
        Category.HITBOX, List.of(Types.Hitbox.rectangle, Types.Hitbox.custom),
        Category.SPAWN_INFO, List.of(Types.SpawnInfo.single, Types.SpawnInfo.repeat)
    );

    final private static Map<Category, List<String>> labelsMap = Map.of(
        Category.VISUAL, List.of("Scrolling Image", "Animation"),
        Category.TRAJECTORY, List.of("Fixed", "Player Controlled"),
        Category.ENTITY, List.of("Projectile", "Ship"),
        Category.SPAWN, List.of("Display", "Entity"),
        Category.HITBOX, List.of("Rectangle", "Custom"),
        Category.SPAWN_INFO, List.of("Single", "Repeat")
    );

    final private static Map<Category, List<Supplier<EditionData>>> constructorsMap = Map.of(
        Category.VISUAL, List.of(Defaults::ScrollingImage, Defaults::Animation),
        Category.TRAJECTORY, List.of(Defaults::FixedTrajectory, Defaults::PlayerControlledTrajectory),
        Category.ENTITY, List.of(Defaults::Projectile, Defaults::Ship),
        Category.SPAWN, List.of(Defaults::DisplaySpawn, Defaults::EntitySpawn),
        Category.HITBOX, List.of(Defaults::RectangleHitbox, Defaults::CustomHitbox),
        Category.SPAWN_INFO, List.of(Defaults::SingleSpawnInfo, Defaults::RepeatSpawnInfo)
    );

    final public static float selectorSpacing = 70f;

    final public static Vec2D buttonSize = new Vec2D(250f, 60f);

    final public static Vec2D buttonStride = new Vec2D(280f, 0f);

    final private Category category;

    final private List<EditionDataFields> editionDataFieldsList;

    private final List<Type> types;

    private boolean isActive;

    private Menu menu;

    final private SelectorButtons selectorButtons;

    final private TextDisplay typeSelectText;

    public EditionDataTypeSelect(EditionData editionData, Vec2D startPosition) {
        assert editionData.getCategory() != Category.NONE : "Invalid editionData type";
        this.category = editionData.getCategory();
        Type editionDataType = editionData.getType();
        int selectedValue = 0;
        this.types = typesMap.get(this.category);
        List<String> typeLabels = labelsMap.get(this.category);
        List<Supplier<EditionData>> defaultConstructors = constructorsMap.get(this.category);

        List<EditionData> dataList = new ArrayList<>(types.size());
        for (int i = 0; i < types.size(); i++) {
            EditionData data;
            if (editionDataType == types.get(i)) {
                data = editionData;
                selectedValue = i;
            }
            else {
                data = defaultConstructors.get(i).get();
            }
            dataList.add(data);
        }
        this.editionDataFieldsList = new ArrayList<>(dataList.size());
        for (EditionData data : dataList) {
            editionDataFieldsList.add(new EditionDataFields(data, startPosition.add(0f, -60f)));
        }
        this.isActive = false;
        BiConsumer<SelectorButtons, Integer> onChange = (selector, newValue) -> {
            if (this.isActive) {
                editionDataFieldsList.get(selector.getSelectedValue()).setActive(false);
                editionDataFieldsList.get(newValue).setActive(true);
            }
        };
        this.menu = null;
        this.typeSelectText = new TextDisplay(2, false, startPosition, "Type:", Style.Text.menuTextStyle, TextAlignment.LEFT);
        Vec2D fieldPosition = startPosition.add(200f, 0f);
        Vec2D selectorButtonsSize = new Vec2D(200f, buttonSize.y);
        Vec2D selectorButtonsStride = new Vec2D(220f, 0.0f);
        this.selectorButtons = Widgets.TypeSelectorButtons(2, dataList.size(), selectorButtonsSize, fieldPosition, selectorButtonsStride, typeLabels, onChange, selectedValue);
        this.selectorButtons.setSelectedValue(selectedValue);
    }

    @Override
    public EditionData getEditionData() {
        return editionDataFieldsList.get(selectorButtons.getSelectedValue()).getEditionData();
    }

    @Override
    public void setEditionData(EditionData editionData) {
        editionData.checkForCategory(this.category);
        Type type = editionData.getType();
        for (int i = 0; i < this.types.size(); i++) {
            if (type == this.types.get(i)) {
                editionDataFieldsList.get(i).setEditionData(editionData);
            }
        }
    }

    @Override
    public void setMenu(Menu menu) {
        this.menu = menu;
        for (EditionDataFields fields : editionDataFieldsList) {
            fields.setMenu(menu);
        }
    }

    @Override
    public void setActive(boolean active) {
        if (this.isActive != active) {
            this.isActive = active;
            if (this.menu != null) {
                if (active) {
                    menu.addToCurrentScreen(typeSelectText);
                    menu.addToCurrentScreen(selectorButtons);
                }
                else {
                    menu.removeFromCurrentScreen(typeSelectText);
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
