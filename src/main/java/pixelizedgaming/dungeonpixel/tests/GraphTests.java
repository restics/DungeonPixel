package pixelizedgaming.dungeonpixel.tests;

import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import org.bukkit.util.Vector;
import org.junit.Test;
import pixelizedgaming.dungeonpixel.util.math.GraphUtils;
import pixelizedgaming.dungeonpixel.util.math.Triangle;



import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class GraphTests {

    @Test
      void testDelaunayTriangulation() {
        // Test case with three points forming a triangle
        List<Vector> points1 = new ArrayList<>();
        points1.add(new Vector(0, 0,0));
        points1.add(new Vector(1, 0 ,0));
        points1.add(new Vector(0.5, 0, 1));

        Set<Triangle> triangulation1 = GraphUtils.graphFromPoints(points1);
        assertEquals(1, triangulation1.size());

        // Test case with four points forming a convex quadrilateral
        List<Vector> points2 = new ArrayList<>();
        points2.add(new Vector(0,0 , 0));
        points2.add(new Vector(2, 0 ,0));
        points2.add(new Vector(2, 0 ,2));
        points2.add(new Vector(0, 0 ,2));

        Set<Triangle> triangulation2 = GraphUtils.graphFromPoints(points2);
        assertEquals(2, triangulation2.size());

        // Test case with five points forming a concave pentagon
        List<Vector> points3 = new ArrayList<>();
        points3.add(new Vector(0,0, 0));
        points3.add(new Vector(2,0, 0));
        points3.add(new Vector(3, 0,1));
        points3.add(new Vector(2,0, 2));
        points3.add(new Vector(0, 0,2));

        Set<Triangle> triangulation3 = GraphUtils.graphFromPoints(points3);
        assertEquals(3, triangulation3.size());
    }

    @Test
    void testEmptySet() {
        // Test with an empty set of points
        List<Vector> emptySet = new ArrayList<>();
        Set<Triangle> triangulation = GraphUtils.graphFromPoints(emptySet);
        assertEquals(0, triangulation.size());
    }

    @Test
    void testSinglePoint() {
        // Test with a single point
        List<Vector> singlePointSet = new ArrayList<>();
        singlePointSet.add(new Vector(1,0, 1));
        Set<Triangle> triangulation = GraphUtils.graphFromPoints(singlePointSet);
        assertEquals(0, triangulation.size());
    }

    @Test
    public void testEmptyTriangleSet() {
        Set<Triangle> emptySet = new HashSet<>();
        MutableGraph<Vector> resultGraph = GraphUtils.trianglesToLocGraph(emptySet);
        assertTrue("Graph should be empty for an empty Triangle set", resultGraph.nodes().isEmpty());
    }

    @Test
    public void testSingleTriangle() {
        Triangle triangle = new Triangle(new Vector(0,0, 0), new Vector(1,0, 0), new Vector(0, 0,1));
        Set<Triangle> singleTriangleSet = new HashSet<>(Arrays.asList(triangle));
        MutableGraph<Vector> resultGraph = GraphUtils.trianglesToLocGraph(singleTriangleSet);
        assertEquals("Graph should have 3 nodes for a single triangle",3 , resultGraph.nodes().size());
        assertTrue("Graph should have an edge between p1 and p2", resultGraph.hasEdgeConnecting(triangle.p1, triangle.p2));
        assertTrue("Graph should have an edge between p1 and p3", resultGraph.hasEdgeConnecting(triangle.p1, triangle.p3));
        assertTrue("Graph should have an edge between p2 and p3", resultGraph.hasEdgeConnecting(triangle.p2, triangle.p3));
    }

    @Test
    public void testMultipleTriangles() {
        Triangle triangle1 = new Triangle(new Vector(0, 0,0), new Vector(1, 0,0), new Vector(0,0, 1));
        Triangle triangle2 = new Triangle(new Vector(1, 0,0), new Vector(1, 0,1), new Vector(0,0, 1));
        Set<Triangle> triangleSet = new HashSet<>(Arrays.asList(triangle1, triangle2));
        MutableGraph<Vector> resultGraph = GraphUtils.trianglesToLocGraph(triangleSet);
        assertEquals("Graph should have 4 nodes for two triangles sharing an edge", String.valueOf(4), resultGraph.nodes().size());
        assertTrue("Graph should have an edge between p1 and p2 of triangle1", resultGraph.hasEdgeConnecting(triangle1.p1, triangle1.p2));
        assertTrue("Graph should have an edge between p1 and p3 of triangle1", resultGraph.hasEdgeConnecting(triangle1.p1, triangle1.p3));
        assertTrue("Graph should have an edge between p2 and p3 of triangle1", resultGraph.hasEdgeConnecting(triangle1.p2, triangle1.p3));
        assertTrue("Graph should have an edge connecting p2 of triangle1 and p3 of triangle2", resultGraph.hasEdgeConnecting(triangle1.p2, triangle2.p3));
        assertTrue("Graph should have an edge between p1 and p2 of triangle2", resultGraph.hasEdgeConnecting(triangle2.p1, triangle2.p2));
        assertTrue("Graph should have an edge between p1 and p3 of triangle2", resultGraph.hasEdgeConnecting(triangle2.p1, triangle2.p3));
        assertTrue("Graph should have an edge between p2 and p3 of triangle2", resultGraph.hasEdgeConnecting(triangle2.p2, triangle2.p3));
    }
    @Test
    void testPrimAlgorithm() {
        // Test Case 1: Basic test
        MutableGraph<Vector> graph1 = GraphBuilder.undirected().build();
        graph1.putEdge(new Vector(0,0 , 0), new Vector(2,0, 0));
        graph1.putEdge(new Vector(0,0 , 0), new Vector(3, 0,1));
        graph1.putEdge(new Vector(2,0, 0), new Vector(3, 0,1));
        graph1.putEdge(new Vector(2,0, 0), new Vector(2,0, 2));
        graph1.putEdge(new Vector(3, 0,1), new Vector(2,0, 2));

        Graph<Vector> minimumSpanningTree1 = GraphUtils.findMST(graph1);
        assertEquals("[[2.0,0.0,0.0, 0.0,0.0,0.0], [3.0,0.0,1.0, 2.0,0.0,0.0], [2.0,0.0,2.0, 3.0,0.0,1.0]]", minimumSpanningTree1.edges().toString());

        // Test Case 2: Empty graph
        MutableGraph<Vector> graph2 = GraphBuilder.undirected().build();
        Graph<Vector> minimumSpanningTree2 = GraphUtils.findMST(graph2);
        assertTrue(minimumSpanningTree2.edges().isEmpty());
    }

}