package engine.assets;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTFontinfo;
import static org.lwjgl.stb.STBTruetype.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class TrueTypeFont {
    private String filepath;
    private float normalizedDescent;
    private float normalizedLineGap;
    private ArrayList<FontCharInfo> charInfoList;

    public TrueTypeFont(String filepath) throws IOException {
        this.filepath = filepath;
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
        final int ascent = ascentBuf[0];
        this.normalizedDescent = (float) descentBuf[0] / ascent;
        this.normalizedLineGap = (float) lineGapBuf[0] / ascent;

        int startCodepoint = 32;
        int endCodepoint = 126;
        this.charInfoList = new ArrayList<>(endCodepoint - startCodepoint + 1);
        for(int codepoint = startCodepoint; codepoint <= endCodepoint; codepoint++){
            int[] x0Buf = new int[]{0};
            int[] x1Buf = new int[]{0};
            int[] y0Buf = new int[]{0};
            int[] y1Buf = new int[]{0};
            stbtt_GetCodepointBox(fontinfo, codepoint, x0Buf, y0Buf, x1Buf, y1Buf);
            int[] advanceWidth = new int[]{0};
            int[] leftSideBearing = new int[]{0};
            stbtt_GetCodepointHMetrics(fontinfo, codepoint, advanceWidth, leftSideBearing);
            System.out.println("character: '" + (char) codepoint + "'");
            System.out.println("x0 = " + x0Buf[0] + ", y0 = " + y0Buf[0] + ", x1 = " + x1Buf[0] + ", y1 = " + y1Buf[0] + ", advanceWidth = " + advanceWidth[0] + ", leftSideBearing = " + leftSideBearing[0] + "\n");

        }

    }
}