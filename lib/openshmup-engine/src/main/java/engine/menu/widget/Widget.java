package engine.menu.widget;

import engine.visual.VisualGroup;

public interface Widget {

    VisualGroup getVisualGroup();

    void handleInputs();
}
