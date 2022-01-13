package eu.kwrhannover.jufo.metag;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

import static eu.kwrhannover.jufo.metag.Settings.MetaGSettings;

public class UIController {

    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button buttonAnalyze;
    @FXML
    private Button buttonBrowse;
    @FXML
    private TextField textFieldPath;
    @FXML
    private CheckBox checkBoxSubfolders;
    @FXML
    private Slider sliderMaxPos;
    @FXML
    private Label labelMaxPos;
    @FXML
    private CheckBox checkBoxSVG;
    @FXML
    private Label labelScale;
    @FXML
    private Slider sliderScale;
    @FXML
    private Label labelColumns;
    @FXML
    private Slider sliderColumns;
    @FXML
    private Label labelDiagrammSettings;
    @FXML
    private Label labelScaleName;
    @FXML
    private Label labelColumnName;
    @FXML
    private Label labelMaxPosName;
    @FXML
    private Button buttonReset;
    @FXML
    private VBox vBox;

    private final HashMap<Integer, Integer> hashMapSliderToScale = new HashMap<>();
    private final HashMap<Integer, Integer> hashMapScaleToSlider = new HashMap<>();
    private final HashMap<Integer, Integer> hashMapSliderToColumns = new HashMap<>();
    private final HashMap<Integer, Integer> hashMapColumnsToSlider = new HashMap<>();

    @FXML
    protected void onButtonChooseClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Path lastDir = MetaGSettings.getDirectory();
        Path parent = lastDir.getParent();
        Path dir = parent != null ? parent : lastDir;
        directoryChooser.setInitialDirectory(dir.toFile());
        Path path = directoryChooser.showDialog(null).toPath();

        textFieldPath.setText(path.toString());
    }

    @FXML
    protected void isSettingsWindowActive(boolean activityStatus) {
        vBox.setDisable(!activityStatus);
        buttonAnalyze.setDisable(!activityStatus);
    }

    @FXML
    protected void onButtonAnalyzeClick(){
        isSettingsWindowActive(false);

        MetaGSettings.setDirectory(Path.of(textFieldPath.getText()));
        MetaGSettings.setBrowseSubfolders(checkBoxSubfolders.isSelected());
        MetaGSettings.setMaxPositions((int) sliderMaxPos.getValue());
        MetaGSettings.setSVGOutput(checkBoxSVG.isSelected());
        MetaGSettings.setGraphScale(hashMapSliderToScale.get((int) sliderScale.getValue()));
        MetaGSettings.setBarCount(hashMapSliderToColumns.get((int) sliderColumns.getValue()));

        buttonAnalyze.setText("ANALYZING...");

        MetaG metaG = new MetaG();

        Thread thread = new Thread(() -> {
            try {
                metaG.execute(progress -> Platform.runLater(() -> progressBar.setProgress(progress)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(this::completeAnalyzing);
        });
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    protected void onCheckboxSVGAction() {
        boolean select = checkBoxSVG.isSelected();
        sliderScale.setDisable(!select);
        sliderColumns.setDisable(!select);
        labelScale.setDisable(!select);
        labelColumns.setDisable(!select);
        labelScaleName.setDisable(!select);
        labelColumnName.setDisable(!select);
        labelDiagrammSettings.setDisable(!select);
    }

    @FXML
    protected void onSliderScaleAction() {
        int scale = (int) sliderScale.getValue();
        labelScale.setText(Integer.toString(hashMapSliderToScale.get(scale)));
    }

    @FXML
    protected void onSliderColumnAction() {
        int column = (int) sliderColumns.getValue();
        labelColumns.setText(Integer.toString(hashMapSliderToColumns.get(column)));
    }

    @FXML
    protected void onSliderMaxPosAction() {
        int maxPos = (int) sliderMaxPos.getValue();
        labelMaxPos.setText(Integer.toString(maxPos));
    }

    @FXML
    protected void onButtonResetClick() {
        checkBoxSubfolders.setSelected(Settings.Defaults.BROWSE_SUBFOLDERS);
        sliderMaxPos.setValue(Settings.Defaults.MAX_POSITIONS);
        labelMaxPos.setText(Integer.toString(Settings.Defaults.MAX_POSITIONS));
        checkBoxSVG.setSelected(Settings.Defaults.SVG_OUTPUT);
        labelScale.setText(Integer.toString(Settings.Defaults.GRAPH_SCALE));
        sliderScale.setValue(hashMapScaleToSlider.get(Settings.Defaults.GRAPH_SCALE));
        labelColumns.setText(Integer.toString(Settings.Defaults.BAR_COUNT));
        sliderColumns.setValue(hashMapColumnsToSlider.get(Settings.Defaults.BAR_COUNT));
        onCheckboxSVGAction();
    }

    private void completeAnalyzing() {
        isSettingsWindowActive(true);
        progressBar.setProgress(0.0);
        buttonAnalyze.setText("ANALYZE");
    }

    public UIController() {
        hashMapScaleToSlider.put(1, 1);
        hashMapScaleToSlider.put(2, 2);
        hashMapScaleToSlider.put(5, 3);
        hashMapScaleToSlider.put(10, 4);
        hashMapScaleToSlider.put(20, 5);

        hashMapColumnsToSlider.put(50, 1);
        hashMapColumnsToSlider.put(40, 2);
        hashMapColumnsToSlider.put(30, 3);
        hashMapColumnsToSlider.put(20, 4);
        hashMapColumnsToSlider.put(15, 5);
        hashMapColumnsToSlider.put(10, 6);
        hashMapColumnsToSlider.put(5, 7);

        hashMapSliderToScale.put(1, 1);
        hashMapSliderToScale.put(2, 2);
        hashMapSliderToScale.put(3, 5);
        hashMapSliderToScale.put(4, 10);
        hashMapSliderToScale.put(5, 20);

        hashMapSliderToColumns.put(1, 50);
        hashMapSliderToColumns.put(2, 40);
        hashMapSliderToColumns.put(3, 30);
        hashMapSliderToColumns.put(4, 20);
        hashMapSliderToColumns.put(5, 15);
        hashMapSliderToColumns.put(6, 10);
        hashMapSliderToColumns.put(7, 5);

        Thread thread = new Thread(() -> {
            Platform.runLater(() -> textFieldPath.setText(MetaGSettings.getDirectory().toString()));
            Platform.runLater(() -> checkBoxSubfolders.setSelected(MetaGSettings.getBrowseSubfolders()));
            Platform.runLater(() -> sliderMaxPos.setValue(MetaGSettings.getMaxPositions()));
            Platform.runLater(() -> labelMaxPos.setText(Integer.toString(MetaGSettings.getMaxPositions())));
            Platform.runLater(() -> checkBoxSVG.setSelected(MetaGSettings.getSVGOutput()));
            Platform.runLater(() -> labelScale.setText(Integer.toString(MetaGSettings.getGraphScale())));
            Platform.runLater(() -> sliderScale.setValue(hashMapScaleToSlider.getOrDefault(MetaGSettings.getGraphScale(), Settings.Defaults.GRAPH_SCALE)));
            Platform.runLater(() -> labelColumns.setText(Integer.toString(MetaGSettings.getBarCount())));
            Platform.runLater(() -> sliderColumns.setValue(hashMapColumnsToSlider.getOrDefault(MetaGSettings.getBarCount(), Settings.Defaults.BAR_COUNT)));
            Platform.runLater(this::onCheckboxSVGAction);
        });
        thread.setDaemon(true);
        thread.start();
    }
}