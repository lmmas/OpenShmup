package editor.editionData;

import editor.attribute.FloatAttribute;
import json.JsonFieldNames;
import lombok.Getter;

import java.util.List;

@Getter final public class ShotEditionData implements EditionData, ExtraComponentEditionData {

    private FloatAttribute shotPeriod;

    private FloatAttribute firstShotTime;

    private List<SpawnableEditionData> spawnables;

    public ShotEditionData(float shotPeriod, float firstShotTime, List<SpawnableEditionData> spawnables) {
        this.shotPeriod = new FloatAttribute("Shot period", JsonFieldNames.Shot.shotPeriod, shotPeriod);
        this.firstShotTime = new FloatAttribute("First shot time", JsonFieldNames.Shot.firstShotTime, firstShotTime);
        this.spawnables = spawnables;
    }
}
