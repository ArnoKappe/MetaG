package eu.kwrhannover.jufo.metag;

import cern.colt.list.DoubleArrayList;
import cern.colt.list.LongArrayList;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static eu.kwrhannover.jufo.metag.MetaG.barCount;
import static eu.kwrhannover.jufo.metag.Result.DiagnosticFinding.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public final class Analysis {

    private final Path sourceFile;

    public Analysis(Path sourceFile) {
        this.sourceFile = sourceFile;
    }

    private Result resultOf(Result.DiagnosticFinding diagnosticFinding, double analysis) {
        return new Result(sourceFile, diagnosticFinding, analysis);
    }

    public Result analyseDistances(DoubleArrayList scaledBars) {
        double analyse = 0;
        for (int i = 0; i < (scaledBars.size() - 1); ++i) {
            analyse += ((scaledBars.get(i)) - (scaledBars.get(i + 1)));
        }
        // Die Analyse wird für folgende Säulenanzahlen / res unterstützt: 5, 10, 15, 20, 30, 40, 50
        // Dies liegt daran, dass die Grenzwerte der Analysewerte per Hand ermittelt werden und damit auch nur vorhergesehene Fälle analysiert werden können.
        // Man könnte an dieser Stelle einen Kalibrierungsalgorithmus einbringen, der immer Grenzwerte für fremde Anzahlen an Säulen festlegt.
        // Bei einer solchen Anwendung ist, anders als gewohnt, nicht mit zügigen Ergebnissen zu rechnen.
        switch (barCount) {
            case 5:
                if (analyse <= 11.6) {
                    return resultOf(POSITIVE, analyse);
                } else if (analyse >= 58.9) {
                    return resultOf(NEGATIVE, analyse);
                } else {
                    return resultOf(UNKNOWN, analyse);
                }
            case 10:
                if (analyse <= 7.9) {
                    return resultOf(POSITIVE, analyse);
                } else if (analyse >= 34.9) {
                    return resultOf(NEGATIVE, analyse);
                } else {
                    return resultOf(UNKNOWN, analyse);
                }
            case 15:
                if (analyse <= 6.8) { //6.7+
                    return resultOf(POSITIVE, analyse);
                } else if (analyse >= 24.8) {
                    return resultOf(NEGATIVE, analyse);
                } else {
                    return resultOf(UNKNOWN, analyse);
                }
            case 20:
                if (analyse <= 5.8) {
                    return resultOf(POSITIVE, analyse);
                } else if (analyse >= 19.2) {
                    return resultOf(NEGATIVE, analyse);
                } else {
                    return resultOf(UNKNOWN, analyse);
                }
            case 30:
                if (analyse <= 4.5) { //4.4+
                    return resultOf(POSITIVE, analyse);
                } else if (analyse >= 13) {
                    return resultOf(NEGATIVE, analyse);
                } else {
                    return resultOf(UNKNOWN, analyse);
                }
            case 40:
                if (analyse <= 3.9) { //3.8+
                    return resultOf(POSITIVE, analyse);
                } else if (analyse >= 9.9) {
                    return resultOf(NEGATIVE, analyse);
                } else {
                    return resultOf(UNKNOWN, analyse);
                }
            case 50:
                if (analyse <= 3.6) {
                    return resultOf(POSITIVE, analyse);
                } else if (analyse >= 8) {
                    return resultOf(NEGATIVE, analyse);
                } else {
                    return resultOf(UNKNOWN, analyse);
                }
        }
        throw new UnsupportedOperationException("Diagnostic finding only works with 10, 20 and 50 bars");
    }

    public static void createAnalysisFile(List<Result> results, Path targetAnalysisFile) {

        try (BufferedWriter analysisWriter = Files.newBufferedWriter(targetAnalysisFile, UTF_8)) {
            for (Result result : results) {
                analysisWriter.write(result.getSourceFile() + "\t" + result.getAnalysisResult() + "\t" + result.getAnalysisValue() + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DoubleArrayList scaleBars(final LongArrayList bars, final long distanceCount) {
        final double[] scaledBars = new double[barCount];
        final double p100 = 100.0 / distanceCount;
        double d;
        for (int i = 0; i < bars.size(); ++i) {
            d = bars.get(i) * p100;
            scaledBars[i] = d;
        }
        return new DoubleArrayList(scaledBars);
    }

    public static LongArrayList groupDistances(LongArrayList distanceIntervals) {
        final long[] bars = new long[barCount];
        final int barsize = (int) Math.round((double) distanceIntervals.size() / barCount);

        for (int intervalIndex = 0, barIndex = 0, i = 0;
             intervalIndex != distanceIntervals.size();
             ++intervalIndex) {

            if (!(barIndex < barCount)) {
                break;
            }

            bars[barIndex] += distanceIntervals.get(intervalIndex);

            ++i;
            if (i == barsize) {
                i = 0;
                ++barIndex;
            }
        }
        return new LongArrayList(bars);
    }

    private static <T> List<T> fill(final List<T> list, final int size, final T element) {
        for (int i = 0; i != size; ++i)
            list.add(element);
        return list;
    }

    public static LongArrayList calculateDistanceIntervals(final DoubleArrayList positions) {
        final int distanceIntervalCount = barCount * 10;
        final long[] distanceIntervals = new long[distanceIntervalCount];

        for (int i = 0; i != positions.size(); ++i) {
            for (int j = i + 1; j != positions.size(); ++j) {

                final double pos1 = positions.get(i);
                final double pos2 = positions.get(j);

                final double d;
                if (pos2 > pos1) {
                    d = pos2 - pos1;
                } else {
                    d = pos1 - pos2;
                }

                final int intervalIndex;
                if (d > 0.5) {
                    intervalIndex = distanceIntervalIndex(1.0 - d, distanceIntervalCount);
                } else {
                    intervalIndex = distanceIntervalIndex(d, distanceIntervalCount);
                }

                ++distanceIntervals[intervalIndex];
            }
        }
        return new LongArrayList(distanceIntervals);
    }

    private static int distanceIntervalIndex(final double distance, int distanceIntervalCount) {
        if (distance < 0.0 || distance > 0.5) {
            throw new IllegalArgumentException("Invalid distance value: " + distance);
        }
        // distances mit allen Abständen d zwischen zwei beliebigen Positionen: (positions.size() * (positions.size() - 1)) / 2
        // Zu groß!
        // Liste mit fester Größe (z.B. 1000). Jede Position steht für einen Abstandsbereich. Der Wert ist ein Zähler.
        // Abstände: 0..0.5, aufgeteilt auf 1000 Bereiche. Breite eines Bereichs: 0.5/1000 = 1 / 2000.
        // Abstand d: d / (1/2000) = index in Liste. ... sinnvoller geschrieben d * 2000
        final int i = (int) (distance * 2 * distanceIntervalCount);
        final int interval = i == distanceIntervalCount ? i - 1 : i;
        if (interval < 0 || interval >= distanceIntervalCount) {
            System.out.println("distance = " + distance + " -> index = " + interval);
        }
        return interval;
    }

}

