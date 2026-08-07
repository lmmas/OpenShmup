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

    public Menu() {
        this.displayedMenuScreens = new ArrayList<>();
    }

    @Override
    public void update() {
        if (displayedMenuScreens.isEmpty()) {
            return;
        }
        MenuScreen currentScreen = displayedMenuScreens.getLast();
        List<Widget> widgetListCopy = currentScreen.getWidgets().getObjectList();
        widgetListCopy.forEach(Widget::handleInputs);
    }

    @Override
    public int getUpdateIndex() {
        return 8;
    }

    private void addMenuScreenToScene(MenuScreen menuScreen) {
        assert scene != null : "no scene attached to this menu";
        menuScreen.getWidgets().forEachObject((widget, widgetLayer) -> widget.getVisualLayers()
            .forEachObject((visual, visualLayer) -> scene.addVisual(visual, menuScreen.getBackgroundLayer() + widgetLayer + visualLayer)));
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
    }

    private void removeMenuScreenFromScene(MenuScreen menuScreen) {
        assert scene != null : "no scene attached to this menu";
        menuScreen.getWidgets().forEachObject((widget, widgetLayer) -> widget.getVisualLayers()
            .forEachObject((visual, visualLayer) -> scene.removeVisual(visual, menuScreen.getBackgroundLayer() + widgetLayer + visualLayer)));
        menuScreen.getOtherVisuals().forEachObject((visual, visualLayer) -> scene.removeVisual(visual, menuScreen.getBackgroundLayer() + visualLayer));
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

    public void addToCurrentScreen(Widget widget, int layer) {
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        displayedMenuScreens.getLast().addWidget(widget, layer);
        if (scene != null) {
            widget.getVisualLayers().forEachObject((visual, visualLayer) -> scene.addVisual(visual, displayedMenuScreens.getLast().getBackgroundLayer() + layer + visualLayer));
        }
    }

    public void addToCurrentScreen(SceneVisual visual, int layer) {
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        displayedMenuScreens.getLast().addVisual(visual, layer);
        if (scene != null) {
            scene.addVisual(visual, displayedMenuScreens.getLast().getBackgroundLayer() + layer);
        }
    }

    public void addToCurrentScreen(MenuItemGroup menuItemGroup) {
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        MenuScreen currentScreen = displayedMenuScreens.getLast();
        currentScreen.addElementGroup(menuItemGroup);
        if (scene != null) {
            menuItemGroup.getVisuals().forEachObject((visual, layer) -> scene.addVisual(visual, currentScreen.getBackgroundLayer() + layer));
            menuItemGroup.getWidgets().forEachObject((widget, widgetLayer) -> widget.getVisualLayers()
                .forEachObject((visual, visualLayer) -> scene.addVisual(visual, currentScreen.getBackgroundLayer() + widgetLayer + visualLayer)));
        }
    }

    public void removeFromCurrentScreen(Widget widget) {
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        MenuScreen currentScreen = displayedMenuScreens.getLast();
        int widgetLayer = currentScreen.getWidgets().getLayerOfObject(widget);
        currentScreen.removeWidget(widget);
        if (scene != null) {
            widget.getVisualLayers().forEachObject((visual, visualLayer) -> scene.removeVisual(visual, currentScreen.getBackgroundLayer() + widgetLayer + visualLayer));
        }
    }

    public void removeFromCurrentScreen(SceneVisual visual) {
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        MenuScreen currentScreen = displayedMenuScreens.getLast();
        int visualLayer = currentScreen.getOtherVisuals().getLayerOfObject(visual);
        currentScreen.removeVisual(visual);
        if (scene != null) {
            scene.removeVisual(visual, currentScreen.getBackgroundLayer() + visualLayer);
        }
    }

    public void removeFromCurrentScreen(MenuItemGroup menuItemGroup) {
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        MenuScreen currentScreen = displayedMenuScreens.getLast();
        currentScreen.removeElementGroup(menuItemGroup);
        if (scene != null) {
            menuItemGroup.getVisuals().forEachObject((visual, visualLayer) -> scene.removeVisual(visual, currentScreen.getBackgroundLayer() + visualLayer));
            menuItemGroup.getWidgets().forEachObject(((widget, widgetLayer) -> widget.getVisualLayers()
                .forEachObject((visual, visualLayer) -> scene.removeVisual(visual, currentScreen.getBackgroundLayer() + widgetLayer + visualLayer))));
        }
    }
}
