package engine.visual;

import engine.assets.Font;
import engine.assets.FontCharInfo;
import engine.graphics.Graphic;
import engine.graphics.image.Image;
import engine.types.RGBAValue;
import engine.types.Vec2D;
import engine.visual.style.TextAlignment;
import engine.visual.style.TextStyle;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static engine.Engine.assetManager;

final public class TextDisplay extends SceneVisual {

    final public static int lineBreakCodepoint = "\n".codePointAt(0);

    final private Vec2D position;
    @Setter
    private String displayedString;

    private final Font font;

    private final float textHeight;

    private final RGBAValue textColor;

    private final TextAlignment alignment;

    final private boolean dynamicText;

    final private ArrayList<ArrayList<TextCharacter>> textLines;

    final private ArrayList<Float> normalizedLineWidthsList;

    public TextDisplay(int layer, Font font, boolean dynamicText, float textHeight, float positionX, float positionY, String displayedString, float r, float g, float b, float a, TextAlignment alignment) {
        super(layer, new ArrayList<>());
        this.position = new Vec2D(positionX, positionY);
        this.textHeight = textHeight;
        this.displayedString = displayedString;
        this.font = font;
        this.dynamicText = dynamicText;
        this.textLines = new ArrayList<>();
        this.normalizedLineWidthsList = new ArrayList<>();
        this.textColor = new RGBAValue(r, g, b, a);
        this.alignment = alignment;
        updateText();
    }

    public TextDisplay(int layer, boolean dynamicText, float positionX, float positionY, String displayedString, TextStyle style, TextAlignment alignment) {
        this(layer, assetManager.getFont(style.fontFilepath()), dynamicText, style.textHeight(), positionX, positionY, displayedString, style.textColor().r, style.textColor().b, style.textColor().b, style.textColor().a, alignment);
    }

    private void updateText() {
        for (var line : textLines) {
            for (TextCharacter character : line) {
                character.image.remove();
            }
        }
        textLines.clear();
        graphicalSubLayers.clear();
        textLines.add(new ArrayList<>());
        displayedString.codePoints().forEach(this::addCharacter);
        updateTextColor();
        calculateLineWidths();
        updateTextPosition();
    }

    private void calculateLineWidths() {
        normalizedLineWidthsList.clear();
        for (ArrayList<TextCharacter> line : textLines) {
            float currentLineWidth = 0.0f;
            if (!line.isEmpty()) {
                for (int i = 0; i < line.size() - 1; i++) {
                    currentLineWidth += line.get(i).fontCharInfo.normalizedAdvance();
                }
                currentLineWidth += line.getLast().fontCharInfo.normalizedQuadSize().x;
            }
            normalizedLineWidthsList.add(currentLineWidth);
        }
    }

    private void addCharacter(int newCodepoint) {
        if (newCodepoint == lineBreakCodepoint) {
            textLines.add(new ArrayList<>());
        }
        else {
            TextCharacter newCharacter = new TextCharacter(newCodepoint, font);
            textLines.getLast().add(newCharacter);
            graphicalSubLayers.add(0);
        }
    }

    private void updateTextPosition() {
        int lineCount = normalizedLineWidthsList.size();
        for (int lineIndex = 0; lineIndex < lineCount; lineIndex++) {
            ArrayList<TextCharacter> currentLine = textLines.get(lineIndex);
            float currentLineWidth = normalizedLineWidthsList.get(lineIndex) * textHeight;
            float characterBaselineX;
            switch (alignment) {
                case LEFT -> characterBaselineX = position.x;
                case RIGHT -> characterBaselineX = position.x - currentLineWidth;
                case CENTER -> characterBaselineX = position.x - currentLineWidth / 2;
                case null -> {
                    assert false : "uninitialized alignment";
                    characterBaselineX = 0f;
                }
            }
            float characterBaselineY = position.y + (((float) (lineCount - 1) / 2) - (float) lineIndex) * font.getNormalizedLineHeight() * textHeight - (textHeight / 2);
            for (TextCharacter character : currentLine) {
                Vec2D characterPositionOffset = character.fontCharInfo.normalizedQuadPositionOffset();
                character.setPosition(characterBaselineX + characterPositionOffset.x * textHeight, characterBaselineY + characterPositionOffset.y * textHeight);
                characterBaselineX += character.fontCharInfo.normalizedAdvance() * textHeight;
            }
        }
    }

    public void updateTextColor() {
        for (ArrayList<TextCharacter> line : textLines) {
            for (TextCharacter character : line) {
                character.setColor(textColor.r, textColor.g, textColor.b, textColor.a);
            }
        }
    }

    @Override
    public SceneVisual copy() {
        return new TextDisplay(sceneLayer, font, dynamicText, textHeight, position.x, position.y, displayedString, textColor.r, textColor.g, textColor.b, textColor.a, alignment);
    }

    @Override
    public List<Graphic<?, ?>> getGraphics() {
        return textLines.stream().flatMap(List::stream).map(TextCharacter::getImage).collect(Collectors.toUnmodifiableList());
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
    public void update(double currentTimeSeconds) {
        if (dynamicText) {
            updateText();
            this.setReloadGraphicsFlag(true);
        }
    }

    final public class TextCharacter {

        final private int codepoint;

        private final FontCharInfo fontCharInfo;
        @Getter
        private final Image image;

        public TextCharacter(int codepoint, Font font) {
            this.codepoint = codepoint;
            this.fontCharInfo = font.getCharInfo(codepoint).orElseThrow();
            Vec2D charSize = fontCharInfo.normalizedQuadSize();
            Vec2D bitmapTextureSize = fontCharInfo.bitmapTextureSize();
            Vec2D bitmapTexturePosition = fontCharInfo.bitmapTexturePosition();
            this.image = new Image(font.getBitmap(), TextDisplay.this.dynamicText,
                charSize.x * textHeight, charSize.y * textHeight,
                0.0f, 0.0f,
                bitmapTextureSize.x, bitmapTextureSize.y,
                bitmapTexturePosition.x, bitmapTexturePosition.y,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 0.0f, 0.0f, 0.0f);
        }

        public void setPosition(float positionX, float positionY) {
            image.setPosition(positionX, positionY);
        }

        public void setSize(float sizeX, float sizeY) {
            image.setScale(sizeX, sizeY);
        }

        public void setColor(float r, float g, float b, float a) {
            image.setColorCoefs(r, g, b, a);
        }

    }
}
