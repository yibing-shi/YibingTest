package au.yibing;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: yibing
 * Date: 8/24/12
 * Time: 12:06 AM
 */
public class Synthesizer {
    public static final BigDecimal INIT_PRICE = BigDecimal.valueOf(10000);

    static Analytic synthesize (ArrayList<Analytic> analytics) {
        if (analytics.size() < 1)
            return null;

        PriceInfo overallLow = analytics.get(0).getLow();
        BigDecimal overallMaxProfit = analytics.get(0).getMaxProfit();
        PriceInfo overallMaxProfitHigh = analytics.get(0).getMaxProfitHigh();
        PriceInfo overallMaxProfitLow = analytics.get(0).getMaxProfitLow();
        for (int i = 1; i < analytics.size(); ++i) {
            Analytic adjustedAnalytic = analytics.get(i).withNewInitPrice(analytics.get(i - 1).getLastPrice());
            BigDecimal curMaxProfit = adjustedAnalytic.getMaxProfit();
            if (curMaxProfit.compareTo(overallMaxProfit) > 0) {
                overallMaxProfit = curMaxProfit;
                overallMaxProfitHigh = adjustedAnalytic.getMaxProfitHigh();
                overallMaxProfitLow = adjustedAnalytic.getMaxProfitLow();
            }
            BigDecimal anotherPossibleMaxProfit = adjustedAnalytic.getHigh().getPrice().subtract(overallLow.getPrice());
            if (anotherPossibleMaxProfit.compareTo(overallMaxProfit) > 0) {
                overallMaxProfit = anotherPossibleMaxProfit;
                overallMaxProfitHigh = adjustedAnalytic.getHigh();
                overallMaxProfitLow = overallLow;
            }
        }
        return new Analytic(null, null, overallMaxProfitHigh, overallMaxProfitLow, null);
    }
}
