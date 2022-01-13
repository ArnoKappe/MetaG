package eu.kwrhannover.jufo.metag;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MetaGApplication extends Application {

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage metaGStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MetaGApplication.class.getResource("metag_ui.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 625, 515);
        metaGStage.setTitle("MetaG");
        metaGStage.setScene(scene);
        metaGStage.setMinHeight(550);
        metaGStage.setMinWidth(550);
        metaGStage.getIcons().add(new Image(Objects.requireNonNull(MetaGApplication.class.getResourceAsStream("icon.png"))));
        metaGStage.show();
    }
}
