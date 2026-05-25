package engine.menu;

import engine.menu.item.MenuItem;
import engine.scene.visual.SceneVisual;

import java.util.ArrayList;
import java.util.List;

public record ItemGroup(
    List<MenuItem> items,
    List<SceneVisual> otherVisuals
) {

    public ItemGroup() {
        this(new ArrayList<>(), new ArrayList<>());
    }
}
