package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    private Statement statement;
    public static final String DB_NAME = "payments.db";
    private String connectionString;
    private String dbFile;

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
    public static final String HEATING_TARIFF = "heatingTariff";
    public static final String HEATING_TARIFF_TO_PRINT = "heatingTariffToPrint";
    public static final String WATER_TARIFF = "waterTariff";
    public static final String WATER_TARIFF_TO_PRINT = "waterTariffToPrint";
    public static final String FLAT_TARIFF = "flatTariff";
    public static final String FLAT_TARIFF_TO_PRINT = "flatTariffToPrint";
    public static final String GARBAGE_TARIFF = "garbageTariff";
    public static final String GARBAGE_TARIFF_TO_PRINT = "garbageTariffToPrint";
    public static final String ELECTRO_MUST_PAY = "electroMustPay";
    public static final String ELECTRO_PAYMENT_SET = "electroPaymentSet";
    public static final String HEATING_MUST_PAY = "heatingMustPay";
    public static final String HEATING_PAYMENT_SET = "heatingPaymentSet";
    public static final String WATER_MUST_PAY = "waterMustPay";
    public static final String WATER_PAYMENT_SET = "waterPaymentSet";
    public static final String FLAT_MUST_PAY = "flatMustPay";
    public static final String FLAT_PAYMENT_SET = "flatPaymentSet";
    public static final String GARBAGE_MUST_PAY = "garbageMustPay";
    public static final String GARBAGE_PAYMENT_SET = "garbagePaymentSet";
    public static final String ELECTRO_START = "electroStart";
    public static final String ELECTRO_END = "electroEnd";
    public static final String K_WATT_CONSUMED = "kWattConsumed";
    public static final String WATER_START = "waterStart";
    public static final String WATER_END = "waterEnd";
    public static final String M_3_CONSUMED = "m3consumed";
    public static final String PAYMENT_FOR_ELECTRICITY = "paymentForElectricity";
    public static final String PAYMENT_FOR_HEATING = "paymentForHeating";
    public static final String PAYMENT_FOR_WATER = "paymentForWater";
    public static final String PAYMENT_FOR_FLAT = "paymentForFlat";
    public static final String PAYMENT_FOR_GARBAGE = "paymentForGarbage";
    public static final String TOTAL = "total";

    public PaymentsDataSource() {
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
            Alerts.alertInfo("Ошибка обработки файла базы данных платежей",
                    "Не удалось получить путь файла базы данных тарифов");
            e.printStackTrace();
        }
        compileStringCreateTable();
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
            Alerts.alertInfo("Ошибка доступа к базе данных",
                    "Ошибка при подключении к базе данных\n" +
                            "(При попытке создания таблицы платежей initializePaymentsDBTable() )");
        } finally {
            if (openAndClose) {
                close();
            }
        }
    }

    // This method returns the list of payments markers (payment id, name, and fullDescription)
    // of the payments stored in the database. Names and full descriptions of markers are shown in the list view
    // in the program window.
    // In case of SQLException it returns null
    public ObservableList<PaymentMarker> getPaymentMarkers() {
        PaymentMarker paymentMarker;
        ResultSet results;

        ObservableList<PaymentMarker> paymentMarkers = FXCollections.observableArrayList();
        // This list will be returned from this method only if no SQLException occur, itherwise null will be returned instead

        open();
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
            Alerts.alertInfo("Ошибка базы данных", "Ошибка при попытке считывания маркеров платежей\n" +
                    "в список маркеров");
            return null;
        } finally {
            close();
        }
    }
    /* This method receives a payment and either inserts a new entry into the payments database table
    or updates the existing entry depending on whether there's entry with the id of received payment
    is already in the database. The fields of the table entry are populated from the corresponding fields
    of the received payment */

    public void storePayment(Payment payment, boolean openAndClose) {
        if (payment == null) {
            Alerts.alertInfo("Ошибка сохранения платежа",
                    "Для сохранения в базе данных был передан не платеж, а null");
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
//                System.out.println(insertStatement); // print out the insert statement for info TODO: comment this entire line
                statement.executeUpdate(insertStatement);
            } else {
                String updateStatement = compileUpdateStatement(payment);
//                System.out.println(updateStatement); // print out the update statement for info TODO: comment this entire line
                statement.executeUpdate(updateStatement);
            }
        } catch (SQLException e) {
//            e.printStackTrace();
            Alerts.alertInfo("Ошибка записи в базу данных",
                    "Ошибка при добавлении платежа в базу данных");
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
//                System.out.println(payment.getName()); 
//// for info and handmade debugging  TODO comment that line
                payment.setFullDescription(results.getString(FULL_DESCRIPTION));
//                System.out.println(payment.getFullDescription()); 
//// for info and handmade debugging  TODO comment that line
                payment.setYear(results.getInt(YEAR));
                payment.setMonth(results.getInt(MONTH));
//                System.out.println("Payment month: " + payment.getMonth()); 
// for info and handmade debugging TODO comment that line
                payment.setElectroTariff1(results.getDouble(ELECTRO_TARIFF_1));
//                System.out.println("Payment electroTariff1: " + payment.getElectroTariff1()); 
//// for info and handmade debugging TODO comment that line
                payment.setElectroLimit1(results.getInt(ELECTRO_LIMIT_1));
//                System.out.println("Payment electroLimit: " + payment.getElectroLimit1()); 
//// for info and handmade debugging TODO comment that line
                payment.setElectroTariff2(results.getDouble(ELECTRO_TARIFF_2));
                payment.setElectroLimit2(results.getInt(ELECTRO_LIMIT_2));
                payment.setElectroTariff3(results.getDouble(ELECTRO_TARIFF_3));
                payment.setElectroLimit3(results.getInt(ELECTRO_LIMIT_3));
                payment.setElectroTariff4(results.getDouble(ELECTRO_TARIFF_4));
//                System.out.println("Payment electroTariff4: " + payment.getElectroTariff4()); 
//// for info and handmade debugging TODO comment that line
                payment.setHeatingTariff(results.getDouble(HEATING_TARIFF));
                payment.setHeatingTariffToPrint(results.getDouble(HEATING_TARIFF_TO_PRINT));
                payment.setWaterTariff(results.getDouble(WATER_TARIFF));
                payment.setWaterTariffToPrint(results.getDouble(WATER_TARIFF_TO_PRINT));
                payment.setFlatTariff(results.getDouble(FLAT_TARIFF));
                payment.setFlatTariffToPrint(results.getDouble(FLAT_TARIFF_TO_PRINT));
                payment.setGarbageTariff(results.getDouble(GARBAGE_TARIFF));
                payment.setGarbageTariffToPrint(results.getDouble(GARBAGE_TARIFF_TO_PRINT));
//                System.out.println("Payment garbage tariff to print: " + payment.getGarbageTariffToPrint()); 
//// for info and handmade debugging TODO comment that line
                payment.setElectroMustPay(results.getDouble(ELECTRO_MUST_PAY));
                payment.setElectroPaymentSet(results.getInt(ELECTRO_PAYMENT_SET) == 1);
                payment.setHeatingMustPay(results.getDouble(HEATING_MUST_PAY));
                payment.setHeatingPaymentSet(results.getInt(HEATING_PAYMENT_SET) == 1);
                payment.setWaterMustPay(results.getDouble(WATER_MUST_PAY));
                payment.setWaterPaymentSet(results.getInt(WATER_PAYMENT_SET) == 1);
                payment.setFlatMustPay(results.getDouble(FLAT_MUST_PAY));
                payment.setFlatPaymentSet(results.getInt(FLAT_PAYMENT_SET) == 1);
                payment.setGarbageMustPay(results.getDouble(GARBAGE_MUST_PAY));
                payment.setGarbagePaymentSet(results.getInt(GARBAGE_PAYMENT_SET) == 1);
                payment.setElectroStart(results.getInt(ELECTRO_START));
                payment.setElectroEnd(results.getInt(ELECTRO_END));
//                System.out.println("Payment electro end: " + payment.getElectroEnd()); 
//// for info and handmade debugging TODO comment that line
                payment.setkWattConsumed(results.getInt(K_WATT_CONSUMED));
                payment.setWaterStart(results.getInt(WATER_START));
                payment.setWaterEnd(results.getInt(WATER_END));
                payment.setM3consumed(results.getInt(M_3_CONSUMED));
                payment.setPaymentForElectricity(results.getInt(PAYMENT_FOR_ELECTRICITY));
                payment.setPaymentForHeating(results.getInt(PAYMENT_FOR_HEATING));
                payment.setPaymentForWater(results.getInt(PAYMENT_FOR_WATER));
                payment.setPaymentForFlat(results.getInt(PAYMENT_FOR_FLAT));
                payment.setPaymentForGarbage(results.getInt(PAYMENT_FOR_GARBAGE));
                payment.setTotal(results.getInt(TOTAL));
//                System.out.println("Payment total: " + payment.getTotal()); 
//// for info and handmade debugging TODO comment that line
            }
        } catch (SQLException e) {
            Alerts.alertInfo("Ошибка чтения из базы данных",
                    "Ошибка при выдаче выбранного платежа из базы данных");
        } finally {
            if (openAndClose) {
                close();
            }
        }
        if (payment == null) {
            Alerts.alertInfo("Ошибка чтения из базы данных",
                    "Для выдачи был получен маркер платежа,\n" +
                            "но из базы данных не был получен платеж");
        }
        return payment;
    }

    public void deletePayment(int id, boolean openAndClose) throws SQLException {
        if (openAndClose) {
            open();
        }
        String deleteStatement = "DELETE FROM " + TABLE_PAYMENTS + " WHERE " + ID + " = " + id;
        /* In case of sql exception , let's not catch it but throw to the invoker method */
        try {
            statement.execute(deleteStatement);
        } catch (SQLException e) {
            throw e; // let's throw in to the invoker method.
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
                payment.getHeatingTariff() + ", " +
                payment.getHeatingTariffToPrint() + ", " +
                payment.getWaterTariff() + ", " +
                payment.getWaterTariffToPrint() + ", " +
                payment.getFlatTariff() + ", " +
                payment.getFlatTariffToPrint() + ", " +
                payment.getGarbageTariff() + ", " +
                payment.getGarbageTariffToPrint() + ", " +
                payment.getElectroMustPay() + ", " +
                (payment.isElectroPaymentSet() ? 1 : 0) + ", " +
                payment.getHeatingMustPay() + ", " +
                (payment.isHeatingPaymentSet() ? 1 : 0) + ", " +
                payment.getWaterMustPay() + ", " +
                (payment.isWaterPaymentSet() ? 1 : 0) + ", " +
                payment.getFlatMustPay() + ", " +
                (payment.isFlatPaymentSet() ? 1 : 0) + ", " +
                payment.getGarbageMustPay() + ", " +
                (payment.isGarbagePaymentSet() ? 1 : 0) + ", " +
                payment.getElectroStart() + ", " +
                payment.getElectroEnd() + ", " +
                payment.getkWattConsumed() + ", " +
                payment.getWaterStart() + ", " +
                payment.getWaterEnd() + ", " +
                payment.getM3consumed() + ", " +
                payment.getPaymentForElectricity() + ", " +
                payment.getPaymentForHeating() + ", " +
                payment.getPaymentForWater() + ", " +
                payment.getPaymentForFlat() + ", " +
                payment.getPaymentForGarbage() + ", " +
                payment.getTotal() +
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
                HEATING_TARIFF + " = " + payment.getHeatingTariff() + ", " +
                HEATING_TARIFF_TO_PRINT + " = " + payment.getHeatingTariffToPrint() + ", " +
                WATER_TARIFF + " = " + payment.getWaterTariff() + ", " +
                WATER_TARIFF_TO_PRINT + " = " + payment.getWaterTariffToPrint() + ", " +
                FLAT_TARIFF + " = " + payment.getFlatTariff() + ", " +
                FLAT_TARIFF_TO_PRINT + " = " + payment.getFlatTariffToPrint() + ", " +
                GARBAGE_TARIFF + " = " + payment.getGarbageTariff() + ", " +
                GARBAGE_TARIFF_TO_PRINT + " = " + payment.getGarbageTariffToPrint() + ", " +
                ELECTRO_MUST_PAY + " = " + payment.getElectroMustPay() + ", " +
                ELECTRO_PAYMENT_SET + " = " + (payment.isElectroPaymentSet() ? 1 : 0) + ", " +
                HEATING_MUST_PAY + " = " + payment.getHeatingMustPay() + ", " +
                HEATING_PAYMENT_SET + " = " + (payment.isHeatingPaymentSet() ? 1 : 0) + ", " +
                WATER_MUST_PAY + " = " + payment.getWaterMustPay() + ", " +
                WATER_PAYMENT_SET + " = " + (payment.isWaterPaymentSet() ? 1 : 0) + ", " +
                FLAT_MUST_PAY + " = " + payment.getFlatMustPay() + ", " +
                FLAT_PAYMENT_SET + " = " + (payment.isFlatPaymentSet() ? 1 : 0) + ", " +
                GARBAGE_MUST_PAY + " = " + payment.getGarbageMustPay() + ", " +
                GARBAGE_PAYMENT_SET + " = " + (payment.isGarbagePaymentSet() ? 1 : 0) + ", " +
                ELECTRO_START + " = " + payment.getElectroStart() + ", " +
                ELECTRO_END + " = " + payment.getElectroEnd() + ", " +
                K_WATT_CONSUMED + " = " + payment.getkWattConsumed() + ", " +
                WATER_START + " = " + payment.getWaterStart() + ", " +
                WATER_END + " = " + payment.getWaterEnd() + ", " +
                M_3_CONSUMED + " = " + payment.getM3consumed() + ", " +
                PAYMENT_FOR_ELECTRICITY + " = " + payment.getPaymentForElectricity() + ", " +
                PAYMENT_FOR_HEATING + " = " + payment.getPaymentForHeating() + ", " +
                PAYMENT_FOR_WATER + " = " + payment.getPaymentForWater() + ", " +
                PAYMENT_FOR_FLAT + " = " + payment.getPaymentForFlat() + ", " +
                PAYMENT_FOR_GARBAGE + " = " + payment.getPaymentForGarbage() + ", " +
                TOTAL + " = " + payment.getTotal() +
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
                ELECTRO_LIMIT_1 + " integer, " +
                ELECTRO_TARIFF_2 + " real, " +
                ELECTRO_LIMIT_2 + " integer, " +
                ELECTRO_TARIFF_3 + " real, " +
                ELECTRO_LIMIT_3 + " integer, " +
                ELECTRO_TARIFF_4 + " real, " +
                HEATING_TARIFF + " real, " +
                HEATING_TARIFF_TO_PRINT + " real, " +
                WATER_TARIFF + " real, " +
                WATER_TARIFF_TO_PRINT + " real, " +
                FLAT_TARIFF + " real, " +
                FLAT_TARIFF_TO_PRINT + " real, " +
                GARBAGE_TARIFF + " real, " +
                GARBAGE_TARIFF_TO_PRINT + " real, " +
                ELECTRO_MUST_PAY + " real, " +
                ELECTRO_PAYMENT_SET + " integer, " +
                HEATING_MUST_PAY + " real, " +
                HEATING_PAYMENT_SET + " integer, " +
                WATER_MUST_PAY + " real, " +
                WATER_PAYMENT_SET + " integer, " +
                FLAT_MUST_PAY + " real, " +
                FLAT_PAYMENT_SET + " integer, " +
                GARBAGE_MUST_PAY + " real, " +
                GARBAGE_PAYMENT_SET + " integer, " +
                ELECTRO_START + " integer, " +
                ELECTRO_END + " integer, " +
                K_WATT_CONSUMED + " integer, " +
                WATER_START + " integer, " +
                WATER_END + " integer, " +
                M_3_CONSUMED + " integer, " +
                PAYMENT_FOR_ELECTRICITY + " integer, " +
                PAYMENT_FOR_HEATING + " integer, " +
                PAYMENT_FOR_WATER + " integer, " +
                PAYMENT_FOR_FLAT + " integer, " +
                PAYMENT_FOR_GARBAGE + " integer, " +
                TOTAL + " integer" +
                ", PRIMARY KEY (" + ID + ") " +
                ")";
    }

    /*Opening both the connection with the payments database and the statement*/
    public void open() {

        try {
            conn = DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
//            System.out.println("Exception when opening connection in PaymentsDataSource");
            Alerts.alertInfo("Ошибка доступа к базе данных",
                    "Ошибка при создании подключения к базе данных");
        }
        try {
            statement = conn.createStatement();
        } catch (SQLException e) {
//            System.out.println("Exception when creating a Statement in PaymentsDataSource");
            Alerts.alertInfo("Ошибка доступа к базе данных",
                    "Ошибка при создании Statement после подключения к базе данных");
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
//            System.out.println("Exception when closing connection in PaymentsDataSource");
            /* We shouldn't actualy care about exceptions when closing the connection and the statement, but still */
            Alerts.alertInfo("Ошибка доступа к базе данных",
                    "Ошибка при закрытии соединения с базой данных");
        }
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
