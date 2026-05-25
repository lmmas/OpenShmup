package engine.menu.widget;

import engine.scene.visual.SceneVisual;

import java.util.List;

public interface Widget {

    List<SceneVisual> getVisuals();

    void handleInputs();
}
