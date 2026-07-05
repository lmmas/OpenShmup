package editor.fieldNode;

import engine.menu.Menu;

public sealed interface FieldNode permits EditionDataFieldNode, ListFields {

    void setMenu(Menu menu);

    void setActive(boolean active);

    void applyChanges();
}
