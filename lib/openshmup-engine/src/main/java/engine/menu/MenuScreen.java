package engine.menu;

import engine.menu.widget.Widget;
import engine.scene.visual.SceneVisual;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter final public class MenuScreen {

    private int backgroundLayer;

    final private ArrayList<Widget> widgets;

    final private ArrayList<SceneVisual> otherVisuals;
    @Setter
    private boolean isOpen;

    public MenuScreen(int backgroundLayer) {
        this.backgroundLayer = backgroundLayer;
        this.widgets = new ArrayList<>();
        this.otherVisuals = new ArrayList<>();
        this.isOpen = false;
    }

    public void addWidget(Widget widget) {
        assert !widgets.contains(widget) : "item is already present in item list of the screen";
        widgets.add(widget);
    }

    public void removeWidget(Widget widget) {
        assert widgets.contains(widget) : "item is not present in item list of the screen";
        widgets.remove(widget);
    }

    public void addVisual(SceneVisual visual) {
        assert !otherVisuals.contains(visual) : "visual is already present in visual list of the screen";
        otherVisuals.add(visual);
    }

    public void removeVisual(SceneVisual visual) {
        assert otherVisuals.contains(visual) : "visual is not present in visual list of the screen";
        otherVisuals.remove(visual);
    }

    public void addElementGroup(MenuElementGroup elementGroup) {
        elementGroup.widgets().forEach(this::addWidget);
        elementGroup.visuals().forEach(this::addVisual);
    }

    public void removeElementGroup(MenuElementGroup elementGroup) {
        elementGroup.widgets().forEach(this::removeWidget);
        elementGroup.visuals().forEach(this::removeVisual);
    }
}
