package Utils;

public class DataSet {
    private SPair<Boolean> N;
    private SPair<Boolean> E;
    private SPair<Boolean> S;
    private SPair<Boolean> W;
    private boolean lights;

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

    public SPair<Boolean> getS() {
        return S;
    }

    public SPair<Boolean> getW() {
        return W;
    }

    public boolean isLights() {
        return lights;
    }
}
