package map.intersection;

public enum StreetType {
    noStreet(0),
    inStreet(1),
    outStreet(2);

    private final int type;
    StreetType(int number){
        type = number;
    }

    public int getVal(){
        return type;
    }
}
