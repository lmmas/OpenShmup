package engine.menu;

import engine.menu.widget.Widget;
import engine.scene.visual.SceneVisual;

import java.util.ArrayList;
import java.util.List;

public record MenuElementGroup(
    ArrayList<Widget> widgets,
    ArrayList<SceneVisual> visuals
) {

    public MenuElementGroup(List<? extends Widget> widgets, List<? extends SceneVisual> visuals) {
        this(new ArrayList<>(widgets), new ArrayList<>(visuals));
    }

    public MenuElementGroup() {
        this(new ArrayList<>(), new ArrayList<>());
    }
}
