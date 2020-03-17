package com.sean.financialtracker;

import com.sean.financialtracker.Data.Expenditure;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ExpenditureUnitTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Parameterized.Parameters
    public static Collection<String[]> data() {
        return Arrays.asList(new String[][] {
                {"2019-12-31 23:59:00.000", "1 minute ago"},
                {"2019-12-31 23:59:59.000", "0 minutes ago"},
                {"2020-01-01 00:00:00.000", "0 minutes ago"},
                {"2019-12-31 23:00:01.000", "59 minutes ago"},
                {"2019-12-31 23:00:00.000", "1 hour ago"},
                {"2019-12-31 22:59:59.000", "1 hour ago"},
                {"2019-12-31 22:00:00.000", "2 hours ago"},
                {"2019-12-31 00:00:01.000", "23 hours ago"},
                {"2019-12-31 00:00:00.000", "31 Dec 2019 00:00"}
        });
    }

    private Date now;
    private String date;
    private String expected;

    public ExpenditureUnitTest(String date, String expected) {
        // 2020-01-01 00:00:00
        now = new Date(2020 - 1900, 0, 1, 0, 0, 0);
        this.date = date;
        this.expected = expected;
    }

    @Test
    public void expenditure_getDateReadable_shouldReturnCorrectResult() {
        Expenditure expenditure = new Expenditure(1, 10f, "Transport", "Took the bus", date);
        String result = expenditure.getDateReadable(now);
        assertEquals(expected, result);
    }
}
