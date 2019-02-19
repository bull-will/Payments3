package sample;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import java.io.*;
import java.sql.SQLException;
import java.util.Optional;

import static sample.TextDeliverer.getAlertText;
import static sample.TextDeliverer.getText;

public class Controller {
    @FXML
    private BorderPane mainBorderPane;

    private PaymentsDataSource paymentsDataSource;

    private TariffsData tariffsData;

    ObservableList<PaymentMarker> paymentMarkers;

    @FXML
    private ListView<PaymentMarker> paymentsListView;

    @FXML
    private TextArea paymentTextArea;

    @FXML
    private ContextMenu listContextMenu;

    public void initialize() {

        paymentsDataSource = new PaymentsDataSource();
        paymentsDataSource.initializePaymentsDBTable(true);
        tariffsData = new TariffsData();
        tariffsData.loadTariffs();
        /* Payments markers of all the stored payments are collected into a list
        by the query to the payments database. */
        paymentMarkers = paymentsDataSource.getPaymentMarkers();

        /* Creating a context menu for the list in the window */
        listContextMenu = new ContextMenu();
        MenuItem editMenuItem = new MenuItem(getText("editPaymentMenuItem"));
        editMenuItem.setOnAction((ActionEvent actionEvent) -> {
            editPayment(paymentsListView.getSelectionModel().getSelectedItem());

        });
        MenuItem newPaymenBasedMenuItem = new MenuItem(getText("newPaymentBasedMenuItem"));
        newPaymenBasedMenuItem.setOnAction((ActionEvent actionEvent) -> {
            newPaymentBased(paymentsListView.getSelectionModel().getSelectedItem());

        });
        MenuItem deleteMenuItem = new MenuItem(getText("deletePaymentMenuItem"));
        deleteMenuItem.setOnAction((ActionEvent actionEvent) -> {
            deletePayment(paymentsListView.getSelectionModel().getSelectedItem());

        });
        listContextMenu.getItems().addAll(editMenuItem, newPaymenBasedMenuItem, deleteMenuItem);

        /* Some necessary  configuring of the list view */
        paymentsListView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends PaymentMarker> observable, PaymentMarker oldValue, PaymentMarker newValue) ->
                {
                    if (newValue != null) {
                        PaymentMarker item = paymentsListView.getSelectionModel().getSelectedItem();
                        paymentTextArea.setText(item.getFullDescription());
                    }
                }
        );

        /* Binding the list of payment markers to the list view in the main window*/
        paymentsListView.setItems(paymentMarkers);
        paymentsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        paymentsListView.getSelectionModel().selectFirst();

        /* Setting a cell factory for the list view */
        paymentsListView.setCellFactory((ListView<PaymentMarker> paymentListView) -> {
            ListCell<PaymentMarker> cell = new ListCell<PaymentMarker>() {
                @Override
                protected void updateItem(PaymentMarker item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            };

            cell.emptyProperty().addListener(
                    (abs, wasEmpty, isNowEmpty) -> {
                        if (isNowEmpty) {
                            cell.setContextMenu(null);
                        } else {
                            cell.setContextMenu(listContextMenu);
                        }
                    });

            return cell;
        });
    }

    @FXML
    public void newPayment(ActionEvent actionEvent) {
        Payment newPaymentBase; // the basis for a new payment
        PaymentMarker lastPaymentMarker = null;
        Payment lastPayment = null;

        int newPaymentId;
        int newPaymentYear = 0;
        int newPaymentMonth = 0;
        int newPaymentElectroStart = 0;
        int newPaymentWaterStart = 0;
        // Checking if the list of payments contains anything. It will help to set the new payment id
        if (paymentMarkers.isEmpty()) {
            newPaymentId = 1;
        } else {
            // Retrieving the las payment marker as far as we've defined the list of markers is not empty
            lastPaymentMarker = paymentMarkers.get(paymentMarkers.size() - 1);
            // Retrieving the id of the last payment. Incremented by one, it's to be the new payment's id
            newPaymentId = lastPaymentMarker.getId() + 1;
        }
        /* Was the last payment made for the concrete month? If so its name ends with the name of that month
        And also if so, the new payment is going to continue if for the next month */
        /* Checking if the last payment's name doesn't end with "No_month".
         * This seems to be an awkward crutch, and it is, but still it's done only for pre-filling
         * four text fields in the payment creation window. It's not critical, just a light convenience */
        if (lastPaymentMarker != null && !lastPaymentMarker.getName().endsWith(Payment.NAMES_OF_MONTHS[0])) {
            /* In this case let's retrieve the last payment from the database
            to fill some of the new payment's fields */
            lastPayment = paymentsDataSource.getPayment(newPaymentId - 1, true);
            if (lastPayment.getMonth() == 12) {
                newPaymentYear = lastPayment.getYear() + 1;
                newPaymentMonth = 1;
            } else {
                newPaymentYear = lastPayment.getYear();
                newPaymentMonth = lastPayment.getMonth() + 1;
            }
            newPaymentElectroStart = lastPayment.getElectroEnd();
            newPaymentWaterStart = lastPayment.getWaterEnd();
        }
        newPaymentBase = new Payment(newPaymentId, newPaymentYear, newPaymentMonth,
                newPaymentElectroStart, 0, newPaymentWaterStart, 0);
        newPayment(newPaymentBase, false);
    }

    @FXML
    public void newPaymentBased(ActionEvent actionEvent) {
        PaymentMarker selectedPaymentMarker = paymentsListView.getSelectionModel().getSelectedItem();
        if (selectedPaymentMarker != null) {
            newPaymentBased(selectedPaymentMarker);
        } else {
            Alerts.alertInfo(getAlertText("noChosenBasePaymentTitle"),
                    getAlertText("noChosenBasePaymentMessage"));
        }
    }

    private void newPaymentBased(PaymentMarker marker) {
        /* Obtaining the payment corresponding the selected marker from the database
         * to be the basis for the new payment. Also cloning it цшер штскуьутештп its id by one */
        Payment basePaymentFromDB = paymentsDataSource.getPayment(marker.getId(), true);
        if (basePaymentFromDB != null) {
            Payment basePaymentWithIncrementedID = basePaymentFromDB.cloneIncrementId();
            newPayment(basePaymentWithIncrementedID, true);
        } else {
            Alerts.alertInfo(getAlertText("noChosenBasePaymentInDBTitle"),
                    getAlertText("noChosenBasePaymentInDBMessage"));
        }
    }

    private void newPayment(Payment basePayment, boolean based) {
        /* The new payment is the result of the processing the base payment */
        Payment newPayment = processPaymentWithDialog(basePayment, false, based);
        /* Storing the processed payment and the new payment marker */
        if (newPayment != null) {
            paymentsDataSource.storePayment(newPayment, true);
            PaymentMarker newPaymentMarker = new PaymentMarker
                    (newPayment.getId(), newPayment.getName(), newPayment.getFullDescription());
            paymentMarkers.add(newPaymentMarker);
            paymentsListView.getSelectionModel().select(newPaymentMarker);
        }
    }

    /* for both newPayment() and editPayment() */
    public Payment processPaymentWithDialog(Payment payment, boolean editPaymentOrNot, boolean fillSetFields) {

        Dialog dialog = new Dialog();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle(getText("processPaymentTitle"));
        dialog.setHeaderText(editPaymentOrNot ? getText("processPaymentEdit") : getText("processPaymentNew"));
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("paymentDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
//            System.out.println("Couldn't load the dialog");
            Alerts.alertInfo(getAlertText("paymentProcessingDialogError"),
                    getAlertText("paymentProcessingDialogErrorMessage"));
            return null;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        PaymentDialogController paymentDialog = fxmlLoader.getController();

        /* let's fill dialog window's fields with numbers from the basis payment (new/being edited) */
        paymentDialog.showDialogFillFields(payment, editPaymentOrNot, fillSetFields, tariffsData);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                Payment processedPayment = paymentDialog.processPayment(payment);
                return processedPayment; /* to editPayment() as well as to newPayment() */
            }
            if (result.get() == ButtonType.CANCEL) {
//                paymentsListView.getSelectionModel().select(toBeSelected);
                return null; // This line is actually redundant because it returns null later anyways
            }
        } else {
            Alerts.alertInfo(getAlertText("paymentProcessingDialogNoResultTitle"),
                    getAlertText("paymentProcessingDialogNoResultMessage"));
        }
        return null;
    }

    @FXML
    public void editPayment(ActionEvent actionEvent) {
        PaymentMarker selectedPaymentMarker = paymentsListView.getSelectionModel().getSelectedItem();
        if (selectedPaymentMarker != null) {
            editPayment(selectedPaymentMarker);
        } else {
            Alerts.alertInfo(getAlertText("noSelectedPaymentTitle"),
                    getAlertText("noSelectedPaymentMessage"));
        }
    }

    private void editPayment(PaymentMarker marker) {
        /* Obtaining the payment corresponding the selected marker from the database for editing */
        Payment paymentToBeEdited = paymentsDataSource.getPayment(marker.getId(), true);
        Payment editedPayment = processPaymentWithDialog(paymentToBeEdited, true, true);
        if (editedPayment != null) {
            paymentsDataSource.storePayment(editedPayment, true);
            marker.setName(editedPayment.getName());
            marker.setFullDescription(editedPayment.getFullDescription());
            paymentsListView.refresh();
            paymentTextArea.setText(marker.getFullDescription());
        }
    }


    @FXML
    public void deletePayment(ActionEvent actionEvent) {
        PaymentMarker selectedPaymentMarker = paymentsListView.getSelectionModel().getSelectedItem();
        if (selectedPaymentMarker == null) {
            if (paymentMarkers.isEmpty()) {
                Alerts.alertInfo(getAlertText("noSelectedPaymentToDeleteTitle"), getAlertText("noSelectedPaymentToDeleteMessage"));
                return;
            }
        } else deletePayment(selectedPaymentMarker);
    }

    private void deletePayment(PaymentMarker paymentToDelete) {
        if (paymentToDelete == null) {
            Alerts.alertInfo(getAlertText("deletePaymentErrorTitle"),
                    getAlertText("deletePaymentErrorMessage"));
            return;
        }
        Alert alert = Alerts.alertConfirmation(getAlertText("deletePaymentConfirmTitle"),
                getAlertText("deletePaymentConfirmHeader"),
                getAlertText("deletePaymentConfirmMessage") + " " + paymentToDelete.getName());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                paymentsDataSource.deletePayment(paymentToDelete.getId(), true);
            } catch (SQLException e) {
                Alerts.alertInfo(getAlertText("deletePaymentFromDBErrorTitle"),
                        getAlertText("deletePaymentFromDBErrorMessage"));
                return;
            }
            paymentMarkers.remove(paymentToDelete);
//            paymentTextArea.clear();
            paymentsListView.refresh();
            if (!paymentMarkers.isEmpty()) {
                paymentsListView.getSelectionModel().selectLast();
                PaymentMarker selectedPaymentMarker = paymentsListView.getSelectionModel().getSelectedItem();
                paymentTextArea.setText(selectedPaymentMarker.getFullDescription());
            }
        }
    }

    @FXML
    /* a user can watch and edit his tariffs in this window */
    public void tariffsDialog(ActionEvent actionEvent) {
        Dialog dialog = new Dialog();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle(getText("tariffsTitle"));
        dialog.setHeaderText(getText("tariffsHeader"));
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("tariffsDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
//            System.out.println("Couldn't load the dialog");
            Alerts.alertInfo(getAlertText("tariffsDialogErrorTitle"), getAlertText("tariffsDialogErrorMessage"));
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        TariffsDialogController tariffsDialog = fxmlLoader.getController();

        /* let's fill dialog window's fields from the tariffs file */
        tariffsDialog.showDialogFillFields(tariffsData);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            tariffsData = tariffsDialog.processTariffs(tariffsData);
            tariffsData.save();
        }
    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        PaymentMarker selectedPayment = paymentsListView.getSelectionModel().getSelectedItem();
        if (selectedPayment != null) {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                deletePayment(selectedPayment);
            }
        }
    }

    @FXML
    public void toDoc(ActionEvent actionEvent) throws IOException {
        PaymentMarker paymentToDoc = paymentsListView.getSelectionModel().getSelectedItem();
        if (paymentToDoc == null) {
            Alerts.alertInfo(getAlertText("noPaymentToDocSelectedTitle"),
                    getAlertText("noPaymentToDocSelectedMessage"));
            return;
        }

        String nameFromThePayment = paymentToDoc.getName();
        File outPutFileToPrint = makeOutPutFile(nameFromThePayment); // Actual file name may be further updated (or not)

        /* This line creates output stream writer for the .doc file */
        OutputStreamWriter dataOut = makeWriter(outPutFileToPrint);

        /* And now let's fill the file with contents */
        try {
            writeLinesToFile(paymentToDoc, dataOut);
            Alerts.alertInfo(getAlertText("paymentWrittenToDocTitle"),
                    getAlertText("paymentWrittenToDocMessage") + " " + outPutFileToPrint);
        } catch (IOException e) {
            Alerts.alertInfo(getAlertText("writingToDocErrorTitle"),
                    getAlertText("writingToDocErrorMessage"));
        }
    }

    /* This method sets a directory, a name and an extension for output file */
    File makeOutPutFile(String paymentName) throws IOException {
        String workingDir = System.getProperty("user.dir"); // Files will be stored in the project folder

        /* setting a directory for generated files */
        String dirForGeneratedFiles = workingDir + "\\" + getText("nameForPaymentsInDocDirectory") + "\\";
        if (!new File(dirForGeneratedFiles).exists()) {
            new File(dirForGeneratedFiles).mkdir();
        }
        String outPutFileName = dirForGeneratedFiles + paymentName;
        String extension = ".doc"; // set the extension of the output file

        // the following section changes the file name if a file with this name already exists:
        int counter = 0; // has to be given to the next method
        if (new File(outPutFileName + extension).exists())
            outPutFileName = changeOutPutFileName(outPutFileName, extension, counter);
        return new File(outPutFileName + extension);
    }

    static OutputStreamWriter makeWriter(File outPutFileToPrint) throws FileNotFoundException {
        BufferedOutputStream outputStream
                = new BufferedOutputStream(
                new FileOutputStream(
                        outPutFileToPrint));
        return new OutputStreamWriter(outputStream);
    }

    private void writeLinesToFile(PaymentMarker paymentToDoc, OutputStreamWriter dataOut) throws IOException {

        String writeLine = paymentToDoc.getFullDescription();
        dataOut.write(writeLine);
        dataOut.close();
    }

    static String changeOutPutFileName(String outPutFileName, String extension, int counter) {
        counter++;
        if (new File(outPutFileName + "_" + counter + extension).exists()) {
            return changeOutPutFileName(outPutFileName, extension, counter);
        } else {
            return outPutFileName + "_" + counter;
        }
    }
}
