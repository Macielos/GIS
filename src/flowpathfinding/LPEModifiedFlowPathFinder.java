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
 * Klasa zawierajaca implementacje algorytmu LPEmodified
 */

public class LPEModifiedFlowPathFinder  implements IFlowPathFinder {

    @Override
    public FlowPathResult findFlowPaths(FlowPathParameters parameters) {

        long startTime = System.currentTimeMillis();

        int unitsLeft = parameters.getUnitCount();
        List<FlowPath> flowPaths = new ArrayList<>();

        /* zmienne pomocnicze do znajdywania najkrotszej sciezki zawierajaca krawedz z najdluzszej sciezki */

        ArrayList<EdgeWithCapacity> chosenPath = new ArrayList<EdgeWithCapacity>();

        /* dlugosc dekompozycji */
        int decompositionLength = 0;

        /* dopóki nie zostały wysłane wszystkie jednostki */
        while (unitsLeft > 0) {

            /* znajdowanie najdłuższej ścieżki w grafie */
            GraphPath<Integer, EdgeWithCapacity> longestPath = BellmanFordShortestPath.findPathBetween(parameters.getGraph(), parameters.getSource(), parameters.getSink());

            if(longestPath == null) {
                /* nie istnieje najdluzsza sciezka - koniec dzialania algorytmu */
                break;
            }

            int pathLenght = longestPath.getLength();

            /* ustaw wagi wszystkich krawedzi na 1 */
            for (EdgeWithCapacity e : parameters.getGraph().edgeSet()){
                parameters.getGraph().setEdgeWeight(e,1);
            }

            /* dla każdej krawędzi należącej do tej ścieżki znajdź najkrótszą ścieżkę w grafie zawierającą tę krawędź */
            for (EdgeWithCapacity e : longestPath.getEdgeList()) {
                Integer u = parameters.getGraph().getEdgeSource(e);
                Integer v = parameters.getGraph().getEdgeTarget(e);

                BellmanFordShortestPath bellmanFord1 = new BellmanFordShortestPath(parameters.getGraph());
                GraphPath a = bellmanFord1.getPath(parameters.getSource(), u);
                GraphPath b = bellmanFord1.getPath(v, parameters.getSink());

                ArrayList<EdgeWithCapacity> path = new ArrayList<EdgeWithCapacity>();

                for (EdgeWithCapacity e1 : (List<EdgeWithCapacity>) a.getEdgeList()) {
                    path.add(e1);
                }
                path.add(e);
                for (EdgeWithCapacity e2 : (List<EdgeWithCapacity>) b.getEdgeList()) {
                    path.add(e2);
                }

                /* wybierz najdluzsza ze znalezionych sciezek */
                if (path.size() >= pathLenght) {
                    chosenPath = path;
                    pathLenght = path.size();
                }

                /* ustal dlugosc dekompozycji */
                if (path.size() >= decompositionLength) {
                    decompositionLength = path.size();
                }
            }

        /* przepustowosc sciezki to min z przepustowosci wszystkich krawedzi w sciezce */
            int pathCapacity = Math.min(chosenPath.stream().map(e -> e.getCapacity()).min(Comparator.naturalOrder()).orElse(0), unitsLeft);
            if(pathCapacity > 0) {
                List<Integer> vertexList = new ArrayList<Integer>();
                chosenPath.forEach(e -> vertexList.add(e.getEdgeSource()));
                vertexList.add(parameters.getSink());
                flowPaths.add(new FlowPath(vertexList, pathCapacity));
            }

            /* redukcja przepustowosci o to, co zostalo przeslane w tej iteracji */
            for(EdgeWithCapacity e: chosenPath) {
                e.setCapacity(e.getCapacity() - pathCapacity);
                if(e.getCapacity() == 0) {
                    parameters.getGraph().removeEdge(e);
                }
            }

            /* zmniejszenie liczby jednostek do przeslania */
            unitsLeft -= pathCapacity;
        }

        System.out.println("Lenght of decomposition: " + decompositionLength);
        return new FlowPathResult(unitsLeft, flowPaths, System.currentTimeMillis() - startTime);
    }

}
