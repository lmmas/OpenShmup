package engine.menu;

import engine.EngineSystem;
import engine.menu.widget.Widget;
import engine.scene.Scene;
import engine.scene.visual.SceneVisual;

import java.util.ArrayList;
import java.util.List;

public class Menu implements EngineSystem {

    private Scene scene;

    final private ArrayList<MenuScreen> displayedMenuScreens;

    private MenuScreen currentScreen;

    public Menu() {
        this.displayedMenuScreens = new ArrayList<>();
        this.currentScreen = null;
        this.scene = null;
    }

    @Override
    public void update() {
        if (displayedMenuScreens.isEmpty()) {
            return;
        }
        List<Widget> widgetListCopy = currentScreen.getWidgets().getObjectList();
        widgetListCopy.forEach(Widget::handleInputs);
    }

    @Override
    public int getUpdateIndex() {
        return 8;
    }

    private void addMenuScreenToScene(MenuScreen menuScreen) {
        assert scene != null : "no scene attached to this menu";
        menuScreen.getWidgets().forEachObject((widget, widgetLayer) ->
            scene.addVisual(widget.getVisualGroup(), menuScreen.getBackgroundLayer() + widgetLayer));
        menuScreen.getOtherVisuals().forEachObject((visual, layer) ->
            scene.addVisual(visual, menuScreen.getBackgroundLayer() + layer)
        );
    }

    public void addMenuScreen(MenuScreen menuScreen) {
        assert !menuScreen.isOpen() : "menu screen already open";
        if (scene != null) {
            addMenuScreenToScene(menuScreen);
        }
        displayedMenuScreens.add(menuScreen);
        menuScreen.setOpen(true);
        this.currentScreen = menuScreen;
    }

    public void removeMenuScreen(MenuScreen menuScreen) {
        assert menuScreen.isOpen() : "menu screen not open";
        assert displayedMenuScreens.contains(menuScreen) : "menu screen not found";
        if (scene != null) {
            menuScreen.getWidgets().forEachObject((widget, widgetLayer) -> widget.getVisualGroup().setToRemove(true));
            menuScreen.getOtherVisuals().forEachObject((visual, visualLayer) -> visual.setToRemove(true));
        }
        displayedMenuScreens.remove(menuScreen);
        menuScreen.setOpen(false);
        if (!displayedMenuScreens.isEmpty()) {
            currentScreen = displayedMenuScreens.getLast();
        }
        else {
            currentScreen = null;
        }
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        if (scene != null) {
            displayedMenuScreens.forEach(this::addMenuScreenToScene);
        }
    }

    public void addToCurrentScreen(Widget widget, int layer) {
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        currentScreen.addWidget(widget, layer);
        if (scene != null) {
            scene.addVisual(widget.getVisualGroup(), displayedMenuScreens.getLast().getBackgroundLayer() + layer);
        }
    }

    public void addToCurrentScreen(SceneVisual visual, int layer) {
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        currentScreen.addVisual(visual, layer);
        if (scene != null) {
            scene.addVisual(visual, currentScreen.getBackgroundLayer() + layer);
        }
    }

    public void addToCurrentScreen(MenuItemGroup menuItemGroup) {
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        currentScreen.addElementGroup(menuItemGroup);
        if (scene != null) {
            menuItemGroup.getVisuals().forEachObject((visual, layer) -> scene.addVisual(visual, currentScreen.getBackgroundLayer() + layer));
            menuItemGroup.getWidgets().forEachObject((widget, widgetLayer) -> scene.addVisual(widget.getVisualGroup(), currentScreen.getBackgroundLayer() + widgetLayer));
        }
    }

    public void removeFromCurrentScreen(Widget widget) {
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        currentScreen.removeWidget(widget);
        if (scene != null) {
            widget.getVisualGroup().setToRemove(true);
        }
    }

    public void removeFromCurrentScreen(SceneVisual visual) {
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        currentScreen.removeVisual(visual);
        if (scene != null) {
            visual.setToRemove(true);
        }
    }

    public void removeFromCurrentScreen(MenuItemGroup menuItemGroup) {
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        currentScreen.removeElementGroup(menuItemGroup);
        if (scene != null) {
            menuItemGroup.getVisuals().forEachObject((visual, visualLayer) -> visual.setToRemove(true));
            menuItemGroup.getWidgets().forEachObject(((widget, widgetLayer) -> widget.getVisualGroup().setToRemove(true)));
        }
    }
}
