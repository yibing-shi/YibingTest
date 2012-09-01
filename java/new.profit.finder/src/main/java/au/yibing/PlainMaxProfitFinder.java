package au.yibing;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import java.io.*;
import java.text.DecimalFormat;

public class PlainMaxProfitFinder {

    private static String inputFilePath;
    private static String outputFilePath;

    public static void main(String[] args) throws IOException, ParseException {
        long start = System.nanoTime();

        parseArguments(args);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilePath)));
        PriceInfo low = null;
        PriceInfo high = null;
        double maxProfit = 0;
        PriceInfo sellPoint = null;
        PriceInfo buyPoint = null;
        double currentPrice = 1;
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] fields = line.split(",");
            double move;
            String time;
            if (fields.length == 1) {
                move = 0;
                time = fields[0];
            } else {
                move = Double.valueOf(fields[0]);
                time = fields[1];
            }
            currentPrice = currentPrice * (move / 100 + 1);
            PriceInfo priceInfo = new PriceInfo(currentPrice, time);
            if (low == null || priceInfo.getPrice() < low.getPrice())
                low = priceInfo;
            if (high == null || priceInfo.getPrice() > high.getPrice())
                high = priceInfo;
            double curPossibleMaxProfit = priceInfo.getPrice() - low.getPrice();
            if (curPossibleMaxProfit > maxProfit) {
                maxProfit = curPossibleMaxProfit;
                sellPoint = priceInfo;
                buyPoint = low;
            }
        }
        if (buyPoint != null & sellPoint != null) {
            writeResult(buyPoint, sellPoint, maxProfit);
        }else {
            System.err.println("Cannot find any profit");
        }

        System.out.println("Total time: " + (System.nanoTime() - start) / 1000000 + " milli seconds");
    }


    private static void parseArguments(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("input", true, "The input file path");
        options.addOption("output", true, "The output file path");

        CommandLine commandLine = new PosixParser().parse(options, args);

        inputFilePath = commandLine.getOptionValue("input", "price_moves.csv");
        outputFilePath = commandLine.getOptionValue("output", "result.txt");
    }

    private static void writeResult(PriceInfo buyPoint, PriceInfo sellPoint, double profit) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(outputFilePath));
        printWriter.println(buyPoint.getDateTime());
        printWriter.println(sellPoint.getDateTime());
        double profitRatio = profit / buyPoint.getPrice() * 100;
        DecimalFormat df = new DecimalFormat("#.###");
        printWriter.println(df.format(profitRatio));
        printWriter.flush();
        printWriter.close();
    }
}
