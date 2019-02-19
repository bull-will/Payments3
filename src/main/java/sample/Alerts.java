package sample;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import static sample.TextDeliverer.getAlertText;

public class Alerts {

    static void alertInfo(String title, String content) {
        alertInfo(title, null, content);
    }

    /* This method is for creating information alerts with addition to its texts */
    static void alertInfo(String title, String header, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    /* This method is for creating confirmation alerts with addition to its texts */
    static Alert alertConfirmation(String title, String header, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(contentText );
        return alert;
    }
}
