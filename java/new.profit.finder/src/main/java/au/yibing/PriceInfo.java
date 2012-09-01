package au.yibing;


public class PriceInfo {

    private final double price;
    private final String dateTime;

    public PriceInfo(double price, String dateTime) {
        this.price = price;
        this.dateTime = dateTime;
    }

    public double getPrice() {
        return price;
    }

    public String getDateTime() {
        return dateTime;
    }

}
