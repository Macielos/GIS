package main;

import com.sun.deploy.util.StringUtils;
import datasource.GraphFileLoader;
import flowpathfinding.*;
import results.ResultSaver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Maciek on 14.05.2017.
 */
public class Main {

    public static final String OUTPUT_DIR = "results"+File.separator;

    private static GraphFileLoader graphFileLoader = new GraphFileLoader();
    private static IFlowPathFinder SPEflowPathFinder = new SPEModifiedFlowPathFinder();
    private static IFlowPathFinder LPEflowPathFinder = new LPEModifiedFlowPathFinder();
    private static ResultSaver resultSaver = new ResultSaver();
    private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_hhmmss");



    public static void main(String[] args) throws IOException {

        /* wczytanie grafu z pliku */
        if(args.length < 3) {
            System.out.println("Args: algorithm inputDir unitCount1 unitCount2...");
            return;
        }
        String algorithm = args[0];
        String inputDir = args[1];
        List<Integer> unitCounts = new ArrayList<>();
        for(int i=2; i<args.length; ++i) {
            unitCounts.add(Integer.parseInt(args[i]));
        }
        File dir = new File(inputDir);
        if(!dir.exists() || !dir.isDirectory()) {
            System.out.println("Invalid input dir");
            return;
        }

        System.out.println("Input dir "+dir.getPath());
        for(File inputFile: dir.listFiles()) {
            for(Integer unitCount: unitCounts) {
                findFlowPaths(inputDir, simpleFileName(inputFile), algorithm, unitCount);
            }
        }
    }

    private static String simpleFileName(File inputFile) {
        String path = inputFile.getPath();
        int lastSlash = path.lastIndexOf(File.separator);
        return lastSlash == -1 ? path : path.substring(lastSlash);
    }

    private static void findFlowPaths(String inputDir, String inputFile, String algorithm, Integer unitCount) throws IOException {
        System.out.println("Finding flow paths for inputFile: "+inputFile+", algorithm: "+algorithm+", unitCount: "+unitCount);
        FlowPathParameters parameters = graphFileLoader.loadGraph(inputDir+File.separator+inputFile, unitCount, algorithm);

        FlowPathResult flowPathResult = null;
        switch(algorithm) {
            case "SPE":
                flowPathResult = SPEflowPathFinder.findFlowPaths(parameters);
                break;
            case "LPE":
                flowPathResult = LPEflowPathFinder.findFlowPaths(parameters);
                break;
            default:
                System.out.println("This algorithm is not supported");
                return;
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
