package main;

import com.sun.deploy.util.StringUtils;
import datasource.GraphFileLoader;
import flowpathfinding.*;
import savers.ResultSaver;
import savers.StatisticsSaver;

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

    public static final String OUTPUT_DIR = "results" +File.separator;
    public static final String OUTPUT_STATS_DIR = "stats" +File.separator;

    private static GraphFileLoader graphFileLoader = new GraphFileLoader();
    private static IFlowPathFinder SPEflowPathFinder = new SPEModifiedFlowPathFinder();
    private static IFlowPathFinder LPEflowPathFinder = new LPEModifiedFlowPathFinder();
    private static ResultSaver resultSaver = new ResultSaver();
    private static StatisticsSaver statisticsSaver = new StatisticsSaver();
    private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_hhmmss");

    public static void main(String[] args) throws IOException {
        /* wczytanie grafu z pliku */
        if(args.length < 2) {
            System.out.println("Args: inputDir unitCount1 unitCount2...");
            return;
        }
        String inputDir = args[0];
        List<Integer> unitCounts = new ArrayList<>();
        for(int i=2; i<args.length; ++i) {
            unitCounts.add(Integer.parseInt(args[i]));
        }

        File outputDir = new File(OUTPUT_DIR);
        if(!outputDir.exists()) {
            outputDir.mkdirs();
        }
        File statsDir = new File(OUTPUT_STATS_DIR);
        if(!statsDir.exists()) {
            statsDir.mkdirs();
        }

        File dir = new File(inputDir);
        if(!dir.exists() || !dir.isDirectory()) {
            System.out.println("Invalid input dir");
            return;
        }

        System.out.println("Input dir "+dir.getPath());
        List<String> algorithms = Arrays.asList("LPE", "SPE");
        for (File inputFile : dir.listFiles()) {
            for (Integer unitCount : unitCounts) {
                for(String slgorithm: algorithms) {
                    findFlowPaths(inputDir, simpleFileName(inputFile), slgorithm, unitCount);
                }
            }
        }

        statisticsSaver.saveStatistics(statsFile());
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

        statisticsSaver.addResult(parameters, flowPathResult);

        System.out.println("Finding flow paths done");
        System.out.println("-----------");
        System.out.println("Time required: " + flowPathResult.getTime() + "ms");

        /* zapis wynikow do pliku */
        String outputFile = outputFile(inputFile, algorithm, unitCount);
        System.out.println("Saving savers to "+outputFile+"...");
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

    private static String statsFile() {
        return OUTPUT_STATS_DIR + "stats_"+ df.format(new Date()) + ".csv";
    }

    private static String removeExtension(String inputFile) {
        int lastDot = inputFile.lastIndexOf(".");
        return lastDot == -1 ? inputFile : inputFile.substring(0, lastDot);
    }
}
