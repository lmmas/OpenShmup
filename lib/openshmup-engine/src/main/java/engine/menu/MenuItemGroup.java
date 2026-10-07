package engine.menu;

import engine.menu.widget.Widget;
import engine.visual.Visual;
import layer.LayerMap;
import lombok.Getter;

import java.util.Map;

@Getter
public final class MenuItemGroup {

    final private LayerMap<Visual> visuals;

    final private LayerMap<Widget> widgets;

    public MenuItemGroup() {
        this.visuals = new LayerMap<>();
        this.widgets = new LayerMap<>();
    }

    public MenuItemGroup(Map<Visual, Integer> visuals, Map<Widget, Integer> widgetLayers) {
        this.visuals = new LayerMap<>(visuals);
        this.widgets = new LayerMap<>(widgetLayers);
    }

    public void addVisual(Visual visual, int layer) {
        visuals.add(visual, layer);
    }

    public void addWidget(Widget widget, int layer) {
        widgets.add(widget, layer);
    }
}
