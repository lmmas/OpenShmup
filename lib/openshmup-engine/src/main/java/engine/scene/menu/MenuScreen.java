package engine.scene.menu;

import engine.scene.visual.SceneVisual;

import java.util.List;

public record MenuScreen(int backgroundLayer, SceneVisual backgroundDisplay, List<MenuItem> menuItems) {

}
