package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropLoader {

    public static String getS(String name) {
        FileInputStream fis;
        Properties property = new Properties();
        try {
            fis = new FileInputStream("src/test/java/resources/data.properties");
            property.load(fis);
        } catch (IOException e) {
            System.err.println("Ошибка: Файл свойств отсуствует!");
        }
        String value = property.getProperty(name);
        if (value == null) {
            throw new IllegalArgumentException("Отсутствует параметр: " + name);
        }
        return value;
    }

    static public Integer getI(String name) {
        return Integer.parseInt(getS(name));
    }

    static public Float getF(String name) {
        return Float.parseFloat(getS(name));
    }

}
