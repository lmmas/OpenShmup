package json.editionData;

import json.attribute.Attribute;
import json.attribute.DoubleAttribute;
import json.attribute.IntegerAttribute;
import json.attribute.ListAttribute;

import java.util.List;

final public class RepeatSpawnInfoEditionData implements SpawnInfoEditionData {

    final private DoubleAttribute startTime;

    final private IntegerAttribute spawnCount;

    final private DoubleAttribute interval;

    final private ListAttribute spawns;

    public RepeatSpawnInfoEditionData() {
        this.startTime = new DoubleAttribute(Keys.SpawnInfo.Repeat.startTime);
        this.spawnCount = new IntegerAttribute(Keys.SpawnInfo.Repeat.spawnCount);
        this.interval = new DoubleAttribute(Keys.SpawnInfo.Repeat.interval);
        this.spawns = new ListAttribute(Keys.SpawnInfo.Repeat.spawns);
    }

    public RepeatSpawnInfoEditionData(double startTime, int spawnCount, double interval, List<EditionData> spawnList) {
        this();
        this.startTime.setValue(startTime);
        this.spawnCount.setValue(spawnCount);
        this.interval.setValue(interval);
        this.spawns.setDataList(spawnList);
    }

    @Override
    public Category getCategory() {
        return Category.SPAWN_INFO;
    }
    @Override
    public Type getType() {
        return Types.SpawnInfo.repeat;
    }
    @Override
    public List<Attribute> getAttributes() {
        return List.of(startTime, spawnCount, interval, spawns);
    }
    @Override
    public void setToDefault() {
        this.startTime.setValue(0.0d);
        this.spawnCount.setValue(1);
        this.interval.setValue(1.0d);
        this.spawns.setDataList(List.of());
    }

    public static RepeatSpawnInfoEditionData DEFAULT() {
        RepeatSpawnInfoEditionData data = new RepeatSpawnInfoEditionData();
        data.setToDefault();
        return data;
    }
}
