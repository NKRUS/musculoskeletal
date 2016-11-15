package ru.kit.musculoskeletal;


import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by Kit on 11.11.2016.
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        Parent root = new Pane();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        Stage stage = new OdaStage(false,"");

        stage.show();
    }
}
