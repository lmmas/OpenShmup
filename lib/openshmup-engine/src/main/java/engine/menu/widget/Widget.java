package engine.menu.widget;

import engine.scene.visual.style.VisualGroup;

public interface Widget {

    VisualGroup getVisualGroup();

    void handleInputs();
}
