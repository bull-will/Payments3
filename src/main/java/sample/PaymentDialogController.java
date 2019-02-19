package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import static sample.TextDeliverer.getAlertText;

public class PaymentDialogController {

    @FXML
    private DialogPane dialogPane; /* do I even need this? */

    @FXML
    private TextField monthField;
    @FXML
    private TextField yearField;
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
    private TextField electroStartField;
    @FXML
    private TextField electroEndField;
    @FXML
    private TextField electroMustPayField;
    @FXML
    private TextField waterTariffField;
    @FXML
    private TextField waterStartField;
    @FXML
    private TextField waterEndField;
    @FXML
    private TextField waterMustPayField;
    @FXML
    private TextField flatTariffField;
    @FXML
    private TextField flatMustPayField;
    @FXML
    private TextField heatingTariffField;
    @FXML
    private TextField heatingMustPayField;
    @FXML
    private TextField garbageTariffField;
    @FXML
    private TextField garbageMustPayField;

    @FXML
    RadioButton electroByCounter;
    @FXML
    RadioButton electroBySet;
    @FXML
    RadioButton waterByCounter;
    @FXML
    RadioButton waterBySet;
    @FXML
    RadioButton flatByTariff;
    @FXML
    RadioButton flatBySet;
    @FXML
    RadioButton heatingByTariff;
    @FXML
    RadioButton heatingBySet;
    @FXML
    RadioButton garbageByTariff;
    @FXML
    RadioButton garbageBySet;

    /* These are going to be collected from the basis payment,
    then changed(or not) in the dialog, and then put into new/edited payment */
    private double electroTariff1;
    private int electroLimit1;
    private double electroTariff2;
    private int electroLimit2;
    private double electroTariff3;
    private int electroLimit3;
    private double electroTariff4;

    private double heatingTariff;
    private double waterTariff;
    private double flatTariff;
    private double garbageTariff;

    private double electroMustPay;
    private double heatingMustPay;
    private double waterMustPay;
    private double flatMustPay;
    private double garbageMustPay;

    private int year;
    private int month;

    private int electroStart;
    private int electroEnd;
    private int waterStart;
    private int waterEnd;
    private boolean someFieldsProcessedWrong = false;

    /* switching radio buttons in the dialog window
    according to whether the stuff is supposed to be payd by the tariff or by the random set amount of money*/
    public void electroSetInField(MouseEvent mouseEvent) {
        electroBySet.setSelected(true);
    }

    public void waterSetInField(MouseEvent mouseEvent) {
        waterBySet.setSelected(true);
    }

    @FXML
    public void flatSetInField(MouseEvent mouseEvent) {
        flatBySet.setSelected(true);
    }

    @FXML
    public void heatingSetInField(MouseEvent mouseEvent) {
        heatingBySet.setSelected(true);
    }

    @FXML
    public void garbageSetInField(MouseEvent mouseEvent) {
        garbageBySet.setSelected(true);
    }


    /* Obtaining what was in the textfields of the payment dialog, either pre-filled or manually entered */
    public void obtainNumbersFromTextFields() {
        try {
            year = Integer.parseInt(yearField.getText());
        } catch (Exception e) {
            year = 0;
            someFieldsProcessedWrong = true;
        }
        try {
            month = Integer.parseInt(monthField.getText());
        } catch (Exception e) {
            month = 0;
            someFieldsProcessedWrong = true;
        }
        try {
            electroTariff1 = Double.parseDouble(electroTariff1Field.getText().replace(',', '.'));
        } catch (Exception e) {
            electroTariff1 = 0;
            someFieldsProcessedWrong = true;
        }
        try {
            electroLimit1 = Integer.parseInt(electroLimit1Field.getText().replace(',', '.'));
        } catch (Exception e) {
            electroLimit1 = 0;
            someFieldsProcessedWrong = true;
        }
        try {
            electroTariff2 = Double.parseDouble(electroTariff2Field.getText().replace(',', '.'));
        } catch (Exception e) {
            electroTariff2 = 0;
            someFieldsProcessedWrong = true;
        }
        try {
            electroLimit2 = Integer.parseInt(electroLimit2Field.getText().replace(',', '.'));
        } catch (Exception e) {
            electroLimit2 = 0;
            someFieldsProcessedWrong = true;
        }
        try {
            electroTariff3 = Double.parseDouble(electroTariff3Field.getText().replace(',', '.'));
        } catch (Exception e) {
            electroTariff3 = 0;
            someFieldsProcessedWrong = true;
        }
        try {
            electroLimit3 = Integer.parseInt(electroLimit3Field.getText().replace(',', '.'));
        } catch (Exception e) {
            electroLimit3 = 0;
            someFieldsProcessedWrong = true;
        }
        try {
            electroTariff4 = Double.parseDouble(electroTariff4Field.getText().replace(',', '.'));
        } catch (Exception e) {
            electroTariff4 = 0;
            someFieldsProcessedWrong = true;
        }
        try {
            electroStart = Integer.parseInt(electroStartField.getText().replace(',', '.'));
        } catch (Exception e) {
            electroStart = 0;
            someFieldsProcessedWrong = true;
        }
        try {
            electroEnd = Integer.parseInt(electroEndField.getText().replace(',', '.'));
        } catch (Exception e) {
            electroEnd = 0;
            someFieldsProcessedWrong = true;
        }
        try {
            waterTariff = Double.parseDouble(waterTariffField.getText().replace(',', '.'));
        } catch (Exception e) {
            waterTariff = 0;
            someFieldsProcessedWrong = true;
        }
        try {
            waterStart = Integer.parseInt(waterStartField.getText().replace(',', '.'));
        } catch (Exception e) {
            waterStart = 0;
            someFieldsProcessedWrong = true;
        }
        try {
            waterEnd = Integer.parseInt(waterEndField.getText().replace(',', '.'));
        } catch (Exception e) {
            waterEnd = 0;
            someFieldsProcessedWrong = true;
        }
        try {
            heatingTariff = Double.parseDouble(heatingTariffField.getText().replace(',', '.'));
        } catch (Exception e) {
            heatingTariff = 0;
            someFieldsProcessedWrong = true;
        }
        try {
            flatTariff = Double.parseDouble(flatTariffField.getText().replace(',', '.'));
        } catch (Exception e) {
            flatTariff = 0;
            someFieldsProcessedWrong = true;
        }
        try {
            garbageTariff = Double.parseDouble(garbageTariffField.getText().replace(',', '.'));
        } catch (Exception e) {
            garbageTariff = 0;
            someFieldsProcessedWrong = true;
        }


        try {
            if (electroBySet.isSelected())
                electroMustPay = Double.parseDouble(electroMustPayField.getText().replace(',', '.'));
        } catch (Exception e) {
            electroMustPay = 0;
            someFieldsProcessedWrong = true;
        }
        try {
            if (waterBySet.isSelected())
                waterMustPay = Double.parseDouble(waterMustPayField.getText().replace(',', '.'));
        } catch (Exception e) {
            waterMustPay = 0;
            someFieldsProcessedWrong = true;
        }
        try {
            if (flatBySet.isSelected())
                flatMustPay = Double.parseDouble(flatMustPayField.getText().replace(',', '.'));
        } catch (Exception e) {
            flatMustPay = 0;
            someFieldsProcessedWrong = true;
        }
        try {
            if (heatingBySet.isSelected())
                heatingMustPay = Double.parseDouble(heatingMustPayField.getText().replace(',', '.'));
        } catch (Exception e) {
            heatingMustPay = 0;
            someFieldsProcessedWrong = true;
        }
        try {
            if (garbageBySet.isSelected())
                garbageMustPay = Double.parseDouble(garbageMustPayField.getText().replace(',', '.'));
        } catch (Exception e) {
            garbageMustPay = 0;
            someFieldsProcessedWrong = true;
        }

        if (someFieldsProcessedWrong) {

            Alerts.alertInfo(getAlertText("paymentDialogControllerProcessingErrorTitle"),
                    getAlertText("paymentDialogControllerProcessingErrorMessage"));
        }
    }

    public Payment processPayment(Payment payment) {

        obtainNumbersFromTextFields();

        payment.setYear(year);
        payment.setMonth(month);
        payment.buildName();

        payment.setElectroTariff1(electroTariff1);
        payment.setElectroLimit1(electroLimit1);
        payment.setElectroTariff2(electroTariff2);
        payment.setElectroLimit2(electroLimit2);
        payment.setElectroTariff3(electroTariff3);
        payment.setElectroLimit3(electroLimit3);
        payment.setElectroTariff4(electroTariff4);
        payment.setHeatingTariff(heatingTariff);
        payment.setWaterTariff(waterTariff);
        payment.setFlatTariff(flatTariff);
        payment.setGarbageTariff(garbageTariff);
        payment.setElectroStart(electroStart);
        payment.setElectroEnd(electroEnd);
        payment.setWaterStart(waterStart);
        payment.setWaterEnd(waterEnd);

        /* If a user set one or another payment to be paid not by the tariff but set the sum himself */
        if (electroBySet.isSelected() && !electroMustPayField.getText().equals("")) {
            payment.setElectroPayment(electroMustPay);
        } else {
            payment.setDefaultElectroPayment();
        }
        if (waterBySet.isSelected() && !waterMustPayField.getText().equals("")) {
            payment.setWaterPayment(waterMustPay);
        } else {
            payment.setDefaultWaterPayment();
        }
        if (flatBySet.isSelected() && !flatMustPayField.getText().equals("")) {
            payment.setFlatPayment(flatMustPay);
        } else {
            payment.setDefaultFlatPayment();
        }
        if (heatingBySet.isSelected() && !heatingMustPayField.getText().equals("")) {
            payment.setHeatingPayment(heatingMustPay);
        } else {
            payment.setDefaultHeatingPayment();
        }
        if (garbageBySet.isSelected() && !garbageMustPayField.getText().equals("")) {
            payment.setGarbagePayment(garbageMustPay);
        } else {
            payment.setDefaultGarbagePayment();
        }

        payment.payForEverything();
        payment.buidFullDescription();
        return payment;
    }

    public void showDialogFillFields(Payment payment, boolean refillAllFieldsForEditing, boolean fillSetFields,
                                     TariffsData tariffsData) {
        monthField.setText(String.valueOf(payment.getMonth()));
        yearField.setText(String.valueOf(payment.getYear()));

        if (fillSetFields) {
            electroTariff1Field.setText(String.valueOf(payment.getElectroTariff1()));
            electroTariff2Field.setText(String.valueOf(payment.getElectroTariff2()));
            electroTariff3Field.setText(String.valueOf(payment.getElectroTariff3()));
            electroTariff4Field.setText(String.valueOf(payment.getElectroTariff4()));
            electroLimit1Field.setText(String.valueOf(payment.getElectroLimit1()));
            electroLimit2Field.setText(String.valueOf(payment.getElectroLimit2()));
            electroLimit3Field.setText(String.valueOf(payment.getElectroLimit3()));
            waterTariffField.setText(String.valueOf(payment.getWaterTariff()));
            flatTariffField.setText(String.valueOf(payment.getFlatTariff()));
            heatingTariffField.setText(String.valueOf(payment.getHeatingTariff()));
            garbageTariffField.setText(String.valueOf(payment.getGarbageTariff()));
        } else {
            electroTariff1Field.setText(String.valueOf(tariffsData.electroTariff1));
            electroTariff2Field.setText(String.valueOf(tariffsData.electroTariff2));
            electroTariff3Field.setText(String.valueOf(tariffsData.electroTariff3));
            electroTariff4Field.setText(String.valueOf(tariffsData.electroTariff4));
            electroLimit1Field.setText(String.valueOf(tariffsData.electroLimit1));
            electroLimit2Field.setText(String.valueOf(tariffsData.electroLimit2));
            electroLimit3Field.setText(String.valueOf(tariffsData.electroLimit3));
            waterTariffField.setText(String.valueOf(tariffsData.waterTariff));
            flatTariffField.setText(String.valueOf(tariffsData.flatTariff));
            heatingTariffField.setText(String.valueOf(tariffsData.heatingTariff));
            garbageTariffField.setText(String.valueOf(tariffsData.garbageTariff));
        }

        electroStartField.setText(String.valueOf(payment.getElectroStart()));
        electroEndField.setText(String.valueOf(payment.getElectroEnd()));

        waterStartField.setText(String.valueOf(payment.getWaterStart()));
        waterEndField.setText(String.valueOf(payment.getWaterEnd()));


        /* these fields' data is collected from the basis payment only if it's being edited */
        if (refillAllFieldsForEditing || fillSetFields) {

            if (payment.isElectroPaymentSet()) {
                electroBySet.setSelected(true);
                electroMustPayField.setText(String.valueOf(payment.getElectroMustPay()));
            }
            if (payment.isWaterPaymentSet()) {
                waterBySet.setSelected(true);
                waterMustPayField.setText(String.valueOf(payment.getWaterMustPay()));
            }
            if (payment.isFlatPaymentSet()) {
                flatBySet.setSelected(true);
                flatMustPayField.setText(String.valueOf(payment.getFlatMustPay()));
            }
            if (payment.isHeatingPaymentSet()) {
                heatingBySet.setSelected(true);
                heatingMustPayField.setText(String.valueOf(payment.getHeatingMustPay()));
            }
            if (payment.isGarbagePaymentSet()) {
                garbageBySet.setSelected(true);
                garbageMustPayField.setText(String.valueOf(payment.getGarbageMustPay()));
            }

            if (refillAllFieldsForEditing) {
                electroStartField.setText(String.valueOf(payment.getElectroStart()));
                electroEndField.setText(String.valueOf(payment.getElectroEnd()));
                waterStartField.setText(String.valueOf(payment.getWaterStart()));
                waterEndField.setText(String.valueOf(payment.getWaterEnd()));
            }
        }
    }

}
