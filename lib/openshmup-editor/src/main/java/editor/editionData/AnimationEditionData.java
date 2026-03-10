package editor.editionData;

import editor.attribute.*;
import json.JsonFieldNames;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public final class AnimationEditionData implements VisualEditionData {

    private IntegerAttribute idAttribute;

    private IntegerAttribute layer;

    private Vec2DAttribute size;

    private EditionDataAttribute<SpritesheetInfoData> spritesheetInfo;

    private DoubleAttribute framePeriodSeconds;

    private BooleanAttribute looping;

    public AnimationEditionData(int id, int layer, float sizeX, float sizeY, double framePeriodSeconds, boolean looping, SpritesheetInfoData spritesheetInfo) {
        this.idAttribute = new IntegerAttribute("Visual ID", JsonFieldNames.Animation.id, id);
        this.layer = new IntegerAttribute("Scene layer", JsonFieldNames.Animation.layer, layer);
        this.size = new Vec2DAttribute("Size", JsonFieldNames.Animation.size, sizeX, sizeY);
        this.framePeriodSeconds = new DoubleAttribute("Frame period (seconds)", JsonFieldNames.Animation.framePeriodSeconds, framePeriodSeconds);
        this.looping = new BooleanAttribute("Looping", JsonFieldNames.Animation.looping, looping);
        this.spritesheetInfo = new EditionDataAttribute<SpritesheetInfoData>("Spritesheet info", JsonFieldNames.Animation.spritesheetInfo, spritesheetInfo);
    }

    @Override
    public int getId() {
        return idAttribute.getValue();
    }

    @Override public List<Attribute> getAttributes() {
        return List.of(idAttribute, layer, size, spritesheetInfo, framePeriodSeconds, looping);
    }

    @Getter @Setter final public static class SpritesheetInfoData implements EditionData {

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

        @Override public List<Attribute> getAttributes() {
            return List.of(fileName, frameCount, frameSize, startPosition, stride);
        }
    }
}
