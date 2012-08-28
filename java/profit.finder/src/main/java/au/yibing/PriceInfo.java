package au.yibing;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: yibing
 * Date: 8/23/12
 * Time: 11:23 PM
 */
public class PriceInfo implements Comparable<PriceInfo> {
    private final BigDecimal price;
    private final String dateTime;

    public PriceInfo(BigDecimal price, String dateTime) {
        this.price = price;
        this.dateTime = dateTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDateTime() {
        return dateTime;
    }

    @Override
    public int compareTo(PriceInfo o) {
        return this.price.compareTo(o.price);
    }
}
