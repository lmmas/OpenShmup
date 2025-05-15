package engine.scene.display;

import engine.assets.Font;
import engine.assets.FontCharInfo;
import engine.assets.Texture;
import engine.graphics.*;
import engine.render.RenderInfo;
import engine.scene.Scene;
import engine.types.Vec2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TextBox implements SceneDisplay{
    int layer;
    private Vec2D position;
    private float textSize;
    private String displayedString;
    private Font font;
    private ArrayList<FontCharInfo> characterInfoList;
    private ArrayList<Image2D> characterImageList;
    private final boolean staticText;

    public TextBox(float positionX, float positionY, float textSize, String displayedString, Font font, boolean staticText) {
        this.position = new Vec2D(positionX, positionY);
        this.textSize = textSize;
        this.displayedString = displayedString;
        this.font = font;
        this.staticText = staticText;
        this.characterImageList = new ArrayList<>(displayedString.length());
        this.characterInfoList = new ArrayList<>(displayedString.length());
    }

    private void updateChars(){
        characterImageList.clear();
        characterInfoList.clear();
        for(int i = 0; i < displayedString.length(); i++){
            addCharacter(displayedString.codePointAt(i));
        }
    }

    private void addCharacter(int newCodepoint){
        Optional<FontCharInfo> fontCharInfoOptional = font.getCharInfo(newCodepoint);
        if(fontCharInfoOptional.isPresent()){
            FontCharInfo newCharInfo = fontCharInfoOptional.orElseThrow();
            characterInfoList.add(newCharInfo);
            if(staticText){
                characterImageList.add(new StaticImage(font.getBitmap(), layer, newCharInfo.normalizedQuadSize().x * textSize, newCharInfo.normalizedQuadSize().y * textSize));
            }
            else{
                characterImageList.add(new DynamicImage(font.getBitmap(), layer, newCharInfo.normalizedQuadSize().x * textSize, newCharInfo.normalizedQuadSize().y * textSize));
            }
        }
    }

    @Override
    public SceneDisplay copy() {
        return null;
    }

    @Override
    public Optional<RenderInfo> getRenderInfo() {
        if(staticText) {
            return Optional.of(new RenderInfo(layer, GraphicType.STATIC_IMAGE));
        }
        return Optional.of(new RenderInfo(layer, GraphicType.DYNAMIC_IMAGE));
    }

    @Override
    public List<Graphic<?, ?>> getGraphics() {
        return Collections.unmodifiableList(characterImageList);
    }

    @Override
    public Optional<Texture> getTexture() {
        return Optional.empty();
    }

    @Override
    public void setScene(Scene scene) {

    }

    @Override
    public void setPosition(float positionX, float positionY) {

    }

    @Override
    public void update(float currentTimeSeconds) {
        if(staticText){

        }
    }

    @Override
    public boolean shouldBeRemoved() {
        return false;
    }
}
