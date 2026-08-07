package engine.menu.widget;

import engine.scene.visual.SceneVisual;
import types.LayerMap;

public interface Widget {

    LayerMap<SceneVisual> getVisualLayers();

    void handleInputs();
}
