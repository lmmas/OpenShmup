package engine.scene.menu;

import engine.visual.SceneVisual;

import java.util.ArrayList;
import java.util.List;

public record MenuScreen(int backgroundLayer, ArrayList<MenuItem> menuItems, ArrayList<SceneVisual> otherVisuals) {
    public MenuScreen(int backgroundLayer, List<MenuItem> menuItems, List<SceneVisual> otherVisuals) {
        this(backgroundLayer, new ArrayList<>(menuItems), new ArrayList<>(otherVisuals));
    }

    public MenuScreen(int backgroundLayer) {
        this(backgroundLayer, new ArrayList<>(), new ArrayList<>());
    }
}
