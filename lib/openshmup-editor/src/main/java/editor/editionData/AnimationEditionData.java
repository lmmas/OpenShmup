package editor.editionData;

import editor.attribute.*;
import json.JsonFieldNames;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class AnimationEditionData implements EditionData, VisualEditionData {

    private IntegerAttribute idAttribute;

    private IntegerAttribute layer;

    private Vec2DAttribute size;

    private SpritesheetInfoData spritesheetInfo;

    private DoubleAttribute framePeriodSeconds;

    private BooleanAttribute looping;

    public AnimationEditionData(int id, int layer, float sizeX, float sizeY, double framePeriodSeconds, boolean looping, SpritesheetInfoData spritesheetInfo) {
        this.idAttribute = new IntegerAttribute("Visual ID", JsonFieldNames.Animation.id, id);
        this.layer = new IntegerAttribute("Scene layer", JsonFieldNames.Animation.layer, layer);
        this.size = new Vec2DAttribute("Size", JsonFieldNames.Animation.size, sizeX, sizeY);
        this.framePeriodSeconds = new DoubleAttribute("Frame period (seconds)", JsonFieldNames.Animation.framePeriodSeconds, framePeriodSeconds);
        this.looping = new BooleanAttribute("Looping", JsonFieldNames.Animation.looping, looping);
        this.spritesheetInfo = spritesheetInfo;
    }

    @Getter @Setter
    public static class SpritesheetInfoData {

        final private StringAttribute fileName;

        final private IntegerAttribute frameCount;

        final private IVec2DAttribute frameSize;

        final private IVec2DAttribute startPosition;

        final private IVec2DAttribute stride;

        public SpritesheetInfoData(
            String fileName,
            int frameCount,
            int frameSizeX, int frameSizeY,
            int startPositionX, int startPositionY,
            int strideX, int strideY) {
            this.fileName = new StringAttribute("File name", JsonFieldNames.Animation.SpritesheetInfo.fileName, fileName);
            this.frameCount = new IntegerAttribute("Frame count", JsonFieldNames.Animation.SpritesheetInfo.frameCount, frameCount);
            this.frameSize = new IVec2DAttribute("Frame count", JsonFieldNames.Animation.SpritesheetInfo.frameSize, frameSizeX, frameSizeY);
            this.startPosition = new IVec2DAttribute("Start position", JsonFieldNames.Animation.SpritesheetInfo.startingPosition, startPositionX, startPositionY);
            this.stride = new IVec2DAttribute("Stride", JsonFieldNames.Animation.SpritesheetInfo.stride, strideX, strideY);
        }
    }

    @Override
    public int getId() {
        return idAttribute.getValue();
    }
}
