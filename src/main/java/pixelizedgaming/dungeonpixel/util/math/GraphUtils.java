package pixelizedgaming.dungeonpixel.util.math;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import org.bukkit.util.Vector;

import java.util.*;

public class GraphUtils {

    // generates delaunay triangulation of a set of points using the bowyer-watson algorithm
    public static Set<Triangle> graphFromPoints(List<Vector> locSet){

        Set<Triangle> triangulation = new HashSet<>();

        if (locSet.size() == 0){
            return triangulation;
        }

        double y = locSet.get(0).getY();
        double maxX = Double.NEGATIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;
        double minX = Double.POSITIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;

        for (Vector point : locSet) {
            maxX = Math.max(maxX, point.getX());
            maxZ = Math.max(maxZ, point.getZ());
            minX = Math.min(minX, point.getX());
            minZ = Math.min(minZ, point.getZ());
        }

        double dx = maxX - minX;
        double dz = maxZ - minZ;
        double deltaMax = Math.max(dx, dz);
        double midx = (minX + maxX) / 2.0;
        double midz = (minZ + maxZ) / 2.0;


        Vector p1 = new Vector(midx - 20 * deltaMax,y ,  midz - deltaMax);
        Vector p2 = new Vector(midx,y, midz + 20 * deltaMax);
        Vector p3 = new Vector(midx + 20 * deltaMax,y , midz - deltaMax);

        Triangle superTriangle = new Triangle(p1, p2, p3);

        triangulation.add(superTriangle);

        for (Vector point : locSet) {
            Set<Triangle> badTriangles = new HashSet<>();
            for (Triangle triangle : triangulation) {
                if (triangleCircumcircleContains(triangle, point)) {
                    badTriangles.add(triangle);
                }
            }

            Set<Edge> polygon = new HashSet<>();
            for (Triangle triangle : badTriangles) {
                for (Edge edge : triangle.edges()) {
                    if (!sharedByOtherTriangles(edge, triangle, badTriangles)) {
                        polygon.add(edge);
                    }
                }
            }

            triangulation.removeAll(badTriangles);

            for (Edge edge : polygon) {
                Triangle newTriangle = new Triangle(edge.p1, edge.p2, point);
                triangulation.add(newTriangle);
            }
        }

        // Remove triangles that share vertices with the super-triangle
        triangulation.removeIf(t -> t.p1 == p1 || t.p1 == p2 || t.p1 == p3
                || t.p2 == p1 || t.p2 == p2 || t.p2 == p3
                || t.p3 == p1 || t.p3 == p2 || t.p3 == p3);

        return triangulation;
    }

    private static boolean triangleCircumcircleContains(Triangle triangle, Vector point) {

        // Calculate circumcenter
        double D = 2 * (triangle.p1.getX() * (triangle.p2.getZ() - triangle.p3.getZ()) + triangle.p2.getX() * (triangle.p3.getZ() - triangle.p1.getZ()) + triangle.p3.getX() * (triangle.p1.getZ() - triangle.p2.getZ()));
        double centerX = ((triangle.p1.getX() * triangle.p1.getX()+ triangle.p1.getZ() * triangle.p1.getZ()) * (triangle.p2.getZ() - triangle.p3.getZ()) + (triangle.p2.getX() * triangle.p2.getX() + triangle.p2.getZ() * triangle.p2.getZ()) * (triangle.p3.getZ() - triangle.p1.getZ()) + (triangle.p3.getX() * triangle.p3.getX() + triangle.p3.getZ() * triangle.p3.getZ()) * (triangle.p1.getZ() - triangle.p2.getZ())) / D;
        double centerZ = ((triangle.p1.getX() * triangle.p1.getX() + triangle.p1.getZ() * triangle.p1.getZ()) * (triangle.p3.getX() - triangle.p2.getX()) + (triangle.p2.getX() * triangle.p2.getX() + triangle.p2.getZ() * triangle.p2.getZ()) * (triangle.p1.getX() - triangle.p3.getX() ) + (triangle.p3.getX()  * triangle.p3.getX()  + triangle.p3.getZ() * triangle.p3.getZ()) * (triangle.p2.getX() - triangle.p1.getX())) / D;

        // Distance from center to point
        double radiusSquared = Math.pow(centerX - point.getX(), 2) + Math.pow(centerZ - point.getZ() , 2);

        // Radius of circumcircle
        double distSquared = Math.pow(triangle.p1.getX() - centerX, 2) + Math.pow(triangle.p1.getZ()  - centerZ, 2);

        return distSquared >= radiusSquared;
    }

    private static boolean sharedByOtherTriangles(Edge edge, Triangle triangle, Set<Triangle> triangles) {
        for (Triangle t : triangles) {
            if (t != triangle && t.contains(edge.p1) && t.contains(edge.p2)) {
                return true;
            }
        }
        return false;
    }

    static class Edge {
        Vector p1, p2;

        public Edge(Vector p1, Vector p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Edge edge = (Edge) obj;
            return (p1.equals(edge.p1) && p2.equals(edge.p2)) || (p1.equals(edge.p2) && p2.equals(edge.p1));
        }

        @Override
        public int hashCode() {
            return p1.hashCode() + p2.hashCode();
        }
    }

    // Add each triangle edge to an edge set, make graph from edge set
    public static MutableGraph<Vector> trianglesToLocGraph(Set<Triangle> triangleSet){
        Set<Edge> edges = new HashSet<>();
        Set<Vector> vectors = new HashSet<>();
        for(Triangle tri : triangleSet){
            edges.addAll(Arrays.asList(tri.edges()));
            vectors.add(tri.p1);
            vectors.add(tri.p2);
            vectors.add(tri.p3);
        }

        MutableGraph<Vector> graph = GraphBuilder
                .undirected()
                .expectedNodeCount(vectors.size())
                .allowsSelfLoops(false)
                .build();
        for(Vector v : vectors){
            graph.addNode(v);
        }
        for(Edge e: edges){
            graph.putEdge(e.p1, e.p2);
        }
        return graph;
    }

    // Takes a mst and adds random edges from original graph
    public static MutableGraph<Vector> addLoops(MutableGraph<Vector> originalGraph, MutableGraph<Vector> mst){
        Random rand = new Random();
        for(EndpointPair<Vector> e : originalGraph.edges()) {
            if (rand.nextInt(100) < 25 && !mst.hasEdgeConnecting(e)){
                mst.putEdge(e);
            }
        }
        return mst;
    }

    // Prims algorithm to get minimum spanning tree of a graph
    public static MutableGraph<Vector> findMST(MutableGraph<Vector> graph){
        Set<Vector> visitedNodes = new HashSet<>();
        MutableGraph<Vector> minimumSpanningTree = GraphBuilder.undirected().build();

        if (graph.nodes().isEmpty()) {
            return minimumSpanningTree;
        }

        Vector startNode = graph.nodes().iterator().next();
        visitedNodes.add(startNode);

        while (visitedNodes.size() < graph.nodes().size()) {
            Edge minEdge = findMinimumEdge(graph, visitedNodes);
            if (minEdge != null) {
                minimumSpanningTree.putEdge(minEdge.p1, minEdge.p2);
                visitedNodes.add(minEdge.p2);
            } else {
                break; // The graph is not connected
            }
        }

        return minimumSpanningTree;
    }

    private static Edge findMinimumEdge(Graph<Vector> graph, Set<Vector> visitedNodes) {
        Edge minEdge = null;
        double minWeight = Double.MAX_VALUE;

        for (Vector node : visitedNodes) {
            for (Vector successor : graph.successors(node)) {
                if (!visitedNodes.contains(successor)) {
                    double weight = node.distanceSquared(successor);
                    if (weight < minWeight) {
                        minWeight = weight;
                        minEdge = new Edge(node, successor);
                    }
                }
            }
        }

        return minEdge;
    }


}
