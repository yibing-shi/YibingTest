package au.yibing;

import java.math.BigDecimal;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: yibing
 * Date: 8/23/12
 * Time: 11:14 PM
 */
public class Analyzer {
    private final Iterator<String> inputIterator;

    public Analyzer(Iterator<String> inputIterator) {
        this.inputIterator = inputIterator;
    }

    public Analytic parse() {
        PriceInfo low = null;
        PriceInfo high = null;
        BigDecimal maxProfit = BigDecimal.valueOf(0);
        PriceInfo maxProfitHigh = null;
        PriceInfo maxProfitLow = null;
        BigDecimal currentPrice = Synthesizer.INIT_PRICE;
        while (inputIterator.hasNext()) {
            String line = inputIterator.next();
            String[] fields = line.split(",");
            if (fields.length > 2) {
                throw new RuntimeException("Invalid line: " + line);
            }

            PriceInfo priceInfo;
            if (fields.length == 2) {
                BigDecimal move = new BigDecimal(fields[0]);
                currentPrice = currentPrice.multiply(move.divide(BigDecimal.valueOf(100)).add(BigDecimal.ONE));
                priceInfo = new PriceInfo(currentPrice, fields[1]);
            } else {
                priceInfo = new PriceInfo(currentPrice, fields[0]);
            }
            if (low == null || priceInfo.compareTo(low) < 0)
                low = priceInfo;
            if (high == null || priceInfo.compareTo(high) > 0)
                high = priceInfo;
            BigDecimal curPossibleMaxProfit = priceInfo.getPrice().subtract(low.getPrice());
            if (curPossibleMaxProfit.compareTo(maxProfit) > 0) {
                maxProfit = curPossibleMaxProfit;
                maxProfitHigh = priceInfo;
                maxProfitLow = low;
            }
        }

        return new Analytic(high, low, maxProfitHigh, maxProfitLow, currentPrice);
    }

}
