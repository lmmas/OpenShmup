package engine.scene;

import engine.entity.hitbox.Hitbox;
import engine.graphics.Graphic;
import engine.scene.menu.MenuItem;
import engine.scene.menu.MenuScreen;
import engine.types.RGBAValue;
import engine.types.Vec2D;
import engine.visual.SceneVisual;
import engine.visual.TextDisplay;
import engine.visual.style.TextStyle;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import static engine.Application.graphicsManager;
import static engine.Application.inputStatesManager;
import static engine.GlobalVars.Paths.debugFont;
import static engine.GlobalVars.debugDisplayLayer;


abstract public class Scene {
    protected float sceneTime;
    final protected SceneTimer timer;
    protected float lastDrawTime = 0.0f;
    final protected TreeMap<Integer, ArrayList<SceneVisual>> visualLayers;
    final protected HashSet<SceneVisual> visualsToRemove;
    final protected ArrayList<MenuScreen> displayedMenus;
    protected boolean debugModeEnabled = false;
    protected boolean leftClickPressedOnItem = false;
    protected MenuItem leftClickPressedItem = null;
    final protected SceneDebug sceneDebug;

    public Scene() {
        this.sceneTime = 0.0f;
        this.timer = new SceneTimer();
        this.visualLayers = new TreeMap<>();
        this.visualsToRemove = new HashSet<>();
        this.displayedMenus = new ArrayList<>();
        this.sceneDebug = new SceneDebug(false);
    }

    public void handleInputs() {
        if (displayedMenus.isEmpty()) {
            return;
        }
        Vec2D cursorPosition = inputStatesManager.getCursorPosition();
        if (leftClickPressedOnItem) {
            if (!inputStatesManager.getLeftClickState()) {
                if (leftClickPressedItem.getClickHitbox().containsPoint(cursorPosition)) {
                    leftClickPressedItem.onClick();
                }
                leftClickPressedItem = null;
                leftClickPressedOnItem = false;
            }
        }
        else {
            for (MenuItem menuItem : displayedMenus.getLast().menuItems()) {
                Hitbox clickHitbox = menuItem.getClickHitbox();
                if (inputStatesManager.getLeftClickState() && clickHitbox.containsPoint(cursorPosition)) {
                    leftClickPressedOnItem = true;
                    leftClickPressedItem = menuItem;
                }
            }
        }
    }

    public void update() {
        if (!timer.isPaused()) {
            sceneTime = timer.getTimeSeconds();
            for (var visualLayer : visualLayers.values()) {
                for (SceneVisual visual : visualLayer) {
                    visual.update(sceneTime);
                    if (visual.shouldBeRemoved()) {
                        visualsToRemove.add(visual);
                    }
                    if (visual.getReloadGraphicsFlag()) {
                        int sceneLayerGraphicalIndex = getSceneLayerGraphicalIndex(visual.getSceneLayer());
                        var graphicalLayers = visual.getGraphicalSubLayers();
                        var graphics = visual.getGraphics();
                        for (int i = 0; i < graphicalLayers.size(); i++) {
                            graphicsManager.addGraphic(graphics.get(i), sceneLayerGraphicalIndex + graphicalLayers.get(i));
                        }

                        visual.setReloadGraphicsFlag(false);
                    }
                }
            }
            for (var display : visualsToRemove) {
                removeVisual(display);
            }
            visualsToRemove.clear();
        }
        sceneDebug.update();
        lastDrawTime = sceneTime;
    }


    final public void addVisual(SceneVisual visual) {
        int visualMaxGraphicalSubLayer = visual.getMaxGraphicalSubLayer();
        int sceneLayerIndex = visual.getSceneLayer();
        int sceneLayerGraphicalIndex = getSceneLayerGraphicalIndex(sceneLayerIndex);

        //determining how many graphical layers need to be inserted
        int graphicalLayersToInsertCount = 0;
        int sceneLayerMaxGraphicalSubLayer = -1; // -1 corresponds to no sublayer present, it's different from 0 which means 1 sublayer (of index 0)
        if (!visualLayers.containsKey(sceneLayerIndex)) {
            visualLayers.put(sceneLayerIndex, new ArrayList<>());
            graphicalLayersToInsertCount = visualMaxGraphicalSubLayer + 1;
        }
        else {
            var sceneLayer = visualLayers.get(sceneLayerIndex);
            sceneLayerMaxGraphicalSubLayer = sceneLayer.stream()
                .flatMap(sceneVisual -> sceneVisual.getGraphicalSubLayers().stream())
                .mapToInt(n -> n).max().orElse(0);
            if (visualMaxGraphicalSubLayer > sceneLayerMaxGraphicalSubLayer) {
                graphicalLayersToInsertCount = visualMaxGraphicalSubLayer - sceneLayerMaxGraphicalSubLayer;
            }
        }

        //inserting the graphical layers
        for (int i = 0; i < graphicalLayersToInsertCount; i++) {
            graphicsManager.insertNewLayer(sceneLayerGraphicalIndex + sceneLayerMaxGraphicalSubLayer + 1);
        }

        //adding the graphics to the renderers
        var graphicalLayers = visual.getGraphicalSubLayers();
        var graphics = visual.getGraphics();
        for (int i = 0; i < graphicalLayers.size(); i++) {
            graphicsManager.addGraphic(graphics.get(i), sceneLayerGraphicalIndex + graphicalLayers.get(i));
        }

        visualLayers.get(sceneLayerIndex).add(visual);
        visual.initDisplay(this.sceneTime);
    }

    private Integer getSceneLayerGraphicalIndex(int sceneLayerIndex) {
        int layerSum = 0;
        for (var layerIndex : visualLayers.keySet()) {
            if (layerIndex >= sceneLayerIndex) {
                break;
            }
            if (!visualLayers.get(layerIndex).isEmpty()) {
                layerSum += visualLayers.get(layerIndex).stream()
                    .mapToInt(SceneVisual::getMaxGraphicalSubLayer).max().orElse(0) + 1;
            }
        }
        return layerSum;
    }

    final public void removeVisual(SceneVisual visual) {
        int sceneLayerIndex = visual.getSceneLayer();
        visualLayers.get(sceneLayerIndex).remove(visual);
        List<Graphic<?, ?>> graphics = visual.getGraphics();
        for (var graphic : graphics) {
            graphic.remove();
        }
    }

    final public float getSceneTimeSeconds() {
        return sceneTime;
    }

    final public void setSpeed(float speed) {
        timer.setSpeed(speed);
    }

    final public void addMenu(MenuScreen menuScreen) {
        menuScreen.menuItems().stream().flatMap(menuItem -> menuItem.getVisuals().stream())
            .forEach(this::addVisual);
        SceneVisual menuBackground = menuScreen.backgroundDisplay();
        if (menuBackground != null) {
            addVisual(menuBackground);
        }
        displayedMenus.add(menuScreen);
    }

    final public void removeMenu(MenuScreen menuScreen) {
        menuScreen.menuItems().stream().flatMap(menuItem -> menuItem.getVisuals().stream())
            .forEach(this::removeVisual);
        SceneVisual menuBackground = menuScreen.backgroundDisplay();
        if (menuBackground != null) {
            removeVisual(menuBackground);
        }
        displayedMenus.remove(menuScreen);
    }

    protected class SceneDebug {
        private TextDisplay fpsDisplay;
        final private RGBAValue fpsDisplayTextColor = new RGBAValue(1.0f, 1.0f, 1.0f, 1.0f);

        public SceneDebug(boolean debugModeEnabled) {
            if (debugModeEnabled) {
                this.enable();
            }
        }

        public void enable() {
            TextStyle fpsDisplayTextStyle = new TextStyle(debugFont, fpsDisplayTextColor, 0.02f);
            this.fpsDisplay = new TextDisplay(debugDisplayLayer, true, 0.9f, 0.9f, "", fpsDisplayTextStyle);
            Scene.this.addVisual(fpsDisplay);
        }

        public void disable() {
            Scene.this.removeVisual(fpsDisplay);
        }

        public void toggle() {
            if (debugModeEnabled) {
                this.disable();
            }
            else {
                this.enable();
            }
        }

        public void update() {
            if (debugModeEnabled) {
                DecimalFormat df = new DecimalFormat("#");
                df.setRoundingMode(RoundingMode.HALF_DOWN);
                float fpsVal = 1 / (sceneTime - lastDrawTime);
                fpsDisplay.setDisplayedString(df.format(fpsVal) + " FPS");
            }
        }
    }

}
