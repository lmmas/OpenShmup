package engine.types;

public final class Reference<T> {

    private T value;

    public Reference(T value) {
        this.value = value;
    }

    public T get() {
        return this.value;
    }

    public void set(T value) {
        this.value = value;
    }
}
