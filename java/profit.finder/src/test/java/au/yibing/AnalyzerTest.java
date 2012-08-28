package au.yibing;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class AnalyzerTest {

    @Test
    public void testDataFromClient() {
        List<String> input = Arrays.asList(
                "06/10/2006-09:00:00.000",
                "-0.008,06/10/2006-09:01:00.000",
                "0.01,06/10/2006-09:02:00.000",
                "-0.005,06/10/2006-09:03:00.000",
                "0.007,06/10/2006-09:04:00.000",
                "-0.002,06/10/2006-09:05:00.000");
        Analyzer analyzer = new Analyzer(input.iterator());
        Analytic result = analyzer.parse();
        assertEquals("06/10/2006-09:01:00.000", result.getMaxProfitLow().getDateTime());
        assertEquals("06/10/2006-09:04:00.000", result.getMaxProfitHigh().getDateTime());
        assertEquals("06/10/2006-09:01:00.000", result.getLow().getDateTime());
        assertEquals("06/10/2006-09:04:00.000", result.getHigh().getDateTime());
    }

    @Test
    public void testNonProfit() {
        List<String> input = Arrays.asList(
                "06/10/2006-09:00:00.000",
                "-0.008,06/10/2006-09:01:00.000",
                "-0.01,06/10/2006-09:02:00.000",
                "-0.005,06/10/2006-09:03:00.000",
                "-0.007,06/10/2006-09:04:00.000",
                "-0.002,06/10/2006-09:05:00.000");
        Analyzer analyzer = new Analyzer(input.iterator());
        Analytic result = analyzer.parse();
        assertNull(result.getMaxProfitHigh());
        assertNull(result.getMaxProfitLow());
        assertEquals(BigDecimal.ZERO, result.getMaxProfit());
        assertEquals("06/10/2006-09:05:00.000", result.getLow().getDateTime());
        assertEquals("06/10/2006-09:00:00.000", result.getHigh().getDateTime());
    }

    @Test
    public void testMaxProfitInIncreasing() {
        List<String> input = Arrays.asList(
                "06/10/2006-09:00:00.000",
                "0.008,06/10/2006-09:01:00.000",
                "0.01,06/10/2006-09:02:00.000",
                "0.005,06/10/2006-09:03:00.000",
                "0.007,06/10/2006-09:04:00.000",
                "0.002,06/10/2006-09:05:00.000");
        Analyzer analyzer = new Analyzer(input.iterator());
        Analytic result = analyzer.parse();
        assertEquals("06/10/2006-09:00:00.000", result.getMaxProfitLow().getDateTime());
        assertEquals("06/10/2006-09:05:00.000", result.getMaxProfitHigh().getDateTime());
        assertEquals("06/10/2006-09:00:00.000", result.getLow().getDateTime());
        assertEquals("06/10/2006-09:05:00.000", result.getHigh().getDateTime());
    }

    @Test
    public void testMaxProfitInDecreasing() {
        List<String> input = Arrays.asList(
                "06/10/2006-09:00:00.000",
                "-0.2,06/10/2006-09:01:00.000",
                "0.15,06/10/2006-09:02:00.000",
                "-0.1,06/10/2006-09:03:00.000",
                "-0.1,06/10/2006-09:04:00.000",
                "-0.3,06/10/2006-09:05:00.000");
        Analyzer analyzer = new Analyzer(input.iterator());
        Analytic result = analyzer.parse();
        assertEquals("06/10/2006-09:01:00.000", result.getMaxProfitLow().getDateTime());
        assertEquals("06/10/2006-09:02:00.000", result.getMaxProfitHigh().getDateTime());
        assertEquals("06/10/2006-09:05:00.000", result.getLow().getDateTime());
        assertEquals("06/10/2006-09:00:00.000", result.getHigh().getDateTime());
    }

    @Test
    public void testIncreaseDecreaseIncreaseDecrease() {
        List<String> input = Arrays.asList(
                "06/10/2006-09:00:00.000",
                "0.008,06/10/2006-09:01:00.000",
                "-0.015,06/10/2006-09:02:00.000",
                "0.015,06/10/2006-09:03:00.000",
                "0.017,06/10/2006-09:04:00.000",
                "-0.1,06/10/2006-09:05:00.000");
        Analyzer analyzer = new Analyzer(input.iterator());
        Analytic result = analyzer.parse();
        assertEquals("06/10/2006-09:02:00.000", result.getMaxProfitLow().getDateTime());
        assertEquals("06/10/2006-09:04:00.000", result.getMaxProfitHigh().getDateTime());
        assertEquals("06/10/2006-09:05:00.000", result.getLow().getDateTime());
        assertEquals("06/10/2006-09:04:00.000", result.getHigh().getDateTime());
    }

}
