package map;

import map.intersection.IntersectionType;
import map.intersection.Type;
import mvc.exception.ExceptionType;
import mvc.exception.MildException;
import map.event.MapEvent;
import util.*;

import java.util.*;

import vehicle.*;

public class GridNode extends Coords implements Time {
    private Pair<Street, Street> nStreets;
    private Pair<Street, Street> eStreets;
    private Pair<Street, Street> sStreets;
    private Pair<Street, Street> wStreets;

    private IntersectionType type;

    private boolean isNSGreen;
    private boolean isWithoutLights;
    private boolean isEmpty;

    private float time;

    private MapEvent event;

    protected static final int START_FLAG = 1;
    protected static final int DEFAULT_FLAG = 0;

    public GridNode(Coords coords) {
        super(coords);
        this.type = new IntersectionType();
        this.isEmpty = false;
        this.isNSGreen = false;
        this.isWithoutLights = false;

        this.time = 0f;
    }

    @Override
    public void update() {
        if (this.getType().get_Type().equals(Type.LType) || this.getType().get_Type().equals(Type.oType)
        || this.getType().get_Type().equals(Type.IType))
            return;

        if (!this.isWithoutLights) {
            float delta = 0f;
            if (this.isNSGreen) {
                if (this.nStreets != null) {
                    if (this.nStreets.getX() != null)
                        delta = this.nStreets.getX().getLights().getGreenLightTime();
                    else
                        delta = this.sStreets.getX().getLights().getGreenLightTime();
                } else
                    delta = this.sStreets.getX().getLights().getGreenLightTime();
            } else {
                if (this.wStreets != null) {
                    if (this.wStreets.getX() != null) {
                        delta = this.wStreets.getX().getLights().getGreenLightTime();
                    }
                } else if (this.eStreets != null)
                    if (this.eStreets.getX() != null)
                        delta = this.eStreets.getX().getLights().getGreenLightTime();
            }

            if (this.time >= delta) {
                this.time = 0f;
                this.changeLights(DEFAULT_FLAG);
                event.onLightsChange(getPosition().getX(), getPosition().getY());
            }

            if (this.nStreets != null)
                if (this.nStreets.getX() != null)
                    this.nStreets.getX().getLights().update();

            if (this.eStreets != null)
                if (this.eStreets.getX() != null)
                    this.eStreets.getX().getLights().update();

            if (this.sStreets != null)
                if (this.sStreets.getX() != null)
                    this.sStreets.getX().getLights().update();

            if (this.wStreets != null)
                if (this.wStreets.getX() != null)
                    this.wStreets.getX().getLights().update();

            this.time += TimeController.getInstance().getDeltaTime();
        }
    }

    public void fillNode(List<String> data) {
        this.name = data.get(1);
        this.type = new IntersectionType(data.get(3), data.get(4), data.get(5));
        this.checkIfEmpty();
    }

    public void addCarToLights(Car car, Direction direction) {
        Pair<Street, Street> pair = this.getStreets(Direction.opposite(direction));
        if (pair != null)
            if (pair.getX() != null)
                pair.getX().getLights().addCarToQue(car);
    }

    public void adjustLights() {
        if (type.get_Type().equals(Type.LType) || type.get_Type().equals(Type.IType))
            isWithoutLights = true;
        else
            isWithoutLights = type.adjustTrafficLights();

    }

    public void adjustStreets() {
        if (nStreets != null)
            if (nStreets.getX() == null && nStreets.getY() == null)
                nStreets = null;

        if (sStreets != null)
            if (sStreets.getX() == null && sStreets.getY() == null)
                sStreets = null;

        if (eStreets != null)
            if (eStreets.getX() == null && eStreets.getY() == null)
                eStreets = null;

        if (wStreets != null)
            if (wStreets.getX() == null && wStreets.getY() == null)
                wStreets = null;
    }

    public void changeLights(int flag) {
        this.isNSGreen = !this.isNSGreen;
        changeAllLights(flag);
    }

    private void changeAllLights(int flag) {
        if (flag != START_FLAG) {
            if (this.eStreets != null)
                if (this.eStreets.getX() != null)
                    this.eStreets.getX().getLights().changeLights();

            if (this.wStreets != null)
                if (this.wStreets.getX() != null)
                    this.wStreets.getX().getLights().changeLights();
        }


        if (this.nStreets != null)
            if (this.nStreets.getX() != null)
                this.nStreets.getX().getLights().changeLights();

        if (this.sStreets != null)
            if (this.sStreets.getX() != null)
                this.sStreets.getX().getLights().changeLights();
    }


    public Street pickStartingPoint() throws MildException{
        if (!this.isNSGreen) {
            if (this.sStreets != null)
                if (this.sStreets.getX() != null)
                    return this.sStreets.getX();

            if (this.nStreets != null)
                if (this.nStreets.getX() != null)
                    return this.nStreets.getX();
        } else {
            if (this.wStreets != null)
                if (this.wStreets.getX() != null)
                    return this.wStreets.getX();

            if (this.eStreets != null)
                if (this.eStreets.getX() != null)
                    return this.eStreets.getX();
        }

        throw new MildException("UNABLE TO PICK STARTING POINT FOR CAR", this.getClass().getName(), ExceptionType.CAR_POSITIONING_EXCEPTION);
    }

    public boolean checkIfEmpty() {
        if (this.isEmpty)
            return true;

        if (this.type.get_Type().equals(Type.oType))
            this.isEmpty = true;
        else
            return false;
        return true;
    }

    public void addStreets(Pair<Street, Street> streets, Direction direction) {
        switch (direction) {
            case North:
                this.nStreets = new Pair<>(streets.getX(), streets.getY());
                if (streets.getX() != null)
                    streets.getX().setDirection(Direction.South);
                if (streets.getY() != null)
                    streets.getY().setDirection(Direction.North);
                break;
            case East:
                this.eStreets = new Pair<>(streets.getX(), streets.getY());
                if (streets.getX() != null)
                    streets.getX().setDirection(Direction.West);
                if (streets.getY() != null)
                    streets.getY().setDirection(Direction.East);
                break;
            case South:
                this.sStreets = new Pair<>(streets.getX(), streets.getY());
                if (streets.getX() != null)
                    streets.getX().setDirection(Direction.North);
                if (streets.getY() != null)
                    streets.getY().setDirection(Direction.South);
                break;
            case West:
                this.wStreets = new Pair<>(streets.getX(), streets.getY());
                if (streets.getX() != null)
                    streets.getX().setDirection(Direction.East);
                if (streets.getY() != null)
                    streets.getY().setDirection(Direction.West);
                break;
        }
    }

    public int queSize(){
        int max = 0;
        if(!isNSGreen()){
            if(nStreets != null)
                if(nStreets.getX() != null)
                    max = nStreets.getX().getLights().getQue().size();
            if(sStreets != null)
                if(sStreets.getX() != null)
                    if(max < sStreets.getX().getLights().getQue().size())
                        max = sStreets.getX().getLights().getQue().size();
        }
        else{
            if(wStreets != null)
                if(wStreets.getX() != null)
                    max = wStreets.getX().getLights().getQue().size();
            if(eStreets != null)
                if(eStreets.getX() != null)
                    if(max < eStreets.getX().getLights().getQue().size())
                        max = eStreets.getX().getLights().getQue().size();
        }
        return max;
    }

    public void setEvent(MapEvent event) {
        this.event = event;
    }

    //GET
    public Pair<Street, Street> getStreets(Direction direction) {
        switch (direction) {
            case North:
                return nStreets;

            case East:
                return eStreets;

            case South:
                return sStreets;

            case West:
                return wStreets;

        }

        return nStreets;
    }

    public IntersectionType getType() {
        return type;
    }

    public boolean getIsWithoutLights() {
        return isWithoutLights;
    }

    public boolean isNSGreen() {
        return isNSGreen;
    }

    public static ArrayList<Pair<GridNode, Direction>> getAdjacentNodes(GridNode node) {
        ArrayList<Pair<GridNode, Direction>> list = new ArrayList<>();

        if (node.checkIfEmpty())
            return list;

        if (node.getStreets(Direction.North) != null)
            if (node.nStreets.getY() != null)
                list.add(new Pair<>(node.nStreets.getY().getDestination(), Direction.North));
        if (node.getStreets(Direction.East) != null)
            if (node.eStreets.getY() != null)
                list.add(new Pair<>(node.eStreets.getY().getDestination(), Direction.East));
        if (node.getStreets(Direction.South) != null)
            if (node.sStreets.getY() != null)
                list.add(new Pair<>(node.sStreets.getY().getDestination(), Direction.South));
        if (node.getStreets(Direction.West) != null)
            if (node.wStreets.getY() != null)
                list.add(new Pair<>(node.wStreets.getY().getDestination(), Direction.West));

        return list;
    }

    public void resetTime() {
        time = 0f;
    }

    @Override
    public String toString() {
        String str = "";
        str += this.name + "\n";
        str += this.position.getX() + ":" + this.position.getY() + "\n";
        str += this.type.get_Type();

        return str;
    }

}
