package engine.menu;

import engine.EngineSystem;
import engine.menu.widget.Widget;
import engine.scene.Scene;
import engine.scene.visual.SceneVisual;

import java.util.ArrayList;

public class Menu implements EngineSystem {

    private Scene scene;

    final private ArrayList<MenuScreen> displayedMenuScreens;

    final private ArrayList<Widget> widgetsToAdd;

    final private ArrayList<SceneVisual> visualsToAdd;

    final private ArrayList<Widget> widgetsToRemove;

    final private ArrayList<SceneVisual> visualsToRemove;

    public Menu() {
        this.displayedMenuScreens = new ArrayList<>();
        this.widgetsToAdd = new ArrayList<>();
        this.visualsToAdd = new ArrayList<>();
        this.widgetsToRemove = new ArrayList<>();
        this.visualsToRemove = new ArrayList<>();
    }

    @Override
    public void update() {
        if (displayedMenuScreens.isEmpty()) {
            assert widgetsToAdd.isEmpty();
            assert widgetsToRemove.isEmpty();
            return;
        }
        MenuScreen currentScreen = displayedMenuScreens.getLast();
        currentScreen.getWidgets().forEach(Widget::handleInputs);
        MenuScreen newCurrentScreen = displayedMenuScreens.getLast();
        if (!widgetsToAdd.isEmpty()) {
            widgetsToAdd.forEach(newCurrentScreen::addWidget);
            if (scene != null) {
                widgetsToAdd.stream().flatMap(widget -> widget.getVisuals().stream())
                    .forEach(visual -> scene.addVisual(visual, newCurrentScreen.getBackgroundLayer() + visual.getSceneLayerIndex()));
            }
            widgetsToAdd.clear();
        }
        if (!visualsToAdd.isEmpty()) {
            visualsToAdd.forEach(newCurrentScreen::addVisual);
            if (scene != null) {
                visualsToAdd.forEach(visual -> scene.addVisual(visual, newCurrentScreen.getBackgroundLayer() + visual.getSceneLayerIndex()));
            }
            visualsToAdd.clear();
        }
        if (!widgetsToRemove.isEmpty()) {
            widgetsToRemove.forEach(newCurrentScreen::removeWidget);
            if (scene != null) {
                widgetsToRemove.stream().flatMap(widget -> widget.getVisuals().stream())
                    .forEach(visual -> scene.removeVisual(visual, newCurrentScreen.getBackgroundLayer() + visual.getSceneLayerIndex()));
            }
            widgetsToRemove.clear();
        }
        if (!visualsToRemove.isEmpty()) {
            visualsToRemove.forEach(newCurrentScreen::removeVisual);
            if (scene != null) {
                visualsToRemove.forEach(visual -> scene.removeVisual(visual, newCurrentScreen.getBackgroundLayer() + visual.getSceneLayerIndex()));
            }
            visualsToRemove.clear();
        }
    }

    @Override
    public int getUpdateIndex() {
        return 8;
    }

    private void addMenuScreenToScene(MenuScreen menuScreen) {
        assert scene != null : "no scene attached to this menu";
        menuScreen.getWidgets().stream().flatMap(widget -> widget.getVisuals().stream())
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
        menuScreen.getWidgets().stream().flatMap(widget -> widget.getVisuals().stream())
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

    public void addToCurrentScreen(Widget widget) {
        widgetsToAdd.add(widget);
    }

    public void addToCurrentScreen(SceneVisual visual) {
        visualsToAdd.add(visual);
    }

    public void addToCurrentScreen(MenuElementGroup menuElementGroup) {
        widgetsToAdd.addAll(menuElementGroup.widgets());
        visualsToAdd.addAll(menuElementGroup.visuals());
    }

    public void removeFromCurrentScreen(Widget widget) {
        widgetsToRemove.add(widget);
    }

    public void removeFromCurrentScreen(SceneVisual visual) {
        visualsToRemove.add(visual);
    }

    public void removeFromCurrentScreen(MenuElementGroup menuElementGroup) {
        widgetsToRemove.addAll(menuElementGroup.widgets());
        visualsToRemove.addAll(menuElementGroup.visuals());
    }
}
