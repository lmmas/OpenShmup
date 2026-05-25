package editor.fieldNode;

import engine.menu.Menu;
import engine.menu.MenuElementGroup;

public interface FieldNode {

    void setMenu(Menu menu);

    MenuElementGroup getAllActiveElements();

    void setActive(boolean active);

    void applyChanges();
}
