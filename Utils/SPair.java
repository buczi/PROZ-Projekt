package Utils;

public class SPair<T> {
    private T x_;
    private T y_;

    public SPair(T x_, T y_) {
        this.x_ = x_;
        this.y_ = y_;
    }

    public SPair() {
        this.x_ = null;
        this.y_ = null;
    }

    public T getX_() {
        return x_;
    }

    public T getY_() {
        return y_;
    }

    public void setX_(T x_) {
        this.x_ = x_;
    }

    public void setY_(T y_) {
        this.y_ = y_;
    }

    @Override
    public String toString() {
        return "Value x:" + this.x_.toString() + "\tValue y:" + this.y_.toString();
    }

}