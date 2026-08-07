package engine.menu;

import engine.menu.widget.Widget;
import engine.scene.visual.SceneVisual;
import lombok.Getter;
import types.LayerMap;

import java.util.Map;

@Getter
public class MenuItemGroup {

    final private LayerMap<SceneVisual> visuals;

    final private LayerMap<Widget> widgets;

    public MenuItemGroup() {
        this.visuals = new LayerMap<>();
        this.widgets = new LayerMap<>();
    }

    public MenuItemGroup(Map<SceneVisual, Integer> visuals, Map<Widget, Integer> widgetLayers) {
        this.visuals = new LayerMap<>(visuals);
        this.widgets = new LayerMap<>(widgetLayers);
    }

    public void addVisual(SceneVisual visual, int layer) {
        visuals.add(visual, layer);
    }

    public void addWidget(Widget widget, int layer) {
        widgets.add(widget, layer);
    }
}
