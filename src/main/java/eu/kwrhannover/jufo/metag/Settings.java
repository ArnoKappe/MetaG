package eu.kwrhannover.jufo.metag;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public enum Settings {

    MetaGSettings;

    private static final Path path = Paths.get("MetaG.properties").toAbsolutePath().normalize();

    private final Properties properties = Defaults.get();

    private static class Keys {
        static final String DIRECTORY = "DIRECTORY";
        static final String BROWSE_SUBFOLDERS = "BROWSE_SUBFOLDERS";
        static final String BAR_COUNT = "BAR_COUNT";
        static final String MAX_POSITIONS = "MAX_POSITIONS";
        static final String GRAPH_SCALE = "GRAPH_SCALE";
        static final String SVG_OUTPUT = "SVG_OUTPUT";
    }

    public static class Defaults {
        static final Path DIRECTORY = Paths.get(System.getProperty("user.home", "."));
        static final boolean BROWSE_SUBFOLDERS = true;
        static final int BAR_COUNT = 50;
        static final int MAX_POSITIONS = 10000;
        static final int GRAPH_SCALE = 1;
        static final boolean SVG_OUTPUT = false;

        private static Properties get() {
            final Properties properties = new Properties();
            properties.setProperty(Keys.DIRECTORY, Defaults.DIRECTORY.toString());
            properties.setProperty(Keys.BROWSE_SUBFOLDERS, Boolean.toString(Defaults.BROWSE_SUBFOLDERS));
            properties.setProperty(Keys.BAR_COUNT, Integer.toString(Defaults.BAR_COUNT));
            properties.setProperty(Keys.MAX_POSITIONS, Integer.toString(Defaults.MAX_POSITIONS));
            properties.setProperty(Keys.GRAPH_SCALE, Integer.toString(Defaults.GRAPH_SCALE));
            properties.setProperty(Keys.SVG_OUTPUT, Boolean.toString(Defaults.SVG_OUTPUT));
            return properties;
        }
    }

    private Properties getProperties() {
        try (InputStream inStream = Files.newInputStream(path)) {
            properties.load(inStream);

        } catch (NoSuchFileException e) {
            System.out.println("Properties " + path + " must be generated - " + e);
            saveProperties();

        } catch (IOException e) {
            System.out.println("Properties " + path + " couldn't be loaded - " + e);
        }
        return properties;
    }

    private void saveProperties() {
        try (OutputStream out = Files.newOutputStream(path)) {
            properties.store(out, "MetaG.properties");
        } catch (IOException e) {
            System.out.println("Properties " + path + " couldn't be saved - " + e);
        }
    }

    public Path getDirectory() {
        return Paths.get(getProperties().getProperty(Keys.DIRECTORY, Defaults.DIRECTORY.toString())).toAbsolutePath().normalize();
    }

    public void setDirectory(final Path lastDirectory) {
        final Path path = lastDirectory.toAbsolutePath().normalize();
        if (Files.isDirectory(path)) {
            getProperties().setProperty(Keys.DIRECTORY, path.toString());
        } else {
            getProperties().setProperty(Keys.DIRECTORY, path.getParent().toString());
        }
        saveProperties();
    }


    public int getGraphScale() {
        try {
            return Integer.parseInt(getProperties().getProperty(Keys.GRAPH_SCALE, Integer.toString(Defaults.GRAPH_SCALE)));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return Defaults.GRAPH_SCALE;
    }

    public void setGraphScale(final int graphScale) {
        getProperties().setProperty(Keys.GRAPH_SCALE, Integer.toString(graphScale));
        saveProperties();
    }

    public int getBarCount() {
        try {
            return Integer.parseInt(getProperties().getProperty(Keys.BAR_COUNT, Integer.toString(Defaults.BAR_COUNT)));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return Defaults.BAR_COUNT;
    }

    public void setBarCount(int barCount) {
        getProperties().setProperty(Keys.BAR_COUNT, Integer.toString(barCount));
        saveProperties();
    }

    public int getMaxPositions() {
        try {
            return Integer.parseInt(getProperties().getProperty(Keys.MAX_POSITIONS, Integer.toString(Defaults.MAX_POSITIONS)));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return Defaults.MAX_POSITIONS;
    }

    public void setMaxPositions(int maxPositions) {
        getProperties().setProperty(Keys.MAX_POSITIONS, Integer.toString(maxPositions));
        saveProperties();
    }

    public boolean getBrowseSubfolders() {
        try {
            return Boolean.parseBoolean(getProperties().getProperty(Keys.BROWSE_SUBFOLDERS, Boolean.toString(Defaults.BROWSE_SUBFOLDERS)));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return Defaults.BROWSE_SUBFOLDERS;
    }

    public void setBrowseSubfolders(boolean fileSearchDepth) {
        getProperties().setProperty(Keys.BROWSE_SUBFOLDERS, Boolean.toString(fileSearchDepth));
        saveProperties();
    }

    public boolean getSVGOutput(){
        try {
            return Boolean.parseBoolean(getProperties().getProperty(Keys.SVG_OUTPUT, Boolean.toString(Defaults.SVG_OUTPUT)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Defaults.SVG_OUTPUT;
    }

    public void setSVGOutput(boolean svgOutput){
        getProperties().setProperty(Keys.SVG_OUTPUT, Boolean.toString(svgOutput));
        saveProperties();
    }
}
