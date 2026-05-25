package engine.menu;

import engine.EngineSystem;
import engine.menu.item.MenuItem;
import engine.scene.Scene;
import engine.scene.visual.SceneVisual;

import java.util.ArrayList;

public class Menu implements EngineSystem {

    private Scene scene;

    final private ArrayList<MenuScreen> displayedMenuScreens;

    final private ArrayList<MenuItem> itemsToAdd;

    final private ArrayList<SceneVisual> visualsToAdd;

    final private ArrayList<MenuItem> itemsToRemove;

    final private ArrayList<SceneVisual> visualsToRemove;

    public Menu() {
        this.displayedMenuScreens = new ArrayList<>();
        this.itemsToAdd = new ArrayList<>();
        this.visualsToAdd = new ArrayList<>();
        this.itemsToRemove = new ArrayList<>();
        this.visualsToRemove = new ArrayList<>();
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
            itemsToAdd.forEach(currentMenuScreen::addItem);
            if (scene != null) {
                itemsToAdd.stream().flatMap(menuItem -> menuItem.getVisuals().stream())
                    .forEach(visual -> scene.addVisual(visual, currentMenuScreen.getBackgroundLayer() + visual.getSceneLayerIndex()));
            }
            itemsToAdd.clear();
        }
        if (!visualsToAdd.isEmpty()) {
            visualsToAdd.forEach(currentMenuScreen::addVisual);
            if (scene != null) {
                visualsToAdd.forEach(visual -> scene.addVisual(visual, currentMenuScreen.getBackgroundLayer() + visual.getSceneLayerIndex()));
            }
            visualsToAdd.clear();
        }
        if (!itemsToRemove.isEmpty()) {
            itemsToRemove.forEach(currentMenuScreen::removeItem);
                if (scene != null) {
                    itemsToRemove.stream().flatMap(menuItem -> menuItem.getVisuals().stream())
                        .forEach(visual -> scene.removeVisual(visual, currentMenuScreen.getBackgroundLayer() + visual.getSceneLayerIndex()));
                }
            itemsToRemove.clear();
        }
        if (!visualsToRemove.isEmpty()) {
            visualsToRemove.forEach(currentMenuScreen::removeVisual);
            if (scene != null) {
                visualsToRemove.forEach(visual -> scene.removeVisual(visual, currentMenuScreen.getBackgroundLayer() + visual.getSceneLayerIndex()));
            }
            visualsToRemove.clear();
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

    public void addToCurrentScreen(MenuItem menuItem) {
        itemsToAdd.add(menuItem);
    }

    public void addToCurrentScreen(SceneVisual visual) {
        visualsToAdd.add(visual);
    }

    public void addToCurrentScreen(ItemGroup itemGroup) {
        itemsToAdd.addAll(itemGroup.items());
        visualsToAdd.addAll(itemGroup.otherVisuals());
    }

    public void removeFromCurrentScreen(MenuItem menuItem) {
        itemsToRemove.add(menuItem);
    }

    public void removeFromCurrentScreen(SceneVisual visual) {
        visualsToRemove.add(visual);
    }

    public void removeFromCurrentScreen(ItemGroup itemGroup) {
        itemsToRemove.addAll(itemGroup.items());
        visualsToRemove.addAll(itemGroup.otherVisuals());
    }
}
