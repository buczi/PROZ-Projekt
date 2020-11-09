package Algorithm;

import Map.*;
import Utils.*;

import java.util.*;


public class A_star {
    private final List<Node> open;
    private final List<Node> closed;
    private final List<Node> path;
    private Node now;

    private final EndPointFinder endPoints;

    static class Node implements Comparable<Node> {
        public Node parent;
        public int x, y;
        public float g;
        public float h;

        Node(Node parent, int ypos, int xpos, float g, float h) {
            this.parent = parent;
            this.x = xpos;
            this.y = ypos;
            this.g = g;
            this.h = h;
        }

        // Compare by f value (g + h)
        @Override
        public int compareTo(Node o) {
            Node that = (Node) o;
            return (int) ((this.g + this.h) - (that.g + that.h));
        }
    }

    public A_star(GridNode[][] map) {
        this.open = new ArrayList<>();
        this.closed = new ArrayList<>();
        this.path = new ArrayList<>();

        this.endPoints = new EndPointFinder(map);
        this.now = new Node(null, this.endPoints.getStartPoint().getY_(), this.endPoints.getStartPoint().getX_(), 0, 0);
    }

    public A_star(GridNode[][] map, SPair<Integer> start, SPair<Integer> end) {
        this.open = new ArrayList<>();
        this.closed = new ArrayList<>();
        this.path = new ArrayList<>();

        this.endPoints = new EndPointFinder(map, start, end);
        this.now = new Node(null, this.endPoints.getStartPoint().getY_(), this.endPoints.getStartPoint().getX_(), 0, 0);
    }

    public LinkedList<GridNode> findPathTo() {
        this.closed.add(this.now);
        addNeighborsToOpenList();
        while (this.now.y != this.endPoints.getEndPoint().getY_() || this.now.x != this.endPoints.getEndPoint().getX_()) {
            if (this.open.isEmpty()) { // Nothing to examine
                return null;
            }
            this.now = this.open.get(0); // get first node (lowest f score)
            this.open.remove(0); // remove it
            this.closed.add(this.now); // and add to the closed
            addNeighborsToOpenList();
        }
        this.path.add(0, this.now);
        while (this.now.x != this.endPoints.getStartPoint().getX_() || this.now.y != this.endPoints.getStartPoint().getY_()) {
            this.now = this.now.parent;
            this.path.add(0, this.now);
        }
        LinkedList<GridNode> list = new LinkedList<>();
        for (Node node : this.path) {
            list.add(this.endPoints.getMap()[node.y][node.x]);
        }
        return list;
    }

    private static boolean findNeighborInList(List<Node> array, Node node) {
        return array.stream().anyMatch((n) -> (n.x == node.x && n.y == node.y));
    }

    private float distance(int dx, int dy) {
        return Math.abs(this.now.x + dx - this.endPoints.getStartPoint().getX_()) + Math.abs(this.now.y + dy - this.endPoints.getStartPoint().getY_()); // else return "Manhattan distance"
    }

    private void addNeighborsToOpenList() {
        Node node;
        List<Pair<GridNode, Direction>> nodes = GridNode.getAdjacentNodes(this.endPoints.getMap()[this.now.y][this.now.x]);
        int x = 0, y = 0;
        for (Pair<GridNode, Direction> item : nodes) {
            switch (item.getY_()) {
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