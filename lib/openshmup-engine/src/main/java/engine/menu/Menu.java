package engine.menu;

import engine.EngineSystem;
import engine.menu.widget.Widget;
import engine.scene.Scene;
import engine.scene.visual.SceneVisual;

import java.util.ArrayList;
import java.util.HashMap;

public class Menu implements EngineSystem {

    private Scene scene;

    final private ArrayList<MenuScreen> displayedMenuScreens;

    final private HashMap<MenuScreen, ArrayList<Widget>> widgetsToAddMap;

    final private HashMap<MenuScreen, ArrayList<SceneVisual>> visualsToAddMap;

    final private HashMap<MenuScreen, ArrayList<Widget>> widgetsToRemoveMap;

    final private HashMap<MenuScreen, ArrayList<SceneVisual>> visualsToRemoveMap;

    public Menu() {
        this.displayedMenuScreens = new ArrayList<>();
        this.widgetsToAddMap = new HashMap<>();
        this.visualsToAddMap = new HashMap<>();
        this.widgetsToRemoveMap = new HashMap<>();
        this.visualsToRemoveMap = new HashMap<>();
    }

    @Override
    public void update() {
        if (displayedMenuScreens.isEmpty()) {
            return;
        }
        MenuScreen currentScreen = displayedMenuScreens.getLast();
        currentScreen.getWidgets().forEach(Widget::handleInputs);
        for (var entry : widgetsToAddMap.entrySet()) {
            MenuScreen menuScreen = entry.getKey();
            ArrayList<Widget> widgetsToAddList = entry.getValue();
            widgetsToAddList.forEach(menuScreen::addWidget);
            if (scene != null) {
                widgetsToAddList.stream().flatMap(widget -> widget.getVisuals().stream())
                    .forEach(visual -> scene.addVisual(visual, menuScreen.getBackgroundLayer() + visual.getSceneLayerIndex()));
            }
            widgetsToAddList.clear();
        }
        for (var entry : visualsToAddMap.entrySet()) {
            MenuScreen menuScreen = entry.getKey();
            ArrayList<SceneVisual> visualsToAddList = entry.getValue();
            visualsToAddList.forEach(menuScreen::addVisual);
            if (scene != null) {
                visualsToAddList.forEach(visual -> scene.addVisual(visual, menuScreen.getBackgroundLayer() + visual.getSceneLayerIndex()));
            }
            visualsToAddList.clear();
        }
        for (var entry : widgetsToRemoveMap.entrySet()) {
            MenuScreen menuScreen = entry.getKey();
            ArrayList<Widget> widgetsToRemoveList = entry.getValue();
            widgetsToRemoveList.forEach(menuScreen::removeWidget);
            if (scene != null) {
                widgetsToRemoveList.stream().flatMap(widget -> widget.getVisuals().stream())
                    .forEach(visual -> scene.removeVisual(visual, menuScreen.getBackgroundLayer() + visual.getSceneLayerIndex()));
            }
            widgetsToRemoveList.clear();
        }
        for (var entry : visualsToRemoveMap.entrySet()) {
            MenuScreen menuScreen = entry.getKey();
            ArrayList<SceneVisual> visualsToRemoveList = entry.getValue();
            visualsToRemoveList.forEach(menuScreen::removeVisual);
            if (scene != null) {
                visualsToRemoveList.forEach(visual -> scene.removeVisual(visual, menuScreen.getBackgroundLayer() + visual.getSceneLayerIndex()));
            }
            visualsToRemoveList.clear();
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
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        widgetsToAddMap.computeIfAbsent(displayedMenuScreens.getLast(), k -> new ArrayList<>()).add(widget);
    }

    public void addToCurrentScreen(SceneVisual visual) {
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        visualsToAddMap.computeIfAbsent(displayedMenuScreens.getLast(), k -> new ArrayList<>()).add(visual);
    }

    public void addToCurrentScreen(MenuElementGroup menuElementGroup) {
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        widgetsToAddMap.computeIfAbsent(displayedMenuScreens.getLast(), k -> new ArrayList<>()).addAll(menuElementGroup.widgets());
        visualsToAddMap.computeIfAbsent(displayedMenuScreens.getLast(), k -> new ArrayList<>()).addAll(menuElementGroup.visuals());
    }

    public void removeFromCurrentScreen(Widget widget) {
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        widgetsToRemoveMap.computeIfAbsent(displayedMenuScreens.getLast(), k -> new ArrayList<>()).add(widget);
    }

    public void removeFromCurrentScreen(SceneVisual visual) {
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        visualsToAddMap.computeIfAbsent(displayedMenuScreens.getLast(), k -> new ArrayList<>()).add(visual);
    }

    public void removeFromCurrentScreen(MenuElementGroup menuElementGroup) {
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        widgetsToRemoveMap.computeIfAbsent(displayedMenuScreens.getLast(), k -> new ArrayList<>()).addAll(menuElementGroup.widgets());
        visualsToRemoveMap.computeIfAbsent(displayedMenuScreens.getLast(), k -> new ArrayList<>()).addAll(menuElementGroup.visuals());
    }
}
