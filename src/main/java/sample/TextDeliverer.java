package sample;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

class TextDeliverer {
    private static String textsPropertiesFile;
    private static String alertsPropertiesFile;
    private static Properties textsProperties;
    private static Properties alertsProperties;
    static String commonPartOfThePath = null;

    static {
        textsProperties = new Properties();
        alertsProperties = new Properties();

        try {
            commonPartOfThePath = (new File(".").getCanonicalPath().endsWith("target") ?
                    "classes" + File.separator :
                    ("src" + File.separator + "main" + File.separator + "resources" + File.separator));
            textsPropertiesFile = commonPartOfThePath + "windowsTexts.xml";
            /* ↓↓↓ For xml properties version ↓↓↓ */
            textsProperties.loadFromXML(new FileInputStream(textsPropertiesFile));

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ошибка доступа к файлу текстов программы");
            alert.setHeaderText(null);
            alert.setContentText("Не удалось получить доступ к файлу с текстами");
            alert.showAndWait();
        }

        try {
            alertsPropertiesFile = commonPartOfThePath + "alerts.xml";
            alertsProperties.loadFromXML(new FileInputStream(alertsPropertiesFile));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ошибка создания сообщения об ошибке");
            alert.setHeaderText(null);
            alert.setContentText("Произошла какая-то ошибка, но кроме того,\n" +
                    "не удалось создать сообщение о ней");
            alert.showAndWait();
        }
    }

    /* Next two method differ in which xml file they get texts from */
    public static String getText(String key) {
        return getAnyText(key, textsProperties);
    }

    public static String getAlertText(String key) {
        return getAnyText(key, alertsProperties);
    }

    private static String getAnyText(String key, Properties properties) {
        if (key == null) {
            return null;
        }
        String result = null;
        try {
            /* If there's '\n' sequence in the string in the xml file, it will not become a new line
             * in the result String, instead it will literally be two visible symbols '\n' in the string.
             * So it needs additional replacement for a new line sequence */
            String received = properties.getProperty(key);
            result = received.replaceAll("\\\\n", "\n");
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ошибка текстовой части");
            alert.setHeaderText(null);
            alert.setContentText("Произошла ошибка загрузки текста из файла текстов");
            alert.showAndWait();
        }
        return result;
    }
}
