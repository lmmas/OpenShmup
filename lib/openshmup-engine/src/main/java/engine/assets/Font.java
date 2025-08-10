package engine.assets;

import engine.types.Vec2D;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;
import static org.lwjgl.stb.STBTruetype.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Optional;

final public class Font {
    final private String fontFilepath;
    final private Texture bitmap;
    final private float capHeightPixels;
    final private float normalizedAscent;
    final private float normalizedDescent;
    final private float normalizedLineGap;
    final private float normalizedLineHeight;
    final private HashMap<Integer , FontCharInfo> charInfoMap;

    private Font(String fontFilepath, Texture bitmap, float capHeightPixels, float normalizedAscent, float normalizedDescent, float normalizedLineGap, float normalizedLineHeight, HashMap<Integer, FontCharInfo> charInfoMap) {
        this.fontFilepath = fontFilepath;
        this.bitmap = bitmap;
        this.capHeightPixels = capHeightPixels;
        this.normalizedAscent = normalizedAscent;
        this.normalizedDescent = normalizedDescent;
        this.normalizedLineGap = normalizedLineGap;
        this.normalizedLineHeight = normalizedLineHeight;
        this.charInfoMap = charInfoMap;
    }

    public static Font createFromTTF(String filepath) throws IOException {
        byte[] fontBytes = Files.readAllBytes(Path.of(filepath));
        ByteBuffer dataBuffer = BufferUtils.createByteBuffer(fontBytes.length);
        dataBuffer.put(fontBytes);
        dataBuffer.flip();
        STBTTFontinfo fontinfo = STBTTFontinfo.create();
        stbtt_InitFont(fontinfo, dataBuffer);
        int[] ascentBuf = new int[]{0};
        int[] descentBuf = new int[]{0};
        int[] lineGapBuf = new int[]{0};
        stbtt_GetFontVMetrics(fontinfo, ascentBuf, descentBuf, lineGapBuf);
        int[] x0H = new int[1];
        int[] x1H = new int[1];
        int[] y0H = new int[1];
        int[] y1H = new int[1];
        stbtt_GetCodepointBox(fontinfo, 'H', x0H, y0H, x1H, y1H);
        int capHeightNativeValue = y1H[0] - y0H[0];
        float normalizedAscent = (float) ascentBuf[0] / capHeightNativeValue;
        float normalizedDescent = (float) descentBuf[0] / capHeightNativeValue;
        float normalizedLineGap = (float) lineGapBuf[0] / capHeightNativeValue;
        float normalizedLineHeight = (float) (ascentBuf[0] - descentBuf[0] + lineGapBuf[0]) / capHeightNativeValue;

        int startCodepoint = 10;
        int endCodepoint = 126;

        int bitmapWidth = 512;
        int bitmapHeight = 512;
        float capHeightPixels = 50.0f;

        ByteBuffer bitmap = BufferUtils.createByteBuffer(bitmapWidth * bitmapHeight);
        STBTTBakedChar.Buffer charBuffer = STBTTBakedChar.malloc(endCodepoint - startCodepoint + 1);
        stbtt_BakeFontBitmap(
                dataBuffer, capHeightPixels * normalizedLineHeight, bitmap, bitmapWidth, bitmapHeight, startCodepoint, charBuffer);

        HashMap<Integer, FontCharInfo> charInfoMap = new HashMap<>(endCodepoint - startCodepoint + 1);
        for(int i = 0; i < charBuffer.capacity(); i++){
            STBTTBakedChar charData = charBuffer.get(i);
            int codepoint = startCodepoint + i;
            short x0 = charData.x0();
            int y0 = bitmapHeight - charData.y1();
            short x1 = charData.x1();
            int y1 = bitmapHeight - charData.y0();
            float xoff = charData.xoff();
            float yoff = charData.yoff();
            float xadvance = charData.xadvance();
            assert x1 -x0 != 0 || y1 - y0 != 0 || xadvance != 0 : "error loading font info for character of codepoint " + codepoint + " in font " + filepath;

            Vec2D normalizedQuadSize = new Vec2D((float)(x1 - x0) / capHeightPixels, (float)(y1 - y0) / capHeightPixels);
            Vec2D normalizedQuadPositionOffset = new Vec2D(xoff / capHeightPixels + normalizedQuadSize.x / 2, -yoff / capHeightPixels - normalizedQuadSize.y / 2);
            float normalizedAdvance = xadvance / capHeightPixels;

            Vec2D bitmapTextureSize = new Vec2D( (float)(x1 - x0) / bitmapWidth, (float)(y1 - y0) / bitmapHeight);
            Vec2D bitmapTexturePosition = new Vec2D((float)x0 / bitmapWidth, (float)y0 / bitmapHeight);
            FontCharInfo fontCharInfo = new FontCharInfo(codepoint, normalizedQuadSize, normalizedQuadPositionOffset, normalizedAdvance, bitmapTextureSize, bitmapTexturePosition);
            charInfoMap.put(codepoint, fontCharInfo);
        }
        Texture fontTexture = new Texture(bitmapWidth, bitmapHeight, 1, bitmap);
        fontTexture.flipImageBuffer();
        return new Font(filepath, fontTexture, capHeightPixels, normalizedAscent, normalizedDescent, normalizedLineGap, normalizedLineHeight, charInfoMap);
    }

    public Optional <FontCharInfo> getCharInfo(int codepoint){
        if(charInfoMap.containsKey(codepoint)) {
            return Optional.of(charInfoMap.get(codepoint));
        }
        return Optional.empty();
    }

    public Texture getBitmap() {
        return bitmap;
    }

    public float getNormalizedLineHeight() {
        return normalizedLineHeight;
    }
}