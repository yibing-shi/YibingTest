package au.yibing;

import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Random;

public class Generator {

    static DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy-HH:mm:ss.SSS");
    static DecimalFormat decimalFormat = new DecimalFormat("#.###");

    public static void main(String[] args) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream("/home/yibing/price_moves.csv")));
        Random random = new Random();
        Instant instant = new Instant();
        for (long i = 0; i < Integer.MAX_VALUE; i++) {
            int r = random.nextInt(100) + 10;
            double f = (double)r / 10000;
            instant = instant.plus(60 * 1000);
            String leading = r % 2 == 0 ? "" : "-";
            writer.println(leading + decimalFormat.format(f) + "," + instant.toDateTime().toString(formatter));
        }
        writer.close();
    }
}
