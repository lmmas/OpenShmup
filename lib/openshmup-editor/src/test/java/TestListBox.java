import editor.ListBox;
import engine.Engine;
import engine.menu.Menu;
import engine.menu.MenuScreen;
import types.IVec2D;
import types.Vec2D;

import java.io.IOException;
import java.util.List;


public class TestListBox {
    public static void main(String[] args) throws IOException {
        Engine.init();

        Menu menu = new Menu();
        MenuScreen menuScreen = new MenuScreen(0);
        Vec2D listSize = new Vec2D(300f, 175f);
        Vec2D listPosition = new Vec2D(500f, 1000f);
        List<String> labels = List.of("Test 1", "Test 2", "Test 3");
        menuScreen.addWidget(new ListBox(listSize, listPosition, labels), 1);
        menu.addMenuScreen(menuScreen);

        Engine.setNativeResolution(new IVec2D(1000, 1000));
        Engine.initGraphicsManager();
        Engine.initInputStatesManager();
        Engine.switchCurrentMenu(menu);
        Engine.window.show();
        Engine.run();
    }
}
