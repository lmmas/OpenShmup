package json.editionData;

import json.attribute.Attribute;
import json.attribute.DoubleAttribute;
import json.attribute.ListAttribute;

import java.util.List;

final public class SingleSpawnInfoEditionData implements SpawnInfoEditionData {

    final private DoubleAttribute spawnTime;

    final private ListAttribute spawns;

    public SingleSpawnInfoEditionData() {
        this.spawnTime = new DoubleAttribute(Keys.SpawnInfo.Single.spawnTime);
        this.spawns = new ListAttribute(Keys.SpawnInfo.Single.spawns);
    }

    public SingleSpawnInfoEditionData(double spawnTime, List<EditionData> spawnList) {
        this();
        this.spawnTime.setValue(spawnTime);
        this.spawns.setDataList(spawnList);
    }
    @Override
    public Category getCategory() {
        return Category.SPAWN_INFO;
    }
    @Override
    public Type getType() {
        return Types.SpawnInfo.single;
    }
    @Override
    public List<Attribute> getAttributes() {
        return List.of(spawnTime, spawns);
    }
    @Override
    public void setToDefault() {
        spawnTime.setValue(0.0d);
        spawns.setDataList(List.of());
    }

    public static SingleSpawnInfoEditionData DEFAULT() {
        SingleSpawnInfoEditionData data = new SingleSpawnInfoEditionData();
        data.setToDefault();
        return data;
    }
}
