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
    private ArrayList<Float> normalizedLineWidthsList;

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
        this.normalizedLineWidthsList = new ArrayList<>();
        updateText();
    }

    private void updateText(){
        characterImageList.clear();
        characterInfoList.clear();
        displayedString.codePoints().forEach(this::addCharacter);
        updateTextPosition();
    }

    private void calculateLineWidths(){
        int charInfoIndex = 0;
        int lineBreakCodepoint = "\n".codePointAt(0);
        while(charInfoIndex < characterInfoList.size()){
            float normalizedLineWidth = 0.0f;
            if(characterInfoList.get(charInfoIndex).codepoint() != lineBreakCodepoint){
                while (charInfoIndex + 1 < characterInfoList.size() && characterInfoList.get(charInfoIndex + 1).codepoint() != lineBreakCodepoint) {
                    normalizedLineWidth += characterInfoList.get(charInfoIndex).normalizedAdvance();
                    charInfoIndex++;
                }
                normalizedLineWidth += characterInfoList.get(charInfoIndex).normalizedQuadSize().x;
                charInfoIndex++;
            }
            normalizedLineWidthsList.add(normalizedLineWidth);
            charInfoIndex ++;
        }
    }

    private void addCharacter(int newCodepoint){
        float textHeight = textHeightPixels / GameConfig.getEditionHeight();
        Optional<FontCharInfo> fontCharInfoOptional = font.getCharInfo(newCodepoint);
        if(fontCharInfoOptional.isPresent()){
            FontCharInfo newCharInfo = fontCharInfoOptional.orElseThrow();
            characterInfoList.add(newCharInfo);
            if (newCodepoint != " ".codePointAt(0) && newCodepoint != "\n".codePointAt(0) ){
                Image2D newCharacterImage;
                if(renderInfo.graphicType() == GraphicType.STATIC_IMAGE){
                    newCharacterImage = new StaticImage(font.getBitmap(), renderInfo.layer(), newCharInfo.normalizedQuadSize().x * textHeight, newCharInfo.normalizedQuadSize().y * textHeight);
                }
                else{
                    newCharacterImage  = new DynamicImage(font.getBitmap(), renderInfo.layer(), newCharInfo.normalizedQuadSize().x * textHeight, newCharInfo.normalizedQuadSize().y * textHeight);
                }
                Vec2D textureSize = newCharInfo.bitmapTextureSize();
                newCharacterImage.setTextureSize(textureSize.x, textureSize.y);
                Vec2D texturePosition = newCharInfo.bitmapTexturePosition();
                newCharacterImage.setTexturePosition(texturePosition.x, texturePosition.y);
                characterImageList.add(newCharacterImage);
            }
        }
    }

    private void updateTextPosition(){
        int numberOfLines = normalizedLineWidthsList.size();
        float textHeight = textHeightPixels / GameConfig.getEditionHeight();
        int characterImageIndex = 0;
        int charInfoIndex = 0;
        int lineBreakCodepoint = "\n".codePointAt(0);
        int spaceCodepoint = " ".codePointAt(0);
        for(int lineIndex = 0; lineIndex < numberOfLines; lineIndex++){
            float currentLineWidth = normalizedLineWidthsList.get(lineIndex) * textHeight;
            float characterBaselineX = position.x - currentLineWidth / 2;
            float characterBaselineY = position.y + ((float)lineIndex - ((float) (numberOfLines - 1) / 2)) * font.getNormalizedLineHeight() * textHeight - textHeight / 2;
            while(charInfoIndex < characterInfoList.size() && characterInfoList.get(charInfoIndex).codepoint() != lineBreakCodepoint){
                if(characterInfoList.get(charInfoIndex).codepoint() != spaceCodepoint){
                    FontCharInfo currentCharInfo = characterInfoList.get(charInfoIndex);
                    characterImageList.get(characterImageIndex).setPosition(characterBaselineX + currentCharInfo.normalizedQuadPositionOffset().x * textHeight, characterBaselineY + currentCharInfo.normalizedQuadPositionOffset().y * textHeight);
                    characterImageIndex++;
                }
                characterBaselineX+= characterInfoList.get(charInfoIndex).normalizedAdvance() * textHeight;
                charInfoIndex++;
            }
            charInfoIndex++;
        }
    }

    @Override
    public SceneDisplay copy() {
        return new TextBox(renderInfo.layer(), renderInfo.graphicType() == GraphicType.DYNAMIC_IMAGE, position.x, position.y, textHeightPixels, displayedString, font);
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
        return Optional.of(font.getBitmap());
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
}
