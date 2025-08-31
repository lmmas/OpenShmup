package engine.scene.display;

import engine.GameConfig;
import engine.assets.Font;
import engine.assets.FontCharInfo;
import engine.assets.Texture;
import engine.graphics.*;
import engine.render.RenderInfo;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static engine.Engine.graphicsManager;

final public class TextDisplay extends SceneVisual {
    final public int lineBreakCodepoint = "\n".codePointAt(0);
    final private RenderInfo renderInfo;
    final private Vec2D position;
    private float textHeightPixels;
    private String displayedString;
    private Font font;
    final private boolean dynamicText;
    private final RGBAValue textColor;
    final private ArrayList<ArrayList<TextCharacter>> textLines;
    final private ArrayList<Float> normalizedLineWidthsList;

    public TextDisplay(int layer, boolean dynamicText, float positionX, float positionY, float textHeightPixels, String displayedString, Font font) {
        if(dynamicText){
            this.renderInfo = new RenderInfo(layer, RenderType.DYNAMIC_IMAGE);
        }
        else{
            this.renderInfo = new RenderInfo(layer, RenderType.STATIC_IMAGE);
        }
        this.position = new Vec2D(positionX, positionY);
        this.textHeightPixels = textHeightPixels;
        this.displayedString = displayedString;
        this.font = font;
        this.dynamicText = dynamicText;
        this.textLines = new ArrayList<>();
        this.normalizedLineWidthsList = new ArrayList<>();
        this.textColor = new RGBAValue(0.0f, 0.0f, 0.0f, 0.0f);
        updateText();
    }

    private void updateText(){
        for(var line: textLines){
            for(TextCharacter character: line){
                character.image.delete();
            }
        }
        textLines.clear();
        textLines.add(new ArrayList<>());
        displayedString.codePoints().forEach(this::addCharacter);
        calculateLineWidths();
        updateTextPosition();
    }

    private void calculateLineWidths(){
        normalizedLineWidthsList.clear();
        for(ArrayList<TextCharacter> line: textLines){
            float currentLineWidth = 0.0f;
            if(!line.isEmpty()){
                for(int i = 0; i < line.size() - 1; i++){
                    currentLineWidth += line.get(i).fontCharInfo.normalizedAdvance();
                }
                currentLineWidth += line.getLast().fontCharInfo.normalizedQuadSize().x;
            }
            normalizedLineWidthsList.add(currentLineWidth);
        }
    }

    private void addCharacter(int newCodepoint){
        if(newCodepoint == lineBreakCodepoint){
            textLines.add(new ArrayList<>());
        }
        else{
            TextCharacter newCharacter = new TextCharacter(newCodepoint, font);
            textLines.getLast().add(newCharacter);
            graphicsManager.addGraphic(newCharacter.image);
        }
    }

    private void updateTextPosition(){
        int lineCount = normalizedLineWidthsList.size();
        float textHeight = textHeightPixels / GameConfig.getEditionHeight();
        for(int lineIndex = 0; lineIndex < lineCount; lineIndex++){
            ArrayList<TextCharacter> currentLine = textLines.get(lineIndex);
            float currentLineWidth = normalizedLineWidthsList.get(lineIndex) * textHeight;
            float characterBaselineX = position.x - currentLineWidth / 2;
            float characterBaselineY = position.y + (((float) (lineCount - 1) / 2) - (float)lineIndex) * font.getNormalizedLineHeight() * textHeight - (textHeight / 2);
            for(TextCharacter character: currentLine){
                Vec2D characterPositionOffset = character.fontCharInfo.normalizedQuadPositionOffset();
                character.setPosition(characterBaselineX + characterPositionOffset.x * textHeight, characterBaselineY + characterPositionOffset.y * textHeight);
                characterBaselineX += character.fontCharInfo.normalizedAdvance() * textHeight;
            }
        }
    }

    public void setDisplayedString(String displayedString){
        this.displayedString = displayedString;
    }

    public void setTextColor(float r, float g, float b, float a){
        this.textColor.r = r;
        this.textColor.g = g;
        this.textColor.b = b;
        this.textColor.a = a;
        for(ArrayList<TextCharacter> line: textLines){
            for(TextCharacter character: line){
                character.setColor(textColor.r, textColor.g, textColor.b, textColor.a);
            }
        }
    }

    @Override
    public SceneVisual copy() {
        return new TextDisplay(renderInfo.layer(), dynamicText, position.x, position.y, textHeightPixels, displayedString, font);
    }

    @Override
    public List<RenderInfo> getRenderInfos() {
        return List.of(renderInfo);
    }

    @Override
    public List<Graphic<?, ?>> getGraphics() {
        return textLines.stream().flatMap(List::stream).map(TextCharacter::getImage).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Texture> getTextures() {
        return List.of(font.getBitmap());
    }

    @Override
    public void setPosition(float positionX, float positionY) {
        position.x = positionX;
        position.y = positionY;
        updateTextPosition();
    }

    @Override
    public void setScale(float scaleX, float scaleY) {

    }

    @Override
    public void initDisplay(float startingTimeSeconds) {

    }

    @Override
    public void update(float currentTimeSeconds) {
        if(dynamicText){
            updateText();
        }
    }

    final public class TextCharacter{
        final private int codepoint;
        private FontCharInfo fontCharInfo;
        private Image2D image;
        public TextCharacter(int codepoint, Font font){
            this.codepoint = codepoint;
            this.fontCharInfo = font.getCharInfo(codepoint).orElseThrow();
            Vec2D charSize = fontCharInfo.normalizedQuadSize();
            float textHeight = TextDisplay.this.textHeightPixels / GameConfig.getEditionHeight();
            this.image = new Image2D(font.getBitmap(), TextDisplay.this.renderInfo.layer(), TextDisplay.this.dynamicText, charSize.x * textHeight, charSize.y * textHeight);
            Vec2D bitmapTextureSize = fontCharInfo.bitmapTextureSize();
            Vec2D bitmapTexturePosition = fontCharInfo.bitmapTexturePosition();
            image.setTextureSize(bitmapTextureSize.x, bitmapTextureSize.y);
            image.setTexturePosition(bitmapTexturePosition.x, bitmapTexturePosition.y);
        }

        public void setPosition(float positionX, float positionY){
            image.setPosition(positionX, positionY);
        }

        public void setSize(float sizeX, float sizeY){
            image.setScale(sizeX, sizeY);
        }

        public void setColor(float r, float g, float b, float a){
            image.setColorCoefs(r,g,b,a);
        }

        public Image2D getImage(){
            return image;
        }
    }
}
