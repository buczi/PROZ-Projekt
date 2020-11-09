package Map;

import java.util.*;

import Utils.*;

public class IntersectionType {
    public final static String t_Type = "T";
    public final static String x_Type = "X";
    public final static String I_Type = "I";
    public final static String L_Type = "L";
    public final static String o_Type = "null";

    public final static int no_street = 0;
    public final static int in_street = 1;
    public final static int out_street = 2;

    private String type;
    private List<Integer> list;

    private boolean N;
    private Pair<Integer, Integer> _N;
    private boolean E;
    private Pair<Integer, Integer> _E;
    private boolean S;
    private Pair<Integer, Integer> _S;
    private boolean W;
    private Pair<Integer, Integer> _W;

    private float NS_time;
    private float WE_time;

    public IntersectionType() {
        this.type = null;
        this.list = new ArrayList<Integer>();
        this.NS_time = 0f;
        this.WE_time = 0f;
        this.N = false;
        this.E = false;
        this.S = false;
        this.W = false;
    }

    public IntersectionType(String specString, String NS_time, String WE_time) {
        this();

        for (int i = 0; i < specString.length(); i++) {
            if ((i + 1) % 3 != 0)
                this.list.add(Integer.parseInt(String.valueOf(specString.charAt(i))));
        }

        int i = 0;
        while (i < this.list.size()) {
            if (this.list.get(i) != no_street || this.list.get(i + 1) != no_street) {
                switch (i) {
                    case 0:
                        N = true;
                        break;
                    case 2:
                        E = true;
                        break;
                    case 4:
                        S = true;
                        break;
                    case 6:
                        W = true;
                }
            }
            i += 2;
        }
        this._N = new Pair<Integer, Integer>(this.list.get(0), this.list.get(1));
        this._E = new Pair<Integer, Integer>(this.list.get(2), this.list.get(3));
        this._S = new Pair<Integer, Integer>(this.list.get(4), this.list.get(5));
        this._W = new Pair<Integer, Integer>(this.list.get(6), this.list.get(7));

        this.setTime(Float.parseFloat(NS_time), Float.parseFloat(WE_time));
        this.type = this.CalculateType();
    }

    private String CalculateType() {
        boolean empty = true;

        for (int item : list)
            if (item != no_street) {
                empty = false;
                break;
            }

        if (empty)
            return type = o_Type;

        if (N && E && S && W)
            return x_Type;
        else if ((N && S && !E && !W) || (E && W && !N && !S))
            return I_Type;
        else if ((N && E && !W && !S) || (N && W && !E && !S) || (!N && !E && W && S) || (!N && E && !W && S))
            return L_Type;
        else
            return t_Type;
    }

    public void AdjustType(boolean N, boolean E, boolean S, boolean W) {
        this.N = N;
        this.E = E;
        this.S = S;
        this.W = W;
        this.type = CalculateType();
    }

    public boolean AdjustTrafficLights() {
        if (this.type == o_Type || this.type == L_Type || this.type == I_Type)
            return false;

        if (this.type == x_Type) {
            if (this._E.getX_() == no_street && this.get_W().getX_() == no_street)
                return true;
            if (this._N.getX_() == no_street && this.get_S().getX_() == no_street)
                return true;

            return false;
        }

        if (this.type == t_Type) {
            if (!this.N) {
                if (this._S.getX_() == no_street)
                    return true;

                if (this._E.getX_() != no_street)
                    return false;

                if (this._W.getX_() != no_street)
                    return false;

                return true;
            }

            if (!this.S) {
                if (this._N.getX_() == no_street)
                    return true;

                if (this._E.getX_() != no_street)
                    return false;

                if (this._W.getX_() != no_street)
                    return false;

                return true;
            }

            if (!this.E) {
                if (this._W.getX_() == no_street)
                    return true;

                if (this._S.getX_() != no_street)
                    return false;

                if (this._N.getX_() != no_street)
                    return false;

                return true;
            }

            if (!this.W) {
                if (this._E.getX_() == no_street)
                    return true;

                if (this._S.getX_() != no_street)
                    return false;

                if (this._N.getX_() != no_street)
                    return false;

                return true;
            }
        }

        return false;
    }

    public void setTime(float NS_time, float WE_time) {
        this.NS_time = NS_time;
        this.WE_time = WE_time;
    }

    //GET
    public String get_Type() {
        return type;
    }

    public float getNS_time() {
        return NS_time;
    }

    public float getWE_time() {
        return WE_time;
    }

    public boolean getN() {
        return N;
    }

    public Pair<Integer, Integer> get_N() {
        return _N;
    }

    public boolean getE() {
        return E;
    }

    public Pair<Integer, Integer> get_E() {
        return _E;
    }

    public boolean getS() {
        return S;
    }

    public Pair<Integer, Integer> get_S() {
        return _S;
    }

    public boolean getW() {
        return W;
    }

    public Pair<Integer, Integer> get_W() {
        return _W;
    }
}