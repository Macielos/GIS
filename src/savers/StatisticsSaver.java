package savers;

import com.sun.deploy.util.StringUtils;
import flowpathfinding.FlowPath;
import flowpathfinding.FlowPathParameters;
import flowpathfinding.FlowPathResult;
import org.jgrapht.alg.util.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Arjan on 21.05.2017.
 */
public class StatisticsSaver {

    private List<Pair<FlowPathParameters, FlowPathResult>> results = new ArrayList<>();

    public void addResult(FlowPathParameters flowPathParameters, FlowPathResult flowPathResult) {
        results.add(new Pair<>(flowPathParameters, flowPathResult));
    }

    public void saveStatistics(String outputFile) throws IOException {
        try(FileWriter fw = new FileWriter(outputFile); BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(StringUtils.join(Arrays.asList("Algorithm", "Vertices", "Edges", "Units", "Time (ms)", "Units left", "Units left percentage", "Paths", "Longest path length"), ";"));
            bw.newLine();
            for (Pair<FlowPathParameters, FlowPathResult> result : results) {
                bw.write(StringUtils.join(Arrays.asList(
                        result.getFirst().getAlgorithm(),
                        result.getFirst().getVertices(),
                        result.getFirst().getEdges(),
                        result.getFirst().getUnitCount(),
                        result.getSecond().getTime(),
                        result.getSecond().getUnitsLeft(),
                        percent(result.getSecond().getUnitsLeft(), result.getFirst().getUnitCount()),
                        result.getSecond().getFlowPaths().size(),
                        result.getSecond().getFlowPaths().stream()
                                .mapToInt(p -> p.getPath().size())
                                .max().getAsInt()
                        ).stream().map(x -> x.toString()).collect(Collectors.toList()), ";"));
                bw.newLine();
            }
        }
    }

    private double percent(int i, int j) {
        return ((double) i) / ((double) j) * 100.0;
    }
}
