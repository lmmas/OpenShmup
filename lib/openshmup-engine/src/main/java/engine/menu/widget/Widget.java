package engine.menu.widget;

import engine.scene.visual.SceneVisual;
import layer.LayerMap;

public interface Widget {

    LayerMap<SceneVisual> getVisualLayers();

    void handleInputs();
}
