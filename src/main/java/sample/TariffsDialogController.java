package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import static sample.TextDeliverer.getAlertText;

public class TariffsDialogController {

    @FXML
    private CheckBox roundCheckBox;

    @FXML
    private TextField electroTariff1Field;
    @FXML
    private TextField electroLimit1Field;
    @FXML
    private TextField electroTariff2Field;
    @FXML
    private TextField electroLimit2Field;
    @FXML
    private TextField electroTariff3Field;
    @FXML
    private TextField electroLimit3Field;
    @FXML
    private TextField electroTariff4Field;

    @FXML
    private TextField waterTariffField;
    @FXML
    private TextField hotWaterTariffField;
    @FXML
    private TextField heatingTariffField;
    @FXML
    private TextField gasTariffField;
    @FXML
    private TextField sewageTariffField;
    @FXML
    private TextField flatTariffField;
    @FXML
    private TextField garbageTariffField;

    private double electroTariff1;
    private double electroLimit1;
    private double electroTariff2;
    private double electroLimit2;
    private double electroTariff3;
    private double electroLimit3;
    private double electroTariff4;
    private double waterTariff;
    private double hotWaterTariff;
    private double heatingTariff;
    private double gasTariff;
    private double sewageTariff;
    private double flatTariff;
    private double garbageTariff;
    private boolean round;

    private boolean someFieldsProcessedWrong = false;


    private String makeDoublesLookNice(double value) {
        if (value % 1 == 0) {
            return String.valueOf(Math.round(value));
        } else {
            return String.valueOf(value);/* This part is needed
            because the result of calculation of consumed quantity often has infinite periodic decimal part*/
        }
    }

    public void showDialogFillFields(TariffsData tariffsData) {
        electroTariff1Field.setText(makeDoublesLookNice(tariffsData.getElectroTariff1()));
        electroTariff2Field.setText(makeDoublesLookNice(tariffsData.getElectroTariff2()));
        electroTariff3Field.setText(makeDoublesLookNice(tariffsData.getElectroTariff3()));
        electroTariff4Field.setText(makeDoublesLookNice(tariffsData.getElectroTariff4()));
        electroLimit1Field.setText(makeDoublesLookNice(tariffsData.getElectroLimit1()));
        electroLimit2Field.setText(makeDoublesLookNice(tariffsData.getElectroLimit2()));
        electroLimit3Field.setText(makeDoublesLookNice(tariffsData.getElectroLimit3()));
        waterTariffField.setText(makeDoublesLookNice(tariffsData.getWaterTariff()));
        hotWaterTariffField.setText(makeDoublesLookNice(tariffsData.getHotWaterTariff()));
        heatingTariffField.setText(makeDoublesLookNice(tariffsData.getHeatingTariff()));
        gasTariffField.setText(makeDoublesLookNice(tariffsData.getGasTariff()));
        sewageTariffField.setText(makeDoublesLookNice(tariffsData.getSewageTariff()));
        flatTariffField.setText(makeDoublesLookNice(tariffsData.getFlatTariff()));
        garbageTariffField.setText(makeDoublesLookNice(tariffsData.getGarbageTariff()));
        roundCheckBox.setSelected(tariffsData.isRound());
    }

    private double obtainDoubleFromFields(TextField textField) {
        double value = 0d;
        try {
            value = Double.parseDouble(textField.getText().replace(',', '.'));
        } catch (Exception e) {
            someFieldsProcessedWrong = true;
        }
        return value;
    }

    public void obtainNumbersFromTextFields() {
        electroTariff1 = obtainDoubleFromFields(electroTariff1Field);
        electroLimit1 = obtainDoubleFromFields(electroLimit1Field);
        electroTariff2 = obtainDoubleFromFields(electroTariff2Field);
        electroLimit2 = obtainDoubleFromFields(electroLimit2Field);
        electroTariff3 = obtainDoubleFromFields(electroTariff3Field);
        electroLimit3 = obtainDoubleFromFields(electroLimit3Field);
        electroTariff4 = obtainDoubleFromFields(electroTariff4Field);
        waterTariff = obtainDoubleFromFields(waterTariffField);
        hotWaterTariff = obtainDoubleFromFields(hotWaterTariffField);
        heatingTariff = obtainDoubleFromFields(heatingTariffField);
        gasTariff = obtainDoubleFromFields(gasTariffField);
        sewageTariff = obtainDoubleFromFields(sewageTariffField);
        flatTariff = obtainDoubleFromFields(flatTariffField);
        garbageTariff = obtainDoubleFromFields(garbageTariffField);

        try {
            round = roundCheckBox.isSelected();
        } catch (Exception e) {
            round = true;
            someFieldsProcessedWrong = true;
        }

        if (someFieldsProcessedWrong) {
            Alerts.alertInfo(getAlertText("tariffsNumbersProcessedWrongTitle"),
                    getAlertText("tariffsNumbersProcessedWrongMessage"));
        }
    }

    public TariffsData processTariffs(TariffsData tariffsData) {
        obtainNumbersFromTextFields();

        tariffsData.setElectroTariff1(electroTariff1);
        tariffsData.setElectroLimit1(electroLimit1);
        tariffsData.setElectroTariff2(electroTariff2);
        tariffsData.setElectroLimit2(electroLimit2);
        tariffsData.setElectroTariff3(electroTariff3);
        tariffsData.setElectroLimit3(electroLimit3);
        tariffsData.setElectroTariff4(electroTariff4);
        tariffsData.setWaterTariff(waterTariff);
        tariffsData.setHotWaterTariff(hotWaterTariff);
        tariffsData.setHeatingTariff(heatingTariff);
        tariffsData.setGasTariff(gasTariff);
        tariffsData.setSewageTariff(sewageTariff);
        tariffsData.setFlatTariff(flatTariff);
        tariffsData.setGarbageTariff(garbageTariff);
        tariffsData.setRound(round);

        return tariffsData;
    }

    public void backToDefaults(ActionEvent actionEvent) {
        TariffsData newDefaultTariffsData = new TariffsData();
        showDialogFillFields(newDefaultTariffsData);
//        obtainNumbersFromTextFields();
    }
}
