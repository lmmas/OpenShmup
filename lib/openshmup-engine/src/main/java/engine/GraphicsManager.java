package engine;

import engine.graphics.*;
import engine.render.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class GraphicsManager {
    final private TreeMap<Integer, ArrayList<Renderer<?,?>>> layers;
    public GraphicsManager(){
        this.layers = new TreeMap<>();
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
        assert renderers != null: "bad Renderer generation";
        for(Renderer<?,?> renderer : renderers){
            if(renderer.getType() == renderInfo.graphicType()){
                switch (renderInfo.graphicType()){
                    case STATIC_IMAGE -> {
                        StaticImageRenderer staticImageRenderer = (StaticImageRenderer) renderer;
                        StaticImage staticImage = (StaticImage) newGraphic;
                        staticImageRenderer.addGraphic(staticImage);
                    }
                    case DYNAMIC_IMAGE -> {
                        DynamicImageRenderer dynamicImageRenderer = (DynamicImageRenderer) renderer;
                        DynamicImage dynamicImage = (DynamicImage) newGraphic;
                        dynamicImageRenderer.addGraphic(dynamicImage);
                    }
                    case COLOR_RECTANGLE -> {
                        ColorRectangleRenderer colorRectangleRenderer = (ColorRectangleRenderer) renderer;
                        ColorRectangle colorRectangle = (ColorRectangle) newGraphic;
                        colorRectangleRenderer.addGraphic(colorRectangle);
                    }
                }
                return;
            }
        }
    }

    private void createNewRenderer(int layer, GraphicType graphicType){
        if(!layers.containsKey(layer)){
            layers.put(layer, new ArrayList<>());
        }
        ArrayList<Renderer<?,?>> rendererList = layers.get(layer);
        switch (graphicType) {
            case STATIC_IMAGE-> {
                StaticImageRenderer newRenderer = new StaticImageRenderer();
                rendererList.add(newRenderer);
            }
            case DYNAMIC_IMAGE -> {
                DynamicImageRenderer newRenderer = new DynamicImageRenderer();
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
                createNewRenderer(renderInfo.layer(), renderInfo.graphicType());
            }
            else{
                ArrayList<Renderer<?,?>> rendererList = layers.get(renderInfo.layer());
                boolean rendererFound = false;
                for(var renderer: rendererList){
                    if (renderer.getType() == renderInfo.graphicType()) {
                        rendererFound = true;
                        break;
                    }
                }
                if(!rendererFound){
                    createNewRenderer(renderInfo.layer(), renderInfo.graphicType());
                }
            }
        }
    }

    public void clearLayers(){
        layers.clear();
    }
}
