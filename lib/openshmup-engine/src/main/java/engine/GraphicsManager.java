package engine;

import engine.graphics.*;
import engine.render.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

final public class GraphicsManager {
    final private TreeMap<Integer, ArrayList<Renderer<?,?>>> layers;
    public GraphicsManager(){
        this.layers = new TreeMap<>();
        createNewRenderer(GlobalVars.debugDisplayLayer, RenderType.STATIC_IMAGE);
        createNewRenderer(GlobalVars.debugDisplayLayer, RenderType.DYNAMIC_IMAGE);
        createNewRenderer(GlobalVars.debugDisplayLayer, RenderType.COLOR_RECTANGLE);
    }
    public void drawGraphics(){
        for(Map.Entry<Integer,ArrayList<Renderer<?,?>>> renderers: layers.entrySet()){
            for(Renderer<?,?> renderer : renderers.getValue()){
                renderer.draw();
            }
        }
    }

    public void addGraphic(Graphic<?,?> newGraphic){
        RenderInfo renderInfo = newGraphic.getRenderInfo();
        ArrayList<Renderer<?,?>> renderers = layers.get(renderInfo.layer());
        if (renderers != null) {
            for (Renderer<?, ?> renderer : renderers) {
                if (renderer.getType() == renderInfo.renderType()) {
                    addGraphicToMatchingRenderer(newGraphic, renderer);
                    return;
                }
            }
        }
        createNewRenderer(renderInfo.layer(), renderInfo.renderType());
        addGraphicToMatchingRenderer(newGraphic, layers.get(renderInfo.layer()).getLast());
    }

    private void addGraphicToMatchingRenderer(Graphic<?,?> newGraphic, Renderer<?,?> renderer){
        assert newGraphic.getRenderInfo().renderType() == renderer.getType(): "wrong renderer for graphic";
        switch (renderer.getType()){
            case STATIC_IMAGE, DYNAMIC_IMAGE -> {
                Image2DRenderer ImageRenderer = (Image2DRenderer) renderer;
                Image2D image = (Image2D) newGraphic;
                ImageRenderer.addGraphic(image);
            }
            case COLOR_RECTANGLE -> {
                ColorRectangleRenderer colorRectangleRenderer = (ColorRectangleRenderer) renderer;
                ColorRectangle colorRectangle = (ColorRectangle) newGraphic;
                colorRectangleRenderer.addGraphic(colorRectangle);
            }
        }
    }

    private void createNewRenderer(int layer, RenderType renderType){
        if(!layers.containsKey(layer)){
            layers.put(layer, new ArrayList<>());
        }
        ArrayList<Renderer<?,?>> rendererList = layers.get(layer);
        switch (renderType) {
            case STATIC_IMAGE, DYNAMIC_IMAGE -> {
                Image2DRenderer newRenderer = new Image2DRenderer(renderType);
                rendererList.add(newRenderer);
            }
            case COLOR_RECTANGLE -> {
                ColorRectangleRenderer newRenderer = new ColorRectangleRenderer();
                rendererList.add(newRenderer);
            }
        }
    }

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

    public void clearLayers(){
        layers.clear();
    }
}
