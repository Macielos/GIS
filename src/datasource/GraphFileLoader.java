package datasource;

import flowpathfinding.FlowPathParameters;
import graphs.EdgeWithCapacity;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Maciek on 14.05.2017.
 */
public class GraphFileLoader {

    public FlowPathParameters loadGraph(String filename, int unitCount) throws IOException {
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

        for(int i=0; i<vertexCount; ++i) {
            graph.addVertex(i);
        }
        while(iterator.hasNext()) {
            String line = iterator.next();
            String[] parts = line.split(" ");
            if(parts.length != 3) {
                throw new RuntimeException("Incorrect format - line: "+line);
            }
            int edgeSource = Integer.parseInt(parts[0]);
            int edgeTarget = Integer.parseInt(parts[1]);
            int capacity = Integer.parseInt(parts[2]);
            if(edgeSource >= vertexCount || edgeTarget >= vertexCount) {
                throw new RuntimeException("Incorrect format - invalid vertices in line: "+line);
            }
            if(capacity > 0) {
                graph.addEdge(edgeSource, edgeTarget, new EdgeWithCapacity(capacity));
            }
        }
        System.out.println("Graph loaded with "+graph.vertexSet().size()+" vertices, sending "+unitCount+" units from "+source+" to "+sink);
        return new FlowPathParameters(graph, source, sink, unitCount);
    }
}
