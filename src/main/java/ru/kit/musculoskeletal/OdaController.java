package ru.kit.musculoskeletal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kit on 14.11.2016.
 */
public class OdaController {
    private boolean isMan;
    private String path;

    public boolean isMan() {
        return isMan;
    }

    public void setMan(boolean man) {
        isMan = man;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @FXML
    GridPane frontalPane,sagitalPane;
    @FXML
    private ImageView frontal1, frontal2, frontal3;
    @FXML
    private ImageView sagital1,sagital2,sagital3;
    private static ObservableList<ImageView> frontalList = FXCollections.observableArrayList();
    private static ObservableList<ImageView> sagitalList = FXCollections.observableArrayList();

    private static final String NOT_CHOOSEN_IMAGE_STYLE = "-fx-border-color: transparent;";
    private static final String CHOOSEN_IMAGE_STYLE = "-fx-border-color: #36804d; -fx-border-width: 3; -fx-border-radius: 10";
    private Map<String,String> result = new HashMap<>();

    private OdaStage stage;

    @FXML
    private void cancel () {stage.close();}

    @FXML
    private void ok() {
        if(commitChoices()!=0){
            writeJSON(result);
            stage.close();
        }
    }

    public void setStage (OdaStage stage) {
        this.stage = stage;
    }


    @FXML
    private void initialize(){


    }
    @FXML
    private void showFrontalPane(){
        sagitalPane.setVisible(false);
        frontalPane.setVisible(true);
    }
    @FXML
    private void showSagitalPane(){
        frontalPane.setVisible(false);
        sagitalPane.setVisible(true);
    }

    void initImageViews(){
        char gender = isMan() ? 'm' : 'f';
        frontal1.setImage(new Image(getClass().getResourceAsStream("/ru/kit/musculoskeletal/image/"+gender+""+"21.jpg")));
        frontal2.setImage(new Image(getClass().getResourceAsStream("/ru/kit/musculoskeletal/image/"+gender+""+"22.jpg")));
        frontal3.setImage(new Image(getClass().getResourceAsStream("/ru/kit/musculoskeletal/image/"+gender+""+"23.jpg")));
        sagital1.setImage(new Image(getClass().getResourceAsStream("/ru/kit/musculoskeletal/image/"+gender+""+"11.jpg")));
        sagital2.setImage(new Image(getClass().getResourceAsStream("/ru/kit/musculoskeletal/image/"+gender+""+"12.jpg")));
        sagital3.setImage(new Image(getClass().getResourceAsStream("/ru/kit/musculoskeletal/image/"+gender+""+"13.jpg")));
        frontalList.add(frontal1);
        frontalList.add(frontal2);
        frontalList.add(frontal3);
        sagitalList.add(sagital1);
        sagitalList.add(sagital2);
        sagitalList.add(sagital3);

        for (ImageView imageView : frontalList) {
            imageView.setOnMouseClicked(new FrontalEventHandler());
        }
        for (ImageView imageView : sagitalList) {
            imageView.setOnMouseClicked(new SagitalEventHandler());
        }

    }


    private class FrontalEventHandler implements EventHandler<Event> {

        public void handle(Event evt) {
            setChoosen(frontalList,((Node)evt.getSource()).getId());
        }
    }
    private class SagitalEventHandler implements EventHandler<Event> {

        public void handle(Event evt) {
            setChoosen(sagitalList,((Node)evt.getSource()).getId());
        }
    }

    void setChoosen(ObservableList<ImageView> images, String choosenImage){
        for (ImageView view :images) {
            if(view.getId()!=choosenImage){
                view.getParent().setStyle(NOT_CHOOSEN_IMAGE_STYLE);
            }else{
                view.getParent().setStyle(CHOOSEN_IMAGE_STYLE);

            }
        }

    }

    private int commitChoices(){
        String frontalId = "";
        String sagitalId = "";

        for (ImageView imageView :frontalList) {
            if(imageView.getParent().getStyle()==CHOOSEN_IMAGE_STYLE){
                frontalId = imageView.getId();
                break;
            }
        }
        for (ImageView imageView :sagitalList) {
            if(imageView.getParent().getStyle()==CHOOSEN_IMAGE_STYLE){
                sagitalId = imageView.getId();
                break;
            }
        }

        if(!frontalId.equals("") && !sagitalId.equals("")){
            setResult(frontalId,sagitalId);
            return 1;//Все ок
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Выбор отсутствует");
            alert.setHeaderText("Фронтальная или Сагиттальная плоскость не указаны");
            alert.setContentText("Укажите обе плоскости");

            alert.showAndWait();
            return 0;//Статус - 0   Не указаны все плоскости
        }

    }

    private void setResult(String frontal, String sagital){
        String frontalResult,sagitalResult;
        switch (frontal){
            case "frontal1":
                frontalResult = "normal";
                break;
            case "frontal2":
                frontalResult = "right";
                break;
            case "frontal3":
                frontalResult = "left";
                break;
            default: frontalResult = "none";
        }
        switch (sagital){
            case "sagital1":
                sagitalResult = "normal";
                break;
            case "sagital2":
                sagitalResult = "backward";
                break;
            case "sagital3":
                sagitalResult = "forward";
                break;
            default: sagitalResult = "none";
        }

        result.put("frontal",frontalResult);
        result.put("sagittal",sagitalResult);
    }
    private JSONObject createJSON(Map<String, String> inspections) {

        JSONObject jsonObject = new JSONObject();

        for (String key : inspections.keySet()) {

            jsonObject.put(key.toLowerCase(), inspections.get(key).toLowerCase());
        }

        return jsonObject;
    }

    private void writeJSON(Map<String, String> inspections) {
        try {
            String jsonFileName = path.concat("oda_output_file.json");
            BufferedWriter e = new BufferedWriter(new FileWriter(new File(jsonFileName)));
            Throwable var2 = null;

            try {
                e.write(this.createJSON(inspections).toString());
                System.err.println("JSON is written to " + jsonFileName);
            } catch (Throwable var12) {
                var2 = var12;
                throw var12;
            } finally {
                if(var2 != null) {
                    try {
                        e.close();
                    } catch (Throwable var11) {
                        var2.addSuppressed(var11);
                    }
                } else {
                    e.close();
                }
            }
        } catch (IOException var14) {
            var14.printStackTrace();
        }

    }

}
