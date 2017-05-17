package flowpathfinding;

import graphs.EdgeWithCapacity;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * Created by Maciek on 14.05.2017.
 */
public class FlowPathParameters {

    private final DefaultDirectedGraph<Integer, EdgeWithCapacity> graph;
    private final int source;
    private final int sink;
    private final int unitCount;

    public FlowPathParameters(DefaultDirectedGraph<Integer, EdgeWithCapacity> graph,
                              int source, int sink, int unitCount) {
        this.graph = graph;
        this.source = source;
        this.sink = sink;
        this.unitCount = unitCount;
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
}
