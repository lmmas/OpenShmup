package engine.menu;

import engine.EngineSystem;
import engine.menu.item.MenuItem;
import engine.scene.Scene;

import java.util.ArrayList;

public class Menu implements EngineSystem {

    private Scene scene;

    final private ArrayList<MenuScreen> displayedMenuScreens;

    final private ArrayList<ItemGroup> itemsToAdd;

    final private ArrayList<ItemGroup> itemsToRemove;

    public Menu() {
        this.displayedMenuScreens = new ArrayList<>();
        this.itemsToAdd = new ArrayList<>();
        this.itemsToRemove = new ArrayList<>();
    }

    @Override public void update() {
        if (displayedMenuScreens.isEmpty()) {
            assert itemsToAdd.isEmpty();
            assert itemsToRemove.isEmpty();
            return;
        }
        MenuScreen currentMenuScreen = displayedMenuScreens.getLast();
        currentMenuScreen.getMenuItems().forEach(MenuItem::handleInputs);
        if (!itemsToAdd.isEmpty()) {
            for (ItemGroup itemGroup : itemsToAdd) {
                currentMenuScreen.addItemGroup(itemGroup);
                if (scene != null) {
                    itemGroup.items().stream().flatMap(menuItem -> menuItem.getVisuals().stream())
                        .forEach(visual -> scene.addVisual(visual));
                    itemGroup.otherVisuals().forEach(visual -> scene.addVisual(visual));
                }
            }
            itemsToAdd.clear();
        }
        if (!itemsToRemove.isEmpty()) {
            for (ItemGroup itemGroup : itemsToRemove) {
                currentMenuScreen.removeItemGroup(itemGroup);
                if (scene != null) {
                    itemGroup.items().stream().flatMap(menuItem -> menuItem.getVisuals().stream())
                        .forEach(visual -> scene.removeVisual(visual));
                    itemGroup.otherVisuals().forEach(visual -> scene.removeVisual(visual));
                }
            }
            itemsToRemove.clear();
        }
    }

    @Override public int getUpdateIndex() {
        return 8;
    }

    private void addMenuScreenToScene(MenuScreen menuScreen) {
        assert scene != null : "no scene attached to this menu";
        menuScreen.getMenuItems().stream().flatMap(menuItem -> menuItem.getVisuals().stream())
            .forEach(visual -> scene.addVisual(visual, menuScreen.getBackgroundLayer() + visual.getSceneLayerIndex())
        );
        menuScreen.getOtherVisuals().forEach(visual ->
            scene.addVisual(visual, menuScreen.getBackgroundLayer() + visual.getSceneLayerIndex())
        );
    }

    public void addMenuScreen(MenuScreen menuScreen) {
        assert !menuScreen.isOpen() : "menu screen already open";
        if (scene != null) {
            addMenuScreenToScene(menuScreen);
        }
        displayedMenuScreens.add(menuScreen);
        menuScreen.setOpen(true);
    }

    private void removeMenuScreenFromScene(MenuScreen menuScreen) {
        assert scene != null : "no scene attached to this menu";
        menuScreen.getMenuItems().stream().flatMap(menuItem -> menuItem.getVisuals().stream())
            .forEach(visual -> scene.removeVisual(visual, menuScreen.getBackgroundLayer() + visual.getSceneLayerIndex()));
        menuScreen.getOtherVisuals().forEach(visual -> scene.removeVisual(visual, menuScreen.getBackgroundLayer() + visual.getSceneLayerIndex()));
    }

    public void removeMenuScreen(MenuScreen menuScreen) {
        assert menuScreen.isOpen() : "menu screen not open";
        if (scene != null) {
            removeMenuScreenFromScene(menuScreen);
        }
        displayedMenuScreens.remove(menuScreen);
        menuScreen.setOpen(false);
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        if (scene != null) {
            displayedMenuScreens.forEach(this::addMenuScreenToScene);
        }
    }

    public void addItemGroupToCurrentScreen(ItemGroup itemGroup) {
        itemsToAdd.add(itemGroup);
    }

    public void removeItemGroupFromCurrentScreen(ItemGroup itemGroup) {
        itemsToRemove.add(itemGroup);
    }
}
