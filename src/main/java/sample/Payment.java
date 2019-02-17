package sample;

public class Payment {

    static final String[] NAMES_OF_MONTHS = {"Без_месяца", "Январь", "Февраль", "Март", "Апрель",
            "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};

    private int id; // must be unique for each payment for it's a key value in the payments database
    private String name; //year and month will be combined to represent the name of a payment
    private String fullDescription;

    private int year;
    private int month;

    private double electroTariff1 = 0.3084;
    private int electroLimit1 = 75;
    private double electroTariff2 = 0.6168;
    private int electroLimit2 = 150;
    private double electroTariff3 = 0.8388;
    private int electroLimit3 = 800;
    private double electroTariff4 = 2.6808;

    private double heatingTariff = 270.11;
    private double heatingTariffToPrint = heatingTariff;
    private double waterTariff = 14.0;
    private double waterTariffToPrint = waterTariff;
    private double flatTariff = 197.0;
    private double flatTariffToPrint = flatTariff;
    private double garbageTariff = 22.0;
    private double garbageTariffToPrint = garbageTariff;

    private double electroMustPay = 0.0;
    private boolean electroPaymentSet = false;
    private double heatingMustPay = 0.0;
    private boolean heatingPaymentSet = false;
    private double waterMustPay = 0.0;
    private boolean waterPaymentSet = false;
    private double flatMustPay = 0.0;
    private boolean flatPaymentSet = false;
    private double garbageMustPay = 0.0;
    private boolean garbagePaymentSet = false;

    private int electroStart;
    private int electroEnd;
    private int kWattConsumed;
    private int waterStart;
    private int waterEnd;
    private int m3consumed;

    private int paymentForElectricity;
    private int paymentForHeating;
    private int paymentForWater;
    private int paymentForFlat;
    private int paymentForGarbage;
    private int total;

    /*
    This constructor generates an instance of a monthly payment by the specified values:
    the number of the month (1 - 12), the start readings and the end readings of the electricity counter
    and the water counter.
    The methods of this class must be run to calculate the payments for all services and the total payment.
    Method payForEverything() (at the bottom) runs them all.
    */
    public Payment(int id, int year, int month, int electroStart, int electroEnd, int waterStart, int waterEnd) {
        this.id = id;
        this.year = year;
        this.month = month;
        buildName();
        this.electroStart = electroStart;
        this.electroEnd = electroEnd;
        this.waterStart = waterStart;
        this.waterEnd = waterEnd;
    }

    public Payment(int id) {
        this.id = id;
        this.year = 0;
        this.month = 0;
        buildName();
        buidFullDescription();
    }

    void buildName() {
        if (month > 0 & month < 13) {
            name = year + "." + month + " " + NAMES_OF_MONTHS[month];
        } else {
            name = year + "." + month + " " + NAMES_OF_MONTHS[0];
        }
    }

    /*This method fills the payment's fullDescription field*/
    void buidFullDescription() {
        printPayment();
    }

    void setElectroPayment(double electroMustPay) {
        this.electroMustPay = electroMustPay;
        electroPaymentSet = true;
    }

    void setDefaultElectroPayment() {
        this.electroPaymentSet = false;
    }

    void payForElectricity() {
        kWattConsumed = electroEnd - electroStart;
        if (electroPaymentSet == true) {
            paymentForElectricity = (int) Math.round(electroMustPay);
            return;
        }
        double paymentForElectricityCopeek = 0;
        if (kWattConsumed >= 0) {
            if (kWattConsumed <= electroLimit1) {
                paymentForElectricityCopeek += kWattConsumed * electroTariff1;
            } else if (kWattConsumed <= electroLimit2) {
                paymentForElectricityCopeek += electroLimit1 * electroTariff1 +
                        (kWattConsumed - electroLimit1) * electroTariff2;
            } else if (kWattConsumed <= electroLimit3) {
                paymentForElectricityCopeek += electroLimit1 * electroTariff1 +
                        (electroLimit2 - electroLimit1) * electroTariff2 +
                        (kWattConsumed - electroLimit2) * electroTariff3;
            } else {
                paymentForElectricityCopeek += electroLimit1 * electroTariff1 +
                        (electroLimit2 - electroLimit1) * electroTariff2 +
                        (electroLimit3 - electroLimit2) * electroTariff3 +
                        (kWattConsumed - electroLimit3) * electroTariff4;

            }
        }
        paymentForElectricity = (int) Math.round(paymentForElectricityCopeek);
    }

    void setHeatingPayment(double heatingMustPay) {
        this.heatingMustPay = heatingMustPay;
        heatingPaymentSet = true;
    }

    void setDefaultHeatingPayment() {
        this.heatingPaymentSet = false;
        this.heatingTariffToPrint = this.heatingTariff;
    }

    void payForHeating() {
        if (heatingPaymentSet == true) {
            heatingTariffToPrint = 0;
            paymentForHeating = (int) Math.round(heatingMustPay);
            return;
        }
        paymentForHeating = (int) Math.round(heatingTariff);
    }

    void setWaterPayment(double waterMustPay) {
        this.waterMustPay = waterMustPay;
        waterPaymentSet = true;
    }

    void setDefaultWaterPayment() {
        this.waterPaymentSet = false;
        this.waterTariffToPrint = this.waterTariff;
    }

    void payForWater() {
        m3consumed = waterEnd - waterStart;
        if (waterPaymentSet == true) {
            waterTariffToPrint = 0;
            paymentForWater = (int) Math.round(waterMustPay);
            return;
        }
        if (m3consumed >= 0) {
            paymentForWater = (int) Math.round(m3consumed * waterTariff);
        }
    }

    void setFlatPayment(double flatMustPay) {
        this.flatMustPay = flatMustPay;
        flatPaymentSet = true;
    }

    void setDefaultFlatPayment() {
        this.flatPaymentSet = false;
        this.flatTariffToPrint = this.flatTariff;
    }

    void payForFlat() {
        if (flatPaymentSet == true) {
            flatTariffToPrint = 0;
            paymentForFlat = (int) Math.round(flatMustPay);
            return;
        }
        paymentForFlat = (int) Math.round(flatTariff);
    }

    void setGarbagePayment(double garbageMustPay) {
        this.garbageMustPay = garbageMustPay;
        garbagePaymentSet = true;
    }

    void setDefaultGarbagePayment() {
        this.garbagePaymentSet = false;
        this.garbageTariffToPrint = this.garbageTariff;
    }

    void payForGarbage() {
        if (garbagePaymentSet == true) {
            garbageTariffToPrint = 0;
            paymentForGarbage = (int) Math.round(garbageMustPay);
            return;
        }
        paymentForGarbage = (int) Math.round(garbageTariff);
    }

    void calculateTotal() {
        total = paymentForElectricity
                + paymentForHeating
                + paymentForWater
                + paymentForFlat
                + paymentForGarbage;
    }

    void payForEverything() {
        payForElectricity();
        payForHeating();
        payForWater();
        payForFlat();
        payForGarbage();
        calculateTotal();
    }

    /* This method combines a String value of the payment's full description, fills its fullDescription field
    and also returns the compiled String */
    public String printPayment() {
        String writeLine = "";
        writeLine = writeLine.concat(name + ":\n\n");
        if (paymentForFlat != 0){
            writeLine = writeLine.concat("Квартплата");
            if (flatTariffToPrint != 0) {
                writeLine = writeLine.concat("\t\t\tТариф " + Math.round(flatTariffToPrint) + " руб.");
            }
            writeLine = writeLine.concat("\n");
            writeLine = writeLine.concat("\t\t\t\t\tПлатеж " + paymentForFlat + " руб.\n\n");
        }
        if (paymentForElectricity != 0){
            writeLine = writeLine.concat("Электричество");
            writeLine = writeLine.concat("\n");
            writeLine = writeLine.concat("(Нач. пок. счетчика:\t" + electroStart + ")\n");
            writeLine = writeLine.concat("(Конеч. пок. счетчика:\t" + electroEnd + ")\n");
            writeLine = writeLine.concat("(Потреблено кВт:\t\t" + kWattConsumed + ")\n");
            writeLine = writeLine.concat("\t\t\t\t\tПлатеж " + paymentForElectricity + " руб.\n\n");
        }
        if (paymentForWater != 0){
            writeLine = writeLine.concat("Вода");
            if (waterTariffToPrint != 0) {
                writeLine = writeLine.concat("\t\t\t\tТариф " + Math.round(waterTariffToPrint) + " руб.");
            }
            writeLine = writeLine.concat("\n");
            writeLine = writeLine.concat("(Нач. пок. счетчика:\t" + waterStart + ")\n");
            writeLine = writeLine.concat("(Конеч. пок. счетчика:\t" + waterEnd + ")\n");
            writeLine = writeLine.concat("(Потреблено куб.м:\t" + m3consumed + ")\n");
            writeLine = writeLine.concat("\t\t\t\t\tПлатеж " + paymentForWater + " руб.\n\n");
        }
        if (paymentForHeating != 0){
            writeLine = writeLine.concat("Отопление");
            if (heatingTariffToPrint != 0) {
                writeLine = writeLine.concat("\t\t\tТариф " + Math.round(heatingTariffToPrint) + " руб.");
            }
            writeLine = writeLine.concat("\n");
            writeLine = writeLine.concat("\t\t\t\t\tПлатеж " + paymentForHeating + " руб.\n\n");
        }
        if (paymentForGarbage != 0){
            writeLine = writeLine.concat("Вывоз мусора");
            if (garbageTariffToPrint != 0) {
                writeLine = writeLine.concat("\t\t\tТариф " + Math.round(garbageTariffToPrint) + " руб.");
            }
            writeLine = writeLine.concat("\n");
            writeLine = writeLine.concat("\t\t\t\t\tПлатеж " + paymentForGarbage + " руб.\n\n");
        }
        writeLine = writeLine.concat("\t\t\t\t\tВсего: " + total + " руб.\n");
        fullDescription = writeLine;
        return writeLine;
    }


    public Payment cloneIncrementId() {
        Payment clone = new Payment(this.id + 1);

        clone.name = this.name;
        clone.fullDescription = this.fullDescription;

        clone.year = this.year;
        clone.month = this.month;

        clone.electroTariff1 = this.electroTariff1;
        clone.electroLimit1 = this.electroLimit1;
        clone.electroTariff2 = this.electroTariff2;
        clone.electroLimit2 = this.electroLimit2;
        clone.electroTariff3 = this.electroTariff3;
        clone.electroLimit3 = this.electroLimit3;
        clone.electroTariff4 = this.electroTariff4;

        clone.heatingTariff = this.heatingTariff;
        clone.heatingTariffToPrint = this.heatingTariffToPrint;
        clone.waterTariff = this.waterTariff;
        clone.waterTariffToPrint = this.waterTariffToPrint;
        clone.flatTariff = this.flatTariff;
        clone.flatTariffToPrint = this.flatTariffToPrint;
        clone.garbageTariff = this.garbageTariff;
        clone.garbageTariffToPrint = this.garbageTariffToPrint;

        clone.electroMustPay = this.electroMustPay;
        clone.electroPaymentSet = this.electroPaymentSet;
        clone.heatingMustPay = this.heatingMustPay;
        clone.heatingPaymentSet = this.heatingPaymentSet;
        clone.waterMustPay = this.waterMustPay;
        clone.waterPaymentSet = this.waterPaymentSet;
        clone.flatMustPay = this.flatMustPay;
        clone.flatPaymentSet = this.flatPaymentSet;
        clone.garbageMustPay = this.garbageMustPay;
        clone.garbagePaymentSet = this.garbagePaymentSet;

        clone.electroStart = this.electroStart;
        clone.electroEnd = this.electroEnd;
        clone.kWattConsumed = this.kWattConsumed;
        clone.waterStart = this.waterStart; // initialized in the constructor
        clone.waterEnd = this.waterEnd;
        clone.m3consumed = this.m3consumed;
        clone.paymentForElectricity = this.paymentForElectricity;
        clone.paymentForHeating = this.paymentForHeating;
        clone.paymentForWater = this.paymentForWater;
        clone.paymentForFlat = this.paymentForFlat;
        clone.paymentForGarbage = this.paymentForGarbage;
        clone.total = this.total;

        return clone;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    // Disabling this method prevents any changes to the payment id which is (supposed to be) initially unique
//    public void setId(int id) {
//        this.id = id;
//    }

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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getElectroTariff1() {
        return electroTariff1;
    }

    public void setElectroTariff1(double electroTariff1) {
        this.electroTariff1 = electroTariff1;
    }

    public int getElectroLimit1() {
        return electroLimit1;
    }

    public void setElectroLimit1(int electroLimit1) {
        this.electroLimit1 = electroLimit1;
    }

    public double getElectroTariff2() {
        return electroTariff2;
    }

    public void setElectroTariff2(double electroTariff2) {
        this.electroTariff2 = electroTariff2;
    }

    public int getElectroLimit2() {
        return electroLimit2;
    }

    public void setElectroLimit2(int electroLimit2) {
        this.electroLimit2 = electroLimit2;
    }

    public double getElectroTariff3() {
        return electroTariff3;
    }

    public void setElectroTariff3(double electroTariff3) {
        this.electroTariff3 = electroTariff3;
    }

    public int getElectroLimit3() {
        return electroLimit3;
    }

    public void setElectroLimit3(int electroLimit3) {
        this.electroLimit3 = electroLimit3;
    }

    public double getElectroTariff4() {
        return electroTariff4;
    }

    public void setElectroTariff4(double electroTariff4) {
        this.electroTariff4 = electroTariff4;
    }

    public double getHeatingTariff() {
        return heatingTariff;
    }

    public void setHeatingTariff(double heatingTariff) {
        this.heatingTariff = heatingTariff;
    }

    public double getHeatingTariffToPrint() {
        return heatingTariffToPrint;
    }

    public void setHeatingTariffToPrint(double heatingTariffToPrint) {
        this.heatingTariffToPrint = heatingTariffToPrint;
    }

    public double getWaterTariff() {
        return waterTariff;
    }

    public void setWaterTariff(double waterTariff) {
        this.waterTariff = waterTariff;
    }

    public double getWaterTariffToPrint() {
        return waterTariffToPrint;
    }

    public void setWaterTariffToPrint(double waterTariffToPrint) {
        this.waterTariffToPrint = waterTariffToPrint;
    }

    public double getFlatTariff() {
        return flatTariff;
    }

    public void setFlatTariff(double flatTariff) {
        this.flatTariff = flatTariff;
    }

    public double getFlatTariffToPrint() {
        return flatTariffToPrint;
    }

    public void setFlatTariffToPrint(double flatTariffToPrint) {
        this.flatTariffToPrint = flatTariffToPrint;
    }

    public double getGarbageTariff() {
        return garbageTariff;
    }

    public void setGarbageTariff(double garbageTariff) {
        this.garbageTariff = garbageTariff;
    }

    public double getGarbageTariffToPrint() {
        return garbageTariffToPrint;
    }

    public void setGarbageTariffToPrint(double garbageTariffToPrint) {
        this.garbageTariffToPrint = garbageTariffToPrint;
    }

    public double getElectroMustPay() {
        return electroMustPay;
    }

    public void setElectroMustPay(double electroMustPay) {
        this.electroMustPay = electroMustPay;
    }

    public boolean isElectroPaymentSet() {
        return electroPaymentSet;
    }

    public void setElectroPaymentSet(boolean electroPaymentSet) {
        this.electroPaymentSet = electroPaymentSet;
    }

    public double getHeatingMustPay() {
        return heatingMustPay;
    }

    public void setHeatingMustPay(double heatingMustPay) {
        this.heatingMustPay = heatingMustPay;
    }

    public boolean isHeatingPaymentSet() {
        return heatingPaymentSet;
    }

    public void setHeatingPaymentSet(boolean heatingPaymentSet) {
        this.heatingPaymentSet = heatingPaymentSet;
    }

    public double getWaterMustPay() {
        return waterMustPay;
    }

    public void setWaterMustPay(double waterMustPay) {
        this.waterMustPay = waterMustPay;
    }

    public boolean isWaterPaymentSet() {
        return waterPaymentSet;
    }

    public void setWaterPaymentSet(boolean waterPaymentSet) {
        this.waterPaymentSet = waterPaymentSet;
    }

    public double getFlatMustPay() {
        return flatMustPay;
    }

    public void setFlatMustPay(double flatMustPay) {
        this.flatMustPay = flatMustPay;
    }

    public boolean isFlatPaymentSet() {
        return flatPaymentSet;
    }

    public void setFlatPaymentSet(boolean flatPaymentSet) {
        this.flatPaymentSet = flatPaymentSet;
    }

    public double getGarbageMustPay() {
        return garbageMustPay;
    }

    public void setGarbageMustPay(double garbageMustPay) {
        this.garbageMustPay = garbageMustPay;
    }

    public boolean isGarbagePaymentSet() {
        return garbagePaymentSet;
    }

    public void setGarbagePaymentSet(boolean garbagePaymentSet) {
        this.garbagePaymentSet = garbagePaymentSet;
    }

    public int getElectroStart() {
        return electroStart;
    }

    public void setElectroStart(int electroStart) {
        this.electroStart = electroStart;
    }

    public int getElectroEnd() {
        return electroEnd;
    }

    public void setElectroEnd(int electroEnd) {
        this.electroEnd = electroEnd;
    }

    public int getkWattConsumed() {
        return kWattConsumed;
    }

    public void setkWattConsumed(int kWattConsumed) {
        this.kWattConsumed = kWattConsumed;
    }

    public int getWaterStart() {
        return waterStart;
    }

    public void setWaterStart(int waterStart) {
        this.waterStart = waterStart;
    }

    public int getWaterEnd() {
        return waterEnd;
    }

    public void setWaterEnd(int waterEnd) {
        this.waterEnd = waterEnd;
    }

    public int getM3consumed() {
        return m3consumed;
    }

    public void setM3consumed(int m3consumed) {
        this.m3consumed = m3consumed;
    }

    public int getPaymentForElectricity() {
        return paymentForElectricity;
    }

    public void setPaymentForElectricity(int paymentForElectricity) {
        this.paymentForElectricity = paymentForElectricity;
    }

    public int getPaymentForHeating() {
        return paymentForHeating;
    }

    public void setPaymentForHeating(int paymentForHeating) {
        this.paymentForHeating = paymentForHeating;
    }

    public int getPaymentForWater() {
        return paymentForWater;
    }

    public void setPaymentForWater(int paymentForWater) {
        this.paymentForWater = paymentForWater;
    }

    public int getPaymentForFlat() {
        return paymentForFlat;
    }

    public void setPaymentForFlat(int paymentForFlat) {
        this.paymentForFlat = paymentForFlat;
    }

    public int getPaymentForGarbage() {
        return paymentForGarbage;
    }

    public void setPaymentForGarbage(int paymentForGarbage) {
        this.paymentForGarbage = paymentForGarbage;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }
}

