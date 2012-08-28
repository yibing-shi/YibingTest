package au.yibing;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: yibing
 * Date: 8/23/12
 * Time: 10:14 PM
 */
public class Analytic {
    private final PriceInfo high;
    private final PriceInfo low;
    private final PriceInfo maxProfitHigh;
    private final PriceInfo maxProfitLow;
    private final BigDecimal lastPrice;

    public Analytic(PriceInfo high,
                    PriceInfo low,
                    PriceInfo maxProfitHigh,
                    PriceInfo maxProfitLow,
                    BigDecimal lastPrice) {
        this.high = high;
        this.low = low;
        this.maxProfitHigh = maxProfitHigh;
        this.maxProfitLow = maxProfitLow;
        this.lastPrice = lastPrice;
    }

    public PriceInfo getHigh() {
        return high;
    }

    public PriceInfo getLow() {
        return low;
    }

    public PriceInfo getMaxProfitHigh() {
        return maxProfitHigh;
    }

    public PriceInfo getMaxProfitLow() {
        return maxProfitLow;
    }

    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public BigDecimal getMaxProfit() {
        if (maxProfitHigh == null || maxProfitLow == null) {
            return BigDecimal.ZERO;
        }

        return maxProfitHigh.getPrice().subtract(maxProfitLow.getPrice());
    }

    public Analytic withNewInitPrice(BigDecimal newInitPrice) {
        BigDecimal ratio = newInitPrice.divide(Synthesizer.INIT_PRICE);
        PriceInfo newHigh = new PriceInfo(high.getPrice().multiply(ratio), high.getDateTime());
        PriceInfo newLow = new PriceInfo(low.getPrice().multiply(ratio), low.getDateTime());
        PriceInfo newMaxProfitHigh = null;
        if (maxProfitHigh != null)
            newMaxProfitHigh = new PriceInfo(maxProfitHigh.getPrice().multiply(ratio), maxProfitHigh.getDateTime());
        PriceInfo newMaxProfitLow = null;
        if (maxProfitLow != null)
            newMaxProfitLow = new PriceInfo(maxProfitLow.getPrice().multiply(ratio), maxProfitLow.getDateTime());
        BigDecimal newLastPrice = lastPrice.multiply(ratio);
        return new Analytic(newHigh, newLow, newMaxProfitHigh, newMaxProfitLow, newLastPrice);
    }
}
