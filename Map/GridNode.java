package Map;

import Utils.Pair;

import java.util.ArrayList;

public class GridNode extends Coords{
    private Pair<Street,Street>[] streets;
    private Pair<TrafficLights,TrafficLights>[] boundedLights;
    private boolean isWithoutLights;

    public GridNode(Coords coords)
    {
        super(coords);
    }

    public void FillNode()
    {
        // Wypelnienie informacji po stworzeniu w klasie map calej siatki wezlow
    }

    public void BoundLights()
    {
        // Sprzezenie ze soba swiatel w celu zmiany swiatel bezproblemowej
    }

    public void ChangeLights()
    {
        // Zmiana swiatel po evencie klikniecia myszka
    }
}
