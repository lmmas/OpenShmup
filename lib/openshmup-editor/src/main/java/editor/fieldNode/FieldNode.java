package editor.fieldNode;

import engine.menu.ItemGroup;
import engine.menu.Menu;

public interface FieldNode {

    void setMenu(Menu menu);

    ItemGroup getAllActiveItems();

    void setActive(boolean active);

    void applyChanges();
}
