package util;

public class DataSet {
    private final SPair<Boolean> N;
    private final SPair<Boolean> E;
    private final SPair<Boolean> S;
    private final SPair<Boolean> W;
    private final boolean lights;

    public DataSet(SPair<Boolean> n, SPair<Boolean> e, SPair<Boolean> s, SPair<Boolean> w, boolean lights)
    {
        this.N = n;
        this.E = e;
        this.S = s;
        this.W = w;
        this.lights = lights;
    }

    public SPair<Boolean> getE() {
        return E;
    }

    public SPair<Boolean> getN() {
        return N;
    }

    public boolean isLights() {
        return lights;
    }
}
