package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static sample.TextDeliverer.getText;

/* This SubMain class extending Application is invoked from the Main class
because a Main class intended to be packed into an executable jar must not extends another classes */

public class SubMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mainWindow.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle(getText("mainWindowTitle"));
        primaryStage.setScene(new Scene(root, 960, 700));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
