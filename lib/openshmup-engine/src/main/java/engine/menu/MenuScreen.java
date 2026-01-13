package engine.menu;

import engine.visual.SceneVisual;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
public class MenuScreen {

    private int backgroundLayer;

    final private ArrayList<MenuItem> menuItems;

    final private ArrayList<SceneVisual> otherVisuals;
    @Setter
    private boolean isOpen;

    public MenuScreen(int backgroundLayer) {
        this.backgroundLayer = backgroundLayer;
        this.menuItems = new ArrayList<>();
        this.otherVisuals = new ArrayList<>();
        this.isOpen = false;
    }

    public void addItem(MenuItem menuItem) {
        menuItems.add(menuItem);
    }

    public void addVisual(SceneVisual visual) {
        otherVisuals.add(visual);
    }
}
