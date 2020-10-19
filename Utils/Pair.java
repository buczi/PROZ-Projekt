package Utils;

public class Pair<T,S> {
    private T x_;
    private S y_;

    public Pair(T x_, S y_)
    {
        this.x_ = x_;
        this.y_ = y_;
    }

    public T getX_() {
        return x_;
    }

    public S getY_() {
        return y_;
    }
}
