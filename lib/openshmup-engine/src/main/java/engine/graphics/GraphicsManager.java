package engine.graphics;

import engine.EngineSystem;
import engine.graphics.colorRectangle.ColorRectangle;
import engine.graphics.colorRectangle.ColorRectangleRenderer;
import engine.graphics.colorRoundedRectangle.ColorRoundedRectangle;
import engine.graphics.colorRoundedRectangle.ColorRoundedRectangleRenderer;
import engine.graphics.image.Image;
import engine.graphics.image.ImageRenderer;
import engine.graphics.roundedRectangleBorder.RoundedRectangleBorder;
import engine.graphics.roundedRectangleBorder.RoundedRectangleBorderRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final public class GraphicsManager implements EngineSystem {

    final private ArrayList<ArrayList<Renderer<?, ?>>> layers;

    final private ArrayList<Renderer<?, ?>> debugLayer;

    public GraphicsManager() {
        this.layers = new ArrayList<>();
        this.debugLayer = new ArrayList<Renderer<?, ?>>(Arrays.asList(
            new ColorRectangleRenderer(),
            new ImageRenderer(RenderType.DYNAMIC_IMAGE),
            new ImageRenderer(RenderType.STATIC_IMAGE),
            new ColorRoundedRectangleRenderer(),
            new RoundedRectangleBorderRenderer()));
    }

    public void drawGraphics() {
        layers.stream().flatMap(List::stream).forEach(Renderer::draw);
        debugLayer.forEach(Renderer::draw);
    }

    public void addGraphic(Graphic<?, ?> newGraphic, int layer) {
        RenderType type = newGraphic.getRenderType();
        ArrayList<Renderer<?, ?>> renderers = layers.get(layer);
        assert renderers != null : "renderer list not found";
        for (Renderer<?, ?> renderer : renderers) {
            if (renderer.getType() == type) {
                addGraphicToMatchingRenderer(newGraphic, renderer);
                return;
            }
        }
        createNewRenderer(layer, type);
        addGraphicToMatchingRenderer(newGraphic, layers.get(layer).getLast());
    }

    public void addDebugGraphic(Graphic<?, ?> newGraphic) {
        RenderType type = newGraphic.getRenderType();
        for (Renderer<?, ?> renderer : debugLayer) {
            if (renderer.getType() == type) {
                addGraphicToMatchingRenderer(newGraphic, renderer);
                return;
            }
        }
    }

    private void addGraphicToMatchingRenderer(Graphic<?, ?> newGraphic, Renderer<?, ?> renderer) {
        assert newGraphic.getRenderType() == renderer.getType() : "wrong renderer for graphic";
        switch (renderer.getType()) {
            case STATIC_IMAGE, DYNAMIC_IMAGE -> {
                ImageRenderer imageRenderer = (ImageRenderer) renderer;
                Image image = (Image) newGraphic;
                imageRenderer.addGraphic(image);
            }
            case COLOR_RECTANGLE -> {
                ColorRectangleRenderer colorRectangleRenderer = (ColorRectangleRenderer) renderer;
                ColorRectangle colorRectangle = (ColorRectangle) newGraphic;
                colorRectangleRenderer.addGraphic(colorRectangle);
            }
            case COLOR_ROUNDED_RECTANGLE -> {
                ColorRoundedRectangleRenderer colorRoundedRectangleRenderer = (ColorRoundedRectangleRenderer) renderer;
                ColorRoundedRectangle colorRoundedRectangle = (ColorRoundedRectangle) newGraphic;
                colorRoundedRectangleRenderer.addGraphic(colorRoundedRectangle);
            }
            case ROUNDED_RECTANGLE_BORDER -> {
                RoundedRectangleBorderRenderer roundedRectangleBorderRenderer = (RoundedRectangleBorderRenderer) renderer;
                RoundedRectangleBorder roundedRectangleBorder = (RoundedRectangleBorder) newGraphic;
                roundedRectangleBorderRenderer.addGraphic(roundedRectangleBorder);
            }
        }
    }

    public void insertNewLayer(int layer) {
        layers.add(layer, new ArrayList<>());
    }

    private void createNewRenderer(int layer, RenderType renderType) {
        assert layer < layers.size() : "layer out of range";
        ArrayList<Renderer<?, ?>> rendererList = layers.get(layer);
        switch (renderType) {
            case STATIC_IMAGE, DYNAMIC_IMAGE -> {
                ImageRenderer newRenderer = new ImageRenderer(renderType);
                rendererList.add(newRenderer);
            }
            case COLOR_RECTANGLE -> {
                ColorRectangleRenderer newRenderer = new ColorRectangleRenderer();
                rendererList.add(newRenderer);
            }
            case COLOR_ROUNDED_RECTANGLE -> {
                ColorRoundedRectangleRenderer newRenderer = new ColorRoundedRectangleRenderer();
                rendererList.add(newRenderer);
            }
            case ROUNDED_RECTANGLE_BORDER -> {
                RoundedRectangleBorderRenderer newRenderer = new RoundedRectangleBorderRenderer();
                rendererList.add(newRenderer);
            }
        }
    }
/*

    public void constructRenderers(HashSet<RenderInfo> allRenderInfos) {

        for(var renderInfo: allRenderInfos){
            if(!layers.containsKey(renderInfo.layer())){
                createNewRenderer(renderInfo.layer(), renderInfo.renderType());
            }
            else{
                ArrayList<Renderer<?,?>> rendererList = layers.get(renderInfo.layer());
                boolean rendererFound = false;
                for(var renderer: rendererList){
                    if (renderer.getType() == renderInfo.renderType()) {
                        rendererFound = true;
                        break;
                    }
                }
                if(!rendererFound){
                    createNewRenderer(renderInfo.layer(), renderInfo.renderType());
                }
            }
        }

    }
*/

    public void clearLayers() {
        layers.clear();
        debugLayer.clear();
        debugLayer.add(new ColorRectangleRenderer());
        debugLayer.add(new ImageRenderer(RenderType.STATIC_IMAGE));
        debugLayer.add(new ImageRenderer(RenderType.DYNAMIC_IMAGE));
        debugLayer.add(new ColorRoundedRectangleRenderer());
        debugLayer.add(new RoundedRectangleBorderRenderer());
    }

    @Override public void update() {
        drawGraphics();
    }
}
