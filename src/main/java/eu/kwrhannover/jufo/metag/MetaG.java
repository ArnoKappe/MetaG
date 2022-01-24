package eu.kwrhannover.jufo.metag;

import cern.colt.list.DoubleArrayList;
import cern.colt.list.LongArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static eu.kwrhannover.jufo.metag.Analysis.*;
import static eu.kwrhannover.jufo.metag.CreateSVG.createTargetFilename;
import static eu.kwrhannover.jufo.metag.CreateSVG.writeSVG;
import static eu.kwrhannover.jufo.metag.ParseCSV.parseCSV;
import static eu.kwrhannover.jufo.metag.Settings.MetaGSettings;
import static java.lang.System.lineSeparator;
import static java.lang.System.out;
import static java.nio.file.Files.deleteIfExists;
import static java.nio.file.Files.isRegularFile;
import static java.util.Locale.ROOT;
import static java.util.stream.Collectors.joining;

public class MetaG {
    private static Path path = MetaGSettings.getDirectory();
    public static int maxPositions = MetaGSettings.getMaxPositions(); //Maximum number of positions
    public static int barCount = MetaGSettings.getBarCount(); //Number of bars
    public static int scale = MetaGSettings.getGraphScale(); //(scale / 100) must be divisible by 5 (not relevant in GUI with slider) //TODO dynamic y-axis labeling
    private static boolean browseSubfolders = MetaGSettings.getBrowseSubfolders();

    public static void main(String[] args) throws IOException {

        MetaG metaG = new MetaG();

        metaG.execute(progress -> {
        });
    }

    public void execute(final Consumer<Double> updateProgress) throws IOException {
        System.out.println("Start ...");

        path = MetaGSettings.getDirectory();
        boolean svgOutput = MetaGSettings.getSVGOutput();
        maxPositions = MetaGSettings.getMaxPositions();
        barCount = MetaGSettings.getBarCount();
        scale = MetaGSettings.getGraphScale();
        browseSubfolders = MetaGSettings.getBrowseSubfolders();

        List<Path> paths = getPaths();

        System.out.println("Files to be processed(" + paths.size() + "): " + paths.stream()
                .map(Path::toString)
                .collect(joining(lineSeparator() + "    ", lineSeparator() + "    ", "")));

        List<Result> results = new ArrayList<>();
        for (Path path : paths) {
            Analysis analysis = new Analysis(path);
            boolean check = path.toString().endsWith(".csv");
            if (check) {
                try {
                    final DoubleArrayList positions = parseCSV(path);

                    final long distanceCount = ((long) positions.size() * (positions.size() - 1)) / 2;

                    final LongArrayList distanceIntervals = calculateDistanceIntervals(positions);
                    final LongArrayList bars = groupDistances(distanceIntervals);

                    DoubleArrayList scaledBars = scaleBars(bars, distanceCount);

                    Result result = analysis.analyseDistances(scaledBars);
                    results.add(result);


                    if (svgOutput) {
                        final Path targetFile = createTargetFilename(path);
                        deleteFile(targetFile);
                        writeSVG(scaledBars, path, targetFile, result);
                    }

                    System.out.println(results.size() + "/" + paths.size());
                    updateProgress.accept(results.size() / (double) paths.size());
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }
        }
        Path analysisFile;
        if (results.size() == 0) {
            out.println("No CSV files found");
        } else {
            if (results.size() == 1) {
                analysisFile = Path.of(MetaGSettings.getDirectory() + "\\_MetaG_" + cutFileExtension(results.get(0).getSourceFile().getFileName()) + "_analysis_" + maxPositions + "pos_res" + barCount + ".txt");
            } else {
                analysisFile = Path.of(MetaGSettings.getDirectory() + "\\_MetaG_analysis_" + maxPositions + "pos_res" + barCount + ".txt");
            }
            deleteFile(analysisFile);
            Analysis.createAnalysisFile(results, analysisFile);
            out.println("Analysisfile created: " + analysisFile);
        }
    }


    private static List<Path> getPaths() {
        int depth;
        if (browseSubfolders) {
            depth = Integer.MAX_VALUE;
        } else {
            depth = 1;
        }

        ArrayList<Path> paths = new ArrayList<>();
        try {
            if (Files.exists(path) && Files.isReadable(path)) {
                MetaGSettings.setDirectory(path);
                if (isRegularFile(path)) {
                    paths.add(path);
                    return paths;
                } else if (Files.isDirectory(path)) {
                    Files.walk(path, depth)
                            .filter(p -> isRegularFile(p) && p.getFileName().toString().toLowerCase(ROOT).endsWith(".csv"))
                            .distinct()
                            .forEach(paths::add);
                    return paths;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paths;
    }

    public static String cutFileExtension(final Path path) {
        final String str = path.toString();
        final int dotPos = str.lastIndexOf('.');
        if (dotPos == -1) {
            return str;
        } else {
            return str.substring(0, dotPos);
        }
    }

    public static void deleteFile(final Path file) throws IOException {
        if (deleteIfExists(file)) {
            out.println("Deleted file: " + file);
        }
    }

}
