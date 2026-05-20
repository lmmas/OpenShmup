package editor.editionData;

import editor.attribute.*;
import engine.types.IVec2D;
import engine.types.Vec2D;
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

    public AnimationEditionData(int id, int layer, Vec2D size, double framePeriodSeconds, boolean looping, SpritesheetInfoData spritesheetInfo) {
        this.idAttribute = new IntegerAttribute("Visual ID", JsonFieldNames.Animation.id, id);
        this.layer = new IntegerAttribute("Scene layer", JsonFieldNames.Animation.layer, layer);
        this.size = new Vec2DAttribute("Size", JsonFieldNames.Animation.size, size);
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
            IVec2D framesize,
            IVec2D startPosition,
            IVec2D stride) {
            this.fileName = new StringAttribute("File name", JsonFieldNames.Animation.SpritesheetInfo.fileName, fileName);
            this.frameCount = new IntegerAttribute("Frame count", JsonFieldNames.Animation.SpritesheetInfo.frameCount, frameCount);
            this.frameSize = new IVec2DAttribute("Frame count", JsonFieldNames.Animation.SpritesheetInfo.frameSize, framesize);
            this.startPosition = new IVec2DAttribute("Start position", JsonFieldNames.Animation.SpritesheetInfo.startingPosition, startPosition);
            this.stride = new IVec2DAttribute("Stride", JsonFieldNames.Animation.SpritesheetInfo.stride, stride);
        }

        @Override public List<Attribute> getAttributes() {
            return List.of(fileName, frameCount, frameSize, startPosition, stride);
        }
    }
}
