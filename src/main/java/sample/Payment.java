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
    private double waterTariff = 14.0;
    private double hotWaterTariff = 0d;
    private double heatingTariff = 270.11;
    private double gasTariff = 0d;
    private double sewageTariff = 0d;
    private double flatTariff = 0d;
    private double garbageTariff = 0d;
    private boolean electroPaymentSet = false;
    private boolean waterPaymentSet = false;
    private boolean hotWaterPaymentSet = false;
    private boolean heatingPaymentSet = false;
    private boolean gasPaymentSet = false;
    private boolean sewagePaymentSet = false;
    private boolean flatPaymentSet = false;
    private boolean garbagePaymentSet = false;
    private boolean electroPaymentByTariff = false;
    private boolean waterPaymentByTariff = false;
    private boolean hotWaterPaymentByTariff = false;
    private boolean heatingPaymentByTariff = false;
    private boolean gasPaymentByTariff = false;
    private boolean sewagePaymentByTariff = false;
    private boolean flatPaymentByTariff = false;
    private boolean garbagePaymentByTariff = false;
    private double electroMustPay = 0.0;
    private double waterMustPay = 0.0;
    private double hotWaterMustPay = 0.0;
    private double heatingMustPay = 0.0;
    private double gasMustPay = 0.0;
    private double sewageMustPay = 0.0;
    private double flatMustPay = 0.0;
    private double garbageMustPay = 0.0;
    private int electroStart;
    private int electroEnd;
    private int kWattConsumed;
    private int waterStart;
    private int waterEnd;
    private int m3consumed;
    private int hotWaterStart;
    private int hotWaterEnd;
    private int hotM3consumed;
    private int heatingStart;
    private int heatingEnd;
    private int heatingConsumed;
    private int gasStart;
    private int gasEnd;
    private int gasM3consumed;
    private int sewageStart;
    private int sewageEnd;
    private int sewageM3consumed;
    private double paymentForElectricity;
    private double paymentForWater;
    private double paymentForHotWater;
    private double paymentForHeating;
    private double paymentForGas;
    private double paymentForSewage;
    private double paymentForFlat;
    private double paymentForGarbage;
    private double total;
    private boolean round;

    /*
        This constructor generates an instance of a monthly payment by the specified values:
        the number of the month (1 - 12), the start readings and the end readings of the electricity counter
        and the water counter.
        */
    public Payment(int id, int year,
                   int month, int electroStart, int electroEnd, int waterStart, int waterEnd,
                   int hotWaterStart, int hotWaterEnd, int heatingStart, int heatingEnd,
                   int gasStart, int gasEnd, int sewageStart, int sewageEnd) {
        this.id = id;
        this.year = year;
        this.month = month;
        buildName();
        this.electroStart = electroStart;
        this.electroEnd = electroEnd;
        this.waterStart = waterStart;
        this.waterEnd = waterEnd;
        this.hotWaterStart = hotWaterStart;
        this.hotWaterEnd = hotWaterEnd;
        this.heatingStart = heatingStart;
        this.heatingEnd = heatingEnd;
        this.gasStart = gasStart;
        this.gasEnd = gasEnd;
        this.sewageStart = sewageStart;
        this.sewageEnd = sewageEnd;
    }


    public Payment(int id) {
        this.id = id;
        this.year = 0;
        this.month = 0;
        buildName();
        buildFullDescription();
    }

    void buildName() {
        if (month > 0 & month < 13) {
            name = year + "." + month + " " + NAMES_OF_MONTHS[month];
        } else {
            name = year + "." + month + " " + NAMES_OF_MONTHS[0];
        }
    }

    /*This method fills the payment's fullDescription field*/
    void buildFullDescription() {
        printPayment();
    }

    void setElectroPayment(double electroMustPay) {
        this.electroMustPay = electroMustPay;
        electroPaymentSet = true;
    }

    void unsetElectroPayment() {
        this.electroPaymentSet = false;
        this.electroMustPay = 0d;
    }

    void payForElectricity() {
        kWattConsumed = electroEnd - electroStart;
        if (electroPaymentSet) {
            paymentForElectricity = electroMustPay;
        } else if (electroPaymentByTariff) {
            paymentForElectricity = electroTariff1;
        } else {
            paymentForElectricity = 0;
            if (kWattConsumed >= 0) {
                if (kWattConsumed <= electroLimit1) {
                    paymentForElectricity += kWattConsumed * electroTariff1;
                } else if (kWattConsumed <= electroLimit2) {
                    paymentForElectricity += electroLimit1 * electroTariff1 +
                            (kWattConsumed - electroLimit1) * electroTariff2;
                } else if (kWattConsumed <= electroLimit3) {
                    paymentForElectricity += electroLimit1 * electroTariff1 +
                            (electroLimit2 - electroLimit1) * electroTariff2 +
                            (kWattConsumed - electroLimit2) * electroTariff3;
                } else {
                    paymentForElectricity += electroLimit1 * electroTariff1 +
                            (electroLimit2 - electroLimit1) * electroTariff2 +
                            (electroLimit3 - electroLimit2) * electroTariff3 +
                            (kWattConsumed - electroLimit3) * electroTariff4;
                }
            }
        }
        paymentForElectricity = roundPayment(paymentForElectricity);
    }

    void setWaterPayment(double waterMustPay) {
        this.waterMustPay = waterMustPay;
        waterPaymentSet = true;
    }

    void unsetWaterPayment() {
        this.waterPaymentSet = false;
        this.waterMustPay = 0d;
    }

    void payForWater() {
        m3consumed = waterEnd - waterStart;
        if (waterPaymentSet) {
            paymentForWater = waterMustPay;
        } else if (waterPaymentByTariff) {
            paymentForWater = waterTariff;
        } else {
            if (m3consumed >= 0) {
                paymentForWater = m3consumed * waterTariff;
            }
        }
        paymentForWater = roundPayment(paymentForWater);
    }

    void setHotWaterPayment(double hotWaterMustPay) {
        this.hotWaterMustPay = hotWaterMustPay;
        hotWaterPaymentSet = true;
    }

    void unsetHotWaterPayment() {
        this.hotWaterPaymentSet = false;
        this.hotWaterMustPay = 0d;
    }

    void payForHotWater() {
        hotM3consumed = hotWaterEnd - hotWaterStart;
        if (hotWaterPaymentSet) {
            paymentForHotWater = hotWaterMustPay;
        } else if (hotWaterPaymentByTariff) {
            paymentForHotWater = hotWaterTariff;
        } else {
            if (hotM3consumed >= 0) {
                paymentForHotWater = hotM3consumed * hotWaterTariff;
            }
        }
        paymentForHotWater = roundPayment(paymentForHotWater);
    }

    void setHeatingPayment(double heatingMustPay) {
        this.heatingMustPay = heatingMustPay;
        heatingPaymentSet = true;
    }

    void unseteatingPayment() {
        this.heatingPaymentSet = false;
        this.heatingMustPay = 0d;
    }

    void payForHeating() {
        heatingConsumed = heatingEnd - heatingStart;
        if (heatingPaymentSet) {
            paymentForHeating = heatingMustPay;
        } else if (heatingPaymentByTariff) {
            paymentForHeating = heatingTariff;
        } else {
            if (heatingConsumed >= 0) {
                paymentForHeating = heatingConsumed * heatingTariff;
            }
        }
        paymentForHeating = roundPayment(paymentForHeating);
    }

    void setGasPayment(double gasMustPay) {
        this.gasMustPay = gasMustPay;
        gasPaymentSet = true;
    }

    void unsetGasPayment() {
        this.gasPaymentSet = false;
        this.gasMustPay = 0d;
    }

    void payForGas() {
        gasM3consumed = gasEnd - gasStart;
        if (gasPaymentSet) {
            paymentForGas = gasMustPay;
        } else if (gasPaymentByTariff) {
            paymentForGas = gasTariff;
        } else {
            if (gasM3consumed >= 0) {
                paymentForGas = gasM3consumed * gasTariff;
            }
        }
        paymentForGas = roundPayment(paymentForGas);
    }

    void setSewagePayment(double sewageMustPay) {
        this.sewageMustPay = sewageMustPay;
        sewagePaymentSet = true;
    }

    void unsetSewagePayment() {
        this.sewagePaymentSet = false;
        this.sewageMustPay = 0d;
    }

    void payForSewage() {
        sewageM3consumed = sewageEnd - sewageStart;
        if (sewagePaymentSet) {
            paymentForSewage = sewageMustPay;
        } else if (sewagePaymentByTariff) {
            paymentForSewage = sewageTariff;
        } else {
            if (sewageM3consumed >= 0) {
                paymentForSewage = sewageM3consumed * sewageTariff;
            }
        }
        paymentForSewage = roundPayment(paymentForSewage);
    }

    void setFlatPayment(double flatMustPay) {
        this.flatMustPay = flatMustPay;
        flatPaymentSet = true;
    }

    void unsetFlatPayment() {
        this.flatPaymentSet = false;
        this.flatMustPay = 0d;
    }

    void payForFlat() {
        if (flatPaymentSet == true) {
            paymentForFlat = flatMustPay;
        } else {
            paymentForFlat = flatTariff;
        }
        paymentForFlat = roundPayment(paymentForFlat);
    }

    void setGarbagePayment(double garbageMustPay) {
        this.garbageMustPay = garbageMustPay;
        garbagePaymentSet = true;
    }

    void setDefaultGarbagePayment() {
        this.garbagePaymentSet = false;
        this.garbageMustPay = 0d;
    }

    void payForGarbage() {
        if (garbagePaymentSet == true) {
            paymentForGarbage = garbageMustPay;
        } else {
            paymentForGarbage = garbageTariff;
        }
        paymentForGarbage = roundPayment(paymentForGarbage);
    }

    private double roundPayment(double paymentFor) {

        return round ? Math.round(paymentFor) : (double) Math.round(paymentFor * 100) / 100;

    }

    void calculateTotal() {
        total = paymentForElectricity
                + paymentForWater
                + paymentForHotWater
                + paymentForHeating
                + paymentForGas
                + paymentForSewage
                + paymentForFlat
                + paymentForGarbage;
        total = roundPayment(total);
    }

    /* This method invokes all the calculations*/
    void payForEverything() {
        payForElectricity();
        payForWater();
        payForHotWater();
        payForHeating();
        payForGas();
        payForSewage();
        payForFlat();
        payForGarbage();
        calculateTotal();
    }

    /* This method combines a String value of the payment's full description, fills its fullDescription field
    and also returns the compiled String */
    public String printPayment() {

        String writeLine = "";
        writeLine = writeLine.concat(name + ":\n\n");
        if (paymentForFlat != 0) {
            writeLine = writeLine.concat("Квартплата");
            if (!flatPaymentSet) {
                writeLine = writeLine.concat("\t\t\tТариф " +
                        (flatTariff % 1 == 0d ? String.valueOf((int) Math.round(flatTariff)) : flatTariff) // rounding tariff if it has no coin part
                        + " руб.");
            }
            writeLine = writeLine.concat("\n");
            writeLine = writeLine.concat("\t\t\t\t\tПлатеж " +
                    (round ? String.valueOf((int) paymentForFlat) : paymentForFlat) + " руб.\n\n");
        }
        if (paymentForElectricity != 0) {
            writeLine = writeLine.concat("Электроэнергия");
            if (electroPaymentByTariff && !electroPaymentSet) {
                writeLine = writeLine.concat("\t\tТариф " +
                        (electroTariff1 % 1 == 0d ? String.valueOf((int) Math.round(electroTariff1)) : electroTariff1)
                        + " руб.");
            }
            writeLine = writeLine.concat("\n");
            if (!electroPaymentByTariff && !electroPaymentSet) {
                writeLine = writeLine.concat("(Нач. пок. счетчика:\t" + electroStart + ")\n");
                writeLine = writeLine.concat("(Конеч. пок. счетчика:\t" + electroEnd + ")\n");
                writeLine = writeLine.concat("(Потреблено кВт:\t\t" + kWattConsumed + ")\n");
            }
            writeLine = writeLine.concat("\t\t\t\t\tПлатеж " +
                    (round ? String.valueOf((int) paymentForElectricity) : paymentForElectricity) + " руб.\n\n");
        }
        if (paymentForGas != 0) {
            writeLine = writeLine.concat("Природный газ");
            if (!gasPaymentSet) {
                writeLine = writeLine.concat("\t\tТариф " +
                        (gasTariff % 1 == 0d ? String.valueOf((int) Math.round(gasTariff)) : gasTariff)
                        + " руб.");
            }
            writeLine = writeLine.concat("\n");
            if (!gasPaymentByTariff && !gasPaymentSet) {
                writeLine = writeLine.concat("(Нач. пок. счетчика:\t" + gasStart + ")\n");
                writeLine = writeLine.concat("(Конеч. пок. счетчика:\t" + gasEnd + ")\n");
                writeLine = writeLine.concat("(Потреблено куб.м:\t" + gasM3consumed + ")\n");
            }
            writeLine = writeLine.concat("\t\t\t\t\tПлатеж " +
                    (round ? String.valueOf((int) paymentForGas) : paymentForGas) + " руб.\n\n");
        }
        if (paymentForWater != 0) {
            writeLine = writeLine.concat("Холодная вода");
            if (!waterPaymentSet) {
                writeLine = writeLine.concat("\t\tТариф " +
                        (waterTariff % 1 == 0d ? String.valueOf((int) Math.round(waterTariff)) : waterTariff)
                        + " руб.");
            }
            writeLine = writeLine.concat("\n");
            if (!waterPaymentByTariff && !waterPaymentSet) {
                writeLine = writeLine.concat("(Нач. пок. счетчика:\t" + waterStart + ")\n");
                writeLine = writeLine.concat("(Конеч. пок. счетчика:\t" + waterEnd + ")\n");
                writeLine = writeLine.concat("(Потреблено куб.м:\t" + m3consumed + ")\n");
            }
            writeLine = writeLine.concat("\t\t\t\t\tПлатеж " +
                    (round ? String.valueOf((int) paymentForWater) : paymentForWater) + " руб.\n\n");
        }
        if (paymentForSewage != 0) {
            writeLine = writeLine.concat("Стоки");
            if (!sewagePaymentSet) {
                writeLine = writeLine.concat("\t\t\t\tТариф " +
                        (sewageTariff % 1 == 0d ? String.valueOf((int) Math.round(sewageTariff)) : sewageTariff)
                        + " руб.");
            }
            writeLine = writeLine.concat("\n");
            if (!sewagePaymentByTariff && !sewagePaymentSet) {
                writeLine = writeLine.concat("(Нач. пок. счетчика:\t" + sewageStart + ")\n");
                writeLine = writeLine.concat("(Конеч. пок. счетчика:\t" + sewageEnd + ")\n");
                writeLine = writeLine.concat("(Потреблено куб.м:\t" + sewageM3consumed + ")\n");
            }
            writeLine = writeLine.concat("\t\t\t\t\tПлатеж " +
                    (round ? String.valueOf((int) paymentForSewage) : paymentForSewage) + " руб.\n\n");
        }
        if (paymentForHeating != 0) {
            writeLine = writeLine.concat("Отопление");
            if (!heatingPaymentSet) {
                writeLine = writeLine.concat("\t\t\tТариф " +
                        (heatingTariff % 1 == 0 ? Math.round(heatingTariff) : heatingTariff)
                        + " руб.");
            }
            writeLine = writeLine.concat("\n");
            if (!heatingPaymentByTariff && !heatingPaymentSet) {
                writeLine = writeLine.concat("(Нач. пок. счетчика:\t" + heatingStart + ")\n");
                writeLine = writeLine.concat("(Конеч. пок. счетчика:\t" + heatingEnd + ")\n");
                writeLine = writeLine.concat("(Потреблено:\t\t\t" + heatingConsumed + ")\n");
            }
            writeLine = writeLine.concat("\t\t\t\t\tПлатеж " +
                    (round ? String.valueOf((int) paymentForHeating) : paymentForHeating) + " руб.\n\n");
        }
        if (paymentForHotWater != 0) {
            writeLine = writeLine.concat("Горячая вода");
            if (!hotWaterPaymentSet) {
                writeLine = writeLine.concat("\t\t\tТариф " +
                        (hotWaterTariff % 1 == 0d ? String.valueOf((int) Math.round(hotWaterTariff)) : hotWaterTariff)
                        + " руб.");
            }
            writeLine = writeLine.concat("\n");
            if (!hotWaterPaymentByTariff && !hotWaterPaymentSet) {
                writeLine = writeLine.concat("(Нач. пок. счетчика:\t" + hotWaterStart + ")\n");
                writeLine = writeLine.concat("(Конеч. пок. счетчика:\t" + hotWaterEnd + ")\n");
                writeLine = writeLine.concat("(Потреблено куб.м:\t" + hotM3consumed + ")\n");
            }
            writeLine = writeLine.concat("\t\t\t\t\tПлатеж " +
                    (round ? String.valueOf((int) paymentForHotWater) : paymentForHotWater) + " руб.\n\n");
        }
        if (paymentForGarbage != 0) {
            writeLine = writeLine.concat("Вывоз мусора");
            if (!garbagePaymentSet) {
                writeLine = writeLine.concat("\t\t\tТариф " +
                        (garbageTariff % 1 == 0d ? String.valueOf((int) Math.round(garbageTariff)) : garbageTariff)
                        + " руб.");
            }
            writeLine = writeLine.concat("\n");
            writeLine = writeLine.concat("\t\t\t\t\tПлатеж " +
                    (round ? String.valueOf((int) paymentForGarbage) : paymentForGarbage) + " руб.\n\n");
        }
        writeLine = writeLine.concat("Всего: \t\t\t\t" +
                (round ? String.valueOf((int) total) : total) + " руб.\n");
        fullDescription = writeLine;
        return writeLine;
    }

    public Payment cloneWithId(int id) {
        Payment clone = new Payment(id);

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
        clone.waterTariff = this.waterTariff;
        clone.hotWaterTariff = this.hotWaterTariff;
        clone.heatingTariff = this.heatingTariff;
        clone.gasTariff = this.gasTariff;
        clone.sewageTariff = this.sewageTariff;
        clone.flatTariff = this.flatTariff;
        clone.garbageTariff = this.garbageTariff;
        clone.electroPaymentSet = this.electroPaymentSet;
        clone.waterPaymentSet = this.waterPaymentSet;
        clone.hotWaterPaymentSet = this.hotWaterPaymentSet;
        clone.heatingPaymentSet = this.heatingPaymentSet;
        clone.sewagePaymentSet = this.sewagePaymentSet;
        clone.gasPaymentSet = this.gasPaymentSet;
        clone.flatPaymentSet = this.flatPaymentSet;
        clone.garbagePaymentSet = this.garbagePaymentSet;
        clone.electroPaymentByTariff = this.electroPaymentByTariff;
        clone.waterPaymentByTariff = this.waterPaymentByTariff;
        clone.hotWaterPaymentByTariff = this.hotWaterPaymentByTariff;
        clone.heatingPaymentByTariff = this.heatingPaymentByTariff;
        clone.sewagePaymentByTariff = this.sewagePaymentByTariff;
        clone.gasPaymentByTariff = this.gasPaymentByTariff;
        clone.flatPaymentByTariff = this.flatPaymentByTariff;
        clone.garbagePaymentByTariff = this.garbagePaymentByTariff;
        clone.electroMustPay = this.electroMustPay;
        clone.waterMustPay = this.waterMustPay;
        clone.hotWaterMustPay = this.hotWaterMustPay;
        clone.heatingMustPay = this.heatingMustPay;
        clone.gasMustPay = this.gasMustPay;
        clone.sewageMustPay = this.sewageMustPay;
        clone.flatMustPay = this.flatMustPay;
        clone.garbageMustPay = this.garbageMustPay;
        clone.electroStart = this.electroStart;
        clone.electroEnd = this.electroEnd;
        clone.kWattConsumed = this.kWattConsumed;
        clone.waterStart = this.waterStart;
        clone.waterEnd = this.waterEnd;
        clone.m3consumed = this.m3consumed;
        clone.hotWaterStart = this.hotWaterStart;
        clone.hotWaterEnd = this.hotWaterEnd;
        clone.hotM3consumed = this.hotM3consumed;
        clone.heatingStart = this.heatingStart;
        clone.heatingEnd = this.heatingEnd;
        clone.heatingConsumed = this.heatingConsumed;
        clone.gasStart = this.gasStart;
        clone.gasEnd = this.gasEnd;
        clone.gasM3consumed = this.gasM3consumed;
        clone.sewageStart = this.sewageStart;
        clone.sewageEnd = this.sewageEnd;
        clone.sewageM3consumed = this.sewageM3consumed;
        clone.paymentForElectricity = this.paymentForElectricity;
        clone.paymentForWater = this.paymentForWater;
        clone.paymentForHotWater = this.paymentForHotWater;
        clone.paymentForHeating = this.paymentForHeating;
        clone.paymentForGas = this.paymentForGas;
        clone.paymentForSewage = this.paymentForSewage;
        clone.paymentForFlat = this.paymentForFlat;
        clone.paymentForGarbage = this.paymentForGarbage;
        clone.total = this.total;
        clone.round = this.round;

        return clone;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    /* Commenting out (i.e. disabling) this method prevents any changes to the payment id
     * which is (supposed to be) initially unique */
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

    public double getWaterTariff() {
        return waterTariff;
    }

    public void setWaterTariff(double waterTariff) {
        this.waterTariff = waterTariff;
    }

    public double getHotWaterTariff() {
        return hotWaterTariff;
    }

    public void setHotWaterTariff(double hotWaterTariff) {
        this.hotWaterTariff = hotWaterTariff;
    }

    public double getHeatingTariff() {
        return heatingTariff;
    }

    public void setHeatingTariff(double heatingTariff) {
        this.heatingTariff = heatingTariff;
    }

    public double getGasTariff() {
        return gasTariff;
    }

    public void setGasTariff(double gasTariff) {
        this.gasTariff = gasTariff;
    }

    public double getSewageTariff() {
        return sewageTariff;
    }

    public void setSewageTariff(double sewageTariff) {
        this.sewageTariff = sewageTariff;
    }

    public double getFlatTariff() {
        return flatTariff;
    }

    public void setFlatTariff(double flatTariff) {
        this.flatTariff = flatTariff;
    }

    public double getGarbageTariff() {
        return garbageTariff;
    }

    public void setGarbageTariff(double garbageTariff) {
        this.garbageTariff = garbageTariff;
    }

    public boolean isElectroPaymentSet() {
        return electroPaymentSet;
    }

    public void setElectroPaymentSet(boolean electroPaymentSet) {
        this.electroPaymentSet = electroPaymentSet;
    }

    public boolean isWaterPaymentSet() {
        return waterPaymentSet;
    }

    public void setWaterPaymentSet(boolean waterPaymentSet) {
        this.waterPaymentSet = waterPaymentSet;
    }

    public boolean isHotWaterPaymentSet() {
        return hotWaterPaymentSet;
    }

    public void setHotWaterPaymentSet(boolean hotWaterPaymentSet) {
        this.hotWaterPaymentSet = hotWaterPaymentSet;
    }

    public boolean isHeatingPaymentSet() {
        return heatingPaymentSet;
    }

    public void setHeatingPaymentSet(boolean heatingPaymentSet) {
        this.heatingPaymentSet = heatingPaymentSet;
    }

    public boolean isSewagePaymentSet() {
        return sewagePaymentSet;
    }

    public void setSewagePaymentSet(boolean sewagePaymentSet) {
        this.sewagePaymentSet = sewagePaymentSet;
    }

    public boolean isGasPaymentSet() {
        return gasPaymentSet;
    }

    public void setGasPaymentSet(boolean gasPaymentSet) {
        this.gasPaymentSet = gasPaymentSet;
    }

    public boolean isFlatPaymentSet() {
        return flatPaymentSet;
    }

    public void setFlatPaymentSet(boolean flatPaymentSet) {
        this.flatPaymentSet = flatPaymentSet;
    }

    public boolean isGarbagePaymentSet() {
        return garbagePaymentSet;
    }

    public void setGarbagePaymentSet(boolean garbagePaymentSet) {
        this.garbagePaymentSet = garbagePaymentSet;
    }

    public boolean isElectroPaymentByTariff() {
        return electroPaymentByTariff;
    }

    public void setElectroPaymentByTariff(boolean electroPaymentByTariff) {
        this.electroPaymentByTariff = electroPaymentByTariff;
    }

    public boolean isWaterPaymentByTariff() {
        return waterPaymentByTariff;
    }

    public void setWaterPaymentByTariff(boolean waterPaymentByTariff) {
        this.waterPaymentByTariff = waterPaymentByTariff;
    }

    public boolean isHotWaterPaymentByTariff() {
        return hotWaterPaymentByTariff;
    }

    public void setHotWaterPaymentByTariff(boolean hotWaterPaymentByTariff) {
        this.hotWaterPaymentByTariff = hotWaterPaymentByTariff;
    }

    public boolean isHeatingPaymentByTariff() {
        return heatingPaymentByTariff;
    }

    public void setHeatingPaymentByTariff(boolean heatingPaymentByTariff) {
        this.heatingPaymentByTariff = heatingPaymentByTariff;
    }

    public boolean isSewagePaymentByTariff() {
        return sewagePaymentByTariff;
    }

    public void setSewagePaymentByTariff(boolean sewagePaymentByTariff) {
        this.sewagePaymentByTariff = sewagePaymentByTariff;
    }

    public boolean isGasPaymentByTariff() {
        return gasPaymentByTariff;
    }

    public void setGasPaymentByTariff(boolean gasPaymentByTariff) {
        this.gasPaymentByTariff = gasPaymentByTariff;
    }

    public boolean isFlatPaymentByTariff() {
        return flatPaymentByTariff;
    }

    public void setFlatPaymentByTariff(boolean flatPaymentByTariff) {
        this.flatPaymentByTariff = flatPaymentByTariff;
    }

    public boolean isGarbagePaymentByTariff() {
        return garbagePaymentByTariff;
    }

    public void setGarbagePaymentByTariff(boolean garbagePaymentByTariff) {
        this.garbagePaymentByTariff = garbagePaymentByTariff;
    }

    public double getElectroMustPay() {
        return electroMustPay;
    }

    public void setElectroMustPay(double electroMustPay) {
        this.electroMustPay = electroMustPay;
    }

    public double getWaterMustPay() {
        return waterMustPay;
    }

    public void setWaterMustPay(double waterMustPay) {
        this.waterMustPay = waterMustPay;
    }

    public double getHotWaterMustPay() {
        return hotWaterMustPay;
    }

    public void setHotWaterMustPay(double hotWaterMustPay) {
        this.hotWaterMustPay = hotWaterMustPay;
    }

    public double getHeatingMustPay() {
        return heatingMustPay;
    }

    public void setHeatingMustPay(double heatingMustPay) {
        this.heatingMustPay = heatingMustPay;
    }

    public double getGasMustPay() {
        return gasMustPay;
    }

    public void setGasMustPay(double gasMustPay) {
        this.gasMustPay = gasMustPay;
    }

    public double getSewageMustPay() {
        return sewageMustPay;
    }

    public void setSewageMustPay(double sewageMustPay) {
        this.sewageMustPay = sewageMustPay;
    }

    public double getFlatMustPay() {
        return flatMustPay;
    }

    public void setFlatMustPay(double flatMustPay) {
        this.flatMustPay = flatMustPay;
    }

    public double getGarbageMustPay() {
        return garbageMustPay;
    }

    public void setGarbageMustPay(double garbageMustPay) {
        this.garbageMustPay = garbageMustPay;
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

    public int getHotWaterStart() {
        return hotWaterStart;
    }

    public void setHotWaterStart(int hotWaterStart) {
        this.hotWaterStart = hotWaterStart;
    }

    public int getHotWaterEnd() {
        return hotWaterEnd;
    }

    public void setHotWaterEnd(int hotWaterEnd) {
        this.hotWaterEnd = hotWaterEnd;
    }

    public int getHotM3consumed() {
        return hotM3consumed;
    }

    public void setHotM3consumed(int hotM3consumed) {
        this.hotM3consumed = hotM3consumed;
    }

    public int getHeatingStart() {
        return heatingStart;
    }

    public void setHeatingStart(int heatingStart) {
        this.heatingStart = heatingStart;
    }

    public int getHeatingEnd() {
        return heatingEnd;
    }

    public void setHeatingEnd(int heatingEnd) {
        this.heatingEnd = heatingEnd;
    }

    public int getHeatingConsumed() {
        return heatingConsumed;
    }

    public void setHeatingConsumed(int heatingConsumed) {
        this.heatingConsumed = heatingConsumed;
    }

    public int getGasStart() {
        return gasStart;
    }

    public void setGasStart(int gasStart) {
        this.gasStart = gasStart;
    }

    public int getGasEnd() {
        return gasEnd;
    }

    public void setGasEnd(int gasEnd) {
        this.gasEnd = gasEnd;
    }

    public int getGasM3consumed() {
        return gasM3consumed;
    }

    public void setGasM3consumed(int gasM3consumed) {
        this.gasM3consumed = gasM3consumed;
    }

    public int getSewageStart() {
        return sewageStart;
    }

    public void setSewageStart(int sewageStart) {
        this.sewageStart = sewageStart;
    }

    public int getSewageEnd() {
        return sewageEnd;
    }

    public void setSewageEnd(int sewageEnd) {
        this.sewageEnd = sewageEnd;
    }

    public int getSewageM3consumed() {
        return sewageM3consumed;
    }

    public void setSewageM3consumed(int sewageM3consumed) {
        this.sewageM3consumed = sewageM3consumed;
    }

    public double getPaymentForElectricity() {
        return paymentForElectricity;
    }

    public void setPaymentForElectricity(double paymentForElectricity) {
        this.paymentForElectricity = paymentForElectricity;
    }

    public double getPaymentForWater() {
        return paymentForWater;
    }

    public void setPaymentForWater(double paymentForWater) {
        this.paymentForWater = paymentForWater;
    }

    public double getPaymentForHotWater() {
        return paymentForHotWater;
    }

    public void setPaymentForHotWater(double paymentForHotWater) {
        this.paymentForHotWater = paymentForHotWater;
    }

    public double getPaymentForHeating() {
        return paymentForHeating;
    }

    public void setPaymentForHeating(double paymentForHeating) {
        this.paymentForHeating = paymentForHeating;
    }

    public double getPaymentForGas() {
        return paymentForGas;
    }

    public void setPaymentForGas(double paymentForGas) {
        this.paymentForGas = paymentForGas;
    }

    public double getPaymentForSewage() {
        return paymentForSewage;
    }

    public void setPaymentForSewage(double paymentForSewage) {
        this.paymentForSewage = paymentForSewage;
    }

    public double getPaymentForFlat() {
        return paymentForFlat;
    }

    public void setPaymentForFlat(double paymentForFlat) {
        this.paymentForFlat = paymentForFlat;
    }

    public double getPaymentForGarbage() {
        return paymentForGarbage;
    }

    public void setPaymentForGarbage(double paymentForGarbage) {
        this.paymentForGarbage = paymentForGarbage;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean isRound() {
        return round;
    }

    public void setRound(boolean round) {
        this.round = round;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Payment)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        Payment that = (Payment) obj;
        if (that.id != this.id) return false;
        if (!that.name.equals(this.name)) return false;
        if (!that.fullDescription.equals(this.fullDescription)) return false;
        if (that.year != this.year) return false;
        if (that.month != this.month) return false;
        if (that.electroTariff1 != this.electroTariff1) return false;
        if (that.electroLimit1 != this.electroLimit1) return false;
        if (that.electroTariff2 != this.electroTariff2) return false;
        if (that.electroLimit2 != this.electroLimit2) return false;
        if (that.electroTariff3 != this.electroTariff3) return false;
        if (that.electroLimit3 != this.electroLimit3) return false;
        if (that.electroTariff4 != this.electroTariff4) return false;
        if (that.waterTariff != this.waterTariff) return false;
        if (that.hotWaterTariff != this.hotWaterTariff) return false;
        if (that.heatingTariff != this.heatingTariff) return false;
        if (that.gasTariff != this.gasTariff) return false;
        if (that.sewageTariff != this.sewageTariff) return false;
        if (that.flatTariff != this.flatTariff) return false;
        if (that.garbageTariff != this.garbageTariff) return false;
        if (that.electroPaymentSet != this.electroPaymentSet) return false;
        if (that.waterPaymentSet != this.waterPaymentSet) return false;
        if (that.hotWaterPaymentSet != this.hotWaterPaymentSet) return false;
        if (that.heatingPaymentSet != this.heatingPaymentSet) return false;
        if (that.gasPaymentSet != this.gasPaymentSet) return false;
        if (that.sewagePaymentSet != this.sewagePaymentSet) return false;
        if (that.flatPaymentSet != this.flatPaymentSet) return false;
        if (that.garbagePaymentSet != this.garbagePaymentSet) return false;
        if (that.electroPaymentByTariff != this.electroPaymentByTariff) return false;
        if (that.waterPaymentByTariff != this.waterPaymentByTariff) return false;
        if (that.hotWaterPaymentByTariff != this.hotWaterPaymentByTariff) return false;
        if (that.heatingPaymentByTariff != this.heatingPaymentByTariff) return false;
        if (that.gasPaymentByTariff != this.gasPaymentByTariff) return false;
        if (that.sewagePaymentByTariff != this.sewagePaymentByTariff) return false;
        if (that.flatPaymentByTariff != this.flatPaymentByTariff) return false;
        if (that.garbagePaymentByTariff != this.garbagePaymentByTariff) return false;
        if (that.electroMustPay != this.electroMustPay) return false;
        if (that.waterMustPay != this.waterMustPay) return false;
        if (that.hotWaterMustPay != this.hotWaterMustPay) return false;
        if (that.heatingMustPay != this.heatingMustPay) return false;
        if (that.gasMustPay != this.gasMustPay) return false;
        if (that.sewageMustPay != this.sewageMustPay) return false;
        if (that.flatMustPay != this.flatMustPay) return false;
        if (that.garbageMustPay != this.garbageMustPay) return false;
        if (that.electroStart != this.electroStart) return false;
        if (that.electroEnd != this.electroEnd) return false;
        if (that.kWattConsumed != this.kWattConsumed) return false;
        if (that.waterStart != this.waterStart) return false;
        if (that.waterEnd != this.waterEnd) return false;
        if (that.m3consumed != this.m3consumed) return false;
        if (that.hotWaterStart != this.hotWaterStart) return false;
        if (that.hotWaterEnd != this.hotWaterEnd) return false;
        if (that.hotM3consumed != this.hotM3consumed) return false;
        if (that.heatingStart != this.heatingStart) return false;
        if (that.heatingEnd != this.heatingEnd) return false;
        if (that.heatingConsumed != this.heatingConsumed) return false;
        if (that.gasStart != this.gasStart) return false;
        if (that.gasEnd != this.gasEnd) return false;
        if (that.gasM3consumed != this.gasM3consumed) return false;
        if (that.sewageStart != this.sewageStart) return false;
        if (that.sewageEnd != this.sewageEnd) return false;
        if (that.sewageM3consumed != this.sewageM3consumed) return false;
        if (that.paymentForElectricity != this.paymentForElectricity) return false;
        if (that.paymentForWater != this.paymentForWater) return false;
        if (that.paymentForHotWater != this.paymentForHotWater) return false;
        if (that.paymentForHeating != this.paymentForHeating) return false;
        if (that.paymentForGas != this.paymentForGas) return false;
        if (that.paymentForSewage != this.paymentForSewage) return false;
        if (that.paymentForFlat != this.paymentForFlat) return false;
        if (that.paymentForGarbage != this.paymentForGarbage) return false;
        if (that.total != this.total) return false;
        if (that.round != this.round) return false;
        return true;
    }
}