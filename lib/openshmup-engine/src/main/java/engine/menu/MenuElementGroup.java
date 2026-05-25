package engine.menu;

import engine.menu.widget.Widget;
import engine.scene.visual.SceneVisual;

import java.util.ArrayList;
import java.util.List;

public record MenuElementGroup(
    List<Widget> widgets,
    List<SceneVisual> visuals
) {

    public MenuElementGroup() {
        this(new ArrayList<>(), new ArrayList<>());
    }
}
