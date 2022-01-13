package eu.kwrhannover.jufo.metag;

import java.nio.file.Path;
import java.util.Objects;

public class Result {

    public enum DiagnosticFinding {
        POSITIVE,
        NEGATIVE,
        UNKNOWN
    }

    private final Path sourceFile;
    private final DiagnosticFinding analysisResult;
    private final double analysisValue;

    public Result(Path sourceFile, DiagnosticFinding analysisResult, double analysisValue) {
        this.sourceFile = sourceFile;
        this.analysisResult = analysisResult;
        this.analysisValue = analysisValue;
    }

    public Path getSourceFile() {
        return sourceFile;
    }

    public DiagnosticFinding getAnalysisResult() {
        return analysisResult;
    }

    public double getAnalysisValue() {
        return analysisValue;
    }

    @Override
    public String toString() {
        return "Result{" +
                "sourceFile=" + sourceFile +
                ", analysisResult=" + analysisResult +
                ", analysisValue=" + analysisValue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return Double.compare(result.analysisValue, analysisValue) == 0 && sourceFile.equals(result.sourceFile) && analysisResult == result.analysisResult;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceFile, analysisResult, analysisValue);
    }
}