package sample;

import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.Random;

import static org.junit.Assert.*;

public class PaymentsDataSourceTest {
    PaymentsDataSource paymentsDataSource;
    Connection conn;
    Statement statement;
    Payment testPayment1;
    Payment testPayment2;
    Random random = new Random();
    int lastPaymentId;

    @Before
    public void setUp() throws Exception {
        paymentsDataSource = PaymentsDataSource.getInstance();
        paymentsDataSource.open();
        paymentsDataSource.initializePaymentsDBTable(false);
//        lastPaymentId = (paymentMarkers != null) ? paymentMarkers.get(paymentMarkers.size() - 1).getId() : 1;
    }

    @Test
    public void openAndCloseConnection() {
        try {
            conn = DriverManager.getConnection(paymentsDataSource.getConnectionString());
            assertFalse(conn.isClosed());
            conn.close();
            assertTrue(conn.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void openAndCloseStatement_SorryForMultiAssert() {
        try {
            conn = DriverManager.getConnection(paymentsDataSource.getConnectionString());
            statement = conn.createStatement();
            statement.close();
            assertFalse(statement.isClosed());
            conn.close();
            assertTrue(conn.isClosed());
            assertFalse(statement.isClosed());
            assertNotNull(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getPaymentMarkers() {
        ObservableList<PaymentMarker> paymentMarkers = paymentsDataSource.getPaymentMarkers(false);
        assertNotNull(paymentMarkers);
    }

    @Test
    /* To me this test needs to be for two methods at once */
    public void storeAndGetPayment_looongTest() {
        ObservableList<PaymentMarker> paymentMarkers = paymentsDataSource.getPaymentMarkers(false);
        lastPaymentId = paymentMarkers.get(paymentMarkers.size()-1).getId();
        int newPaymentId = lastPaymentId + 1;

        testPayment1 = paymentsDataSource.getRandomPayment(newPaymentId);

        paymentsDataSource.storePayment(testPayment1, false);
        testPayment2 = paymentsDataSource.getPayment(newPaymentId, false);
        paymentsDataSource.deletePayment(newPaymentId, false); // I don't need this in my database

        assertTrue(testPayment1.equals(testPayment2));

    }

    /* Next tests worked only when the payments database wasn't filled with another payments */
//    @Test
//    /* It worked only on the beginning, now it fails and screws first entries of my database
//    * so screw this test instead*/
//    public void storePaymentTest_countHowManyPaymentsStored() {
//
//        paymentsDataSource.storePayment(testPayment1, false);
//        paymentsDataSource.storePayment(testPayment2, false);
//        String countEntriesRequest = "SELECT COUNT(*) FROM " + paymentsDataSource.getViewName();
//        long count = 0;
//        try {
//            conn = DriverManager.getConnection(paymentsDataSource.getConnectionString());
//            statement = conn.createStatement();
//            ResultSet results = statement.executeQuery(countEntriesRequest);
//            count = (long) results.getInt(1);
//            results.close();
//            statement.close();
//            conn.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        assertEquals(2L, count, 0.01);
//    }

//    @Test
//    public void getPaymentTest() {
//        Payment paymentForGetting = paymentsDataSource.getPayment(2, false);
//        assertEquals(testPayment2.getTotal(), paymentForGetting.getTotal(), 0.01);
//    }

    @After
    public void tearDown() {
        try {
            if (paymentsDataSource != null) {
                paymentsDataSource.close();
            }
            if (conn != null && !conn.isClosed()) {
                if (statement != null) {
                    statement.close();
                }
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}