package json.editionData;

import json.attribute.Attribute;
import json.attribute.FloatAttribute;
import json.attribute.ListAttribute;
import lombok.Getter;

import java.util.List;

@Getter final public class ShotEditionData implements EditionData {

    final private FloatAttribute shotPeriod;

    final private FloatAttribute firstShotTime;

    final private ListAttribute<SpawnEditionData> spawnables;

    private ShotEditionData() {
        this.shotPeriod = new FloatAttribute(Keys.Shot.shotPeriod);
        this.firstShotTime = new FloatAttribute(Keys.Shot.firstShotTime);
        this.spawnables = new ListAttribute<SpawnEditionData>(Keys.Shot.spawn);
    }

    public ShotEditionData(float shotPeriod, float firstShotTime, List<SpawnEditionData> spawnables) {
        this();
        this.shotPeriod.setValue(shotPeriod);
        this.firstShotTime.setValue(firstShotTime);
        this.spawnables.setDataList(spawnables);
    }
    @Override
    public Category getCategory() {
        return Category.NONE;
    }
    @Override
    public Type getType() {
        return Types.shot;
    }
    @Override
    public List<Attribute> getAttributes() {
        return List.of(shotPeriod, firstShotTime, spawnables);
    }
    @Override
    public void setToDefault() {
        this.shotPeriod.setValue(1.0f);
        this.firstShotTime.setValue(0.0f);
        this.spawnables.setDataList(List.of());
    }

    public static ShotEditionData DEFAULT() {
        ShotEditionData data = new ShotEditionData();
        data.setToDefault();
        return data;
    }
}
