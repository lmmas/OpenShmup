package engine.menu.item;

import engine.scene.visual.SceneVisual;

import java.util.List;

public interface MenuItem {

    List<SceneVisual> getVisuals();

    void handleInputs();
}
