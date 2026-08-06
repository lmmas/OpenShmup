package types;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.BiConsumer;

final public class LayerMap<T> {
    final private TreeMap<Integer, ArrayList<T>> map;

    public LayerMap() {
        this.map = new TreeMap<>();
    }

    public int getLayerCount() {
        return map.size();
    }

    public Integer getMaxLayer() {
        if (map.isEmpty()) {
            return null;
        }
        return map.lastKey();
    }

    public void add(T newObject, int layer) {
        assert (!map.containsKey(layer)) || !map.get(layer).contains(newObject) : "object already in map";
        map.computeIfAbsent(layer, ArrayList::new).add(newObject);
    }

    public void remove(T object, int layer) {
        assert (map.containsKey(layer) && map.get(layer).contains(object)) : "object not found";
        map.get(layer).remove(object);
    }

    public ArrayList<T> getLayer(int layer) {
        return map.get(layer);
    }

    public List<LayerEntry<T>> getEntryList() {
        return map.entrySet().stream()
            .flatMap(entry -> entry.getValue().stream()
                .map(object -> new LayerEntry<T>(object, entry.getKey())))
            .toList();
    }

    public void forEachObject(BiConsumer<? super T, ? super Integer> action) {
        map.forEach((key, value) -> value.forEach(object -> action.accept(object, key)));
    }

    public void forEachLayer(BiConsumer<? super Integer, ? super ArrayList<T>> action) {
        map.forEach(action);
    }

    public void clear() {
        map.clear();
    }
}
