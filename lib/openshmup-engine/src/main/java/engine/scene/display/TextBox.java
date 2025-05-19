package engine.scene.display;

import engine.GameConfig;
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
    private RenderInfo renderInfo;
    private Vec2D position;
    private float textHeightPixels;
    private String displayedString;
    private Font font;
    private ArrayList<FontCharInfo> characterInfoList;
    private ArrayList<Image2D> characterImageList;

    public TextBox(int layer, boolean dynamicText, float positionX, float positionY, float textHeightPixels, String displayedString, Font font) {
        if(dynamicText){
            this.renderInfo = new RenderInfo(layer, GraphicType.DYNAMIC_IMAGE);
        }
        else{
            this.renderInfo = new RenderInfo(layer, GraphicType.STATIC_IMAGE);
        }
        this.position = new Vec2D(positionX, positionY);
        this.textHeightPixels = textHeightPixels;
        this.displayedString = displayedString;
        this.font = font;
        this.characterImageList = new ArrayList<>(displayedString.length());
        this.characterInfoList = new ArrayList<>(displayedString.length());
        updateText();
    }

    private void updateText(){
        characterImageList.clear();
        characterInfoList.clear();
        for(int i = 0; i < displayedString.length(); i++){
            addCharacter(displayedString.codePointAt(i));
        }
        updateTextPosition();
    }

    private void addCharacter(int newCodepoint){
        Optional<FontCharInfo> fontCharInfoOptional = font.getCharInfo(newCodepoint);
        if(fontCharInfoOptional.isPresent()){
            FontCharInfo newCharInfo = fontCharInfoOptional.orElseThrow();
            characterInfoList.add(newCharInfo);
            if (newCodepoint != " ".codePointAt(0) && newCodepoint != "\n".codePointAt(0) ) {
                if(renderInfo.graphicType() == GraphicType.STATIC_IMAGE){
                    characterImageList.add(new StaticImage(font.getBitmap(), renderInfo.layer(), newCharInfo.normalizedQuadSize().x * textHeightPixels, newCharInfo.normalizedQuadSize().y * textHeightPixels));
                }
                else{
                    characterImageList.add(new DynamicImage(font.getBitmap(), renderInfo.layer(), newCharInfo.normalizedQuadSize().x * textHeightPixels, newCharInfo.normalizedQuadSize().y * textHeightPixels));
                }
            }
        }
    }

    private void updateTextPosition(){
        float editionHeight = GameConfig.getEditionHeight();
        int numberOfLines = getNumberOfLines();
        float textHeight = textHeightPixels / editionHeight;
        int currentCharacterIndex = 0;
        int currentImageIndex = 0;
        int currentLineStartIndex = 0;
        int lineBreakCodepoint = "\n".codePointAt(0);
        int spaceCodepoint = " ".codePointAt(0);
        for(int lineIndex = 0; lineIndex < numberOfLines; lineIndex++){
            float currentLineWidth = characterInfoList.get(currentCharacterIndex).normalizedQuadSize().x * textHeight;
            while(displayedString.codePointAt(currentCharacterIndex + 1) != lineBreakCodepoint){
                currentLineWidth += characterInfoList.get(currentCharacterIndex).normalizedAdvance() * textHeight;
                currentCharacterIndex+= Character.charCount(displayedString.codePointAt(currentCharacterIndex));
            }
            currentCharacterIndex = currentLineStartIndex;
            float characterBaselineX = position.x - currentLineWidth / 2;
            float characterBaselineY = position.y + ((float)lineIndex - (float) (numberOfLines - 1) / 2) - textHeight / 2;
            while(displayedString.codePointAt(currentCharacterIndex) != lineBreakCodepoint){
                if(displayedString.codePointAt(currentCharacterIndex) != spaceCodepoint){
                    FontCharInfo currentCharInfo = characterInfoList.get(currentCharacterIndex);
                    characterImageList.get(currentImageIndex).setPosition(characterBaselineX + currentCharInfo.normalizedQuadPositionOffset().x * textHeight, characterBaselineY + currentCharInfo.normalizedQuadPositionOffset().y * textHeight);
                    currentImageIndex++;
                }
                currentCharacterIndex+= Character.charCount(displayedString.codePointAt(currentCharacterIndex));
            }
            currentCharacterIndex++;
        }
    }

    @Override
    public SceneDisplay copy() {
        return null;
    }

    @Override
    public Optional<RenderInfo> getRenderInfo() {
        return Optional.of(renderInfo);
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
        position.x = positionX;
        position.y = positionY;
        updateTextPosition();
    }

    @Override
    public void update(float currentTimeSeconds) {
        if(renderInfo.graphicType() == GraphicType.DYNAMIC_IMAGE){
            updateText();
        }
    }

    @Override
    public boolean shouldBeRemoved() {
        return false;
    }

    private int getNumberOfLines(){
        if(displayedString.isEmpty()){
            return 0;
        }
        int numberOfLines = 1;
        for (int i = 0; i < displayedString.length(); i++){
            if(displayedString.charAt(i) == '\n'){
                numberOfLines++;
            }
        }
        return numberOfLines;
    }
}
