package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{              //Main Window
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Image Management Tool V 1.0");
        primaryStage.setScene(new Scene(root, 735, 685));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
