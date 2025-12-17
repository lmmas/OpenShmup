package engine.scene.menu;

import engine.visual.SceneVisual;

import java.util.ArrayList;
import java.util.List;

public record MenuScreen(int backgroundLayer, SceneVisual backgroundDisplay, ArrayList<MenuItem> menuItems) {
    public MenuScreen(int backgroundLayer, SceneVisual backgroundDisplay, List<MenuItem> menuItems) {
        this(backgroundLayer, backgroundDisplay, new ArrayList<>(menuItems));
    }

    public MenuScreen(int backgroundLayer) {
        this(backgroundLayer, null, new ArrayList<>());
    }
}
