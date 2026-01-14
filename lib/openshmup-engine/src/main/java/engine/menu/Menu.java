package engine.menu;

import engine.Engine;
import engine.EngineSystem;
import engine.InputStatesManager;
import engine.entity.hitbox.Hitbox;
import engine.scene.Scene;
import engine.types.Vec2D;

import java.util.ArrayList;

public class Menu implements EngineSystem {

    private Scene scene;

    final private ArrayList<MenuScreen> displayedMenuScreens;

    private boolean leftClickPressedOnItem;

    private MenuItem leftClickPressedItem;

    public Menu() {
        this.displayedMenuScreens = new ArrayList<>();
        this.leftClickPressedOnItem = false;
        this.leftClickPressedItem = null;
    }

    @Override public void update() {
        if (displayedMenuScreens.isEmpty()) {
            return;
        }
        InputStatesManager inputStatesManager = Engine.getInputStatesManager();
        Vec2D cursorPosition = inputStatesManager.getCursorPosition();
        if (leftClickPressedOnItem) {
            if (!inputStatesManager.getLeftClickState()) {
                if (leftClickPressedItem.getClickHitbox().containsPoint(cursorPosition)) {
                    leftClickPressedItem.getOnClick().run();
                }
                leftClickPressedItem = null;
                leftClickPressedOnItem = false;
            }
        }
        else {
            for (MenuItem menuItem : displayedMenuScreens.getLast().getMenuItems()) {
                Hitbox clickHitbox = menuItem.getClickHitbox();
                if (inputStatesManager.getLeftClickState() && clickHitbox.containsPoint(cursorPosition)) {
                    leftClickPressedOnItem = true;
                    leftClickPressedItem = menuItem;
                }
            }
        }
    }

    private void addMenuScreenToScene(MenuScreen menuScreen) {
        assert scene != null : "no scene attached to this menu";
        menuScreen.getMenuItems().stream().flatMap(menuItem -> menuItem.getVisuals().stream())
            .forEach(scene::addVisual);
        menuScreen.getOtherVisuals().forEach(scene::addVisual);
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
            .forEach(scene::removeVisual);
        menuScreen.getOtherVisuals().forEach(scene::removeVisual);
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
}
