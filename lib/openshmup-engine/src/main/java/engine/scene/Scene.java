package engine.scene;

import engine.Engine;
import engine.EngineSystem;
import engine.Timer;
import engine.graphics.Graphic;
import engine.graphics.GraphicsManager;
import engine.visual.Text;
import engine.visual.Visual;
import engine.visual.style.TextAlignment;
import engine.visual.style.TextStyle;
import layer.LayerEntry;
import layer.LayerMap;
import types.RGBAValue;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import static engine.GlobalVars.Paths.debugFont;


public class Scene implements EngineSystem {

    final protected Timer timer;

    protected double sceneTime;

    protected double lastDrawTime;

    final protected LayerMap<Visual> layers;

    final protected TreeMap<Integer, Integer> layerWidths;

    final protected HashSet<LayerEntry<Visual>> visualsToRemove;

    protected boolean debugModeEnabled;

    final protected SceneDebug sceneDebug;

    public Scene() {
        this.timer = new Timer();
        this.sceneTime = 0.0d;
        this.lastDrawTime = 0.0d;
        this.layers = new LayerMap<>();
        this.layerWidths = new TreeMap<>();
        this.visualsToRemove = new HashSet<>();
        this.sceneDebug = new SceneDebug();
        this.debugModeEnabled = false;
    }

    public void startTimer() {
        this.timer.start();
    }

    @Override
    public void update() {
        sceneTime = this.timer.getTimeSeconds();
        Engine.setSceneTime(sceneTime);
        layers.forEachLayer((sceneLayerIndex, visualList) -> {
            List<Visual> visualListCopy = new ArrayList<>(visualList);
            for (Visual visual : visualListCopy) {
                visual.update();
                int sceneLayerGraphicalIndex = getSceneLayerGraphicalIndex(sceneLayerIndex);
                var graphicsToRemove = visual.getGraphicsToRemove();
                if (!graphicsToRemove.isEmpty()) {
                    for (var graphic : graphicsToRemove) {
                        graphic.remove();
                    }
                    graphicsToRemove.clear();
                }

                if (visual.getShouldBeRemoved()) {
                    visual.getGraphicalLayers().forEachObject(Graphic::remove);
                    layers.remove(visual, sceneLayerIndex);
                    visual.setToRemove(false);
                    continue;
                }

                var graphicsToAdd = visual.getGraphicsToAdd();
                if (!graphicsToAdd.isEmpty()) {
                    for (var entry : graphicsToAdd) {
                        Engine.getGraphicsManager().addGraphic(entry.object(), sceneLayerGraphicalIndex + entry.layer());
                    }
                    graphicsToAdd.clear();
                }
            }
        });
        sceneDebug.update();
        lastDrawTime = sceneTime;
    }

    @Override
    public int getUpdateIndex() {
        return 9;
    }


    final public void addVisual(Visual visual, int sceneLayerIndex) {
        var graphicalLayers = visual.getGraphicalLayers();
        int visualMaxGraphicalSubLayer = graphicalLayers.getMaxLayer();
        var layerList = layers.getLayer(sceneLayerIndex);
        assert (layerList == null) || !layerList.contains(visual) : "visual already in layer";
        int sceneLayerGraphicalIndex = getSceneLayerGraphicalIndex(sceneLayerIndex);

        //determining how many graphical layers need to be inserted
        int graphicalLayersToInsertCount = 0;
        int sceneLayerGraphicalSubLayerCount = 0;
        if (layerList == null) {
            graphicalLayersToInsertCount = visualMaxGraphicalSubLayer + 1;
        }
        else {
            sceneLayerGraphicalSubLayerCount = layerWidths.get(sceneLayerIndex);
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
        graphicalLayers.forEachObject((graphic, graphicLayer) -> graphicsManager.addGraphic(graphic, sceneLayerGraphicalIndex + graphicLayer));


        layers.add(visual, sceneLayerIndex);
        if (visualMaxGraphicalSubLayer + 1 > sceneLayerGraphicalSubLayerCount) {
            layerWidths.put(sceneLayerIndex, visualMaxGraphicalSubLayer + 1);
        }
        visual.init();
    }

    private Integer getSceneLayerGraphicalIndex(int sceneLayerIndex) {
        int layerSum = 0;
        for (var layerIndex : layerWidths.keySet()) {
            if (layerIndex >= sceneLayerIndex) {
                break;
            }
            layerSum += layerWidths.get(layerIndex);
        }
        return layerSum;
    }

    public void toggleDebug() {
        debugModeEnabled = !debugModeEnabled;
        sceneDebug.toggle();
    }

    protected class SceneDebug {

        final private RGBAValue fpsDisplayTextColor = new RGBAValue(1.0f, 1.0f, 1.0f, 1.0f);

        final private TextStyle fpsDisplayTextStyle = new TextStyle(debugFont, fpsDisplayTextColor, 20f);

        final private Text fpsDisplay = new Text(true, Engine.getNativeResolution().scalar(0.9f), "", fpsDisplayTextStyle, TextAlignment.CENTER);

        public void enable() {
        }

        public void disable() {
            fpsDisplay.getGraphicalLayers().forEachObject(Graphic::remove);
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
                fpsDisplay.getGraphicalLayers().forEachObject(graphic -> Engine.getGraphicsManager().addDebugGraphic(graphic));
            }
        }
    }

}
