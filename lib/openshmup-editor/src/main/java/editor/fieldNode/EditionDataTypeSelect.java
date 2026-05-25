package editor.fieldNode;

import editor.MenuItems;
import editor.Style;
import editor.editionData.*;
import engine.menu.ItemGroup;
import engine.menu.Menu;
import engine.menu.item.SelectorButtons;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.TextAlignment;
import engine.types.Vec2D;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class EditionDataTypeSelect<D extends EditionData> implements FieldNode {

    final private static List<String> visualTypes = List.of("scrollingImage", "animation");

    final private static List<String> visualTypeLabels = List.of("Scrolling Image", "Animation");

    final private static List<Supplier<VisualEditionData>> visualDefaultConstructors = List.of(ScrollingImageEditionData::DEFAULT, AnimationEditionData::DEFAULT);

    final private static List<String> trajectoryTypes = List.of("fixed", "player");

    final private static List<String> trajectoryTypeLabels = List.of("Fixed", "Player");

    final private static List<Supplier<TrajectoryEditionData>> trajectoryDefaultConstructors = List.of(FixedTrajectoryEditionData::DEFAULT, PlayerControlledTrajectoryEditionData::DEFAULT);

    final private static List<String> spawnableTypes = List.of("display", "entity");

    final private static List<String> spawnableTypeLabels = List.of("Display", "Entity");

    final private static List<Supplier<SpawnableEditionData>> spawnableDefaultConstructors = List.of(DisplaySpawnInfoEditionData::DEFAULT, EntitySpawnInfoEditionData::DEFAULT);

    final private static List<String> hitboxTypes = List.of("simpleRectangle", "composite");

    final private static List<String> hitboxTypeLabels = List.of("Rectangle", "Custom");

    final private static List<Supplier<HitboxEditionData>> hitboxDefaultConstructors = List.of(SimpleRectangleHitboxEditionData::DEFAULT, CompositeHitboxEditionData::DEFAULT);

    final private static List<String> entityTypes = List.of("projectile", "ship");

    final private static List<String> entityTypeLabels = List.of("Projectile", "Ship");

    final private static List<Supplier<EntityEditionData>> entityDefaultConstructors = List.of(ProjectileEditionData::DEFAULT, ShipEditionData::DEFAULT);

    final public static float selectorSpacing = 70f;

    final public static Vec2D buttonSize = new Vec2D(250f, 60f);

    final public static Vec2D buttonStride = new Vec2D(280f, 0f);

    final private List<EditionDataFields<D>> editionDataFieldsList;

    private boolean isActive;

    private Menu menu;

    final private SelectorButtons selectorButtons;

    final private TextDisplay typeSelectText;

    public EditionDataTypeSelect(D editionData, Vec2D startPosition) {
        List<String> types;
        List<String> typeLabels;
        List<Supplier<D>> defaultConstructors;
        String editionDataType;
        int selectedValue = 0;
        switch (editionData) {
            case VisualEditionData visualEditionData -> {
                types = visualTypes;
                typeLabels = visualTypeLabels;
                defaultConstructors = visualDefaultConstructors.stream().map(c -> (Supplier<D>) c).toList();
                editionDataType = VisualEditionData.getType(visualEditionData);
            }
            case TrajectoryEditionData trajectoryEditionData -> {
                types = trajectoryTypes;
                typeLabels = trajectoryTypeLabels;
                defaultConstructors = trajectoryDefaultConstructors.stream().map(c -> (Supplier<D>) c).toList();
                editionDataType = TrajectoryEditionData.getType(trajectoryEditionData);
            }
            case SpawnableEditionData spawnableEditionData -> {
                types = spawnableTypes;
                typeLabels = spawnableTypeLabels;
                defaultConstructors = spawnableDefaultConstructors.stream().map(c -> (Supplier<D>) c).toList();
                editionDataType = SpawnableEditionData.getType(spawnableEditionData);
            }
            case HitboxEditionData hitboxEditionData -> {
                types = hitboxTypes;
                typeLabels = hitboxTypeLabels;
                defaultConstructors = hitboxDefaultConstructors.stream().map(c -> (Supplier<D>) c).toList();
                editionDataType = HitboxEditionData.getType(hitboxEditionData);
            }
            case EntityEditionData entityEditionData -> {
                types = entityTypes;
                typeLabels = entityTypeLabels;
                defaultConstructors = entityDefaultConstructors.stream().map(c -> (Supplier<D>) c).toList();
                editionDataType = EntityEditionData.getType(entityEditionData);
            }
            default -> throw new IllegalStateException("Unexpected value: " + editionData);
        }
        List<D> dataList = new ArrayList<>(types.size());
        for (int i = 0; i < types.size(); i++) {
            D data;
            if (editionDataType.equals(types.get(i))) {
                data = editionData;
                selectedValue = i;
            }
            else {
                data = defaultConstructors.get(i).get();
            }
            dataList.add(data);
        }
        this.editionDataFieldsList = new ArrayList<>(dataList.size());
        for (D data : dataList) {
            editionDataFieldsList.add(new EditionDataFields<D>(data, startPosition.add(new Vec2D(0f, -60f))));
        }
        this.isActive = false;
        BiConsumer<SelectorButtons, Integer> onChange = (selector, newValue) -> {
            if (this.isActive) {
                editionDataFieldsList.get(selector.getSelectedValue()).setActive(false);
                editionDataFieldsList.get(newValue).setActive(true);
            }
        };
        this.menu = null;
        this.typeSelectText = new TextDisplay(1, false, startPosition, "Type:", Style.Text.menuButtonLabelStyle, TextAlignment.LEFT);
        Vec2D fieldPosition = startPosition.add(new Vec2D(200f, 0f));
        this.selectorButtons = MenuItems.EditorSelector(2, dataList.size(), buttonSize, fieldPosition, buttonStride, typeLabels, onChange, selectedValue);
        this.selectorButtons.setSelectedValue(selectedValue);
    }

    public D getSelectedEditionData() {
        return editionDataFieldsList.get(selectorButtons.getSelectedValue()).getEditionData();
    }

    public void setData(EditionData editionData) {
        List<String> types;
        String type;
        switch (editionData) {
            case VisualEditionData visualEditionData -> {
                types = visualTypes;
                type = VisualEditionData.getType(visualEditionData);
            }
            case TrajectoryEditionData trajectoryEditionData -> {
                types = trajectoryTypes;
                type = TrajectoryEditionData.getType(trajectoryEditionData);
            }
            case SpawnableEditionData spawnableEditionData -> {
                types = spawnableTypes;
                type = SpawnableEditionData.getType(spawnableEditionData);
            }
            case HitboxEditionData hitboxEditionData -> {
                types = hitboxTypes;
                type = HitboxEditionData.getType(hitboxEditionData);
            }
            case EntityEditionData entityEditionData -> {
                types = entityTypes;
                type = EntityEditionData.getType(entityEditionData);
            }
            default -> throw new IllegalStateException("Unexpected value: " + editionData);
        }
        for (int i = 0; i < types.size(); i++) {
            if (type.equals(types.get(i))) {
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
    public ItemGroup getAllActiveItems() {
        ItemGroup allActiveItems = new ItemGroup();
        if (this.isActive) {
            allActiveItems.items().add(selectorButtons);
            allActiveItems.otherVisuals().add(typeSelectText);
            for (EditionDataFields<D> fields : editionDataFieldsList) {
                ItemGroup editionDataFieldsElements = fields.getAllActiveItems();
                allActiveItems.items().addAll(editionDataFieldsElements.items());
                allActiveItems.otherVisuals().addAll(editionDataFieldsElements.otherVisuals());
            }
        }
        return allActiveItems;
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
