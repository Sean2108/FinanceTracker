package com.sean.financialtracker;

import com.sean.financialtracker.Data.DBHandler;
import com.sean.financialtracker.Utils.FormatListExpUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class FormatListExpUtilsUnitTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    DBHandler dbHandlerMock;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {10f, 1, "Transport", "daily", "Today", "Today: $10.00 (1 record)"},
                {10f, 0, "Transport", "daily", "Today", "Today: $10.00 (0 records)"},
                {10f, 2, "Transport", "daily", "Today", "Today: $10.00 (2 records)"},
                {10.22222f, 1, "Transport", "daily", "Today", "Today: $10.22 (1 record)"},
                {10.777777f, 1, "Transport", "daily", "Today", "Today: $10.78 (1 record)"},
                {10.50f, 1, "Transport", "daily", "Today", "Today: $10.50 (1 record)"},
                {10f, 1, "Transport", "weekly", "This week", "This week: $10.00 (1 record)"},
                {10f, 1, "Transport", "monthly", "This month", "This month: $10.00 (1 record)"},
        });
    }

    private float expSum;
    private int expCount;
    private String type;
    private String queryDateRange;
    private String range;
    private String expected;

    public FormatListExpUtilsUnitTest(float expSum, int expCount, String type, String queryDateRange, String range, String expected) {
        this.expSum = expSum;
        this.expCount = expCount;
        this.type = type;
        this.queryDateRange = queryDateRange;
        this.range = range;
        this.expected = expected;
    }

    @Test
    public void getCategoryExpTotal_shouldReturnCorrectText() {
        when(dbHandlerMock.getCategoryExpSum(type, queryDateRange)).thenReturn(expSum);
        when(dbHandlerMock.getCategoryExpCount(type, queryDateRange)).thenReturn(expCount);
        FormatListExpUtils formatListExpUtils = new FormatListExpUtils(dbHandlerMock);

        String result = formatListExpUtils.getCategoryExpTotal(type, queryDateRange, range);
        assertEquals(expected, result);
    }
}