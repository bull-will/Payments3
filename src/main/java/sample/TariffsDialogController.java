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

    public void showDialogFillFields(TariffsData tariffsData) {
        electroTariff1Field.setText(tariffsData.electroTariff1 % 1 == 0d ? String.valueOf(Math.round(tariffsData.electroTariff1)) : String.valueOf(tariffsData.electroTariff1));
        electroTariff2Field.setText(tariffsData.electroTariff2 % 1 == 0d ? String.valueOf(Math.round(tariffsData.electroTariff2)) : String.valueOf(tariffsData.electroTariff2));
        electroTariff3Field.setText(tariffsData.electroTariff3 % 1 == 0d ? String.valueOf(Math.round(tariffsData.electroTariff3)) : String.valueOf(tariffsData.electroTariff3));
        electroTariff4Field.setText(tariffsData.electroTariff4 % 1 == 0d ? String.valueOf(Math.round(tariffsData.electroTariff4)) : String.valueOf(tariffsData.electroTariff4));
        electroLimit1Field.setText(tariffsData.electroLimit1 % 1 == 0d ? String.valueOf(Math.round(tariffsData.electroLimit1)) : String.valueOf(tariffsData.electroLimit1));
        electroLimit2Field.setText(tariffsData.electroLimit2 % 1 == 0d ? String.valueOf(Math.round(tariffsData.electroLimit2)) : String.valueOf(tariffsData.electroLimit2));
        electroLimit3Field.setText(tariffsData.electroLimit3 % 1 == 0d ? String.valueOf(Math.round(tariffsData.electroLimit3)) : String.valueOf(tariffsData.electroLimit3));
        waterTariffField.setText(tariffsData.waterTariff % 1 == 0d ? String.valueOf(Math.round(tariffsData.waterTariff)) : String.valueOf(tariffsData.waterTariff));
        hotWaterTariffField.setText(tariffsData.hotWaterTariff % 1 == 0d ? String.valueOf(Math.round(tariffsData.hotWaterTariff)) : String.valueOf(tariffsData.hotWaterTariff));
        heatingTariffField.setText(tariffsData.heatingTariff % 1 == 0d ? String.valueOf(Math.round(tariffsData.heatingTariff)) : String.valueOf(tariffsData.heatingTariff));
        gasTariffField.setText(tariffsData.gasTariff % 1 == 0d ? String.valueOf(Math.round(tariffsData.gasTariff)) : String.valueOf(tariffsData.gasTariff));
        sewageTariffField.setText(tariffsData.sewageTariff % 1 == 0d ? String.valueOf(Math.round(tariffsData.sewageTariff)) : String.valueOf(tariffsData.sewageTariff));
        flatTariffField.setText(tariffsData.flatTariff % 1 == 0d ? String.valueOf(Math.round(tariffsData.flatTariff)) : String.valueOf(tariffsData.flatTariff));
        garbageTariffField.setText(tariffsData.garbageTariff % 1 == 0d ? String.valueOf(Math.round(tariffsData.garbageTariff)) : String.valueOf(tariffsData.garbageTariff));
        roundCheckBox.setSelected(tariffsData.round);
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

        tariffsData.electroTariff1 = electroTariff1;
        tariffsData.electroLimit1 = electroLimit1;
        tariffsData.electroTariff2 = electroTariff2;
        tariffsData.electroLimit2 = electroLimit2;
        tariffsData.electroTariff3 = electroTariff3;
        tariffsData.electroLimit3 = electroLimit3;
        tariffsData.electroTariff4 = electroTariff4;
        tariffsData.waterTariff = waterTariff;
        tariffsData.hotWaterTariff = hotWaterTariff;
        tariffsData.heatingTariff = heatingTariff;
        tariffsData.gasTariff = gasTariff;
        tariffsData.sewageTariff = sewageTariff;
        tariffsData.flatTariff = flatTariff;
        tariffsData.garbageTariff = garbageTariff;
        tariffsData.round = round;

        return tariffsData;
    }

    public void backToDefaults(ActionEvent actionEvent) {
        TariffsData newDefaultTariffsData = new TariffsData();
        showDialogFillFields(newDefaultTariffsData);
//        obtainNumbersFromTextFields();
    }
}
