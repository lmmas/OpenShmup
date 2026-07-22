package engine.assets;

import lombok.Getter;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;
import types.IVec2D;
import types.Vec2D;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Optional;

import static org.lwjgl.stb.STBTruetype.*;

final public class Font {
    final public static int codepointRangeStart = 10;
    final public static int codepointRangeEnd = 126;

    public static boolean codepointIsSupported(int codepoint){
        return codepoint >= codepointRangeStart && codepoint <= codepointRangeEnd;
    }

    final private Path fontFilepath;
    @Getter
    final private Texture bitmap;

    final private float capHeightPixels;

    final private float normalizedAscent;

    final private float normalizedDescent;

    final private float normalizedLineGap;
    @Getter
    final private float normalizedLineHeight;

    final private HashMap<Integer, FontCharInfo> charInfoMap;

    private Font(Path fontFilepath, Texture bitmap, float capHeightPixels, float normalizedAscent, float normalizedDescent, float normalizedLineGap, float normalizedLineHeight, HashMap<Integer, FontCharInfo> charInfoMap) {
        this.fontFilepath = fontFilepath;
        this.bitmap = bitmap;
        this.capHeightPixels = capHeightPixels;
        this.normalizedAscent = normalizedAscent;
        this.normalizedDescent = normalizedDescent;
        this.normalizedLineGap = normalizedLineGap;
        this.normalizedLineHeight = normalizedLineHeight;
        this.charInfoMap = charInfoMap;
    }

    public static Font createFromTTF(Path filepath) throws IOException {
        byte[] fontBytes = Files.readAllBytes(filepath);
        ByteBuffer dataBuffer = BufferUtils.createByteBuffer(fontBytes.length);
        dataBuffer.put(fontBytes);
        dataBuffer.flip();
        STBTTFontinfo fontInfo = STBTTFontinfo.create();
        stbtt_InitFont(fontInfo, dataBuffer);
        int[] ascentBuf = new int[]{0};
        int[] descentBuf = new int[]{0};
        int[] lineGapBuf = new int[]{0};
        stbtt_GetFontVMetrics(fontInfo, ascentBuf, descentBuf, lineGapBuf);
        int[] x0M = new int[1];
        int[] x1M = new int[1];
        int[] y0M = new int[1];
        int[] y1M = new int[1];
        stbtt_GetCodepointBox(fontInfo, 'M', x0M, y0M, x1M, y1M);
        int capHeightNativeValue = y1M[0] - y0M[0];
        float normalizedAscent = (float) ascentBuf[0] / capHeightNativeValue;
        float normalizedDescent = (float) descentBuf[0] / capHeightNativeValue;
        float normalizedLineGap = (float) lineGapBuf[0] / capHeightNativeValue;
        float normalizedLineHeight = (float) (ascentBuf[0] - descentBuf[0] + lineGapBuf[0]) / capHeightNativeValue;

        int charCount = codepointRangeEnd - codepointRangeStart + 1;
        int bitmapWidth = 1024;
        int bitmapHeight = 1024;
        float capHeightPixels = 96.0f;

        ByteBuffer bitmap = BufferUtils.createByteBuffer(bitmapWidth * bitmapHeight);
        STBTTBakedChar.Buffer charBuffer = STBTTBakedChar.malloc(charCount);
        int result = stbtt_BakeFontBitmap(
            dataBuffer, capHeightPixels * normalizedLineHeight, bitmap, bitmapWidth, bitmapHeight, codepointRangeStart, charBuffer);
        assert result > 0 : "Font " + filepath + ": loading failed";

        HashMap<Integer, FontCharInfo> charInfoMap = new HashMap<>(charCount);
        for (int i = 0; i < charCount; i++) {
            STBTTBakedChar charData = charBuffer.get(i);
            int codepoint = codepointRangeStart + i;
            short x0 = charData.x0();
            int y0 = bitmapHeight - charData.y1();
            short x1 = charData.x1();
            int y1 = bitmapHeight - charData.y0();
            float xoff = charData.xoff();
            float yoff = charData.yoff();
            float xadvance = charData.xadvance();
            assert x1 - x0 != 0 || y1 - y0 != 0 || xadvance != 0 : "error loading font info for character of codepoint " + codepoint + " in font " + filepath;

            Vec2D normalizedQuadSize = new Vec2D((float) (x1 - x0) / capHeightPixels, (float) (y1 - y0) / capHeightPixels);
            Vec2D normalizedQuadPositionOffset = new Vec2D(xoff / capHeightPixels + normalizedQuadSize.x / 2, -yoff / capHeightPixels - normalizedQuadSize.y / 2);
            float normalizedAdvance = xadvance / capHeightPixels;

            Vec2D bitmapTextureSize = new Vec2D((float) (x1 - x0 + 1) / bitmapWidth, (float) (y1 - y0 + 1) / bitmapHeight);
            Vec2D bitmapTexturePosition = new Vec2D(((float) x0 - 0.5f) / bitmapWidth, ((float) y0 - 0.5f) / bitmapHeight);
            FontCharInfo fontCharInfo = new FontCharInfo(codepoint, normalizedQuadSize, normalizedQuadPositionOffset, normalizedAdvance, bitmapTextureSize, bitmapTexturePosition);
            charInfoMap.put(codepoint, fontCharInfo);
        }
        Texture fontTexture = new Texture(new IVec2D(bitmapWidth, bitmapHeight), 1, bitmap);
        fontTexture.flipImageBuffer();
        return new Font(filepath, fontTexture, capHeightPixels, normalizedAscent, normalizedDescent, normalizedLineGap, normalizedLineHeight, charInfoMap);
    }

    public Optional<FontCharInfo> getCharInfo(int codepoint) {
        if (charInfoMap.containsKey(codepoint)) {
            return Optional.of(charInfoMap.get(codepoint));
        }
        return Optional.empty();
    }

}