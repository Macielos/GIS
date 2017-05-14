package main;

import com.sun.deploy.util.StringUtils;
import datasource.GraphFileLoader;
import flowpathfinding.*;
import results.ResultSaver;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Maciek on 14.05.2017.
 */
public class Main {

    public static final String INPUT_DIR = "graphs/";
    public static final String OUTPUT_DIR = "results/";

    private static GraphFileLoader graphFileLoader = new GraphFileLoader();
    private static IFlowPathFinder flowPathFinder = new SPEModifiedFlowPathFinder();
    private static ResultSaver resultSaver = new ResultSaver();
    private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_hhmmss");

    /**
     * TODO
     * - generator grafow
     * - ten drugi alg. od Pieńkosza
     * - testy, porównanie wyników
     *
     */
    public static void main(String[] args) throws IOException {
        if(args.length != 2) {
            System.out.println("Args: inputFile unitCount");
            return;
        }
        String inputFile = args[0];
        int unitCount = Integer.parseInt(args[1]);

        FlowPathParameters parameters = graphFileLoader.loadGraph(INPUT_DIR+inputFile, unitCount);
        System.out.println("Finding flow paths...");
        FlowPathResult flowPathResult = flowPathFinder.findFlowPaths(parameters);
        System.out.println("Finding flow paths done");
        String outputFile = outputFile(inputFile, unitCount);
        System.out.println("Saving results to "+outputFile+"...");
        resultSaver.saveResult(flowPathResult, outputFile);
        System.out.println("DONE");

    }

    private static String outputFile(String inputFile, int unitCount) {
        return OUTPUT_DIR
                +StringUtils.join(Arrays.asList(
                    removeExtension(inputFile),
                    flowPathFinder.getAlgorithmName(),
                    Integer.toString(unitCount),
                    df.format(new Date())), "_")
                +".txt";
    }

    private static String removeExtension(String inputFile) {
        int lastDot = inputFile.lastIndexOf(".");
        return lastDot == -1 ? inputFile : inputFile.substring(0, lastDot);
    }
}
