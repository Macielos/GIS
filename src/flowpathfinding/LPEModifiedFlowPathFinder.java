package flowpathfinding;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import graphs.EdgeWithCapacity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jwetesko on 17.05.17.
 */
public class LPEModifiedFlowPathFinder  implements IFlowPathFinder {

    @Override
    public FlowPathResult findFlowPaths(FlowPathParameters parameters) {

        int unitsLeft = parameters.getUnitCount();
        List<FlowPath> flowPaths = new ArrayList<>();

        //zmienne pomocnicze do znajdywania najkrotszej sciezki zawierajaca krawedz z najdluzszej sciezki

        ArrayList<EdgeWithCapacity> choosenPath = new ArrayList<EdgeWithCapacity>();

        //dopóki nie zostały wysłane wszystkie jednostki
        while (unitsLeft > 0) {

            //znajdź najdłuższą ścieżkę w grafie
            GraphPath<Integer, EdgeWithCapacity> longestPath = BellmanFordShortestPath.findPathBetween(parameters.getGraph(), parameters.getSource(), parameters.getSink());

            if(longestPath == null) {
                //nie istnieje najdluzsza sciezka - koniec dzialania algorytmu
                break;
            }

            int pathLenght = longestPath.getLength();

            //ustaw wagi wszystkich krawedzi na 1
            for (EdgeWithCapacity e : parameters.getGraph().edgeSet()){
                parameters.getGraph().setEdgeWeight(e,1);
            }

            //dla każdej krawędzi należącej do tej ścieżki znajdź najkrótszą ścieżkę w grafie zawierającą tę krawędź
            for (EdgeWithCapacity e : longestPath.getEdgeList()) {
                Integer u = parameters.getGraph().getEdgeSource(e);
                Integer v = parameters.getGraph().getEdgeTarget(e);

                DijkstraShortestPath dijkstra1 = new DijkstraShortestPath(parameters.getGraph());
                GraphPath a = dijkstra1.getPath(parameters.getSource(), u);
                GraphPath b = dijkstra1.getPath(v, parameters.getSink());

                ArrayList<EdgeWithCapacity> path = new ArrayList<EdgeWithCapacity>();

                for (EdgeWithCapacity e1 : (List<EdgeWithCapacity>) a.getEdgeList()) {
                    path.add(e1);
                }
                path.add(e);
                for (EdgeWithCapacity e2 : (List<EdgeWithCapacity>) b.getEdgeList()) {
                    path.add(e2);
                }

                //wybierz najkrotsza ze znalezionych sciezek
                if (path.size() <= pathLenght) {
                    choosenPath = path;
                    pathLenght = path.size();
                }
            }

        //przepustowosc sciezki to min z przepustowosci wszystkich
            int pathCapacity = Math.min(choosenPath.stream().map(e -> e.getCapacity()).min(Comparator.naturalOrder()).orElse(0), unitsLeft);
            if(pathCapacity > 0) {
                List<Integer> vertexList = new ArrayList<Integer>();
                choosenPath.forEach(e -> vertexList.add(e.getEdgeSource()));
                vertexList.add(parameters.getSink());
                flowPaths.add(new FlowPath(vertexList, pathCapacity));
            }

            //redukcja przepustowosci o to, co zostalo przeslane w tej iteracji
            for(EdgeWithCapacity e: choosenPath) {
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
        return "LPEmodified";
    }
}
