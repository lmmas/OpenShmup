package editor.editionData;

import editor.attribute.Attribute;

import java.util.List;

public sealed interface EditionData permits AnimationEditionData.SpritesheetInfoData, EntityEditionData, ExtraComponentEditionData, HitboxEditionData, SpawnableEditionData, TrajectoryEditionData, VisualEditionData {

    List<Attribute> getAttributes();
}
