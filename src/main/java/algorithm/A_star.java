package algorithm;

import map.*;
import util.*;

import java.util.*;


public class A_star {
    // Based on
    // https://rosettacode.org/wiki/A*_search_algorithm#Java
    private final List<Node> open;
    private final List<Node> closed;
    private final List<Node> path;
    private Node now;

    private final EndPointFinder endPoints;

    static class Node implements Comparable<Node> {
        public final Node parent;
        public final int x;
        public final int y;
        public float g;
        public final float h;

        Node(Node parent, int yPos, int xPos, float g, float h) {
            this.parent = parent;
            this.x = xPos;
            this.y = yPos;
            this.g = g;
            this.h = h;
        }

        @Override
        public int compareTo(Node o) {
            return (int) ((this.g + this.h) - (o.g + o.h));
        }
    }

    public A_star(GridNode[][] map) {
        this.open = new ArrayList<>();
        this.closed = new ArrayList<>();
        this.path = new ArrayList<>();

        this.endPoints = new EndPointFinder(map);
        this.now = new Node(null, this.endPoints.getStartPoint().getY(), this.endPoints.getStartPoint().getX(), 0, 0);
    }

    public A_star(GridNode[][] map, SPair<Integer> start, SPair<Integer> end) {
        this.open = new ArrayList<>();
        this.closed = new ArrayList<>();
        this.path = new ArrayList<>();

        this.endPoints = new EndPointFinder(map, start, end);
        this.now = new Node(null, this.endPoints.getStartPoint().getY(), this.endPoints.getStartPoint().getX(), 0, 0);
    }

    public LinkedList<GridNode> findPathTo() {

        this.closed.add(this.now);
        addNeighborsToOpenList();
        while (this.now.y != this.endPoints.getEndPoint().getY() || this.now.x != this.endPoints.getEndPoint().getX()) {
            if (this.open.isEmpty()) {
                return null;
            }
            this.now = this.open.get(0);
            this.open.remove(0);
            this.closed.add(this.now);
            addNeighborsToOpenList();
        }
        this.path.add(0, this.now);
        while (this.now.x != this.endPoints.getStartPoint().getX() || this.now.y != this.endPoints.getStartPoint().getY()) {
            this.now = this.now.parent;
            this.path.add(0, this.now);
        }
        LinkedList<GridNode> list = new LinkedList<>();
        for (Node node : this.path) {
            list.add(this.endPoints.getMap()[node.y][node.x]);
        }

        return list;
    }

    private float distance(int dx, int dy) {
        return Math.abs(this.now.x + dx - this.endPoints.getEndPoint().getX()) + Math.abs(this.now.y + dy - this.endPoints.getEndPoint().getY()); // else return "Manhattan distance"
    }

    private void addNeighborsToOpenList() {
        Node node;
        List<Pair<GridNode, Direction>> nodes = GridNode.getAdjacentNodes(this.endPoints.getMap()[this.now.y][this.now.x]);
        int x = 0, y = 0;
        for (Pair<GridNode, Direction> item : nodes) {
            switch (item.getY()) {
                case North:
                    y = -1;
                    break;
                case East:
                    x = 1;
                    break;
                case South:
                    y = 1;
                    break;
                case West:
                    x = -1;
                    break;
            }
            node = new Node(this.now, this.now.y + y, this.now.x + x, this.now.g, this.distance(x, y));
            node.g += 1;
            open.add(node);
            x = 0;
            y = 0;
        }
        Collections.sort(this.open);
    }
}