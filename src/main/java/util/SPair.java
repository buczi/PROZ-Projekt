package util;

public class SPair<T> {
    private T x;
    private T y;

    public SPair(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public SPair() {
        this.x = null;
        this.y = null;
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }

    public void setX(T x) {
        this.x = x;
    }

    public void setY(T y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Value x:" + this.x.toString() + "\tValue y:" + this.y.toString();
    }

}