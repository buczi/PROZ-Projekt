package util;

public class Pair<T, S> {
    private final T x;
    private final S y;

    public Pair(T x, S y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public S getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Value x:" + this.x.toString() + "\tValue y:" + this.y.toString();
    }
}


