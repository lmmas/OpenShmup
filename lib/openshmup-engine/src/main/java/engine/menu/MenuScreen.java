package engine.menu;

import engine.menu.item.MenuItem;
import engine.scene.visual.SceneVisual;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter final public class MenuScreen {

    private int backgroundLayer;

    final private ArrayList<MenuItem> menuItems;

    final private ArrayList<SceneVisual> otherVisuals;

    final private ArrayList<ItemGroup> itemGroups;
    @Setter
    private boolean isOpen;

    public MenuScreen(int backgroundLayer) {
        this.backgroundLayer = backgroundLayer;
        this.menuItems = new ArrayList<>();
        this.otherVisuals = new ArrayList<>();
        this.itemGroups = new ArrayList<>();
        this.isOpen = false;
    }

    public void addItem(MenuItem menuItem) {
        assert !menuItems.contains(menuItem) : "item is already present in item list of the screen";
        menuItems.add(menuItem);
    }

    public void removeItem(MenuItem menuItem) {
        assert menuItems.contains(menuItem) : "item is not present in item list of the screen";
        menuItems.remove(menuItem);
    }

    public void addVisual(SceneVisual visual) {
        assert !otherVisuals.contains(visual) : "visual is already present in visual list of the screen";
        otherVisuals.add(visual);
    }

    public void removeVisual(SceneVisual visual) {
        assert otherVisuals.contains(visual) : "visual is not present in visual list of the screen";
        otherVisuals.remove(visual);
    }

    public void addItemGroup(ItemGroup itemGroup) {
        assert !itemGroups.contains(itemGroup) : "item group is already present in item group list of the screen";
        itemGroups.add(itemGroup);
        itemGroup.items().forEach(this::addItem);
        itemGroup.otherVisuals().forEach(this::addVisual);
    }

    public void removeItemGroup(ItemGroup itemGroup) {
        assert itemGroups.contains(itemGroup) : "item group is not present in item group list of the screen";
        itemGroups.remove(itemGroup);
        itemGroup.items().forEach(this::removeItem);
        itemGroup.otherVisuals().forEach(this::removeVisual);
    }
}
