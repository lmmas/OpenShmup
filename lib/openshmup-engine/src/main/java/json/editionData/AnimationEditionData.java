package json.editionData;

import engine.types.IVec2D;
import engine.types.Vec2D;
import json.attribute.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public final class AnimationEditionData implements VisualEditionData {

    final private IntegerAttribute idAttribute;

    final private IntegerAttribute layer;

    final private Vec2DAttribute size;

    final private EditionDataAttribute<SpritesheetInfoData> spritesheetInfo;

    final private DoubleAttribute framePeriodSeconds;

    final private BooleanAttribute looping;

    private AnimationEditionData() {
        this.idAttribute = new IntegerAttribute(Keys.Visual.Animation.id);
        this.layer = new IntegerAttribute(Keys.Visual.Animation.layer);
        this.size = new Vec2DAttribute(Keys.Visual.Animation.size);
        this.framePeriodSeconds = new DoubleAttribute(Keys.Visual.Animation.framePeriodSeconds);
        this.looping = new BooleanAttribute(Keys.Visual.Animation.looping);
        this.spritesheetInfo = new EditionDataAttribute<SpritesheetInfoData>(Keys.Visual.Animation.spritesheetInfo, new SpritesheetInfoData());
    }

    public AnimationEditionData(int id, int layer, Vec2D size, double framePeriodSeconds, boolean looping, SpritesheetInfoData spritesheetInfo) {
        this();
        this.idAttribute.setValue(id);
        this.layer.setValue(layer);
        this.size.setValue(size);
        this.framePeriodSeconds.setValue(framePeriodSeconds);
        this.looping.setValue(looping);
        this.spritesheetInfo.setData(spritesheetInfo);
    }
    @Override
    public Category getCategory() {
        return Category.VISUAL;
    }
    @Override
    public Type getType() {
        return Types.Visual.animation;
    }
    @Override
    public int getId() {
        return idAttribute.getValue();
    }
    @Override
    public List<Attribute> getAttributes() {
        return List.of(idAttribute, layer, size, spritesheetInfo, framePeriodSeconds, looping);
    }

    @Override
    public void setToDefault() {
        this.idAttribute.setValue(0);
        this.layer.setValue(0);
        this.size.setValue(Vec2D.ZERO);
        this.framePeriodSeconds.setValue(1.0f);
        this.looping.setValue(false);
        this.spritesheetInfo.getData().setToDefault();
    }

    public static AnimationEditionData DEFAULT() {
        AnimationEditionData data = new AnimationEditionData();
        data.setToDefault();
        return data;
    }

    @Getter @Setter final public static class SpritesheetInfoData implements EditionData {

        final private StringAttribute fileName;

        final private IntegerAttribute frameCount;

        final private IVec2DAttribute frameSize;

        final private IVec2DAttribute startPosition;

        final private IVec2DAttribute stride;

        public SpritesheetInfoData() {
            this.fileName = new StringAttribute(Keys.SpritesheetInfo.fileName);
            this.frameCount = new IntegerAttribute(Keys.SpritesheetInfo.frameCount);
            this.frameSize = new IVec2DAttribute(Keys.SpritesheetInfo.frameSize);
            this.startPosition = new IVec2DAttribute(Keys.SpritesheetInfo.startingPosition);
            this.stride = new IVec2DAttribute(Keys.SpritesheetInfo.stride);
        }

        public SpritesheetInfoData(String fileName, int frameCount, IVec2D framesize, IVec2D startPosition, IVec2D stride) {
            this();
            this.fileName.setValue(fileName);
            this.frameCount.setValue(frameCount);
            this.frameSize.setValue(framesize);
            this.startPosition.setValue(startPosition);
            this.stride.setValue(stride);
        }
        @Override
        public Category getCategory() {
            return Category.NONE;
        }
        @Override
        public Type getType() {
            return Types.spritesheetInfo;
        }
        @Override
        public List<Attribute> getAttributes() {
            return List.of(fileName, frameCount, frameSize, startPosition, stride);
        }

        @Override
        public void setToDefault() {
            this.fileName.setValue("");
            this.frameCount.setValue(0);
            this.frameSize.setValue(IVec2D.ZERO);
            this.stride.setValue(IVec2D.ZERO);
        }

        public static SpritesheetInfoData DEFAULT() {
            SpritesheetInfoData data = new SpritesheetInfoData();
            data.setToDefault();
            return data;
        }
    }
}
