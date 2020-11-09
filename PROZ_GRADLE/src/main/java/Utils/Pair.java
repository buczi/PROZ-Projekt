package Utils;

public class Pair<T, S> {
    private T x_;
    private S y_;

    public Pair(T x_, S y_) {
        this.x_ = x_;
        this.y_ = y_;
    }

    public Pair() {
        this.x_ = null;
        this.y_ = null;
    }

    public T getX_() {
        return x_;
    }

    public S getY_() {
        return y_;
    }

    @Override
    public String toString() {
        return "Value x:" + this.x_.toString() + "\tValue y:" + this.y_.toString();
    }
}


