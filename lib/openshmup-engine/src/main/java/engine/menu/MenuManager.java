package engine.menu;

import engine.EngineSystem;
import engine.entity.hitbox.Hitbox;
import engine.scene.Scene;
import engine.types.Vec2D;

import java.util.ArrayList;

import static engine.Engine.inputStatesManager;

public class MenuManager implements EngineSystem {

    private final Scene scene;

    final private ArrayList<MenuScreen> displayedMenus;

    private boolean leftClickPressedOnItem;

    private MenuItem leftClickPressedItem;

    public MenuManager(Scene scene) {
        this.scene = scene;
        this.displayedMenus = new ArrayList<>();
        this.leftClickPressedOnItem = false;
        this.leftClickPressedItem = null;
    }

    @Override public void update() {
        if (displayedMenus.isEmpty()) {
            return;
        }
        Vec2D cursorPosition = inputStatesManager.getCursorPosition();
        if (leftClickPressedOnItem) {
            if (!inputStatesManager.getLeftClickState()) {
                if (leftClickPressedItem.getClickHitbox().containsPoint(cursorPosition)) {
                    leftClickPressedItem.getOnClick().run(scene);
                }
                leftClickPressedItem = null;
                leftClickPressedOnItem = false;
            }
        }
        else {
            for (MenuItem menuItem : displayedMenus.getLast().getMenuItems()) {
                Hitbox clickHitbox = menuItem.getClickHitbox();
                if (inputStatesManager.getLeftClickState() && clickHitbox.containsPoint(cursorPosition)) {
                    leftClickPressedOnItem = true;
                    leftClickPressedItem = menuItem;
                }
            }
        }
    }

    public void addMenu(MenuScreen menuScreen) {
        assert !menuScreen.isOpen() : "menu screen already open";
        menuScreen.getMenuItems().stream().flatMap(menuItem -> menuItem.getVisuals().stream())
            .forEach(scene::addVisual);
        menuScreen.getOtherVisuals().forEach(scene::addVisual);
        displayedMenus.add(menuScreen);
        menuScreen.setOpen(true);
    }

    public void removeMenu(MenuScreen menuScreen) {
        assert menuScreen.isOpen() : "menu screen not open";
        menuScreen.getMenuItems().stream().flatMap(menuItem -> menuItem.getVisuals().stream())
            .forEach(scene::removeVisual);
        menuScreen.getOtherVisuals().forEach(scene::removeVisual);
        displayedMenus.remove(menuScreen);
        menuScreen.setOpen(false);
    }
}
