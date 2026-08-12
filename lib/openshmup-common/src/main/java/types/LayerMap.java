package types;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

final public class LayerMap<T> {
    final private TreeMap<Integer, ArrayList<T>> map;

    public LayerMap() {
        this.map = new TreeMap<>();
    }

    public LayerMap(List<? extends LayerEntry<? extends T>> entryList) {
        this.map = new TreeMap<>();
        entryList.forEach(entry -> add(entry.object(), entry.layer()));
    }

    public LayerMap(Map<? extends T, Integer> entryMap) {
        this.map = new TreeMap<>();
        entryMap.forEach(this::add);
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

    public boolean contains(T object) {
        for (ArrayList<T> list : map.values()) {
            if (list.contains(object)) {
                return true;
            }
        }
        return false;
    }

    public void add(T newObject, int layer) {
        assert (!map.containsKey(layer)) || !map.get(layer).contains(newObject) : "object already in map";
        map.computeIfAbsent(layer, ArrayList::new).add(newObject);
    }

    public void addLayers(LayerMap<T> otherLayers, int offset) {
        assert offset >= 0 : "layer offset value should be positive";
        otherLayers.forEachObject((object, layer) -> this.add(object, layer + offset));
    }

    public void remove(T object, int layer) {
        assert (map.containsKey(layer) && map.get(layer).contains(object)) : "object not found";
        map.get(layer).remove(object);
    }

    public void remove(T object) {
        for (var list : map.values()) {
            if (list.contains(object)) {
                list.remove(object);
                return;
            }
        }
        assert false : "object not found";
    }

    public ArrayList<T> getLayer(int layer) {
        return map.get(layer);
    }

    public int getLayerOfObject(T object) {
        for (var entry : map.entrySet()) {
            if (entry.getValue().contains(object)) {
                return entry.getKey();
            }
        }
        assert false : "object not found";
        return 0;
    }

    public List<LayerEntry<T>> getEntryList() {
        return map.entrySet().stream()
            .flatMap(entry -> entry.getValue().stream()
                .map(object -> new LayerEntry<T>(object, entry.getKey())))
            .toList();
    }

    public List<T> getObjectList() {
        return map.values().stream()
            .flatMap(Collection::stream)
            .toList();
    }

    public void forEachObject(BiConsumer<? super T, ? super Integer> action) {
        map.forEach((layer, list) -> list.forEach(object -> action.accept(object, layer)));
    }

    public void forEachObject(Consumer<? super T> action) {
        map.forEach((layer, list) -> list.forEach(action));
    }

    public void forEachLayer(BiConsumer<? super Integer, ? super ArrayList<T>> action) {
        map.forEach(action);
    }

    public void clear() {
        map.clear();
    }
}
