package editor.editionData;

import editor.attribute.Attribute;
import editor.attribute.FloatAttribute;
import editor.attribute.ListAttribute;
import json.JsonFieldNames;
import lombok.Getter;

import java.util.List;

@Getter final public class ShotEditionData implements ExtraComponentEditionData {

    private FloatAttribute shotPeriod;

    private FloatAttribute firstShotTime;

    private ListAttribute<SpawnableEditionData> spawnables;

    public ShotEditionData(float shotPeriod, float firstShotTime, List<SpawnableEditionData> spawnables) {
        this.shotPeriod = new FloatAttribute("Shot period", JsonFieldNames.Shot.shotPeriod, shotPeriod);
        this.firstShotTime = new FloatAttribute("First shot time", JsonFieldNames.Shot.firstShotTime, firstShotTime);
        this.spawnables = new ListAttribute<SpawnableEditionData>("Spawnables", JsonFieldNames.Shot.spawn, spawnables);
    }

    @Override public List<Attribute> getAttributes() {
        return List.of(shotPeriod, firstShotTime, spawnables);
    }
}
