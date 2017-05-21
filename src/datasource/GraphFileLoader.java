package datasource;

import flowpathfinding.FlowPathParameters;
import graphs.EdgeWithCapacity;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Maciek on 14.05.2017.
 * Klasa wczytujaca graf z pliku
 */
public class GraphFileLoader {

    public FlowPathParameters loadGraph(String filename, int unitCount, String algorithm) throws IOException {

        /* ograniczenie dł. dekompozycji z dołu*/
        int capacitySum = 0;

        System.out.println("Loading graph from "+filename);
        List<String> lines = Files.lines(Paths.get(filename)).collect(Collectors.toList());

        if(lines.size() < 4) {
            throw new RuntimeException("Incorrect format - file too short");
        }

        Iterator<String> iterator = lines.iterator();
        int vertexCount = Integer.parseInt(iterator.next());
        int source = Integer.parseInt(iterator.next());
        int sink = Integer.parseInt(iterator.next());

        if(source >= vertexCount || sink >= vertexCount) {
            throw new RuntimeException("Incorrect format - invalid source or sink");
        }

        DefaultDirectedGraph<Integer, EdgeWithCapacity> graph = new DefaultDirectedGraph<>(EdgeWithCapacity.class);

        /* wczytywanie wierzcholkow */
        for(int i=0; i<vertexCount; ++i) {
            graph.addVertex(i);
        }

        /* wczytywanie krawedzi */
        while(iterator.hasNext()) {
            String line = iterator.next();
            String[] parts = line.split(" ");
            if(parts.length != 3) {
                throw new RuntimeException("Incorrect format - line: "+line);
            }

            int edgeSource = Integer.parseInt(parts[0]);
            int edgeTarget = Integer.parseInt(parts[1]);
            int capacity = Integer.parseInt(parts[2]);
            capacitySum += capacity;

            if(edgeSource >= vertexCount || edgeTarget >= vertexCount) {
                throw new RuntimeException("Incorrect format - invalid vertices in line: "+line);
            }

            /* ustalanie wag krawedzi zaleznie od tego, czy wykorzystywany jest algorytm Dijkstry czy Bellmana-Forda */
            if(capacity > 0) {
                graph.addEdge(edgeSource, edgeTarget, new EdgeWithCapacity(capacity));
                if (algorithm.equals("SPE"))
                    graph.setEdgeWeight(graph.getEdge(edgeSource, edgeTarget), 1);
                if (algorithm.equals("LPE"))
                    graph.setEdgeWeight(graph.getEdge(edgeSource, edgeTarget), -1);
            }
        }

        System.out.println("Graph loaded with "+graph.vertexSet().size()+" vertices, sending "+unitCount+" units from "+source+" to "+sink);
        System.out.println("Expected lowest decomposition length: "+  capacitySum/unitCount);
        System.out.println("-----------");

        return new FlowPathParameters(graph, source, sink, unitCount, algorithm);
    }
}
