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
    private static IFlowPathFinder SPEflowPathFinder = new SPEModifiedFlowPathFinder();
    private static IFlowPathFinder LPEflowPathFinder = new LPEModifiedFlowPathFinder();
    private static ResultSaver resultSaver = new ResultSaver();
    private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_hhmmss");



    public static void main(String[] args) throws IOException {

        /* wczytanie grafu z pliku */
        if(args.length != 3) {
            System.out.println("Args: algorithm inputFile unitCount");
            return;
        }
        String algorithm = args[0];
        String inputFile = args[1];
        int unitCount = Integer.parseInt(args[2]);

        FlowPathParameters parameters = graphFileLoader.loadGraph(INPUT_DIR+inputFile, unitCount, algorithm);
        System.out.println("Finding flow paths...");

        FlowPathResult flowPathResult = null;

        /* zastosowanie algorytmu SPE */
        if (algorithm.equals("SPE")) {
            flowPathResult = SPEflowPathFinder.findFlowPaths(parameters);
        }

        /* zastosowanie algorytmu LPE */
        else if (algorithm.equals("LPE")) {
            flowPathResult = LPEflowPathFinder.findFlowPaths(parameters);
        }
        else {
            System.out.println("This algorithm is not supported");
        }

        System.out.println("Finding flow paths done");
        System.out.println("-----------");
        System.out.println("Time required: " + flowPathResult.getTime() + "ms");

        /* zapis wynikow do pliku */
        String outputFile = outputFile(inputFile, algorithm, unitCount);
        System.out.println("Saving results to "+outputFile+"...");
        resultSaver.saveResult(flowPathResult, outputFile);
        System.out.println("DONE");
    }

    private static String outputFile(String inputFile, String algorithm, int unitCount) {
        return OUTPUT_DIR
                +StringUtils.join(Arrays.asList(
                    removeExtension(inputFile),
                    algorithm,
                    Integer.toString(unitCount),
                    df.format(new Date())), "_")
                +".txt";
    }

    private static String removeExtension(String inputFile) {
        int lastDot = inputFile.lastIndexOf(".");
        return lastDot == -1 ? inputFile : inputFile.substring(0, lastDot);
    }
}
