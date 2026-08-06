package engine.menu;

import engine.EngineSystem;
import engine.menu.widget.Widget;
import engine.scene.Scene;
import engine.scene.visual.SceneVisual;

import java.util.ArrayList;

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
        ArrayList<Widget> widgetListCopy = new ArrayList<>(currentScreen.getWidgets());
        widgetListCopy.forEach(Widget::handleInputs);
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
        displayedMenuScreens.getLast().addWidget(widget);
        if (scene != null){
            widget.getVisuals().forEach(visual -> scene.addVisual(visual, displayedMenuScreens.getLast().getBackgroundLayer() + visual.getSceneLayerIndex()));
        }
    }

    public void addToCurrentScreen(SceneVisual visual) {
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        displayedMenuScreens.getLast().addVisual(visual);
        if (scene != null) {
            scene.addVisual(visual, displayedMenuScreens.getLast().getBackgroundLayer() + visual.getSceneLayerIndex());
        }
    }

    public void addToCurrentScreen(MenuElementGroup menuElementGroup) {
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        MenuScreen currentScreen = displayedMenuScreens.getLast();
        currentScreen.addElementGroup(menuElementGroup);
        if(scene != null){
            menuElementGroup.visuals().forEach(visual -> scene.addVisual(visual, currentScreen.getBackgroundLayer() + visual.getSceneLayerIndex()));
            menuElementGroup.widgets().stream().flatMap(widget -> widget.getVisuals().stream())
                .forEach(visual -> scene.addVisual(visual, currentScreen.getBackgroundLayer() + visual.getSceneLayerIndex()));
        }
    }

    public void removeFromCurrentScreen(Widget widget) {
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        displayedMenuScreens.getLast().removeWidget(widget);
        if(scene != null){
            widget.getVisuals().forEach(visual -> scene.removeVisual(visual, displayedMenuScreens.getLast().getBackgroundLayer() + visual.getSceneLayerIndex()));
        }
    }

    public void removeFromCurrentScreen(SceneVisual visual) {
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        displayedMenuScreens.getLast().removeVisual(visual);
        if(scene != null){
            scene.removeVisual(visual, displayedMenuScreens.getLast().getBackgroundLayer() + visual.getSceneLayerIndex());
        }
    }

    public void removeFromCurrentScreen(MenuElementGroup menuElementGroup) {
        assert !displayedMenuScreens.isEmpty() : "no menu screen in menu";
        MenuScreen currentScreen = displayedMenuScreens.getLast();
        currentScreen.removeElementGroup(menuElementGroup);
        if(scene != null){
            menuElementGroup.visuals().forEach(visual -> scene.removeVisual(visual, currentScreen.getBackgroundLayer() + visual.getSceneLayerIndex()));
            menuElementGroup.widgets().stream().flatMap(widget -> widget.getVisuals().stream())
                .forEach(visual -> scene.removeVisual(visual, currentScreen.getBackgroundLayer() + visual.getSceneLayerIndex()));
        }
    }
}
