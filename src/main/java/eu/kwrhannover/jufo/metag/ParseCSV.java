package eu.kwrhannover.jufo.metag;

import cern.colt.list.DoubleArrayList;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import static eu.kwrhannover.jufo.metag.MetaG.maxPositions;

public class ParseCSV {
    //TODO Detection of decimal point
    //TODO Detection of position table (other csv files can be ignored)

    public static DoubleArrayList parseCSV(final Path path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(path.toFile())) {
            final DoubleArrayList positions = new DoubleArrayList();
            scanner.useLocale(Locale.ENGLISH); //depends on decimal point: en "." ger ","
            int positionCount = 0;
            int line = 1;
            while (scanner.hasNext() && positionCount <= maxPositions) {
                if (scanner.hasNextDouble()) {
                    double position = scanner.nextDouble();
                    if (position < 0.0 || position > 1.0) {
                        throw new IllegalArgumentException("Invalid content in " + path + " in line" + line + ": Position value '" + position + "'");
                    }
                    positions.add(position);
                    ++positionCount;
                } else {
                    System.out.println("ERROR in line " + line + ": " + scanner.next());
                }
                ++line;
            }
            return positions;
        }
    }

}

