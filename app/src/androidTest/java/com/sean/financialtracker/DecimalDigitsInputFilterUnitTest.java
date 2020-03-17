package com.sean.financialtracker;

import android.text.SpannedString;

import com.sean.financialtracker.Utils.DecimalDigitsInputFilter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import androidx.test.filters.SmallTest;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
@SmallTest
public class DecimalDigitsInputFilterUnitTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"1234.1", 5, 2, null},
                {"1234", 5, 2, null},
                {"1234.12", 5, 2, ""},
                {"12345", 5, 2, ""},
                {"0.1", 5, 2, null},
                {".1", 5, 2, null},
                {".12", 5, 2, ""},
                {"0.12", 5, 2, ""}
        });
    }

    private String input;
    private int digitsBeforeZero;
    private int digitsAfterZero;
    private CharSequence expected;

    public DecimalDigitsInputFilterUnitTest(String input, int digitsBeforeZero, int digitsAfterZero, CharSequence expected) {
        this.input = input;
        this.digitsBeforeZero = digitsBeforeZero;
        this.digitsAfterZero = digitsAfterZero;
        this.expected = expected;
    }

    @Test
    public void DecimalDigitsInputFilter_shouldReturnCorrectValue() {
        DecimalDigitsInputFilter decimalDigitsInputFilter = new DecimalDigitsInputFilter(digitsBeforeZero, digitsAfterZero);
        SpannedString span = new SpannedString(input);
        CharSequence result = decimalDigitsInputFilter.filter("", 0, 0, span, 0, 0);
        assertEquals(expected, result);
    }
}
