package engine.menu;

import engine.menu.widget.Widget;
import engine.scene.visual.SceneVisual;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MenuItemGroup {
    final private HashMap<SceneVisual, Integer> visualMap;
    final private HashMap<Widget, Integer> widgetMap;

    public MenuItemGroup() {
        this.visualMap = new HashMap<>();
        this.widgetMap = new HashMap<>();
    }

    public MenuItemGroup(Map<SceneVisual, Integer> visualMap, Map<Widget, Integer> widgetMap) {
        this.visualMap = new HashMap<>(visualMap);
        this.widgetMap = new HashMap<>(widgetMap);
    }

    public Set<SceneVisual> getVisuals() {
        return visualMap.keySet();
    }

    public Set<Widget> getWidgets() {
        return widgetMap.keySet();
    }

    public Set<Map.Entry<SceneVisual, Integer>> getVisualEntries() {
        return visualMap.entrySet();
    }

    public Set<Map.Entry<Widget, Integer>> getWidgetEntries() {
        return widgetMap.entrySet();
    }

    public void addVisual(SceneVisual visual, int layer) {
        assert !visualMap.containsKey(visual) : "visual already in group";
        visualMap.put(visual, layer);
    }

    public void addWidget(Widget widget, int layer) {
        assert !widgetMap.containsKey(widget) : "widget already in group";
        widgetMap.put(widget, layer);
    }
}
