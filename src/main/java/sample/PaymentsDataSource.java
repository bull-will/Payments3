package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Random;

import static sample.TextDeliverer.getAlertText;

/* The payments database contains the single table with all the stored payments and the full set of their fields
 * Payment markers are used for economizing database connection resources by retrieving only the part of payments fields
 * from the limited set of the database columns by the query to the payment markers view.
 * A payment marker corresponds the full payment stored in the database
 * and has the 'id' field which is equal to the payment's id (as it is a unique key in the database),
 * the 'name' field, and a 'fullDescription' field. Those are also equal to the respective payment's fields.
 * Payments markers of all the stored payments are collected into an ObservableList by the query to the payments database.
 * For simplification of that query, the database has a view containing columns '_id', 'name', and 'fullDescription'.*/
public class PaymentsDataSource {

    private Connection conn;

    private static PaymentsDataSource instance = new PaymentsDataSource();

    private Statement statement;
    public static final String DB_NAME = "payments.db";
    static private String connectionString;
    static private String dbFile;

    /* The statement for creating a payments table in the database */
    private static String createTableStatement;

    /* The names of the payments table and of the payments markers view */
    public static final String TABLE_PAYMENTS = "payments";
    public static final String VIEW_NAME = "payment_markers";

    // Set of names of the database table columns corresponding the fields of a payment
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String FULL_DESCRIPTION = "fullDescription";
    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String ELECTRO_TARIFF_1 = "electroTariff1";
    public static final String ELECTRO_LIMIT_1 = "electroLimit1";
    public static final String ELECTRO_TARIFF_2 = "electroTariff2";
    public static final String ELECTRO_LIMIT_2 = "electroLimit2";
    public static final String ELECTRO_TARIFF_3 = "electroTariff3";
    public static final String ELECTRO_LIMIT_3 = "electroLimit3";
    public static final String ELECTRO_TARIFF_4 = "electroTariff4";
    public static final String WATER_TARIFF = "waterTariff";
    public static final String HOT_WATER_TARIFF = "hotWaterTariff";
    public static final String HEATING_TARIFF = "heatingTariff";
    public static final String GAS_TARIFF = "gasTariff";
    public static final String SEWAGE_TARIFF = "sewageTariff";
    public static final String FLAT_TARIFF = "flatTariff";
    public static final String GARBAGE_TARIFF = "garbageTariff";
    public static final String ELECTRO_PAYMENT_SET = "electroPaymentSet";
    public static final String WATER_PAYMENT_SET = "waterPaymentSet";
    public static final String HOT_WATER_PAYMENT_SET = "hotWaterPaymentSet";
    public static final String HEATING_PAYMENT_SET = "heatingPaymentSet";
    public static final String GAS_PAYMENT_SET = "gasPaymentSet";
    public static final String SEWAGE_PAYMENT_SET = "sewagePaymentSet";
    public static final String FLAT_PAYMENT_SET = "flatPaymentSet";
    public static final String GARBAGE_PAYMENT_SET = "garbagePaymentSet";
    public static final String ELECTRO_PAYMENT_BY_TARIFF = "electroPaymentByTariff";
    public static final String WATER_PAYMENT_BY_TARIFF = "waterPaymentByTariff";
    public static final String HOT_WATER_PAYMENT_BY_TARIFF = "hotWaterPaymentByTariff";
    public static final String HEATING_PAYMENT_BY_TARIFF = "heatingPaymentByTariff";
    public static final String GAS_PAYMENT_BY_TARIFF = "gasPaymentByTariff";
    public static final String SEWAGE_PAYMENT_BY_TARIFF = "sewagePaymentByTariff";
    public static final String FLAT_PAYMENT_BY_TARIFF = "flatPaymentByTariff";
    public static final String GARBAGE_PAYMENT_BY_TARIFF = "garbagePaymentByTariff";
    public static final String ELECTRO_MUST_PAY = "electroMustPay";
    public static final String WATER_MUST_PAY = "waterMustPay";
    public static final String HOT_WATER_MUST_PAY = "hotWaterMustPay";
    public static final String HEATING_MUST_PAY = "heatingMustPay";
    public static final String GAS_MUST_PAY = "gasMustPay";
    public static final String SEWAGE_MUST_PAY = "sewageMustPay";
    public static final String FLAT_MUST_PAY = "flatMustPay";
    public static final String GARBAGE_MUST_PAY = "garbageMustPay";
    public static final String ELECTRO_START = "electroStart";
    public static final String ELECTRO_END = "electroEnd";
    public static final String K_WATT_CONSUMED = "kWattConsumed";
    public static final String WATER_START = "waterStart";
    public static final String WATER_END = "waterEnd";
    public static final String M_3_CONSUMED = "m3consumed";
    public static final String HOT_WATER_START = "hotWaterStart";
    public static final String HOT_WATER_END = "hotWaterEnd";
    public static final String HOT_M_3_CONSUMED = "hotM3consumed";
    public static final String HEATING_START = "heatingStart";
    public static final String HEATING_END = "heatingEnd";
    public static final String HEATING_CONSUMED = "heatingConsumed";
    public static final String GAS_START = "gasStart";
    public static final String GAS_END = "gasEnd";
    public static final String GAS_M_3_CONSUMED = "gasM3consumed";
    public static final String SEWAGE_START = "sewageStart";
    public static final String SEWAGE_END = "sewageEnd";
    public static final String SEWAGE_M_3_CONSUMED = "sewageM3consumed";
    public static final String PAYMENT_FOR_ELECTRICITY = "paymentForElectricity";
    public static final String PAYMENT_FOR_WATER = "paymentForWater";
    public static final String PAYMENT_FOR_HOT_WATER = "paymentForHotWater";
    public static final String PAYMENT_FOR_HEATING = "paymentForHeating";
    public static final String PAYMENT_FOR_GAS = "paymentForGas";
    public static final String PAYMENT_FOR_SEWAGE = "paymentForSewage";
    public static final String PAYMENT_FOR_FLAT = "paymentForFlat";
    public static final String PAYMENT_FOR_GARBAGE = "paymentForGarbage";
    public static final String TOTAL = "total";
    public static final String ROUND = "round";

    static {
        try {
            /* This is kind of a crutch for making this code usable both from intellij idea and from the executable jar.
             * The file path of the payments database is selected by means of checking if the working directory path
             * ends with "target" because *\target is the directory containing the jar file and the
             * payments database for the jar file in the *target\classes subdirectory.
             * If the path doesn't end with *\target it means the program works from the ides project */
            dbFile = new File(".").getCanonicalPath().endsWith("target") ?
                    "classes" + File.separator + DB_NAME :
                    ("src" + File.separator + "main" + File.separator + "resources" + File.separator + DB_NAME);
            connectionString = "jdbc:sqlite:" + dbFile;
        } catch (IOException e) {
            Alerts.alertInfo(getAlertText("paymentsDataSourcePathErrorTitle"),
                    getAlertText("paymentsDataSourcePathErrorMessage"));
            e.printStackTrace();
        }
        compileStringCreateTable();

    }

    /* A singleton's anti-reproduction construction */
    private PaymentsDataSource() {
    }

    public static PaymentsDataSource getInstance() {
        return instance;
    }

    public void initializePaymentsDBTable(boolean openAndClose) {
        try {
            if (openAndClose) {
                open();
            }

            // Checking if the payments table in the database exists and creating it if not
            statement.execute(createTableStatement);

            // Checking if the view of the payment markers exists in the payments database and creating it if not
            statement.execute("CREATE VIEW IF NOT EXISTS " + VIEW_NAME + " AS SELECT " +
                    ID + ", " + NAME +
                    ", " + FULL_DESCRIPTION + " AS " + FULL_DESCRIPTION +
                    " FROM " + TABLE_PAYMENTS);
        } catch (SQLException e) {
            Alerts.alertInfo(getAlertText("paymentsDataSourceDBConnectionErrorTitle"),
                    getAlertText("paymentsDataSourceDBConnectionErrorMessage"));
        } finally {
            if (openAndClose) {
                close();
            }
        }
    }

    /* This method returns the list of payments markers (payment id, name, and fullDescription)
    of the payments stored in the database. Names and full descriptions of markers are shown in the list view
    in the program window. In case of SQLException it returns null */
    public ObservableList<PaymentMarker> getPaymentMarkers(boolean openAndClose) {
        PaymentMarker paymentMarker;
        ResultSet results;

        ObservableList<PaymentMarker> paymentMarkers = FXCollections.observableArrayList();
        // This list will be returned from this method only if no SQLException occur,
        // otherwise null will be returned instead

        if (openAndClose) {
            open();
        }
        try {
            results = statement.executeQuery("SELECT * FROM " + VIEW_NAME);
            while (results.next()) {
                paymentMarker = new PaymentMarker(results.getInt(1),
                        results.getString(2), results.getString(3));
                paymentMarkers.add(paymentMarker);
            }
            results.close();
            return paymentMarkers;
        } catch (SQLException e) {
            Alerts.alertInfo(getAlertText("paymentsDataSourceDBPaymentMarkersErrorTitle"),
                    getAlertText("paymentsDataSourceDBPaymentMarkersErrorMessage"));
            return null;
        } finally {
            if (openAndClose) {
                close();
            }
        }
    }
    /* This method receives a payment and either inserts a new entry into the payments database table
    or updates the existing entry depending on whether there's entry with the id of received payment
    is already in the database. The fields of the table entry are populated from the corresponding fields
    of the received payment */

    public void storePayment(Payment payment, boolean openAndClose) {
        if (payment == null) {
            Alerts.alertInfo(getAlertText("paymentsDataSourceNullForStoringTitle"),
                    getAlertText("paymentsDataSourceNullForStoringMessage"));
            return;
        }
        if (openAndClose) {
            open();
        }
        /* Checking if the payment is already stored in the database to define
         * to whether insert or update it further*/
        String checkingForThisPaymentQuery = "SELECT COUNT(" + ID + ") FROM " + TABLE_PAYMENTS +
                " WHERE " + ID + " = " + payment.getId();
        try {
            ResultSet results = statement.executeQuery(checkingForThisPaymentQuery);
            int idIsThere = results.getInt(1);
            if (idIsThere == 0) {
                String insertStatement = compileInsertStatement(payment);
                statement.executeUpdate(insertStatement);
            } else {
                String updateStatement = compileUpdateStatement(payment);
                statement.executeUpdate(updateStatement);
            }
        } catch (SQLException e) {
            Alerts.alertInfo(getAlertText("paymentsDataSourceStoringToDBErrorTitle"),
                    getAlertText("paymentsDataSourceStoringToDBErrorMessage"));
        } finally {
            if (openAndClose) {
                close();
            }
        }
    }

    public Payment getPayment(int id, boolean openAndClose) {
        if (openAndClose) {
            open();
        }
        Payment payment = null;
        try {
            ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_PAYMENTS + " WHERE " + ID + " = " +
                    id);
            while (results.next()) {
                payment = new Payment(id);
                payment.setName(results.getString(NAME));
                payment.setFullDescription(results.getString(FULL_DESCRIPTION));
                payment.setYear(results.getInt(YEAR));
                payment.setMonth(results.getInt(MONTH));
                payment.setElectroTariff1(results.getDouble(ELECTRO_TARIFF_1));
                payment.setElectroLimit1(results.getDouble(ELECTRO_LIMIT_1));
                payment.setElectroTariff2(results.getDouble(ELECTRO_TARIFF_2));
                payment.setElectroLimit2(results.getDouble(ELECTRO_LIMIT_2));
                payment.setElectroTariff3(results.getDouble(ELECTRO_TARIFF_3));
                payment.setElectroLimit3(results.getDouble(ELECTRO_LIMIT_3));
                payment.setElectroTariff4(results.getDouble(ELECTRO_TARIFF_4));
                payment.setWaterTariff(results.getDouble(WATER_TARIFF));
                payment.setHotWaterTariff(results.getDouble(HOT_WATER_TARIFF));
                payment.setHeatingTariff(results.getDouble(HEATING_TARIFF));
                payment.setGasTariff(results.getDouble(GAS_TARIFF));
                payment.setSewageTariff(results.getDouble(SEWAGE_TARIFF));
                payment.setFlatTariff(results.getDouble(FLAT_TARIFF));
                payment.setGarbageTariff(results.getDouble(GARBAGE_TARIFF));
                payment.setElectroPaymentSet(results.getInt(ELECTRO_PAYMENT_SET) == 1);
                payment.setWaterPaymentSet(results.getInt(WATER_PAYMENT_SET) == 1);
                payment.setHotWaterPaymentSet(results.getInt(HOT_WATER_PAYMENT_SET) == 1);
                payment.setHeatingPaymentSet(results.getInt(HEATING_PAYMENT_SET) == 1);
                payment.setGasPaymentSet(results.getInt(GAS_PAYMENT_SET) == 1);
                payment.setSewagePaymentSet(results.getInt(SEWAGE_PAYMENT_SET) == 1);
                payment.setFlatPaymentSet(results.getInt(FLAT_PAYMENT_SET) == 1);
                payment.setGarbagePaymentSet(results.getInt(GARBAGE_PAYMENT_SET) == 1);
                payment.setElectroPaymentByTariff(results.getInt(ELECTRO_PAYMENT_BY_TARIFF) == 1);
                payment.setWaterPaymentByTariff(results.getInt(WATER_PAYMENT_BY_TARIFF) == 1);
                payment.setHotWaterPaymentByTariff(results.getInt(HOT_WATER_PAYMENT_BY_TARIFF) == 1);
                payment.setHeatingPaymentByTariff(results.getInt(HEATING_PAYMENT_BY_TARIFF) == 1);
                payment.setGasPaymentByTariff(results.getInt(GAS_PAYMENT_BY_TARIFF) == 1);
                payment.setSewagePaymentByTariff(results.getInt(SEWAGE_PAYMENT_BY_TARIFF) == 1);
                payment.setFlatPaymentByTariff(results.getInt(FLAT_PAYMENT_BY_TARIFF) == 1);
                payment.setGarbagePaymentByTariff(results.getInt(GARBAGE_PAYMENT_BY_TARIFF) == 1);
                payment.setElectroMustPay(results.getDouble(ELECTRO_MUST_PAY));
                payment.setWaterMustPay(results.getDouble(WATER_MUST_PAY));
                payment.setHotWaterMustPay(results.getDouble(HOT_WATER_MUST_PAY));
                payment.setHeatingMustPay(results.getDouble(HEATING_MUST_PAY));
                payment.setGasMustPay(results.getDouble(GAS_MUST_PAY));
                payment.setSewageMustPay(results.getDouble(SEWAGE_MUST_PAY));
                payment.setFlatMustPay(results.getDouble(FLAT_MUST_PAY));
                payment.setGarbageMustPay(results.getDouble(GARBAGE_MUST_PAY));
                payment.setElectroStart(results.getDouble(ELECTRO_START));
                payment.setElectroEnd(results.getDouble(ELECTRO_END));
                payment.setkWattConsumed(results.getDouble(K_WATT_CONSUMED));
                payment.setWaterStart(results.getDouble(WATER_START));
                payment.setWaterEnd(results.getDouble(WATER_END));
                payment.setM3consumed(results.getDouble(M_3_CONSUMED));
                payment.setHotWaterStart(results.getDouble(HOT_WATER_START));
                payment.setHotWaterEnd(results.getDouble(HOT_WATER_END));
                payment.setHotM3consumed(results.getDouble(HOT_M_3_CONSUMED));
                payment.setHeatingStart(results.getDouble(HEATING_START));
                payment.setHeatingEnd(results.getDouble(HEATING_END));
                payment.setHeatingConsumed(results.getDouble(HEATING_CONSUMED));
                payment.setGasStart(results.getDouble(GAS_START));
                payment.setGasEnd(results.getDouble(GAS_END));
                payment.setGasM3consumed(results.getDouble(GAS_M_3_CONSUMED));
                payment.setSewageStart(results.getDouble(SEWAGE_START));
                payment.setSewageEnd(results.getDouble(SEWAGE_END));
                payment.setSewageM3consumed(results.getDouble(SEWAGE_M_3_CONSUMED));
                payment.setPaymentForElectricity(results.getDouble(PAYMENT_FOR_ELECTRICITY));
                payment.setPaymentForWater(results.getDouble(PAYMENT_FOR_WATER));
                payment.setPaymentForHotWater(results.getDouble(PAYMENT_FOR_HOT_WATER));
                payment.setPaymentForHeating(results.getDouble(PAYMENT_FOR_HEATING));
                payment.setPaymentForGas(results.getDouble(PAYMENT_FOR_GAS));
                payment.setPaymentForSewage(results.getDouble(PAYMENT_FOR_SEWAGE));
                payment.setPaymentForFlat(results.getDouble(PAYMENT_FOR_FLAT));
                payment.setPaymentForGarbage(results.getDouble(PAYMENT_FOR_GARBAGE));
                payment.setTotal(results.getDouble(TOTAL));
                payment.setRound(results.getInt(ROUND) == 1);
            }
        } catch (SQLException e) {
            Alerts.alertInfo(getAlertText("paymentsDataSourceReadingSQLExceptionTitle"),
                    getAlertText("paymentsDataSourceReadingSQLExceptionMessage"));
        } finally {
            if (openAndClose) {
                close();
            }
        }
        if (payment == null) {
            Alerts.alertInfo(getAlertText("paymentsDataSourceNullPaymentFromDBTitle"),
                    getAlertText("paymentsDataSourceNullPaymentFromDBMessage"));
        }
        return payment;
    }

    public void deletePayment(int id, boolean openAndClose) {
        if (openAndClose) {
            open();
        }
        String deleteStatement = "DELETE FROM " + TABLE_PAYMENTS + " WHERE " + ID + " = " + id;
        try {
            statement.execute(deleteStatement);
        } catch (SQLException e) {
            Alerts.alertInfo(getAlertText("deletePaymentFromDBErrorTitle"),
                    getAlertText("deletePaymentFromDBErrorMessage"));
        } finally {
            if (openAndClose) {
                close();
            }
        }
    }

    private String compileInsertStatement(Payment payment) {
        return "INSERT INTO " + TABLE_PAYMENTS + " VALUES (" +
                payment.getId() + ", '" +
                payment.getName() + "', '" +
                payment.getFullDescription() + "', " +
                payment.getYear() + ", " +
                payment.getMonth() + ", " +
                payment.getElectroTariff1() + ", " +
                payment.getElectroLimit1() + ", " +
                payment.getElectroTariff2() + ", " +
                payment.getElectroLimit2() + ", " +
                payment.getElectroTariff3() + ", " +
                payment.getElectroLimit3() + ", " +
                payment.getElectroTariff4() + ", " +
                payment.getWaterTariff() + ", " +
                payment.getHotWaterTariff() + ", " +
                payment.getHeatingTariff() + ", " +
                payment.getGasTariff() + ", " +
                payment.getSewageTariff() + ", " +
                payment.getFlatTariff() + ", " +
                payment.getGarbageTariff() + ", " +
                (payment.isElectroPaymentSet() ? 1 : 0) + ", " +
                (payment.isWaterPaymentSet() ? 1 : 0) + ", " +
                (payment.isHotWaterPaymentSet() ? 1 : 0) + ", " +
                (payment.isHeatingPaymentSet() ? 1 : 0) + ", " +
                (payment.isGasPaymentSet() ? 1 : 0) + ", " +
                (payment.isSewagePaymentSet() ? 1 : 0) + ", " +
                (payment.isFlatPaymentSet() ? 1 : 0) + ", " +
                (payment.isGarbagePaymentSet() ? 1 : 0) + ", " +
                (payment.isElectroPaymentByTariff() ? 1 : 0) + ", " +
                (payment.isWaterPaymentByTariff() ? 1 : 0) + ", " +
                (payment.isHotWaterPaymentByTariff() ? 1 : 0) + ", " +
                (payment.isHeatingPaymentByTariff() ? 1 : 0) + ", " +
                (payment.isGasPaymentByTariff() ? 1 : 0) + ", " +
                (payment.isSewagePaymentByTariff() ? 1 : 0) + ", " +
                (payment.isFlatPaymentByTariff() ? 1 : 0) + ", " +
                (payment.isGarbagePaymentByTariff() ? 1 : 0) + ", " +
                payment.getElectroMustPay() + ", " +
                payment.getWaterMustPay() + ", " +
                payment.getHotWaterMustPay() + ", " +
                payment.getHeatingMustPay() + ", " +
                payment.getGasMustPay() + ", " +
                payment.getSewageMustPay() + ", " +
                payment.getFlatMustPay() + ", " +
                payment.getGarbageMustPay() + ", " +
                payment.getElectroStart() + ", " +
                payment.getElectroEnd() + ", " +
                payment.getkWattConsumed() + ", " +
                payment.getWaterStart() + ", " +
                payment.getWaterEnd() + ", " +
                payment.getM3consumed() + ", " +
                payment.getHotWaterStart() + ", " +
                payment.getHotWaterEnd() + ", " +
                payment.getHotM3consumed() + ", " +
                payment.getHeatingStart() + ", " +
                payment.getHeatingEnd() + ", " +
                payment.getHeatingConsumed() + ", " +
                payment.getGasStart() + ", " +
                payment.getGasEnd() + ", " +
                payment.getGasM3consumed() + ", " +
                payment.getSewageStart() + ", " +
                payment.getSewageEnd() + ", " +
                payment.getSewageM3consumed() + ", " +
                payment.getPaymentForElectricity() + ", " +
                payment.getPaymentForWater() + ", " +
                payment.getPaymentForHotWater() + ", " +
                payment.getPaymentForHeating() + ", " +
                payment.getPaymentForGas() + ", " +
                payment.getPaymentForSewage() + ", " +
                payment.getPaymentForFlat() + ", " +
                payment.getPaymentForGarbage() + ", " +
                payment.getTotal() + ", " +
                (payment.isRound() ? 1 : 0) +
                ")";
    }

    private String compileUpdateStatement(Payment payment) {
        return "UPDATE " + TABLE_PAYMENTS + " SET " +
                NAME + " = '" + payment.getName() + "', " +
                FULL_DESCRIPTION + " = '" + payment.getFullDescription() + "', " +
                YEAR + " = " + payment.getYear() + ", " +
                MONTH + " = " + payment.getMonth() + ", " +
                ELECTRO_TARIFF_1 + " = " + payment.getElectroTariff1() + ", " +
                ELECTRO_LIMIT_1 + " = " + payment.getElectroLimit1() + ", " +
                ELECTRO_TARIFF_2 + " = " + payment.getElectroTariff2() + ", " +
                ELECTRO_LIMIT_2 + " = " + payment.getElectroLimit2() + ", " +
                ELECTRO_TARIFF_3 + " = " + payment.getElectroTariff3() + ", " +
                ELECTRO_LIMIT_3 + " = " + payment.getElectroLimit3() + ", " +
                ELECTRO_TARIFF_4 + " = " + payment.getElectroTariff4() + ", " +
                WATER_TARIFF + " = " + payment.getWaterTariff() + ", " +
                HOT_WATER_TARIFF + " = " + payment.getHotWaterTariff() + ", " +
                HEATING_TARIFF + " = " + payment.getHeatingTariff() + ", " +
                GAS_TARIFF + " = " + payment.getGasTariff() + ", " +
                SEWAGE_TARIFF + " = " + payment.getSewageTariff() + ", " +
                FLAT_TARIFF + " = " + payment.getFlatTariff() + ", " +
                GARBAGE_TARIFF + " = " + payment.getGarbageTariff() + ", " +
                ELECTRO_PAYMENT_SET + " = " + (payment.isElectroPaymentSet() ? 1 : 0) + ", " +
                WATER_PAYMENT_SET + " = " + (payment.isWaterPaymentSet() ? 1 : 0) + ", " +
                HOT_WATER_PAYMENT_SET + " = " + (payment.isHotWaterPaymentSet() ? 1 : 0) + ", " +
                HEATING_PAYMENT_SET + " = " + (payment.isHeatingPaymentSet() ? 1 : 0) + ", " +
                GAS_PAYMENT_SET + " = " + (payment.isGasPaymentSet() ? 1 : 0) + ", " +
                SEWAGE_PAYMENT_SET + " = " + (payment.isSewagePaymentSet() ? 1 : 0) + ", " +
                FLAT_PAYMENT_SET + " = " + (payment.isFlatPaymentSet() ? 1 : 0) + ", " +
                GARBAGE_PAYMENT_SET + " = " + (payment.isGarbagePaymentSet() ? 1 : 0) + ", " +
                ELECTRO_PAYMENT_BY_TARIFF + " = " + (payment.isElectroPaymentByTariff() ? 1 : 0) + ", " +
                WATER_PAYMENT_BY_TARIFF + " = " + (payment.isWaterPaymentByTariff() ? 1 : 0) + ", " +
                HOT_WATER_PAYMENT_BY_TARIFF + " = " + (payment.isHotWaterPaymentByTariff() ? 1 : 0) + ", " +
                HEATING_PAYMENT_BY_TARIFF + " = " + (payment.isHeatingPaymentByTariff() ? 1 : 0) + ", " +
                GAS_PAYMENT_BY_TARIFF + " = " + (payment.isGasPaymentByTariff() ? 1 : 0) + ", " +
                SEWAGE_PAYMENT_BY_TARIFF + " = " + (payment.isSewagePaymentByTariff() ? 1 : 0) + ", " +
                FLAT_PAYMENT_BY_TARIFF + " = " + (payment.isFlatPaymentByTariff() ? 1 : 0) + ", " +
                GARBAGE_PAYMENT_BY_TARIFF + " = " + (payment.isGarbagePaymentByTariff() ? 1 : 0) + ", " +
                ELECTRO_MUST_PAY + " = " + payment.getElectroMustPay() + ", " +
                WATER_MUST_PAY + " = " + payment.getWaterMustPay() + ", " +
                HOT_WATER_MUST_PAY + " = " + payment.getHotWaterMustPay() + ", " +
                HEATING_MUST_PAY + " = " + payment.getHeatingMustPay() + ", " +
                GAS_MUST_PAY + " = " + payment.getGasMustPay() + ", " +
                SEWAGE_MUST_PAY + " = " + payment.getSewageMustPay() + ", " +
                FLAT_MUST_PAY + " = " + payment.getFlatMustPay() + ", " +
                GARBAGE_MUST_PAY + " = " + payment.getGarbageMustPay() + ", " +
                ELECTRO_START + " = " + payment.getElectroStart() + ", " +
                ELECTRO_END + " = " + payment.getElectroEnd() + ", " +
                K_WATT_CONSUMED + " = " + payment.getkWattConsumed() + ", " +
                WATER_START + " = " + payment.getWaterStart() + ", " +
                WATER_END + " = " + payment.getWaterEnd() + ", " +
                M_3_CONSUMED + " = " + payment.getM3consumed() + ", " +
                HOT_WATER_START + " = " + payment.getHotWaterStart() + ", " +
                HOT_WATER_END + " = " + payment.getHotWaterEnd() + ", " +
                HOT_M_3_CONSUMED + " = " + payment.getHotM3consumed() + ", " +
                HEATING_START + " = " + payment.getHeatingStart() + ", " +
                HEATING_END + " = " + payment.getHeatingEnd() + ", " +
                HEATING_CONSUMED + " = " + payment.getHeatingConsumed() + ", " +
                GAS_START + " = " + payment.getGasStart() + ", " +
                GAS_END + " = " + payment.getGasEnd() + ", " +
                GAS_M_3_CONSUMED + " = " + payment.getGasM3consumed() + ", " +
                SEWAGE_START + " = " + payment.getSewageStart() + ", " +
                SEWAGE_END + " = " + payment.getSewageEnd() + ", " +
                SEWAGE_M_3_CONSUMED + " = " + payment.getSewageM3consumed() + ", " +
                PAYMENT_FOR_ELECTRICITY + " = " + payment.getPaymentForElectricity() + ", " +
                PAYMENT_FOR_WATER + " = " + payment.getPaymentForWater() + ", " +
                PAYMENT_FOR_HOT_WATER + " = " + payment.getPaymentForHotWater() + ", " +
                PAYMENT_FOR_HEATING + " = " + payment.getPaymentForHeating() + ", " +
                PAYMENT_FOR_GAS + " = " + payment.getPaymentForGas() + ", " +
                PAYMENT_FOR_SEWAGE + " = " + payment.getPaymentForSewage() + ", " +
                PAYMENT_FOR_FLAT + " = " + payment.getPaymentForFlat() + ", " +
                PAYMENT_FOR_GARBAGE + " = " + payment.getPaymentForGarbage() + ", " +
                TOTAL + " = " + payment.getTotal() + ", " +
                ROUND + " = " + (payment.isRound() ? 1 : 0) +
                " WHERE " + ID + " = " + payment.getId();
    }

    /* This method compiles the string for the statement for creating the payments table */
    private static void compileStringCreateTable() {
        createTableStatement = "CREATE TABLE IF NOT EXISTS " + TABLE_PAYMENTS + " (" +
                ID + " integer, " +
                NAME + " text, " +
                FULL_DESCRIPTION + " text, " +
                YEAR + " integer, " +
                MONTH + " integer, " +
                ELECTRO_TARIFF_1 + " real, " +
                ELECTRO_LIMIT_1 + " real, " +
                ELECTRO_TARIFF_2 + " real, " +
                ELECTRO_LIMIT_2 + " real, " +
                ELECTRO_TARIFF_3 + " real, " +
                ELECTRO_LIMIT_3 + " real, " +
                ELECTRO_TARIFF_4 + " real, " +
                WATER_TARIFF + " real, " +
                HOT_WATER_TARIFF + " real, " +
                HEATING_TARIFF + " real, " +
                GAS_TARIFF + " real, " +
                SEWAGE_TARIFF + " real, " +
                FLAT_TARIFF + " real, " +
                GARBAGE_TARIFF + " real, " +
                ELECTRO_PAYMENT_SET + " integer, " +
                WATER_PAYMENT_SET + " integer, " +
                HOT_WATER_PAYMENT_SET + " integer, " +
                HEATING_PAYMENT_SET + " integer, " +
                GAS_PAYMENT_SET + " integer, " +
                SEWAGE_PAYMENT_SET + " integer, " +
                FLAT_PAYMENT_SET + " integer, " +
                GARBAGE_PAYMENT_SET + " integer, " +
                ELECTRO_PAYMENT_BY_TARIFF + " integer, " +
                WATER_PAYMENT_BY_TARIFF + " integer, " +
                HOT_WATER_PAYMENT_BY_TARIFF + " integer, " +
                HEATING_PAYMENT_BY_TARIFF + " integer, " +
                GAS_PAYMENT_BY_TARIFF + " integer, " +
                SEWAGE_PAYMENT_BY_TARIFF + " integer, " +
                FLAT_PAYMENT_BY_TARIFF + " integer, " +
                GARBAGE_PAYMENT_BY_TARIFF + " integer, " +
                ELECTRO_MUST_PAY + " real, " +
                WATER_MUST_PAY + " real, " +
                HOT_WATER_MUST_PAY + " real, " +
                HEATING_MUST_PAY + " real, " +
                GAS_MUST_PAY + " real, " +
                SEWAGE_MUST_PAY + " real, " +
                FLAT_MUST_PAY + " real, " +
                GARBAGE_MUST_PAY + " real, " +
                ELECTRO_START + " real, " +
                ELECTRO_END + " real, " +
                K_WATT_CONSUMED + " real, " +
                WATER_START + " real, " +
                WATER_END + " real, " +
                M_3_CONSUMED + " real, " +
                HOT_WATER_START + " real, " +
                HOT_WATER_END + " real, " +
                HOT_M_3_CONSUMED + " real, " +
                HEATING_START + " real, " +
                HEATING_END + " real, " +
                HEATING_CONSUMED + " real, " +
                GAS_START + " real, " +
                GAS_END + " real, " +
                GAS_M_3_CONSUMED + " real, " +
                SEWAGE_START + " real, " +
                SEWAGE_END + " real, " +
                SEWAGE_M_3_CONSUMED + " real, " +
                PAYMENT_FOR_ELECTRICITY + " real, " +
                PAYMENT_FOR_WATER + " real, " +
                PAYMENT_FOR_HOT_WATER + " real, " +
                PAYMENT_FOR_HEATING + " real, " +
                PAYMENT_FOR_GAS + " real, " +
                PAYMENT_FOR_SEWAGE + " real, " +
                PAYMENT_FOR_FLAT + " real, " +
                PAYMENT_FOR_GARBAGE + " real, " +
                TOTAL + " real, " +
                ROUND + " integer, " +
                "PRIMARY KEY (" + ID + ") " +
                ")";
    }

    /*Opening both the connection with the payments database and the statement*/
    public void open() {

        try {
            conn = DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            Alerts.alertInfo(getAlertText("paymentsDataSourceConnectingToDBErrorTitle"),
                    getAlertText("paymentsDataSourceConnectingToDBErrorMessage"));
        }
        try {
            statement = conn.createStatement();
        } catch (SQLException e) {
            Alerts.alertInfo(getAlertText("paymentsDataSourceSQLStatementErrorTitle"),
                    getAlertText("paymentsDataSourceSQLStatementErrorMessage"));
        }
    }

    /*Closing both the connection with the payments database and the statement*/
    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                if (statement != null) {
                    statement.close();
                }
                conn.close();
            }
        } catch (SQLException e) {
            /* We shouldn't actualy care about exceptions when closing the connection and the statement, but still */
            Alerts.alertInfo(getAlertText(getAlertText("paymentsDataSourceClosingDBErrorTitle")),
                    getAlertText("paymentsDataSourceClosingDBErrorMessage"));
        }
    }

    public Payment getRandomPayment(int id) {
        Random random = new Random();
        Payment payment = new Payment(id);
        payment.setName("Test");
        payment.setFullDescription("TestFullDescription");
        payment.setYear(random.nextInt());
        payment.setMonth(random.nextInt());
        payment.setElectroTariff1(random.nextDouble());
        payment.setElectroLimit1(random.nextDouble());
        payment.setElectroTariff2(random.nextDouble());
        payment.setElectroLimit2(random.nextDouble());
        payment.setElectroTariff3(random.nextDouble());
        payment.setElectroLimit3(random.nextDouble());
        payment.setElectroTariff4(random.nextDouble());
        payment.setWaterTariff(random.nextDouble());
        payment.setHotWaterTariff(random.nextDouble());
        payment.setHeatingTariff(random.nextDouble());
        payment.setGasTariff(random.nextDouble());
        payment.setSewageTariff(random.nextDouble());
        payment.setFlatTariff(random.nextDouble());
        payment.setGarbageTariff(random.nextDouble());
        payment.setElectroPaymentSet(random.nextBoolean());
        payment.setWaterPaymentSet(random.nextBoolean());
        payment.setHotWaterPaymentSet(random.nextBoolean());
        payment.setHeatingPaymentSet(random.nextBoolean());
        payment.setGasPaymentSet(random.nextBoolean());
        payment.setSewagePaymentSet(random.nextBoolean());
        payment.setFlatPaymentSet(random.nextBoolean());
        payment.setGarbagePaymentSet(random.nextBoolean());
        payment.setElectroPaymentByTariff(random.nextBoolean());
        payment.setWaterPaymentByTariff(random.nextBoolean());
        payment.setHotWaterPaymentByTariff(random.nextBoolean());
        payment.setHeatingPaymentByTariff(random.nextBoolean());
        payment.setGasPaymentByTariff(random.nextBoolean());
        payment.setSewagePaymentByTariff(random.nextBoolean());
        payment.setFlatPaymentByTariff(random.nextBoolean());
        payment.setGarbagePaymentByTariff(random.nextBoolean());
        payment.setElectroMustPay(random.nextDouble());
        payment.setWaterMustPay(random.nextDouble());
        payment.setHotWaterMustPay(random.nextDouble());
        payment.setHeatingMustPay(random.nextDouble());
        payment.setGasMustPay(random.nextDouble());
        payment.setSewageMustPay(random.nextDouble());
        payment.setFlatMustPay(random.nextDouble());
        payment.setGarbageMustPay(random.nextDouble());
        payment.setElectroStart(random.nextDouble());
        payment.setElectroEnd(random.nextDouble());
        payment.setkWattConsumed(random.nextDouble());
        payment.setWaterStart(random.nextDouble());
        payment.setWaterEnd(random.nextDouble());
        payment.setM3consumed(random.nextDouble());
        payment.setHotWaterStart(random.nextDouble());
        payment.setHotWaterEnd(random.nextDouble());
        payment.setHotM3consumed(random.nextDouble());
        payment.setHeatingStart(random.nextDouble());
        payment.setHeatingEnd(random.nextDouble());
        payment.setHeatingConsumed(random.nextDouble());
        payment.setGasStart(random.nextDouble());
        payment.setGasEnd(random.nextDouble());
        payment.setGasM3consumed(random.nextDouble());
        payment.setSewageStart(random.nextDouble());
        payment.setSewageEnd(random.nextDouble());
        payment.setSewageM3consumed(random.nextDouble());
        payment.setPaymentForElectricity(random.nextDouble());
        payment.setPaymentForWater(random.nextDouble());
        payment.setPaymentForHotWater(random.nextDouble());
        payment.setPaymentForHeating(random.nextDouble());
        payment.setPaymentForGas(random.nextDouble());
        payment.setPaymentForSewage(random.nextDouble());
        payment.setPaymentForFlat(random.nextDouble());
        payment.setPaymentForGarbage(random.nextDouble());
        payment.setTotal(random.nextDouble());
        payment.setRound(random.nextBoolean());
        
        return payment;
    }

    public static String getDbName() {
        return DB_NAME;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public String getTablePaymentsName() {
        return TABLE_PAYMENTS;
    }

    public String getViewName() {
        return VIEW_NAME;
    }

}
