package flowpathfinding;

import graphs.EdgeWithCapacity;
import org.jgrapht.graph.DefaultDirectedGraph;

/**
 * Created by Maciek on 14.05.2017.
 * Klasa reprezentujaca graf oraz liczbe jednostek przesylanych w trakcie dzialania algorytmu
 */
public class FlowPathParameters {

    private final DefaultDirectedGraph<Integer, EdgeWithCapacity> graph;
    private final int source;
    private final int sink;
    private final int unitCount;
    private final String algorithm;
    private final int vertices;
    private final int edges;

    public FlowPathParameters(DefaultDirectedGraph<Integer, EdgeWithCapacity> graph,
                              int source, int sink, int unitCount, String algorithm) {
        this.graph = graph;
        this.source = source;
        this.sink = sink;
        this.unitCount = unitCount;
        this.algorithm = algorithm;
        this.vertices = graph.vertexSet().size();
        this.edges = graph.edgeSet().size();
    }

    public int getSource() {
        return source;
    }

    public int getSink() {
        return sink;
    }

    public int getUnitCount() {
        return unitCount;
    }

    public DefaultDirectedGraph<Integer, EdgeWithCapacity> getGraph() {
        return graph;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public int getVertices() {
        return vertices;
    }

    public int getEdges() {
        return edges;
    }
}
