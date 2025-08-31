package engine.scene.menu;

import engine.scene.display.SceneDisplay;

import java.util.List;

public record MenuScreen(int backgroundLayer, SceneDisplay backgroundDisplay, List<MenuItem> menuItems) {

}
