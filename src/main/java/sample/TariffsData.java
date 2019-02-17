package sample;

import java.io.*;
import java.util.Properties;

public class TariffsData {

    private static String TARIFFS_FILE;


    private static final String ELECTRO_TARIFF_1 = "electro_tariff_1";
    private static final String ELECTRO_LIMIT_1 = "electro_limit_1";
    private static final String ELECTRO_TARIFF_2 = "electro_tariff_2";
    private static final String ELECTRO_LIMIT_2 = "electro_limit_2";
    private static final String ELECTRO_TARIFF_3 = "electro_tariff_3";
    private static final String ELECTRO_LIMIT_3 = "electro_limit_3";
    private static final String ELECTRO_TARIFF_4 = "electro_tariff_4";
    private static final String HEATING_TARIFF = "heating_tariff";
    private static final String WATER_TARIFF = "water_tariff";
    private static final String FLAT_TARIFF = "flat_tariff";
    private static final String GARBAGE_TARIFF = "garbage_tariff";


    double electroTariff1;
    int electroLimit1;
    double electroTariff2;
    int electroLimit2;
    double electroTariff3;
    int electroLimit3;
    double electroTariff4;
    double heatingTariff;
    double waterTariff;
    double flatTariff;
    double garbageTariff;

    private final double electroTariff1Original = 0.3084;
    private final int electroLimit1Original = 75;
    private final double electroTariff2Original = 0.6168;
    private final int electroLimit2Original = 150;
    private final double electroTariff3Original = 0.8388;
    private final int electroLimit3Original = 800;
    private final double electroTariff4Original = 2.6808;
    private final double heatingTariffOriginal = 270.11;
    private final double waterTariffOriginal = 14.0;
    private final double flatTariffOriginal = 197.0;
    private final double garbageTariffOriginal = 22.0;


    public TariffsData() {
        try {
            /* This is kind of a crutch for making this code usable both from intellij idea and from the executable jar.
             * The file path of the tariffs file is selected by means of checking if the working directory path
             * ends with "target" because *\target is the directory containing the jar file and the
             * tariffs file for the jar file in the *target\classes subdirectory.
             * If the path doesn't end with *\target it means the program works from the ides project */
            TARIFFS_FILE = (new File(".").getCanonicalPath().endsWith("target") ?
                    "classes"+File.separator+"tariffs.properties" :
                    ("src"+File.separator+"main"+File.separator+"resources"+File.separator+"tariffs.properties"));
        } catch (IOException e) {
            Alerts.alertInfo("Ошибка обработки файла тарифов",
                    "Не удалось получить путь файла тарифов\n" +
                            "Загружены тарифы по умолчанию");
            e.printStackTrace();
        }
        this.electroTariff1 = electroTariff1Original;
        this.electroLimit1 = electroLimit1Original;
        this.electroTariff2 = electroTariff2Original;
        this.electroLimit2 = electroLimit2Original;
        this.electroTariff3 = electroTariff3Original;
        this.electroLimit3 = electroLimit3Original;
        this.electroTariff4 = electroTariff4Original;
        this.heatingTariff = heatingTariffOriginal;
        this.waterTariff = waterTariffOriginal;
        this.flatTariff = flatTariffOriginal;
        this.garbageTariff = garbageTariffOriginal;
    }

    public void loadTariffs() {

        Properties tariffs = new Properties();

        try {

            // load a properties file
            tariffs.load(new FileInputStream(TARIFFS_FILE));
//            tariffs.load(this.getClass().getResourceAsStream("/tariffs.properties"));

            // get the property values and transmitting them values to fields
            this.electroTariff1 = Double.parseDouble(tariffs.getProperty(ELECTRO_TARIFF_1));
            this.electroLimit1 = Integer.parseInt(tariffs.getProperty(ELECTRO_LIMIT_1));
            this.electroTariff2 = Double.parseDouble(tariffs.getProperty(ELECTRO_TARIFF_2));
            this.electroLimit2 = Integer.parseInt(tariffs.getProperty(ELECTRO_LIMIT_2));
            this.electroTariff3 = Double.parseDouble(tariffs.getProperty(ELECTRO_TARIFF_3));
            this.electroLimit3 = Integer.parseInt(tariffs.getProperty(ELECTRO_LIMIT_3));
            this.electroTariff4 = Double.parseDouble(tariffs.getProperty(ELECTRO_TARIFF_4));
            this.heatingTariff = Double.parseDouble(tariffs.getProperty(HEATING_TARIFF));
            this.waterTariff = Double.parseDouble(tariffs.getProperty(WATER_TARIFF));
            this.flatTariff = Double.parseDouble(tariffs.getProperty(FLAT_TARIFF));
            this.garbageTariff = Double.parseDouble(tariffs.getProperty(GARBAGE_TARIFF));

        } catch (FileNotFoundException fnfe) {
            Alerts.alertInfo("Тарифы не найдены", "Файл тарифов не найден\n" +
                    "(Если это первый запуск, то всё в порядке)" +
                    "Загружены тарифы по умолчанию");
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            Alerts.alertInfo("Проблема чтения тарифов",
                    "Во время загрузки произошла какая-то ошибка ввода-вывода.\n" +
                            "Новые тарифы не загружены или загружены не полностью.\n" +
                            "Часть тарифов может быть заменена тарифами по умолчанию");
            ioe.printStackTrace();
        } catch (NumberFormatException e) {
//            e.printStackTrace();
            Alerts.alertInfo("Ошибка загрузки введенных значений",
                    "Ошибка формата данных сохраненных тарифов.\n" +
                            "Новые тарифы не загружены или загружены не полностью.\n" +
                            "Часть тарифов может быть заменена тарифами по умолчанию");
        }
    }

    public void save() {

        Properties prop = new Properties();
        OutputStream output = null;

        try {
            /* doesn't work. just doesn't */
//            output = new FileOutputStream(this.getClass().getResource("/tariffs.properties").toString());
            /* using the crutch */
            output = new FileOutputStream(TARIFFS_FILE);


            // set the properties value
            prop.setProperty(ELECTRO_TARIFF_1, String.valueOf(electroTariff1));
            prop.setProperty(ELECTRO_LIMIT_1, String.valueOf(electroLimit1));
            prop.setProperty(ELECTRO_TARIFF_2, String.valueOf(electroTariff2));
            prop.setProperty(ELECTRO_LIMIT_2, String.valueOf(electroLimit2));
            prop.setProperty(ELECTRO_TARIFF_3, String.valueOf(electroTariff3));
            prop.setProperty(ELECTRO_LIMIT_3, String.valueOf(electroLimit3));
            prop.setProperty(ELECTRO_TARIFF_4, String.valueOf(electroTariff4));
            prop.setProperty(HEATING_TARIFF, String.valueOf(heatingTariff));
            prop.setProperty(WATER_TARIFF, String.valueOf(waterTariff));
            prop.setProperty(FLAT_TARIFF, String.valueOf(flatTariff));
            prop.setProperty(GARBAGE_TARIFF, String.valueOf(garbageTariff));

            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            Alerts.alertInfo("Проблема записи/сохранения тарифов",
                    "Во время записи произошла какая-то ошибка ввода-вывода.\n" +
                            "Тарифы не изменены");
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    Alerts.alertInfo("Проблема записи/сохранения тарифов",
                            "При попытке закрытия потока вывода произошла какая-то ошибка ввода-вывода.\n" +
                                    "Тарифы не изменены");
                    e.printStackTrace();
                }
            }

        }

    }

    public void backToDefaults() {
        electroTariff1 = electroTariff1Original;
        electroLimit1 = electroLimit1Original;
        electroTariff2 = electroTariff2Original;
        electroLimit2 = electroLimit2Original;
        electroTariff3 = electroTariff3Original;
        electroLimit3 = electroLimit3Original;
        electroTariff4 = electroTariff4Original;
        heatingTariff = heatingTariffOriginal;
        waterTariff = waterTariffOriginal;
        flatTariff = flatTariffOriginal;
        garbageTariff = garbageTariffOriginal;
    }
}
