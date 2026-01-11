package json;

@FunctionalInterface
public interface TetraFunction<T, U, V, W, R> {

    R apply(T t, U u, V v, W w);
}
