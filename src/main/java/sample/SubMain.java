package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/* This SubMain class extending Application is invoked from the Main class
because a Main class intended to be packed into an executable jar must not extends another classes */

public class SubMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("mainWindow.fxml"));
        primaryStage.setTitle("Платежи");
        primaryStage.setScene(new Scene(root, 900, 475));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
