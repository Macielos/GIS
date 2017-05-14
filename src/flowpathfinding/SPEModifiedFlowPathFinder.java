package flowpathfinding;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import graphs.EdgeWithCapacity;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Maciek on 14.05.2017.
 */
public class SPEModifiedFlowPathFinder implements IFlowPathFinder {

    @Override
    public FlowPathResult findFlowPaths(FlowPathParameters parameters) {
        int unitsLeft = parameters.getUnitCount();
        List<FlowPath> flowPaths = new ArrayList<>();
        while(unitsLeft > 0) {
            GraphPath<Integer, EdgeWithCapacity> path = DijkstraShortestPath.findPathBetween(parameters.getGraph(), parameters.getSource(), parameters.getSink());
            if(path == null) {
                //nie istnieje droga - konczymy
                break;
            }
            //przepustowosc sciezki to min z przepustowosci wszystkich krawedzi
            int pathCapacity = Math.min(path.getEdgeList().stream().map(e -> e.getCapacity()).min(Comparator.naturalOrder()).orElse(0), unitsLeft);
            if(pathCapacity > 0) {
                flowPaths.add(new FlowPath(path.getVertexList(), pathCapacity));
            }
            //redukujemy przepustowosci o to, co przeslalismy w tej iteracji
            for(EdgeWithCapacity e: path.getEdgeList()) {
                e.setCapacity(e.getCapacity() - pathCapacity);
                if(e.getCapacity() == 0) {
                    parameters.getGraph().removeEdge(e);
                }
            }
            unitsLeft -= pathCapacity;
        }
        return new FlowPathResult(unitsLeft, flowPaths);
    }

    @Override
    public String getAlgorithmName() {
        return "SPEmodified";
    }
}
