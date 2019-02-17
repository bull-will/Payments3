package sample;

/* A payment marker corresponds the full payment stored in the payments database.
 * A payment marker has the 'id' field which is equal to the payment's id (as it is a unique key in the database),
 * the 'name' field, and a 'fullDescription' field. Those are also equal to the respective payment's fields.
 * Payments markers of all the stored payments are collected into an ObservableList by the query to the payments database.
 * For simplification of that query, the database has a view containing columns '_id', 'name', and 'fullDescription'.
 * Payment markers are used for economizing database connection resources by retrieving only the part of payments fields
 * from the limited set of the database columns by the query to the payment markers view.
 * Names and fullDescriptions of the payment markers in the mentioned above observable list are shown
 * in the list view in the program's window.*/
public class PaymentMarker {
    private int id;
    private String name;
    private String fullDescription;

    public PaymentMarker(int id, String name, String fullDescription) {
        this.id = id;
        this.name = name;
        this.fullDescription = fullDescription;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }
}
