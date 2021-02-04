package map.intersection;

public enum Type {
    tType("T"),
    xType("X"),
    IType("I"),
    LType("L"),
    oType("null");

    private final String value;
    Type(String c){
        value = c;
    }

    public String getValue() {
        return value;
    }
}
