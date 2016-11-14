package ru.kit.musculoskeletal;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Kit on 14.11.2016.
 */
public class OdaStage extends Stage {
    public OdaStage(boolean isMan, String path) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/kit/musculoskeletal/fxml/oda_screen.fxml"));
        Parent root = loader.load();

        OdaController controller = loader.getController();

        controller.setMan(isMan);
        controller.setPath(path);
        controller.initImageViews();
        controller.setStage(this);

        this.setScene(new Scene(root));

    }
}
