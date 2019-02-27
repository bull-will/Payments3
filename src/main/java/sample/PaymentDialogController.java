package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import static sample.TextDeliverer.getAlertText;

public class PaymentDialogController {

    @FXML
    private DialogPane dialogPane; /* do I even need this? */

    @FXML
    private CheckBox roundCheckBox;

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
    private TextField hotWaterTariffField;
    @FXML
    private TextField hotWaterStartField;
    @FXML
    private TextField hotWaterEndField;
    @FXML
    private TextField hotWaterMustPayField;
    @FXML
    private TextField heatingTariffField;
    @FXML
    private TextField heatingStartField;
    @FXML
    private TextField heatingEndField;
    @FXML
    private TextField heatingMustPayField;
    @FXML
    private TextField gasTariffField;
    @FXML
    private TextField gasStartField;
    @FXML
    private TextField gasEndField;
    @FXML
    private TextField gasMustPayField;
    @FXML
    private TextField sewageTariffField;
    @FXML
    private TextField sewageStartField;
    @FXML
    private TextField sewageEndField;
    @FXML
    private TextField sewageMustPayField;
    @FXML
    private TextField flatTariffField;
    @FXML
    private TextField flatMustPayField;
    @FXML
    private TextField garbageTariffField;
    @FXML
    private TextField garbageMustPayField;

    @FXML
    RadioButton electroByCounter;
    @FXML
    RadioButton electroByTariff;
    @FXML
    RadioButton electroBySet;
    @FXML
    RadioButton waterByCounter;
    @FXML
    RadioButton waterByTariff;
    @FXML
    RadioButton waterBySet;
    @FXML
    RadioButton hotWaterByCounter;
    @FXML
    RadioButton hotWaterByTariff;
    @FXML
    RadioButton hotWaterBySet;
    @FXML
    RadioButton heatingByCounter;
    @FXML
    RadioButton heatingByTariff;
    @FXML
    RadioButton heatingBySet;
    @FXML
    RadioButton gasByCounter;
    @FXML
    RadioButton gasByTariff;
    @FXML
    RadioButton gasBySet;
    @FXML
    RadioButton sewageByCounter;
    @FXML
    RadioButton sewageByTariff;
    @FXML
    RadioButton sewageBySet;
    @FXML
    RadioButton flatByTariff;
    @FXML
    RadioButton flatBySet;
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

    private double waterTariff;
    private double hotWaterTariff;
    private double heatingTariff;
    private double gasTariff;
    private double sewageTariff;
    private double flatTariff;
    private double garbageTariff;

    private double electroMustPay;
    private double waterMustPay;
    private double hotWaterMustPay;
    private double heatingMustPay;
    private double gasMustPay;
    private double sewageMustPay;
    private double flatMustPay;
    private double garbageMustPay;

    private int year;
    private int month;

    private int electroStart;
    private int electroEnd;
    private int waterStart;
    private int waterEnd;
    private int hotWaterStart;
    private int hotWaterEnd;
    private int heatingStart;
    private int heatingEnd;
    private int gasStart;
    private int gasEnd;
    private int sewageStart;
    private int sewageEnd;

    private boolean round;

    private boolean someFieldsProcessedWrong = false;

    /* switching radio buttons in the dialog window
    according to whether the stuff is supposed to be payd by the tariff or by the random set amount of money*/
    @FXML
    public void electroSetInField(MouseEvent mouseEvent) {
        electroBySet.setSelected(true);
    }

    @FXML
    public void waterSetInField(MouseEvent mouseEvent) {
        waterBySet.setSelected(true);
    }

    @FXML
    public void hotWaterSetInField(MouseEvent mouseEvent) {
        hotWaterBySet.setSelected(true);
    }

    @FXML
    public void heatingSetInField(MouseEvent mouseEvent) {
        heatingBySet.setSelected(true);
    }

    @FXML
    public void gasSetInField(MouseEvent mouseEvent) {
        gasBySet.setSelected(true);
    }

    @FXML
    public void sewageSetInField(MouseEvent mouseEvent) {
        sewageBySet.setSelected(true);
    }

    @FXML
    public void flatSetInField(MouseEvent mouseEvent) {
        flatBySet.setSelected(true);
    }

    @FXML
    public void garbageSetInField(MouseEvent mouseEvent) {
        garbageBySet.setSelected(true);
    }

    /* Obtaining what was in the textfields of the payment dialog, either pre-filled or manually entered */
    public void obtainNumbersFromTextFields() {
        year = obtainIntFromFields(yearField);
        month = obtainIntFromFields(monthField);
        electroTariff1 = obtainDoubleFromFields(electroTariff1Field);
        electroLimit1 = obtainIntFromFields(electroLimit1Field);
        electroTariff2 = obtainDoubleFromFields(electroTariff2Field);
        electroLimit2 = obtainIntFromFields(electroLimit2Field);
        electroTariff3 = obtainDoubleFromFields(electroTariff3Field);
        electroLimit3 = obtainIntFromFields(electroLimit3Field);
        electroTariff4 = obtainDoubleFromFields(electroTariff4Field);
        electroStart = obtainIntFromFields(electroStartField);
        electroEnd = obtainIntFromFields(electroEndField);
        waterTariff = obtainDoubleFromFields(waterTariffField);
        waterStart = obtainIntFromFields(waterStartField);
        waterEnd = obtainIntFromFields(waterEndField);
        hotWaterTariff = obtainDoubleFromFields(hotWaterTariffField);
        hotWaterStart = obtainIntFromFields(hotWaterStartField);
        hotWaterEnd = obtainIntFromFields(hotWaterEndField);
        heatingTariff = obtainDoubleFromFields(heatingTariffField);
        heatingStart = obtainIntFromFields(heatingStartField);
        heatingEnd = obtainIntFromFields(heatingEndField);
        gasTariff = obtainDoubleFromFields(gasTariffField);
        gasStart = obtainIntFromFields(gasStartField);
        gasEnd = obtainIntFromFields(gasEndField);
        sewageTariff = obtainDoubleFromFields(sewageTariffField);
        sewageStart = obtainIntFromFields(sewageStartField);
        sewageEnd = obtainIntFromFields(sewageEndField);
        flatTariff = obtainDoubleFromFields(flatTariffField);
        garbageTariff = obtainDoubleFromFields(garbageTariffField);
        electroMustPay = obtainSetPayment(electroBySet, electroMustPayField);
        waterMustPay = obtainSetPayment(waterBySet, waterMustPayField);
        hotWaterMustPay = obtainSetPayment(hotWaterBySet, hotWaterMustPayField);
        heatingMustPay = obtainSetPayment(heatingBySet, heatingMustPayField);
        gasMustPay = obtainSetPayment(gasBySet, gasMustPayField);
        sewageMustPay = obtainSetPayment(sewageBySet, sewageMustPayField);
        flatMustPay = obtainSetPayment(flatBySet, flatMustPayField);
        garbageMustPay = obtainSetPayment(garbageBySet, garbageMustPayField);
        round = roundCheckBox.isSelected();

        if (someFieldsProcessedWrong) {
            Alerts.alertInfo(getAlertText("paymentDialogControllerProcessingErrorTitle"),
                    getAlertText("paymentDialogControllerProcessingErrorMessage"));
        }
    }

    private int obtainIntFromFields(TextField textField) {
        int value = 0;
        try {
            value = Integer.parseInt(textField.getText().replace(',', '.'));
        } catch (Exception e) {
            someFieldsProcessedWrong = true;
        }
        return value;
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

    private double obtainSetPayment(RadioButton set, TextField mustPayField) {
        double value = 0d;
        try {
            if (set.isSelected()) {
                value = Double.parseDouble(mustPayField.getText().replace(',', '.'));
            }
        } catch (Exception e) {
            someFieldsProcessedWrong = true;
        }
        return value;
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
        payment.setWaterTariff(waterTariff);
        payment.setHotWaterTariff(hotWaterTariff);
        payment.setHeatingTariff(heatingTariff);
        payment.setGasTariff(gasTariff);
        payment.setSewageTariff(sewageTariff);
        payment.setFlatTariff(flatTariff);
        payment.setGarbageTariff(garbageTariff);
        payment.setElectroStart(electroStart);
        payment.setElectroEnd(electroEnd);
        payment.setWaterStart(waterStart);
        payment.setWaterEnd(waterEnd);
        payment.setHotWaterStart(hotWaterStart);
        payment.setHotWaterEnd(hotWaterEnd);
        payment.setHeatingStart(heatingStart);
        payment.setHeatingEnd(heatingEnd);
        payment.setGasStart(gasStart);
        payment.setGasEnd(gasEnd);
        payment.setSewageStart(sewageStart);
        payment.setSewageEnd(sewageEnd);

        /* Setting payment by counter, by tariff or by set value according to selected radio buttons */

        payment.setElectroPaymentByTariff(electroByTariff.isSelected());
        if (electroBySet.isSelected() && !electroMustPayField.getText().equals("")) {
            payment.setElectroPayment(electroMustPay);
        } else {
            payment.unsetElectroPayment();
        }

        payment.setWaterPaymentByTariff(waterByTariff.isSelected());
        if (waterBySet.isSelected() && !waterMustPayField.getText().equals("")) {
            payment.setWaterPayment(waterMustPay);
        } else {
            payment.unsetWaterPayment();
        }

        payment.setHotWaterPaymentByTariff(hotWaterByTariff.isSelected());
        if (hotWaterBySet.isSelected() && !hotWaterMustPayField.getText().equals("")) {
            payment.setHotWaterPayment(hotWaterMustPay);
        } else {
            payment.unsetHotWaterPayment();
        }

        payment.setHeatingPaymentByTariff(heatingByTariff.isSelected());
        if (heatingBySet.isSelected() && !heatingMustPayField.getText().equals("")) {
            payment.setHeatingPayment(heatingMustPay);
        } else {
            payment.unseteatingPayment();
        }

        payment.setGasPaymentByTariff(gasByTariff.isSelected());
        if (gasBySet.isSelected() && !gasMustPayField.getText().equals("")) {
            payment.setGasPayment(gasMustPay);
        } else {
            payment.unsetGasPayment();
        }

        payment.setSewagePaymentByTariff(sewageByTariff.isSelected());
        if (sewageBySet.isSelected() && !sewageMustPayField.getText().equals("")) {
            payment.setSewagePayment(sewageMustPay);
        } else {
            payment.unsetSewagePayment();
        }

        if (flatBySet.isSelected() && !flatMustPayField.getText().equals("")) {
            payment.setFlatPayment(flatMustPay);
        } else {
            payment.unsetFlatPayment();
        }

        if (garbageBySet.isSelected() && !garbageMustPayField.getText().equals("")) {
            payment.setGarbagePayment(garbageMustPay);
        } else {
            payment.setDefaultGarbagePayment();
        }

        payment.setRound(round);

        payment.payForEverything();
        payment.buildFullDescription();
        return payment;
    }

    /* refillAllFieldsForEditing speaks for itself,
     * and fillSetFields is true when creating a new payment based on an existing one*/
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
            hotWaterTariffField.setText(String.valueOf(payment.getHotWaterTariff()));
            heatingTariffField.setText(String.valueOf(payment.getHeatingTariff()));
            gasTariffField.setText(String.valueOf(payment.getGasTariff()));
            sewageTariffField.setText(String.valueOf(payment.getSewageTariff()));
            flatTariffField.setText(String.valueOf(payment.getFlatTariff()));
            garbageTariffField.setText(String.valueOf(payment.getGarbageTariff()));
            roundCheckBox.setSelected(payment.isRound());
        } else {
            electroTariff1Field.setText(String.valueOf(tariffsData.electroTariff1));
            electroTariff2Field.setText(String.valueOf(tariffsData.electroTariff2));
            electroTariff3Field.setText(String.valueOf(tariffsData.electroTariff3));
            electroTariff4Field.setText(String.valueOf(tariffsData.electroTariff4));
            electroLimit1Field.setText(String.valueOf(tariffsData.electroLimit1));
            electroLimit2Field.setText(String.valueOf(tariffsData.electroLimit2));
            electroLimit3Field.setText(String.valueOf(tariffsData.electroLimit3));
            waterTariffField.setText(String.valueOf(tariffsData.waterTariff));
            hotWaterTariffField.setText(String.valueOf(tariffsData.hotWaterTariff));
            heatingTariffField.setText(String.valueOf(tariffsData.heatingTariff));
            gasTariffField.setText(String.valueOf(tariffsData.gasTariff));
            sewageTariffField.setText(String.valueOf(tariffsData.sewageTariff));
            flatTariffField.setText(String.valueOf(tariffsData.flatTariff));
            garbageTariffField.setText(String.valueOf(tariffsData.garbageTariff));
            roundCheckBox.setSelected(tariffsData.round);
        }

        electroStartField.setText(String.valueOf(payment.getElectroStart()));
        electroEndField.setText(String.valueOf(payment.getElectroEnd()));
        waterStartField.setText(String.valueOf(payment.getWaterStart()));
        waterEndField.setText(String.valueOf(payment.getWaterEnd()));
        hotWaterStartField.setText(String.valueOf(payment.getHotWaterStart()));
        hotWaterEndField.setText(String.valueOf(payment.getHotWaterEnd()));
        heatingStartField.setText(String.valueOf(payment.getHeatingStart()));
        heatingEndField.setText(String.valueOf(payment.getHeatingEnd()));
        gasStartField.setText(String.valueOf(payment.getGasStart()));
        gasEndField.setText(String.valueOf(payment.getGasEnd()));
        sewageStartField.setText(String.valueOf(payment.getSewageStart()));
        sewageEndField.setText(String.valueOf(payment.getSewageEnd()));


        /* these fields' data is collected from the basis payment only if it's being edited */
        if (refillAllFieldsForEditing || fillSetFields) {

            if (payment.isElectroPaymentSet()) {
                electroBySet.setSelected(true);
                electroMustPayField.setText(String.valueOf(payment.getElectroMustPay()));
            } else if (payment.isElectroPaymentByTariff()) {
                electroByTariff.setSelected(true);
            } else electroByCounter.setSelected(true);
            
            if (payment.isWaterPaymentSet()) {
                waterBySet.setSelected(true);
                waterMustPayField.setText(String.valueOf(payment.getWaterMustPay()));
            } else if (payment.isWaterPaymentByTariff()) {
                waterByTariff.setSelected(true);
            } else waterByCounter.setSelected(true);

            if (payment.isHotWaterPaymentSet()) {
                hotWaterBySet.setSelected(true);
                hotWaterMustPayField.setText(String.valueOf(payment.getHotWaterMustPay()));
            } else if (payment.isHotWaterPaymentByTariff()) {
                hotWaterByTariff.setSelected(true);
            } else hotWaterByCounter.setSelected(true);

            if (payment.isHeatingPaymentSet()) {
                heatingBySet.setSelected(true);
                heatingMustPayField.setText(String.valueOf(payment.getHeatingMustPay()));
            } else if (payment.isHeatingPaymentByTariff()) {
                heatingByTariff.setSelected(true);
            } else heatingByCounter.setSelected(true);

            if (payment.isGasPaymentSet()) {
                gasBySet.setSelected(true);
                gasMustPayField.setText(String.valueOf(payment.getGasMustPay()));
            } else if (payment.isGasPaymentByTariff()) {
                gasByTariff.setSelected(true);
            } else gasByCounter.setSelected(true);

            if (payment.isSewagePaymentSet()) {
                sewageBySet.setSelected(true);
                sewageMustPayField.setText(String.valueOf(payment.getSewageMustPay()));
            } else if (payment.isSewagePaymentByTariff()) {
                sewageByTariff.setSelected(true);
            } else sewageByCounter.setSelected(true);

            if (payment.isFlatPaymentSet()) {
                flatBySet.setSelected(true);
                flatMustPayField.setText(String.valueOf(payment.getFlatMustPay()));
            } else flatByTariff.setSelected(true);

            if (payment.isGarbagePaymentSet()) {
                garbageBySet.setSelected(true);
                garbageMustPayField.setText(String.valueOf(payment.getGarbageMustPay()));
            } else garbageByTariff.setSelected(true);

//            if (refillAllFieldsForEditing) {
//                electroStartField.setText(String.valueOf(payment.getElectroStart()));
//                electroEndField.setText(String.valueOf(payment.getElectroEnd()));
//                waterStartField.setText(String.valueOf(payment.getWaterStart()));
//                waterEndField.setText(String.valueOf(payment.getWaterEnd()));
//                hotWaterStartField.setText(String.valueOf(payment.getHotWaterStart()));
//                hotWaterEndField.setText(String.valueOf(payment.getHotWaterEnd()));
//                heatingStartField.setText(String.valueOf(payment.getHeatingStart()));
//                heatingEndField.setText(String.valueOf(payment.getHeatingEnd()));
//                gasStartField.setText(String.valueOf(payment.getGasStart()));
//                gasEndField.setText(String.valueOf(payment.getGasEnd()));
//                sewageStartField.setText(String.valueOf(payment.getSewageStart()));
//                sewageEndField.setText(String.valueOf(payment.getSewageEnd()));
//            }
        }
    }

}
