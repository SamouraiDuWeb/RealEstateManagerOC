package com.openclassrooms.realestatemanager;

import org.junit.Test;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void conversionisCorrect() {
        int currency = 100;
        int euro = Utils.convertDollarToEuro(currency);
        int dollar = Utils.convertEuroToDollar(currency);
        assertEquals(dollar, Utils.convertEuroToDollar(currency));
        assertEquals(euro, Utils.convertDollarToEuro(currency));
    }

    @Test
    public void formatDate_isCorrect() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(System.currentTimeMillis());
        assertEquals(date, Utils.getGoodFormatDate());
    }
}