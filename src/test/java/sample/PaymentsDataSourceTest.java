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
    /* Two methods need to be tested together */
    public void storeAndGetPayment_twoMethodsInOneTest() {
        ObservableList<PaymentMarker> paymentMarkers = paymentsDataSource.getPaymentMarkers(false);
        lastPaymentId = paymentMarkers.get(paymentMarkers.size()-1).getId();
        int newPaymentId = lastPaymentId + 1;

        testPayment1 = paymentsDataSource.getRandomPayment(newPaymentId);

        paymentsDataSource.storePayment(testPayment1, false);
        testPayment2 = paymentsDataSource.getPayment(newPaymentId, false);
        paymentsDataSource.deletePayment(newPaymentId, false); // I don't need this in my database

        assertTrue(testPayment1.equals(testPayment2));

    }

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