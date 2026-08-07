package engine.menu;

import engine.menu.widget.Widget;
import engine.scene.visual.SceneVisual;
import lombok.Getter;
import lombok.Setter;
import types.LayerMap;

@Getter
final public class MenuScreen {

    private int backgroundLayer;

    final private LayerMap<Widget> widgets;

    final private LayerMap<SceneVisual> otherVisuals;
    @Setter
    private boolean isOpen;

    public MenuScreen(int backgroundLayer) {
        this.backgroundLayer = backgroundLayer;
        this.widgets = new LayerMap<>();
        this.otherVisuals = new LayerMap<>();
        this.isOpen = false;
    }

    public void addWidget(Widget widget, int layer) {
        widgets.add(widget, layer);
    }

    public void removeWidget(Widget widget) {
        widgets.remove(widget);
    }

    public void addVisual(SceneVisual visual, int layer) {
        otherVisuals.add(visual, layer);
    }

    public void removeVisual(SceneVisual visual) {
        otherVisuals.remove(visual);
    }

    public void addElementGroup(MenuItemGroup elementGroup) {
        elementGroup.getWidgets().forEachObject(this::addWidget);
        elementGroup.getVisuals().forEachObject(this::addVisual);
    }

    public void removeElementGroup(MenuItemGroup elementGroup) {
        elementGroup.getWidgets().forEachObject((widget, layer) -> widgets.remove(widget, layer));
        elementGroup.getVisuals().forEachObject((visual, layer) -> otherVisuals.remove(visual, layer));
    }
}
