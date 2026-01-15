package engine.scene;

import engine.Engine;
import engine.EngineSystem;
import engine.Timer;
import engine.graphics.Graphic;
import engine.graphics.GraphicsManager;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.TextAlignment;
import engine.scene.visual.style.TextStyle;
import engine.types.RGBAValue;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import static engine.GlobalVars.Paths.debugFont;


public class Scene implements EngineSystem {

    final protected Timer timer;

    protected double sceneTime;

    protected double lastDrawTime;

    final protected TreeMap<Integer, SceneLayer> layers;

    final protected HashSet<SceneVisual> visualsToRemove;

    protected boolean debugModeEnabled;

    final protected SceneDebug sceneDebug;

    public Scene() {
        this.timer = new Timer();
        this.sceneTime = 0.0d;
        this.lastDrawTime = 0.0d;
        this.layers = new TreeMap<>();
        this.visualsToRemove = new HashSet<>();
        this.sceneDebug = new SceneDebug();
        this.debugModeEnabled = false;
    }

    public void start() {
        this.timer.start();
    }

    @Override
    public void update() {
        sceneTime = this.timer.getTimeSeconds();
        Engine.setSceneTime(sceneTime);
        for (SceneLayer layer : layers.values()) {
            for (SceneVisual visual : layer.getVisuals()) {
                visual.update();
                if (visual.shouldBeRemoved()) {
                    visualsToRemove.add(visual);
                }
                if (visual.getReloadGraphicsFlag()) {
                    int sceneLayerGraphicalIndex = getSceneLayerGraphicalIndex(visual.getSceneLayerIndex());
                    var graphicalLayers = visual.getGraphicalSubLayers();
                    var graphics = visual.getGraphics();
                    for (int i = 0; i < graphicalLayers.size(); i++) {
                        Engine.getGraphicsManager().addGraphic(graphics.get(i), sceneLayerGraphicalIndex + graphicalLayers.get(i));
                    }

                    visual.setReloadGraphicsFlag(false);
                }
            }
        }
        for (var display : visualsToRemove) {
            removeVisual(display);
        }
        visualsToRemove.clear();
        sceneDebug.update();
        lastDrawTime = sceneTime;
    }


    final public void addVisual(SceneVisual visual) {
        int visualMaxGraphicalSubLayer = visual.getMaxGraphicalSubLayer();
        int sceneLayerIndex = visual.getSceneLayerIndex();
        SceneLayer sceneLayer = layers.get(sceneLayerIndex);
        int sceneLayerGraphicalIndex = getSceneLayerGraphicalIndex(sceneLayerIndex);

        //determining how many graphical layers need to be inserted
        int graphicalLayersToInsertCount = 0;
        int sceneLayerGraphicalSubLayerCount = 0;
        if (!layers.containsKey(sceneLayerIndex)) {
            layers.put(sceneLayerIndex, new SceneLayer());
            sceneLayer = layers.get(sceneLayerIndex);
            graphicalLayersToInsertCount = visualMaxGraphicalSubLayer + 1;
        }
        else {
            sceneLayerGraphicalSubLayerCount = sceneLayer.getGraphicalSubLayerCount();
            if (visualMaxGraphicalSubLayer >= sceneLayerGraphicalSubLayerCount) {
                graphicalLayersToInsertCount = visualMaxGraphicalSubLayer - sceneLayerGraphicalSubLayerCount + 1;
            }
        }

        //inserting the graphical layers
        GraphicsManager graphicsManager = Engine.getGraphicsManager();
        for (int i = 0; i < graphicalLayersToInsertCount; i++) {
            graphicsManager.insertNewLayer(sceneLayerGraphicalIndex + sceneLayerGraphicalSubLayerCount);
        }

        //adding the graphics to the renderers
        var graphicalLayers = visual.getGraphicalSubLayers();
        var graphics = visual.getGraphics();
        for (int i = 0; i < graphicalLayers.size(); i++) {
            graphicsManager.addGraphic(graphics.get(i), sceneLayerGraphicalIndex + graphicalLayers.get(i));
        }

        sceneLayer.getVisuals().add(visual);
        if (visualMaxGraphicalSubLayer >= sceneLayer.getGraphicalSubLayerCount()) {
            sceneLayer.setGraphicalSubLayerCount(visualMaxGraphicalSubLayer + 1);
        }
        visual.initDisplay();
    }

    private Integer getSceneLayerGraphicalIndex(int sceneLayerIndex) {
        int layerSum = 0;
        for (var layerIndex : layers.keySet()) {
            if (layerIndex >= sceneLayerIndex) {
                break;
            }
            layerSum += layers.get(layerIndex).getGraphicalSubLayerCount();
        }
        return layerSum;
    }

    public void removeVisual(SceneVisual visual) {
        int sceneLayerIndex = visual.getSceneLayerIndex();
        layers.get(sceneLayerIndex).getVisuals().remove(visual);
        List<Graphic<?, ?>> graphics = visual.getGraphics();
        for (var graphic : graphics) {
            graphic.remove();
        }
    }

    public void toggleDebug() {
        debugModeEnabled = !debugModeEnabled;
        sceneDebug.toggle();
    }

    protected class SceneDebug {

        final private RGBAValue fpsDisplayTextColor = new RGBAValue(1.0f, 1.0f, 1.0f, 1.0f);

        final private TextStyle fpsDisplayTextStyle = new TextStyle(debugFont, fpsDisplayTextColor, 20f);

        final private TextDisplay fpsDisplay = new TextDisplay(0, true, 0.9f * Engine.getNativeWidth(), 0.9f * Engine.getNativeHeight(), "", fpsDisplayTextStyle, TextAlignment.CENTER);

        public void enable() {
        }

        public void disable() {
            fpsDisplay.getGraphics().forEach(Graphic::remove);
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
                double fpsVal = 1 / (sceneTime - lastDrawTime);
                fpsDisplay.setDisplayedString(df.format(fpsVal) + " FPS");
                fpsDisplay.update();
                fpsDisplay.getGraphics().forEach(graphic -> Engine.getGraphicsManager().addDebugGraphic(graphic));
            }
        }
    }

}
