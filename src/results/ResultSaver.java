package results;

import com.sun.deploy.util.StringUtils;
import flowpathfinding.FlowPath;
import flowpathfinding.FlowPathResult;
import main.Main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Maciek on 14.05.2017.
 */
public class ResultSaver {

    public void saveResult(FlowPathResult result, String outputFile) throws IOException {
        try(FileWriter fw = new FileWriter(outputFile); BufferedWriter bw = new BufferedWriter(fw)) {
            for (FlowPath flowPath : result.getFlowPaths()) {
                List<String> parts = flowPath.getPath().stream()
                        .map(i -> Integer.toString(i))
                        .collect(Collectors.toList());
                parts.add(Integer.toString(flowPath.getUnitCount()));
                bw.write(StringUtils.join(parts, " "));
                bw.newLine();
            }
        }
    }
}
