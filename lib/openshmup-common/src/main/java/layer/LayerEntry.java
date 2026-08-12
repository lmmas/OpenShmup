package layer;

public record LayerEntry<T>(
    T object,
    int layer
) {

}
