package pixelizedgaming.dungeonpixel.util.math;

import org.bukkit.util.Vector;

public class Triangle {
    public Vector p1;
    public Vector p2;
    public Vector p3;

    public Triangle(Vector p1, Vector p2, Vector p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    boolean contains(Vector e) {
        return (p1 == e || p2 == e || p3 == e );
    }

    GraphUtils.Edge[] edges() {
        GraphUtils.Edge[] edges = new GraphUtils.Edge[3];
        edges[0] = new GraphUtils.Edge(p1, p2);
        edges[1] = new GraphUtils.Edge(p1, p3);
        edges[2] = new GraphUtils.Edge(p2, p3);
        return edges;
    }
}
