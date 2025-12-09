package engine.scene.menu;

import engine.visual.SceneVisual;

import java.util.Collections;
import java.util.List;

public record MenuScreen(int backgroundLayer, SceneVisual backgroundDisplay, List<MenuItem> menuItems) {
    public MenuScreen(int backgroundLayer) {
        this(backgroundLayer, null, Collections.emptyList());
    }
}
