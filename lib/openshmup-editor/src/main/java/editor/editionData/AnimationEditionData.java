package editor.editionData;

import editor.attribute.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class AnimationEditionData extends VisualEditionData implements EditionData {

    private DoubleAttribute framePeriodSeconds;

    private BooleanAttribute looping;

    private AnimationInfoAttributes animationInfo;

    public AnimationEditionData(int id, int layer, float sizeX, float sizeY, double framePeriodSeconds, boolean looping, AnimationInfoAttributes animationInfo) {
        super(id, layer, sizeX, sizeY);
        this.framePeriodSeconds = new DoubleAttribute("Frame period (seconds)", framePeriodSeconds);
        this.looping = new BooleanAttribute("looping", looping);
        this.animationInfo = animationInfo;
    }

    @Getter
    @Setter
    public static class AnimationInfoAttributes {

        final private StringAttribute fileName;

        final private IntegerAttribute frameCount;

        final private IVec2DAttribute frameSize;

        final private IVec2DAttribute startPosition;

        final private IVec2DAttribute stride;

        public AnimationInfoAttributes(String fileName,
                                       int frameCount,
                                       int frameSizeX, int frameSizeY,
                                       int startPositionX, int startPositionY,
                                       int strideX, int strideY) {
            this.fileName = new StringAttribute("File name", fileName);
            this.frameCount = new IntegerAttribute("Frame count", frameCount);
            this.frameSize = new IVec2DAttribute("Frame count", frameSizeX, frameSizeY);
            this.startPosition = new IVec2DAttribute("Start position", startPositionX, startPositionY);
            this.stride = new IVec2DAttribute("Stride", strideX, strideY);
        }
    }
}
