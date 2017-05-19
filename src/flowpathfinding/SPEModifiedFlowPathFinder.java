package flowpathfinding;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import graphs.EdgeWithCapacity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Maciek on 14.05.2017.
 * Klasa zawierajaca implementacje algorytmu SPEmodified
 */
public class SPEModifiedFlowPathFinder implements IFlowPathFinder {

    @Override
    public FlowPathResult findFlowPaths(FlowPathParameters parameters) {

        long startTime = System.currentTimeMillis();

        int unitsLeft = parameters.getUnitCount();
        List<FlowPath> flowPaths = new ArrayList<>();

        int decompositionLength = 0;

        /* dopoki nie przeslane zostaly wszystkie jednostki */
        while(unitsLeft > 0) {

            GraphPath<Integer, EdgeWithCapacity> path = DijkstraShortestPath.findPathBetween(parameters.getGraph(), parameters.getSource(), parameters.getSink());

            if(path == null) {
                /* nie istnieje droga - koniec dzialania algorytmu */
                break;
            }

            /* ustalenie dlugosci dekompozycji */
            if (path.getEdgeList().size() >= decompositionLength) {
                decompositionLength = path.getEdgeList().size();
            }

            /* przepustowosc sciezki to min z przepustowosci wszystkich krawedzi */
            int pathCapacity = Math.min(path.getEdgeList().stream().map(e -> e.getCapacity()).min(Comparator.naturalOrder()).orElse(0), unitsLeft);
            if(pathCapacity > 0) {
                flowPaths.add(new FlowPath(path.getVertexList(), pathCapacity));
            }

            /* redukujemy przepustowosci lukow na znalezionej sciezce o jednostki przeslane w tej iteracji */
            for(EdgeWithCapacity e: path.getEdgeList()) {
                e.setCapacity(e.getCapacity() - pathCapacity);
                if(e.getCapacity() == 0) {
                    parameters.getGraph().removeEdge(e);
                }
            }

            /* redukcja liczby jednostek */
            unitsLeft -= pathCapacity;
        }

        System.out.println("Lenght of decomposition: " + decompositionLength);
        return new FlowPathResult(unitsLeft, flowPaths,  System.currentTimeMillis() - startTime);
    }

    @Override
    public String getAlgorithmName() {
        return "SPEmodified";
    }
}
