package Map;

import Utils.*;

import java.util.*;

import Vehicle.*;

public class GridNode extends Coords implements Time {
    private Pair<Street, Street> N_streets;
    private Pair<Street, Street> E_streets;
    private Pair<Street, Street> S_streets;
    private Pair<Street, Street> W_streets;

    private List<Pair<TrafficLights, TrafficLights>> boundedLights; // N - S     W - E
    private IntersectionType type;

    private boolean isNSGreen;
    private boolean isWithoutLights;
    private boolean isEmpty;

    private float time;

    public GridNode(Coords coords) {
        super(coords);
        this.boundedLights = new ArrayList<Pair<TrafficLights, TrafficLights>>();
        this.type = new IntersectionType();
        this.isEmpty = false;
        this.isNSGreen = false;
        this.isWithoutLights = false;

        this.time = 0f;
    }

    @Override
    public void Update() {
        if (this.getType().get_Type() == IntersectionType.o_Type)
            return;

        if (!this.isWithoutLights) {
            float delta = 0f;
            if (this.isNSGreen) {
                if (this.N_streets != null) {
                    if (this.N_streets.getX_() != null)
                        delta = this.N_streets.getX_().getLights().getGreenLightTime();
                    else
                        delta = this.S_streets.getX_().getLights().getGreenLightTime();
                } else
                    delta = this.S_streets.getX_().getLights().getGreenLightTime();
            } else {
                if (this.W_streets != null)
                    if (this.W_streets.getX_() != null)
                        delta = this.W_streets.getX_().getLights().getGreenLightTime();
                    else if (this.E_streets != null)
                        if (this.E_streets.getX_() != null)
                            delta = this.E_streets.getX_().getLights().getGreenLightTime();
            }

            if (this.time >= delta) {
                this.time = 0f;
                this.ChangeLights();
            }

/*
            for(Pair<TrafficLights,TrafficLights>pair : this.boundedLights)
            {
                if(pair.getX_() != null)
                    pair.getX_().Update();
                if(pair.getY_() != null)
                    pair.getY_().Update();
            }
 */

            if (this.N_streets != null)
                if (this.N_streets.getX_() != null)
                    this.N_streets.getX_().getLights().Update();

            if (this.E_streets != null)
                if (this.E_streets.getX_() != null)
                    this.E_streets.getX_().getLights().Update();

            if (this.S_streets != null)
                if (this.S_streets.getX_() != null)
                    this.S_streets.getX_().getLights().Update();

            if (this.W_streets != null)
                if (this.W_streets.getX_() != null)
                    this.W_streets.getX_().getLights().Update();

            this.time += TimeController.getInstance().getDeltaTime();
        }
    }

    public void FillNode(List<String> data) {
               /* Forma Pliku TXT
          0 POZYCJA Y:X
          1 IMIE name         IO IO IO IO   I- wjazd x_ na O - wyjzad z y_
          2 SYMBOLE WEJSC     NN EE SS WW
          3 WARTOŚCI WĘZŁÓW   00 00 00 00  0 - brak drogi 2 - wyjazd ze skrzyzowania 1 - wjazd na skrzyzowanie
          4 CZAS SWIATLA ZIELONEGO NA LINI NS x.xx
          5 CZAS SWIATLA ZIELONEGO NA LINI WE x.xx
        */

        // Wypelnienie informacji po stworzeniu w klasie map calej siatki wezlow
        this.name = data.get(1);
        this.type = new IntersectionType(data.get(3), data.get(4), data.get(5));
        this.CheckIfEmpty();
    }

    public void BoundLights() {
        if (!isWithoutLights) {
            // N - S bound
            if (this.N_streets.getX_() != null && this.S_streets.getX_() != null)
                this.boundedLights.add(new Pair<TrafficLights, TrafficLights>(this.N_streets.getX_().getLights(),
                        this.S_streets.getX_().getLights()));
            else if (this.N_streets.getX_() == null && this.S_streets.getX_() == null)
                this.boundedLights.add(new Pair<TrafficLights, TrafficLights>(null, null));
            else if (this.N_streets.getX_() == null)
                this.boundedLights.add(new Pair<TrafficLights, TrafficLights>(null,
                        this.S_streets.getX_().getLights()));
            else
                this.boundedLights.add(new Pair<TrafficLights, TrafficLights>(this.N_streets.getX_().getLights(),
                        null));

            // W - E bound
            if (this.E_streets.getX_() != null && this.W_streets.getX_() != null)
                this.boundedLights.add(new Pair<TrafficLights, TrafficLights>(this.E_streets.getX_().getLights(),
                        this.W_streets.getX_().getLights()));
            else if (this.E_streets.getX_() == null && this.W_streets.getX_() == null)
                this.boundedLights.add(new Pair<TrafficLights, TrafficLights>(null, null));
            else if (this.E_streets.getX_() == null)
                this.boundedLights.add(new Pair<TrafficLights, TrafficLights>(null,
                        this.W_streets.getX_().getLights()));
            else
                this.boundedLights.add(new Pair<TrafficLights, TrafficLights>(this.E_streets.getX_().getLights(),
                        null));

            if (this.isNSGreen) {
                if (this.boundedLights.get(0).getX_() != null)
                    this.boundedLights.get(0).getX_().ChangeLights();

                if (this.boundedLights.get(0).getY_() != null)
                    this.boundedLights.get(0).getY_().ChangeLights();
            } else {
                if (this.boundedLights.get(1).getX_() != null)
                    this.boundedLights.get(1).getX_().ChangeLights();

                if (this.boundedLights.get(1).getY_() != null)
                    this.boundedLights.get(1).getY_().ChangeLights();
            }
        }
    }

    public void AddCarToLights(Car car, Direction direction) {
        Pair<Street, Street> pair = this.getStreets(Direction.Oposite(direction));
        if (pair != null)
            if (pair.getX_() != null)
                pair.getX_().getLights().AddCarToQue(car);
    }

    public boolean AdjustLights() {
        if (type.get_Type() == IntersectionType.L_Type || type.get_Type() == IntersectionType.I_Type)
            isWithoutLights = true;
        else
            isWithoutLights = type.AdjustTrafficLights();

        return isWithoutLights;
    }

    public void AdjustStreets() {
        if (N_streets != null)
            if (N_streets.getX_() == null && N_streets.getY_() == null)
                N_streets = null;

        if (S_streets != null)
            if (S_streets.getX_() == null && S_streets.getY_() == null)
                S_streets = null;

        if (E_streets != null)
            if (E_streets.getX_() == null && E_streets.getY_() == null)
                E_streets = null;

        if (W_streets != null)
            if (W_streets.getX_() == null && W_streets.getY_() == null)
                W_streets = null;
    }

    public void ChangeLights() {
        this.isNSGreen = !this.isNSGreen;
        ChangeAllLights();
    }

    public void ChangeAllLights() {
        if (this.E_streets != null)
            if (this.E_streets.getX_() != null)
                this.E_streets.getX_().getLights().ChangeLights();

        if (this.N_streets != null)
            if (this.N_streets.getX_() != null)
                this.N_streets.getX_().getLights().ChangeLights();

        if (this.W_streets != null)
            if (this.W_streets.getX_() != null)
                this.W_streets.getX_().getLights().ChangeLights();

        if (this.S_streets != null)
            if (this.S_streets.getX_() != null)
                this.S_streets.getX_().getLights().ChangeLights();
    }


    public Street PickStartingPoint() {
        if (!this.isNSGreen) {
            if (this.S_streets != null)
                if (this.S_streets.getX_() != null)
                    return this.S_streets.getX_();

            if (this.N_streets != null)
                if (this.N_streets.getX_() != null)
                    return this.N_streets.getX_();
        } else {
            if (this.W_streets != null)
                if (this.W_streets.getX_() != null)
                    return this.W_streets.getX_();

            if (this.E_streets != null)
                if (this.E_streets.getX_() != null)
                    return this.E_streets.getX_();
        }
        return null; //POWINIEN WYRZUCIC BLAD
    }

    public boolean CheckIfEmpty() {
        if (this.isEmpty)
            return true;

        if (this.type.get_Type() == IntersectionType.o_Type)
            this.isEmpty = true;
        else
            return false;
        return true;
    }

    public void AddStreets(Pair<Street, Street> streets, Direction direction) {
        switch (direction) {
            case North:
                this.N_streets = new Pair<Street, Street>(streets.getX_(), streets.getY_());
                if (streets.getX_() != null)
                    streets.getX_().setDirection(Direction.South);
                if (streets.getY_() != null)
                    streets.getY_().setDirection(Direction.North);
                break;
            case East:
                this.E_streets = new Pair<Street, Street>(streets.getX_(), streets.getY_());
                if (streets.getX_() != null)
                    streets.getX_().setDirection(Direction.West);
                if (streets.getY_() != null)
                    streets.getY_().setDirection(Direction.East);
                break;
            case South:
                this.S_streets = new Pair<Street, Street>(streets.getX_(), streets.getY_());
                if (streets.getX_() != null)
                    streets.getX_().setDirection(Direction.North);
                if (streets.getY_() != null)
                    streets.getY_().setDirection(Direction.South);
                break;
            case West:
                this.W_streets = new Pair<Street, Street>(streets.getX_(), streets.getY_());
                if (streets.getX_() != null)
                    streets.getX_().setDirection(Direction.East);
                if (streets.getY_() != null)
                    streets.getY_().setDirection(Direction.West);
                break;
        }
    }

    //GET
    public Pair<Street, Street> getStreets(Direction direction) {
        switch (direction) {
            case North:
                return N_streets;

            case East:
                return E_streets;

            case South:
                return S_streets;

            case West:
                return W_streets;

        }

        return N_streets;
    }

    public IntersectionType getType() {
        return type;
    }

    public boolean getIsWithoutLights() {
        return isWithoutLights;
    }

    public static ArrayList<Pair<GridNode, Direction>> getAdjacentNodes(GridNode node) {
        ArrayList<Pair<GridNode, Direction>> list = new ArrayList<Pair<GridNode, Direction>>();

        if (node.CheckIfEmpty())
            return list;

        if (node.getStreets(Direction.North) != null)
            if (node.N_streets.getY_() != null)
                list.add(new Pair<GridNode, Direction>(node.N_streets.getY_().getDestination(), Direction.North));
        if (node.getStreets(Direction.East) != null)
            if (node.E_streets.getY_() != null)
                list.add(new Pair<GridNode, Direction>(node.E_streets.getY_().getDestination(), Direction.East));
        if (node.getStreets(Direction.South) != null)
            if (node.S_streets.getY_() != null)
                list.add(new Pair<GridNode, Direction>(node.S_streets.getY_().getDestination(), Direction.South));
        if (node.getStreets(Direction.West) != null)
            if (node.W_streets.getY_() != null)
                list.add(new Pair<GridNode, Direction>(node.W_streets.getY_().getDestination(), Direction.West));

        return list;
    }

    @Override
    public String toString() {
        String str = "";
        str += this.name + "\n";
        str += this.position.getX_() + ":" + this.position.getY_() + "\n";
        str += this.type.get_Type();

        return str;
    }

}
