package map.intersection;

import java.util.*;

import util.*;

public class IntersectionType {

    private Type type;
    private final List<Integer> list;

    private boolean N;
    private Pair<Integer, Integer> iN;
    private boolean E;
    private Pair<Integer, Integer> iE;
    private boolean S;
    private Pair<Integer, Integer> iS;
    private boolean W;
    private Pair<Integer, Integer> iW;

    private float nsTime;
    private float weTime;

    // Using function getval() for StreetType because i compare it to chars from txt file

    public IntersectionType() {
        this.type = null;
        this.list = new ArrayList<>();
        this.nsTime = 0f;
        this.weTime = 0f;
        this.N = false;
        this.E = false;
        this.S = false;
        this.W = false;
    }

    public IntersectionType(String specString, String nsTime, String weTime) {
        this();

        for (int i = 0; i < specString.length(); i++) {
            if ((i + 1) % 3 != 0)
                this.list.add(Integer.parseInt(String.valueOf(specString.charAt(i))));
        }

        int i = 0;
        while (i < this.list.size()) {
            if (!this.list.get(i).equals(StreetType.noStreet.getVal()) || !this.list.get(i + 1).equals(StreetType.noStreet.getVal())) {
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
        this.iN = new Pair<>(this.list.get(0), this.list.get(1));
        this.iE = new Pair<>(this.list.get(2), this.list.get(3));
        this.iS = new Pair<>(this.list.get(4), this.list.get(5));
        this.iW = new Pair<>(this.list.get(6), this.list.get(7));

        this.setTime(Float.parseFloat(nsTime), Float.parseFloat(weTime));
        this.type = this.calculateType();
    }

    private Type calculateType() {
        boolean empty = true;

        for (int item : list)
            if (item != StreetType.noStreet.getVal()) {
                empty = false;
                break;
            }

        if (empty)
            return type = Type.oType;

        if (N && E && S && W)
            return Type.xType;
        else if ((N && S && !E && !W) || (E && W && !N && !S))
            return Type.IType;
        else if ((N && E && !W && !S) || (N && W && !E && !S) || (!N && !E && W && S) || (!N && E && !W && S))
            return Type.LType;
        else
            return Type.tType;
    }

    public void adjustType(boolean N, boolean E, boolean S, boolean W) {
        this.N = N;
        this.E = E;
        this.S = S;
        this.W = W;
        this.type = calculateType();
    }

    public boolean adjustTrafficLights() {
        if (this.type.equals(Type.oType) || this.type.equals(Type.LType) ||
                this.type .equals(Type.IType))
            return false;

        if (this.type.equals(Type.xType)) {
            if (this.iE.getX().equals(StreetType.noStreet.getVal()) &&
                    this.getIW().getX().equals(StreetType.noStreet.getVal()))
                return true;
            return this.iN.getX().equals(StreetType.noStreet.getVal()) &&
                    this.getIS().getX().equals(StreetType.noStreet.getVal());
        }

        if (this.type.equals(Type.tType)) {
            if (!this.N) {
                if (this.iS.getX().equals(StreetType.noStreet.getVal()))
                    return true;

                if (!this.iE.getX().equals(StreetType.noStreet.getVal()))
                    return false;

                return this.iW.getX().equals(StreetType.noStreet.getVal());
            }

            if (!this.S) {
                if (this.iN.getX().equals(StreetType.noStreet.getVal()))
                    return true;

                if (!this.iE.getX().equals(StreetType.noStreet.getVal()))
                    return false;

                return this.iW.getX().equals(StreetType.noStreet.getVal());
            }

            if (!this.E) {
                if (this.iW.getX().equals(StreetType.noStreet.getVal()))
                    return true;

                if (!this.iS.getX().equals(StreetType.noStreet.getVal()))
                    return false;

                return this.iN.getX().equals(StreetType.noStreet.getVal());
            }

            if (!this.W) {
                if (this.iE.getX().equals(StreetType.noStreet.getVal()))
                    return true;

                if (this.iS.getX().equals(StreetType.noStreet.getVal()))
                    return false;

                return this.iN.getX().equals(StreetType.noStreet.getVal());
            }
        }

        return false;
    }

    public void setTime(float nsTime, float weTime) {
        this.nsTime = nsTime;
        this.weTime = weTime;
    }

    //GET
    public Type get_Type() {
        return type;
    }

    public float getNsTime() {
        return nsTime;
    }

    public float getWeTime() {
        return weTime;
    }

    public boolean getN() {
        return N;
    }

    public Pair<Integer, Integer> getIN() {
        return iN;
    }

    public boolean getE() {
        return E;
    }

    public Pair<Integer, Integer> getIE() {
        return iE;
    }

    public boolean getS() {
        return S;
    }

    public Pair<Integer, Integer> getIS() {
        return iS;
    }

    public boolean getW() {
        return W;
    }

    public Pair<Integer, Integer> getIW() {
        return iW;
    }
}